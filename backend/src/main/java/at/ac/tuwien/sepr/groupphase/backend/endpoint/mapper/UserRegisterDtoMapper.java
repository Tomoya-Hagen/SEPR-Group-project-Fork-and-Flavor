package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserRegisterDtoMapper {

    UserLoginDto toUserLoginDto(UserRegisterDto userRegisterDto);

}
