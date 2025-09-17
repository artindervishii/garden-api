package com.garden.api.user;

import com.garden.api.exceptions.UserException;
import com.garden.api.role.RoleEnum;
import com.garden.api.user.dtos.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    public static final String BASE_PATH_V1 = "/v1/users";

    private final UserService userService;

    private final UserMapper userMapper;

    private final CurrentUserService currentUserService;

    @GetMapping(BASE_PATH_V1 + "/current-user")
    public ResponseEntity<UserResponse> getUserDetails() {
        User currentUser = currentUserService.getCurrentUserOrThrow();
        return ResponseEntity.ok(userMapper.mapToDto(currentUser));
    }

    @GetMapping(BASE_PATH_V1)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> allUsersByRoleOrName(
            @RequestParam(value = "role", required = false) String roleParam,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault Pageable pageable
    ) {
        RoleEnum roleEnum = null;
        if (roleParam != null && !roleParam.isBlank()) {
            try {
                roleEnum = RoleEnum.valueOf(roleParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new UserException("Invalid role: " + roleParam);
            }
        }

        Page<User> users = userService.findAllByRoleAndName(roleEnum, search, pageable);
        return users.map(userMapper::mapToDto);
    }

    @DeleteMapping(BASE_PATH_V1 + "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(BASE_PATH_V1 + "/self")
    public ResponseEntity<Void> deleteAccount(@Valid @RequestBody DeleteUserRequest deleteUserRequest) {
        User user = currentUserService.getCurrentUserOrThrow();
        userService.selfDeleteAccount(user, deleteUserRequest.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(BASE_PATH_V1 + "/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.mapToDto(user));
    }

    @PostMapping(BASE_PATH_V1 + "/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        userService.forgotPassword(forgotPasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(BASE_PATH_V1 + "/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest, @RequestParam("token") String token) {
        userService.resetPassword(resetPasswordRequest, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping({BASE_PATH_V1 + "/change-password"})
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(BASE_PATH_V1 + "/name")
    public ResponseEntity<Void> updateName(@Valid @RequestBody UpdateNameRequest updateNameRequest) {
        User currentUser = currentUserService.getCurrentUserOrThrow();
        userService.updateName(currentUser, updateNameRequest.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}