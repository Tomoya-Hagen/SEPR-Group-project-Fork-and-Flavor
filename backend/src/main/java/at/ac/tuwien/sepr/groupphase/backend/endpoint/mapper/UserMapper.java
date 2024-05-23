package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the interface for the User entities and related DTOs.
 *
 */
@Mapper
public interface UserMapper {

    default List<UserListDto> userListToUserListDtoList(List<ApplicationUser> users) {
        List<UserListDto> userListDtos = new ArrayList<>();
        for (ApplicationUser user : users) {
            userListDtos.add(userToUserListDto(user));
        }
        return userListDtos;
    }

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.username", target = "name")
    UserListDto userToUserListDto(ApplicationUser user);
}
