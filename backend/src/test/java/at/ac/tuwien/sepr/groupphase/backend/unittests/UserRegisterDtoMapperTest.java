package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserRegisterDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegisterDtoMapperTest implements TestData {

    @Autowired
    private UserRegisterDtoMapper userRegisterDtoMapper;

    private final UserRegisterDto userRegisterDto = UserRegisterDto.UserRegisterDtoBuilder.anUserRegisterDto()
        .withEmail("email@email.com")
        .withPassword("password")
        .withUsername("username")
        .build();

    @Test
    void toUserLoginDto() {
        UserLoginDto userLoginDto = userRegisterDtoMapper.toUserLoginDto(userRegisterDto);
        assertAll(
            () -> assertEquals("email@email.com", userLoginDto.getEmail()),
            () -> assertEquals("password", userLoginDto.getPassword())
        );
    }
}