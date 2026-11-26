package com.api.Projeto_3.dtos.DtosModelos;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;

class PerfilsDtosTest {
    @Test
    void validaOsDadosDoPerfilDaMoradiaEDosPais() {
        PerfilsDtos perfil = new PerfilsDtos();
        perfil.setDataNascimento(LocalDate.now().plusDays(1));
        perfil.setCpf("000.000.000-00");
        perfil.setEmail("email-invalido");
        perfil.setMoradia_fk(new MoradiaDto());

        AfiliacaoDtos pais = new AfiliacaoDtos();
        pais.setMaeNome("Ana");
        pais.setPaiEmail("email-invalido");
        perfil.setPais_fk(pais);

        try (var factory = Validation.buildDefaultValidatorFactory()) {
            assertThat(factory.getValidator().validate(perfil))
                    .extracting(erro -> erro.getPropertyPath().toString())
                    .contains("dataNascimento", "cpf", "email", "moradia_fk.cidade", "pais_fk.maeNome",
                            "pais_fk.paiEmail");
        }
    }
}
