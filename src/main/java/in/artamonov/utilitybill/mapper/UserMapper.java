package in.artamonov.utilitybill.mapper;

import in.artamonov.utilitybill.dto.UserDto;
import in.artamonov.utilitybill.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, UserEntity> {
}
