package in.artamonov.utilitybill.security;

import in.artamonov.utilitybill.dto.UserDto;
import in.artamonov.utilitybill.exceprtion.AuthException;
import in.artamonov.utilitybill.exceprtion.WrongUserException;
import in.artamonov.utilitybill.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiration}")
    private Integer expirationInSecond;

    private TokenDetails generateToken(UserDto user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("username", user.getUsername());
        }};
        return generateToken(claims, user.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMillis = expirationInSecond * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);
        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claims(claims)
                .issuer(issuer)
                .subject(subject)
                .issuedAt(createdDate)
                .id(UUID.randomUUID().toString())
                .expiration(expirationDate)
                .signWith(key)
                .compact();
        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(userDto -> {
                    if (!userDto.isEnable()) {
                        return Mono.error(new AuthException("Account disable", "ACCOUNT_DISABLE"));
                    }
                    if (!passwordEncoder.matches(password, userDto.getPassword())) {
                        return Mono.error(new WrongUserException());
                    }

                    return Mono.just(generateToken(userDto).toBuilder()
                            .userId(userDto.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new WrongUserException()));
    }
}
