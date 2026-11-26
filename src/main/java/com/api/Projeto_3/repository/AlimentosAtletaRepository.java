package com.api.Projeto_3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.dtos.DtosModelos.AlimentosAtleDtos;
import com.api.Projeto_3.model.RecomendacaoAlimentar;

public interface AlimentosAtletaRepository extends Repository<RecomendacaoAlimentar, Long> {
    @Query(value = """
            SELECT r.id, r.atleta_id, r.medico_id, m.per_nome AS medico_nome,
                   r.refeicao, r.objetivo, r.orientacao, r.data_inicio, r.data_revisao, r.concluido
            FROM recomendacao_alimentar r
            INNER JOIN perfis_modelo m ON m.id = r.medico_id
            WHERE r.atleta_id = :atletaId
            ORDER BY r.data_inicio DESC, r.id DESC
            """, nativeQuery = true)
    List<AlimentosAtleDtos> listarPorAtleta(@Param("atletaId") Long atletaId);

    
    @Query(value = """
            UPDATE recomendacao_alimentar
            SET concluido = true
            WHERE id = :id AND atleta_id = :atletaId
            """, nativeQuery = true)
    int marcarConcluido(@Param("id") Long id, @Param("atletaId") Long atletaId);
}
