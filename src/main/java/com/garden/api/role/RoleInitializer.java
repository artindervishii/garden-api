package com.garden.api.role;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Order(1) // Run before UserInitializer
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args){
        Arrays.stream(RoleEnum.values())
                .map(Enum::name)
                .forEach(roleName -> {
                    try {
                        roleRepository.findByName(roleName)
                                .orElseGet(() -> roleRepository.save(new Role(roleName)));
                    } catch (RuntimeException e) {
                        System.err.println("Error saving role " + roleName + ": " + e.getMessage());
                    }
                });
    }
}
