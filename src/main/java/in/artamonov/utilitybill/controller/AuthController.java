package in.artamonov.utilitybill.controller;

import in.artamonov.utilitybill.dto.AuthRequestDto;
import in.artamonov.utilitybill.dto.AuthResponseDto;
import in.artamonov.utilitybill.dto.UserDto;
import in.artamonov.utilitybill.security.CustomPrincipal;
import in.artamonov.utilitybill.security.SecurityService;
import in.artamonov.utilitybill.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final SecurityService securityService;
    private final UserService userService;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }


    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        return securityService.authenticate(authRequestDto.getUsername(), authRequestDto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/me")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId());
    }
}
