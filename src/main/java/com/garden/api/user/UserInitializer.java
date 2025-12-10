package com.garden.api.user;

import com.garden.api.role.Role;
import com.garden.api.role.RoleEnum;
import com.garden.api.role.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default.email}")
    private String defaultAdminEmail;

    @Value("${admin.default.password}")
    private String defaultAdminPassword;

    @Value("${admin.default.name}")
    private String defaultAdminName;

    public UserInitializer(UserRepository userRepository, 
                          RoleRepository roleRepository, 
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(defaultAdminEmail).isPresent()) {
            System.out.println("Admin user already exists with email: " + defaultAdminEmail);
            return;
        }

        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN.name()).orElseThrow(() -> new RuntimeException("ADMIN role not found. Make sure RoleInitializer runs first."));

        User adminUser = new User();
        adminUser.setEmail(defaultAdminEmail);
        adminUser.setName(defaultAdminName);
        adminUser.setPassword(passwordEncoder.encode(defaultAdminPassword));
        adminUser.setProvider("Basic");

        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        adminUser.setRoles(roles);

        userRepository.save(adminUser);
    }
}

