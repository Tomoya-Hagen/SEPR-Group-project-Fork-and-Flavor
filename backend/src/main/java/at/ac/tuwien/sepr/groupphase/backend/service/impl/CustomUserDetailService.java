package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserRegisterDtoMapper;
import at.ac.tuwien.sepr.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.validators.UserValidator;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.exception.EmailException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.UsernameException;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final UserValidator userValidator = new UserValidator();
    private final UserRegisterDtoMapper userRegisterDtoMapper;
    private final RoleRepository rolesRepository;
    private final UserMapper userMapper;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer, UserMapper userMapper,
    UserRegisterDtoMapper userRegisterDtoMapper, RoleRepository rolesRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.userRegisterDtoMapper = userRegisterDtoMapper;
        this.rolesRepository = rolesRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getAdmin()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }
            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        ApplicationUser applicationUser = userRepository.findFirstUserByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
            && passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())
        ) {
            List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
        }
        throw new BadCredentialsException("Username or password is incorrect or account is locked");
    }

    @Override
    public List<UserListDto> findUsersByName(String name, int limit) {
        List<ApplicationUser> users = userRepository.findByNameContainingIgnoreCase(name);
        return userMapper.userListToUserListDtoList(users);
    }

    @Override
    public String register(UserRegisterDto userRegisterDto) {
        LOGGER.debug("Register a new user");
        userValidator.validateForCreate(userRegisterDto);

        if (userRepository.existsByUsername(userRegisterDto.username())) {
            throw new UsernameException("Username already exists");
        }

        if (userRepository.existsByEmail(userRegisterDto.email())) {
            throw new EmailException("Email already exists");
        }

        ApplicationUser applicationUser = new ApplicationUser.ApplicationUserBuilder()
            .withEmail(userRegisterDto.email())
            .withUsername(userRegisterDto.username())
            .withPassword(passwordEncoder.encode(userRegisterDto.password()))
            .withhasProfilePicture(false)
            .withRoles(List.of(rolesRepository.findByName("User")))
            .build();
        userRepository.save(applicationUser);

        // Ensure the user is persisted
        userRepository.flush();

        return login(userRegisterDtoMapper.toUserLoginDto(userRegisterDto));
    }
}
