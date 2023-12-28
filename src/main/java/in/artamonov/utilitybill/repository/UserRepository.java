package in.artamonov.utilitybill.repository;

import in.artamonov.utilitybill.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserEntity, Long> {

    Mono<UserEntity> findByUsername(String username);
}
