package com.example.usuariojwt.infrastructure.util;

import com.example.usuariojwt.domain.model.ERole;
import com.example.usuariojwt.domain.model.Role;
import com.example.usuariojwt.domain.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class CreateRoles implements CommandLineRunner {

    private final RoleRepository roleRepository;
	
	@Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            var rolAdmin = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(rolAdmin);
        }

        if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
            var rolUser = new Role(ERole.ROLE_USER);
            roleRepository.save(rolUser);
        }

    }

	

}
