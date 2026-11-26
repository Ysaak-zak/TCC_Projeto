package com.api.Projeto_3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.Projeto_3.dtos.interfaceCustomer.LinhaDaAgendaCustomer;

public interface AgendaTreinamentoRepository {

    @Query(value = """
            SELECT d.nome AS dia,
                   t.id AS "treinoId", t.nome AS "treinoNome",
                   t.categoria AS "treinoCategoria", t.descricao AS "treinoDescricao",
                   t.horario_inicio AS "horarioInicio", t.horario_fim AS "horarioFim",
                   COALESCE(t.cancelado, false) AS "treinoCancelado",
                   COALESCE(t.concluido, false) AS "treinoConcluido",
                   e.id AS "exercicioId", e.nome AS "exercicioNome",
                   e.descricao AS "exercicioDescricao",
                   COALESCE(e.duracacao, 0.0) AS "exercicioDuracao",
                   COALESCE(e.concluido, false) AS "exercicioConcluido"
            FROM treinamento t
            INNER JOIN dia d ON d.id = t.dia_fk
            INNER JOIN semana s ON s.id = d.semana_fk
            INNER JOIN perfis_modelo p ON p.id = s.atleta_fk
            INNER JOIN exercicio e ON e.treinamento_fk = t.id
            WHERE s.id = :semanaId AND p.cpf = :cpf
            ORDER BY d.nome, t.horario_inicio NULLS LAST, t.id, e.id
            """, nativeQuery = true)
    List<LinhaDaAgendaCustomer> listarDaSemanaDoAtleta(@Param("semanaId") Long semanaId, @Param("cpf") String cpf);
}
