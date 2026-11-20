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
        // Serializa a consulta e a criação quando várias instâncias iniciam juntas.
        roleRepository.bloquearCriacaoDeRoles();
        for (RoleAuthority authority : RoleAuthority.values()) {
            criarSeAusente(authority.name());
        }
    }

    private void criarSeAusente(String authority) {
        if (roleRepository.findFirstByAuthorityOrderByIdAsc(authority).isEmpty()) {
            RolesModel role = new RolesModel();
            role.setAuthority(authority);
            roleRepository.save(role);
        }
    }
}
