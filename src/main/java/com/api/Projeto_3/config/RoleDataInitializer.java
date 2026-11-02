package com.api.Projeto_3.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.Projeto_3.model.RolesModel;
import com.api.Projeto_3.repository.RoleRepository;

@Component
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        atualizarOuCriar(1L, "ROLE_ATLETA");
        atualizarOuCriar(2L, "ROLE_MEDICO");
        atualizarOuCriar(3L, "ROLE_TREINADOR");
        atualizarOuCriar(4L, "ROLE_ADMIN");
    }

    private void atualizarOuCriar(Long id, String authority) {
        RolesModel role = roleRepository.findById(id).orElseGet(RolesModel::new);
        role.setId(id);
        role.setAuthority(authority);
        roleRepository.save(role);
    }
}
