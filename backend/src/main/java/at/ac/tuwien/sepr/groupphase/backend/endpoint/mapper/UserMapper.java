package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserMapper {

    @Mapping(source = "user.id", target = "id")
    default List<UserListDto> userListToUserListDtoList(List<ApplicationUser> users) {
        List<UserListDto> userListDtos = new ArrayList<>();
        for (ApplicationUser user : users) {
            userListDtos.add(userToUserListDto(user));
        }
        return userListDtos;
    }

    UserListDto userToUserListDto(ApplicationUser user);
}
