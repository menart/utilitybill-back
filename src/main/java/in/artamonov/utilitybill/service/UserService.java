package in.artamonov.utilitybill.service;

import in.artamonov.utilitybill.dto.UserDto;
import in.artamonov.utilitybill.entity.UserEntity;
import in.artamonov.utilitybill.enums.UserRole;
import in.artamonov.utilitybill.mapper.UserMapper;
import in.artamonov.utilitybill.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserDto> registerUser(UserDto userDto) {
        UserEntity user = userMapper.toEntity(userDto);
        user = user.toBuilder()
                .password(passwordEncoder.encode(userDto.getPassword()))
                .enable(true)
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return userRepository.save(user)
                .doOnSuccess(userCreated -> {
                    log.info("created user {}", userCreated);
                })
                .map(userMapper::toDto);
    }

    public Mono<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    public Mono<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDto);
    }
}
