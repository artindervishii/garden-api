package com.garden.api.user;

import com.garden.api.email.EmailService;
import com.garden.api.exceptions.ResourceNotFoundException;
import com.garden.api.role.Role;
import com.garden.api.role.RoleRepository;
import com.garden.api.user.dtos.CreateUserRequest;
import com.garden.api.user.dtos.LoginUserRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    @Transactional
    public Optional<User> signup(CreateUserRequest input) {

        if (userRepository.existsByEmail(input.getEmail())) {
            throw new ResourceNotFoundException("User with email " + input.getEmail() + " already exists.");
        }
        User user = new User();

        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        List<Role> roles = roleRepository.findAllByNameIn(input.getRole());
        user.setRoles(roles);
        user.setProvider("Basic");

        userRepository.save(user);
        emailService.sendVerificationEmail(user);

        return Optional.of(user);
    }

    public User authenticate(LoginUserRequest input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        return user;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .toList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}