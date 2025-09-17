package com.garden.api.user;


import com.garden.api.email.EmailDetails;
import com.garden.api.exceptions.ResourceNotFoundException;
import com.garden.api.exceptions.UserException;
import com.garden.api.role.RoleEnum;
import com.garden.api.user.dtos.ChangePasswordRequest;
import com.garden.api.user.dtos.ForgotPasswordRequest;
import com.garden.api.user.dtos.ResetPasswordRequest;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;
    private final EmailDetails emailDetails;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<User> findAllByRole(RoleEnum roleEnum, Pageable pageable) {
        return userRepository.findAllByRoleNameOrdered(roleEnum.name(), pageable);
    }

    @Transactional
    public void deleteById(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found"));
        userRepository.delete(user);
    }

    @Transactional
    public void selfDeleteAccount(@NotNull User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException("Password doesn't match!");
        }
        deleteById(user.getId());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found"));
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new UserException("User not found with email: " + forgotPasswordRequest.getEmail()));
        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        userRepository.save(user);

//        emailService.sendForgotPasswordEmail(user);

    }

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest, String token) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new UserException("Invalid reset token: " + token);
        }
        if (resetPasswordRequest.getNewPassword() == null) {
            throw new UserException("New password cannot be null");
        }
        if (passwordEncoder.matches(resetPasswordRequest.getNewPassword(), user.getPassword())) {
            throw new UserException("New password cannot be the same as old password");
        }
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword())) {
            throw new UserException("Passwords do not match.Please make sure that the new password and confirm password " +
                    "are the same!");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        var currentUser = currentUserService.getCurrentUserOrThrow();

        try {
            if (!isPasswordValid(changePasswordRequest.getOldPassword(), currentUser.getPassword())) {
                throw new UserException("Old password is incorrect");
            }

            validateNewPassword(changePasswordRequest);

            currentUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

            emailDetails.setTo(currentUser.getEmail());
            emailDetails.setSubject("Fjalëkalimi juaj është ndryshuar");
            emailDetails.setText("I nderuar klient,\n\n" +
                    "Ky është një njoftim që fjalëkalimi i llogarisë suaj në Jomuntu është ndryshuar me sukses. Nëse e keni bërë këtë ndryshim, nuk ka nevojë për veprime të mëtejshme.\n\n" +
                    "Nëse nuk keni kërkuar ndryshimin e fjalëkalimit, ju lutemi të kontaktoni menjëherë ekipin tonë në jomuntu@gmail.com për të siguruar llogarinë tuaj.\n\n" +
                    "Faleminderit që përdorni Jomuntu!\n\n" +
                    "Me respekt,\n" +
                    "Ekipi Jomuntu");
//            emailService.send(emailDetails);
        } catch (RuntimeException e) {
            throw new UserException(e.getMessage());
        } catch (Exception e) {
            throw new UserException("Failed to change the password");
        }
    }

    private boolean isPasswordValid(String oldPassword, String encodedOldPassword) {
        return passwordEncoder.matches(oldPassword, encodedOldPassword);
    }

    private void validateNewPassword(ChangePasswordRequest request) {
        if (passwordEncoder.matches(request.getOldPassword(), passwordEncoder.encode(request.getNewPassword()))) {
            throw new UserException("New password do not match");
        }
    }

    @Transactional
    public void updateName(User user, String newName) {
        if (newName == null || newName.isBlank()) {
            throw new UserException("Name cannot be null or blank");
        }

        user.setName(newName);
        userRepository.save(user);
    }

    public Page<User> findAllByRoleAndName(RoleEnum roleEnum, String search, Pageable pageable) {
        String roleName = (roleEnum != null) ? roleEnum.name() : null;
        return userRepository.findAllByRoleAndName(roleName, search, pageable);
    }


}