package com.api.Projeto_3.dtos.DtosModelos;

import java.time.LocalDate;

public record AlimentosAtleDtos(Long id, Long atletaId, Long medicoId, String medicoNome,
        String refeicao, String objetivo, String orientacao, LocalDate dataInicio,
        LocalDate dataRevisao, boolean concluido) {
}
