package com.api.Projeto_3.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.model.RolesModel;
import com.api.Projeto_3.model.enums.RoleAuthority;
import com.api.Projeto_3.repository.RoleRepository;

@Component
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        roleRepository.bloquearCriacaoDeRoles();
        for (RoleAuthority authority : RoleAuthority.values()) {
            String nomeRole = authority.name();
            if (!roleRepository.existsByAuthority(nomeRole)) {
                RolesModel role = new RolesModel();
                role.setAuthority(nomeRole);
                roleRepository.save(role);
            }
        }
    }
}
