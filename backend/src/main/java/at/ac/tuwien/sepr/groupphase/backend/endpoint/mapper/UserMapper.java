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
    /**
     * Converts a list of ApplicationUser entities to a list of UserListDto objects.
     *
     * @param users The list of ApplicationUser entities.
     * @return A list of UserListDto objects.
     */
    List<UserListDto> userListToUserListDtoList(List<ApplicationUser> users);

    /**
     * Converts an ApplicationUser entity to a UserListDto object.
     * Maps the 'username' field of the ApplicationUser to the 'name' field of the UserListDto.
     *
     * @param user The ApplicationUser entity.
     * @return A UserListDto object.
     */
    @Mapping(source = "username", target = "name")
    UserListDto userToUserListDto(ApplicationUser user);

    /**
     * Converts an ApplicationUser entity to a UserDto object.
     * Maps the 'username' field of the ApplicationUser to the 'name' field of the UserDto.
     *
     * @param user The ApplicationUser entity.
     * @return A UserDto object.
     */
    @Mapping(source = "username", target = "name")
    UserDto userToUserDto(ApplicationUser user);
}
