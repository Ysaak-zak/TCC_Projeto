package com.api.Projeto_3.dtos.DtosModelos;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.api.Projeto_3.model.enums.DiasEnum;

import jakarta.validation.Validation;

class CriarTreinoDtoTest {
    @ParameterizedTest
    @CsvSource({
        "08:00, 09:00,",
        "06:00, 22:00,",
        "21:00, 07:00, horarioValido",
        "08:00, 08:00, horarioValido",
        "05:59, 09:00, horarioValido",
        "08:00, 22:01, horarioValido",
        ", 09:00, horarioInicio",
        "08:00, , horarioFim"
    })
    void validaOsHorariosDoCadastro(LocalTime inicio, LocalTime fim, String campoInvalido) {
        CriarTreinoDto treino = new CriarTreinoDto();
        treino.setSemanaId(1L);
        treino.setDia(DiasEnum.SEGUNDA);
        treino.setNome("Preparação física");
        treino.setCategoria("Força");
        treino.setHorarioInicio(inicio);
        treino.setHorarioFim(fim);

        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var erros = factory.getValidator().validate(treino);
            if (campoInvalido == null) {
                assertThat(erros).isEmpty();
            } else {
                assertThat(erros).extracting(erro -> erro.getPropertyPath().toString()).containsExactly(campoInvalido);
            }
        }
    }
}
