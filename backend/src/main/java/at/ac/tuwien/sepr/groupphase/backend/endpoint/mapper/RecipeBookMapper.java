package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRecipeBookDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.UserRecipeBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface RecipeBookMapper {

    @Mapping(source = "userRecipeBookDto.id", target = "id")
    default List<UserRecipeBook> UserRecipeBookDtoListToUserRecipeBookList(List<UserRecipeBookDto> userRecipeBookDtoList) {
        List<UserRecipeBook> userRecipeBookList = new ArrayList<>();
        for (UserRecipeBookDto u : userRecipeBookDtoList) {
            userRecipeBookList.add(userRecipeBookDtoToUserRecipeBook(u));
        }
        return userRecipeBookList;
    }

    UserRecipeBook userRecipeBookDtoToUserRecipeBook(UserRecipeBookDto userRecipeBookDto);
}
