package com.garden.api.user;

import com.garden.api.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        return getCurrentUserEmail().flatMap(userRepository::findByEmail);
    }

    public User getCurrentUserOrThrow() {
        return getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Optional<String> getCurrentUserEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication).map(Authentication::getName);
    }

}