package com.api.Projeto_3.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public interface AgendaTreinamentoRepository {

    List<LinhaDaAgenda> listarDaSemanaDoAtleta(Long semanaId, String cpf);

    record LinhaDaAgenda(
            String dia,
            Long treinoId,
            String treinoNome,
            String treinoCategoria,
            String treinoDescricao,
            LocalTime horarioInicio,
            LocalTime horarioFim,
            Boolean treinoCancelado,
            Boolean treinoConcluido,
            Long exercicioId,
            String exercicioNome,
            String exercicioDescricao,
            Double exercicioDuracao,
            Boolean exercicioConcluido) {

        public LinhaDaAgenda {
            treinoCancelado = Boolean.TRUE.equals(treinoCancelado);
            treinoConcluido = Boolean.TRUE.equals(treinoConcluido);
            exercicioDuracao = Objects.requireNonNullElse(exercicioDuracao, 0.0);
            exercicioConcluido = Boolean.TRUE.equals(exercicioConcluido);
        }
    }
}
