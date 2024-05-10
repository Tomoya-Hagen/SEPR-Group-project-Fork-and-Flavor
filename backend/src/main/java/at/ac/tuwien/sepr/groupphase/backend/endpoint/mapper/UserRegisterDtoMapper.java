package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import org.mapstruct.Mapper;

@Mapper
public class UserRegisterDtoMapper {
    public UserRegisterDtoMapper() {
    }

    public UserLoginDto toUserLoginDto(UserRegisterDto userRegisterDto) {
        UserLoginDto userLoginDtoBuilder = new UserLoginDto();
        userLoginDtoBuilder.setEmail(userRegisterDto.getEmail());
        userLoginDtoBuilder.setPassword(userRegisterDto.getPassword());
        return userLoginDtoBuilder;
    }
}
