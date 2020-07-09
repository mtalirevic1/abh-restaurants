package com.atlantbh.devdays.demo.abh.restaurants.service.users;

import com.atlantbh.devdays.demo.abh.restaurants.configuration.security.Roles;
import com.atlantbh.devdays.demo.abh.restaurants.domain.User;
import com.atlantbh.devdays.demo.abh.restaurants.repository.UserRepository;
import com.atlantbh.devdays.demo.abh.restaurants.service.BaseCrudService;
import com.atlantbh.devdays.demo.abh.restaurants.service.exceptions.AccessDeniedServiceException;
import com.atlantbh.devdays.demo.abh.restaurants.service.exceptions.EntityNotFoundServiceException;
import com.atlantbh.devdays.demo.abh.restaurants.service.users.exception.PasswordMismatchServiceException;
import com.atlantbh.devdays.demo.abh.restaurants.service.users.requests.UserInfoRequest;
import com.atlantbh.devdays.demo.abh.restaurants.service.users.requests.UserRequest;
import com.atlantbh.devdays.demo.abh.restaurants.service.users.requests.UserSecurityInfoRequest;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Users service manages users.
 *
 * <p>Primary annotation was set due to clash with AdminService.
 */
@Service
@Primary
public class UsersService extends BaseCrudService<User, Long, UserRepository>
    implements UserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);

  PasswordEncoder passwordEncoder;

  /**
   * Instantiates a new user service.
   *
   * @param repository User service repository.
   * @param passwordEncoder Password encoder.
   */
  public UsersService(UserRepository repository, PasswordEncoder passwordEncoder) {
    super(repository);
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Returns a user given its id.
   *
   * @param id Model id.
   * @return User.
   * @throws EntityNotFoundServiceException If no user found.
   */
  @Override
  public User get(Long id) throws EntityNotFoundServiceException {
    return findById(id).orElseThrow(() -> new EntityNotFoundServiceException(User.ENTITY_NAME, id));
  }

  /**
   * Returns a user given user details.
   *
   * @param userDetails User details.
   * @return User.
   * @throws EntityNotFoundServiceException If user is not found.
   */
  public User get(UserDetails userDetails) throws EntityNotFoundServiceException {
    User user = repository.findUserByEmail(userDetails.getUsername());
    if (user == null) {
      throw new EntityNotFoundServiceException();
    }

    return user;
  }

  /**
   * Creates a new user and saves it to database.
   *
   * @param request User creation request.
   */
  @Transactional
  public User createUser(UserRequest request) {
    User user = null;
    if (request
        .getEmail()
        .matches(
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
      user = new User();
      user.setFirstName(request.getFirstName());
      user.setLastName(request.getLastName());
      user.setEmail(request.getEmail());
      user.setPassword(passwordEncoder.encode(request.getPassword()));

      repository.save(user);
    }

    return user;
  }

  /**
   * Changes user info.
   *
   * @param userId Id of user.
   * @param request Info request.
   * @param currentUser Current user.
   * @throws EntityNotFoundServiceException If user is not found.
   * @throws AccessDeniedServiceException If user is not allowed to update its info.
   */
  @Transactional
  public void update(Long userId, UserInfoRequest request, UserDetails currentUser)
      throws EntityNotFoundServiceException, AccessDeniedServiceException {
    User user = get(userId);

    assertUserAllowedToUpdateInfo(user, currentUser);

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());

    repository.save(user);
  }

  /**
   * Changes user security info. Security info includes changing email and/or password. In order to
   * change these properties, password needs to be provided.
   *
   * @param userId User id.
   * @param request Security info request.
   * @param currentUser Current user.
   * @throws EntityNotFoundServiceException If user is not found.
   * @throws PasswordMismatchServiceException If old new password does not match.
   * @throws AccessDeniedServiceException If user is not allowed to update its security info.
   */
  @Transactional
  public void updateSecurityInfo(
      Long userId, UserSecurityInfoRequest request, UserDetails currentUser)
      throws EntityNotFoundServiceException, PasswordMismatchServiceException,
          AccessDeniedServiceException {
    User user = get(userId);

    assertUserAllowedToUpdateSecurityInfo(user, currentUser);
    assertUserPasswordMatches(user, request.getOldPassword());

    // Password change.
    if (StringUtils.isNotEmpty(request.getPassword())) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    // Email change
    if (StringUtils.isNotEmpty(request.getEmail())) {
      user.setEmail(request.getEmail());
    }

    repository.save(user);
  }

  /**
   * Loads a user given a user or email.
   *
   * @param usernameOrEmail Username or email.
   * @return User
   * @throws UsernameNotFoundException If user is not found.
   */
  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    User user = repository.findUserByEmail(usernameOrEmail);
    if (user == null) {
      throw new UsernameNotFoundException("User with given email not found!");
    }

    final GrantedAuthority grantedAuthority =
        new SimpleGrantedAuthority(
            "ROLE_" + (user.isAdmin() ? Roles.ADMIN.name() : Roles.USER.name()));

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), Collections.singletonList(grantedAuthority));
  }

  /**
   * Verifies a user with given username exists.
   *
   * @param email Email
   * @return True if user exists.
   */
  @Transactional(readOnly = true)
  public boolean verifyUserExists(String email) {
    return repository.existsByEmail(email);
  }

  /**
   * Checks if provided password matches the one on the user.
   *
   * @param user User to check.
   * @param password Provided old password.
   * @throws PasswordMismatchServiceException If password mismatches.
   */
  private void assertUserPasswordMatches(User user, String password)
      throws PasswordMismatchServiceException {
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new PasswordMismatchServiceException();
    }
  }

  /**
   * Verify user can updated its security info.
   *
   * @param user User to update.
   * @param currentUser Current user details.
   * @throws AccessDeniedServiceException If user is not allowed to updates its security info.
   */
  private void assertUserAllowedToUpdateSecurityInfo(User user, UserDetails currentUser)
      throws AccessDeniedServiceException {
    boolean isOwnSecurityInfo = user.getEmail().equalsIgnoreCase(currentUser.getUsername());
    if (!isOwnSecurityInfo) {
      throw new AccessDeniedServiceException();
    }
  }

  /**
   * Verify user can update its info.
   *
   * @param user User.
   * @param currentUser Current user.
   * @throws AccessDeniedServiceException If user is not allowed to updates its security info.
   */
  private void assertUserAllowedToUpdateInfo(User user, UserDetails currentUser)
      throws AccessDeniedServiceException {
    boolean isOwnInfo = user.getEmail().equalsIgnoreCase(currentUser.getUsername());
    if (!isOwnInfo) {
      throw new AccessDeniedServiceException();
    }
  }
}
