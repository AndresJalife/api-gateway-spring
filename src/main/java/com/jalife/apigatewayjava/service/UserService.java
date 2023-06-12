package com.jalife.apigatewayjava.service;

import com.jalife.apigatewayjava.dto.user.PasswordDTO;
import com.jalife.apigatewayjava.dto.user.UserDTO;
import com.jalife.apigatewayjava.exception.OldAndNewPasswordsTheSameException;
import com.jalife.apigatewayjava.exception.PasswordsDontMatchException;
import com.jalife.apigatewayjava.exception.UserAlreadyExistsException;
import com.jalife.apigatewayjava.exception.UserNotFoundException;
import com.jalife.apigatewayjava.model.Role;
import com.jalife.apigatewayjava.model.User;
import com.jalife.apigatewayjava.repository.RoleRepository;
import com.jalife.apigatewayjava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Service that handles the user requests
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Receives a username and returns a User saved in the database
     *
     * @param username Username
     * @return User
     */
    @Transactional(readOnly = true)
    public Mono<User> getUser(String username) {
        return userRepo.findByUsername(username)
                .flatMap(user -> userRepo.findByUsername(username));
    }

    /**
     * Gets the user details from a user.
     *
     * @param user User
     * @return UserDetails
     */
    @Transactional(readOnly = true)
    public Mono<UserDetails> getUserDetails(User user) {
        return Mono.just(user)
                .flatMap(_user -> {
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
                    return Mono.just(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities));
                });
    }

    /**
     * Saves a user in the database
     */
    @Transactional
    public Mono<UserDTO> saveUser(UserDTO userDTO) {
        return getRole(userDTO.getRole().getRoleId()).flatMap(role -> {
            User user = new User(userDTO, role);
            return userRepo.existsByUsername(user.getUsername()).flatMap(exists -> {
                if (exists) {
                    log.info("User already exists");
                    return Mono.error(new UserAlreadyExistsException());
                } else {
                    log.info("Saving new user");
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepo.save(user);
                    return Mono.just(userDTO);
                }
            });
        });
    }

    /**
     * Deletes a user from the database
     */
    @Transactional
    public Mono<Long> deleteUser(Long userId) {
        return userRepo.findById(userId).flatMap(appUser -> {
            if (appUser != null) {
                log.info("Deleting user");
                userRepo.delete(appUser);
                return Mono.just(appUser.getUserId());
            } else {
                return Mono.error(new UserNotFoundException());
            }
        });
    }

    /**
     * Edits a user from the database
     */
    @Transactional
    public Mono<UserDTO> editUser(UserDTO userDTO) {
        return userRepo.findById(userDTO.getId())
                .flatMap(user -> getRole(user.getRole().getId())
                        .flatMap(role -> {
                            user.setEmail(user.getEmail());
                            user.setName(user.getName());
                            user.setRole(role);
                            user.setUsername(user.getUsername());
                            userRepo.save(user);
                            return Mono.just(userDTO);
                        }))
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    /**
     * Updates a user's password
     */
    @Transactional
    public Mono<Void> updatePassword(PasswordDTO passwordDTO) {
        return this.getUserById(passwordDTO.getId())
                .flatMap(user -> {
                    if (passwordDTO.getOldPassword().equals(passwordDTO.getNewPassword()))
                        return Mono.error(new OldAndNewPasswordsTheSameException());
                    if (passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
                        userRepo.save(user);
                        return Mono.empty();
                    } else {
                        return Mono.error(new PasswordsDontMatchException());
                    }
                });
    }

    /**
     * Gets a user by id
     */
    @Transactional(readOnly = true)
    public Mono<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    private Mono<Role> getRole(Long roleId) {
        return roleRepository.findById(roleId);
    }

}
