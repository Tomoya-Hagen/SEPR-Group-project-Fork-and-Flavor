package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * This is the interface for the User entities and related DTOs.
 *
 */
@Mapper
public interface UserMapper {

    List<UserListDto> userListToUserListDtoList(List<ApplicationUser> users);

    @Mapping(source = "username", target = "name")
    UserListDto userToUserListDto(ApplicationUser user);

    @Mapping(source = "username", target = "name")
    UserDto userToUserDto(ApplicationUser user);

    List<ApplicationUser> userListDtoListToUserList(List<UserListDto> users);

    ApplicationUser userListDtoToUser(UserListDto users);
}
