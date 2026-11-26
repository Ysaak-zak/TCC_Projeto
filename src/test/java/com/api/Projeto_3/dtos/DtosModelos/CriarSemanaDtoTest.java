package com.api.Projeto_3.dtos.DtosModelos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.bind.WebDataBinder;

class CriarSemanaDtoTest {
    @Test
    void recebeOsCamposDoFormulario() {
        CriarSemanaDto semana = new CriarSemanaDto();
        WebDataBinder binder = new WebDataBinder(semana);

        binder.bind(new MutablePropertyValues(Map.of(
                "nome", "Semana 3",
                "descricao", "Treinos da semana",
                "atletaId", "42")));

        assertThat(binder.getBindingResult().hasErrors()).isFalse();
        assertThat(semana.getNome()).isEqualTo("Semana 3");
        assertThat(semana.getDescricao()).isEqualTo("Treinos da semana");
        assertThat(semana.getAtletaId()).isEqualTo(42L);
    }
}
