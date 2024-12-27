package com.secure.log.config;

import com.secure.log.enums.AppRole;
import com.secure.log.model.Role;
import com.secure.log.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByRoleName(AppRole.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(AppRole.ROLE_USER));
        }
        if (roleRepository.findByRoleName(AppRole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(AppRole.ROLE_ADMIN));
        }
    }
}