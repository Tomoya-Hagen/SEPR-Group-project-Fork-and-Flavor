package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeBookListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserPasswordChangeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserPasswordResetDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * This is the interface for the user service extending UserDetailsService.
 */
public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return an application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * Log in a user.
     *
     * @param userLoginDto login credentials
     * @return the JWT, if successful
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are bad
     */
    String login(UserLoginDto userLoginDto);

    /**
     * find limited numbers users using names.
     *
     * @param name  the name to look for
     * @param limit the number of users to get
     * @return a list of found users
     */
    List<UserListDto> findUsersByName(String name, int limit);

    /**
     * Register a new user.
     *
     * @param userRegisterDto login credentials
     * @return the JWT, if successful
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are bad
     * @throws ValidationException                                                 if the input is invalid
     */
    String register(UserRegisterDto userRegisterDto) throws ValidationException;

    /**
     * Reset the password of a user.
     *
     * @param userPasswordResetDto the email address of the user
     * @throws NotFoundException if the input is invalid
     */
    void resetPassword(UserPasswordResetDto userPasswordResetDto) throws NotFoundException;

    /**
     * Retrieves a user based on the provided ID.
     *
     * @param id The ID of the user to be retrieved.
     * @return UserDto object containing the user's details.
     * @throws NotFoundException If no user is found with the provided ID.
     */
    UserDto findUserById(Long id);

    /**
     * This method gets the currently logged-in user.
     *
     * @return the currently logged-in user.
     */
    UserDto getCurrentUser();

    /**
     * Retrieves a list of RecipeBookListDto objects associated with a specific user.
     *
     * @param id The ID of the user.
     * @return List of RecipeBookListDto objects.
     * @throws NotFoundException If no user is found with the provided ID.
     */
    List<RecipeBookListDto> findRecipeBooksByUserId(Long id) throws NotFoundException;

    /**
     * Retrieves a list of RecipeListDto objects associated with a specific user.
     *
     * @param id The ID of the user.
     * @return List of RecipeListDto objects.
     * @throws NotFoundException If no user is found with the provided ID.
     */
    List<RecipeListDto> findRecipesByUserId(Long id) throws NotFoundException;

    /**
     * Changes the password of a user.
     *
     * @param id The ID of the user.
     * @param userPasswordChangeDto Object containing the old and new password.
     * @throws NotFoundException If no user is found with the provided ID.
     * @throws BadCredentialsException If the old password is incorrect.
     * @throws ValidationException If the new password is not valid.
     */
    void changePassword(Long id, UserPasswordChangeDto userPasswordChangeDto) throws NotFoundException, BadCredentialsException, ValidationException;
}
