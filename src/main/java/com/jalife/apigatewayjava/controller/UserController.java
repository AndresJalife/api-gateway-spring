package com.jalife.apigatewayjava.controller;

import com.jalife.apigatewayjava.dto.user.PasswordDTO;
import com.jalife.apigatewayjava.dto.user.UserDTO;
import com.jalife.apigatewayjava.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controller for users.
 */
@AllArgsConstructor
@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * Endpoint for saving a user.
     * @param user User to save
     */
    @PostMapping("/user")
    public Mono<ResponseEntity<UserDTO>> saveUser(@RequestBody UserDTO user) {
        return userService.saveUser(user)
                .map(ResponseEntity::ok);
    }

    /**
     * Endpoint for deleting a user.
     * @param userId Id of the user to delete
     */
    @DeleteMapping("/user")
    public Mono<ResponseEntity<Long>> deleteUser(@RequestParam Long userId) {
        return userService.deleteUser(userId)
                .map(ResponseEntity::ok);
    }

    /**
     * Endpoint for editing a user.
     * @param user User to edit
     */
    @PatchMapping("/user")
    public Mono<ResponseEntity<UserDTO>> editUser(@RequestBody UserDTO user) {
        return userService.editUser(user)
                .map(ResponseEntity::ok);
    }


    /**
     * Updates the password of a user
     * @param passwordDTO DTO containing the user id and the new password
     */
    @PatchMapping("/user/password")
    public Mono<ResponseEntity<Void>> updatePassword(@RequestBody PasswordDTO passwordDTO) {
        return userService.updatePassword(passwordDTO)
                .map(ResponseEntity::ok);
    }

}

