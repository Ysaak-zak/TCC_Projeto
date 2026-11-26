package com.api.Projeto_3.dtos.DtosModelos;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.api.Projeto_3.model.enums.DiasEnum;

public record TreinamentoMedicoDto(
        Long id,
        String semanaNome,
        DiasEnum dia,
        String nome,
        String categoria,
        String descricao,
        LocalTime horarioInicio,
        LocalTime horarioFim,
        Boolean cancelado,
        LocalDateTime canceladoEm,
        String nomeMedicoCancelamento) {
}
