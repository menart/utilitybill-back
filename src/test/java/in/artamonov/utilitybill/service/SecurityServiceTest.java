package in.artamonov.utilitybill.service;

import in.artamonov.utilitybill.AbstractTest;
import in.artamonov.utilitybill.config.TestBeanConfig;
import in.artamonov.utilitybill.entity.UserEntity;
import in.artamonov.utilitybill.errorhandling.AppErrorAttributes;
import in.artamonov.utilitybill.exceprtion.WrongUserException;
import in.artamonov.utilitybill.mapper.UserMapperImpl;
import in.artamonov.utilitybill.repository.UserRepository;
import in.artamonov.utilitybill.security.PBKDF2Encoder;
import in.artamonov.utilitybill.security.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        UserMapperImpl.class,
        SecurityService.class,
        AppErrorAttributes.class,
        UserService.class,
        PBKDF2Encoder.class,
        PasswordEncoder.class,
        TestBeanConfig.class
})
public class SecurityServiceTest extends AbstractTest {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AppErrorAttributes appErrorAttributes;

    @MockBean
    private UserRepository userRepository;

    @Value("${jwt.expiration}")
    private Long expirationInSecond;

    private final static String clazz = "security_service_test";

    @Test
    void testPositiveAuthenticate() {
        UserEntity userEntity = loadObj(clazz, UserEntity.class);
        String username = userEntity.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Mono.just(userEntity));
        var tokenDetail = securityService.authenticate(username, "password").block();
        assertNotNull(tokenDetail);
        assertEquals(1L, tokenDetail.getUserId());
        assertEquals(username, tokenDetail.getUsername());
        assertNotNull(tokenDetail.getToken());
        LocalDateTime expiresAt = tokenDetail.getExpiresAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime issuedAt = tokenDetail.getIssuedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        assertEquals(expirationInSecond, ChronoUnit.SECONDS.between(issuedAt, expiresAt));
    }

    @Test
    void testNegativeAuthenticate() {
        UserEntity userEntity = loadObj(clazz, UserEntity.class);
        String username = userEntity.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Mono.just(userEntity));

        Exception exception = assertThrows(WrongUserException.class, () -> {
            securityService.authenticate(username, "password2").block();
        });

        String expectedMessage = "wrong user name or password";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}


