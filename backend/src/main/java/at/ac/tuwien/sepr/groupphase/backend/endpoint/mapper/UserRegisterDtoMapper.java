package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import org.mapstruct.Mapper;

/**
 * This is the interface for the UserRegisterDto and UserLoginDto.
 */
@Mapper
public interface UserRegisterDtoMapper {

    /**
     * This maps a UserRegisterDto to a UserLoginDto.
     *
     * @param userRegisterDto the UserRegisterDto to map.
     * @return the mapped result.s
     */
    UserLoginDto toUserLoginDto(UserRegisterDto userRegisterDto);

}
