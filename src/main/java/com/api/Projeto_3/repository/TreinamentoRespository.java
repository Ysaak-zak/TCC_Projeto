package com.api.Projeto_3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.Projeto_3.model.TreinamentoModel;

@Repository
public interface TreinamentoRespository extends JpaRepository<TreinamentoModel, Long>, AgendaTreinamentoRepository {

        @NativeQuery("""
                        SELECT t.*
                        FROM treinamento t
                        JOIN dia d ON d.id = t.dia_fk
                        JOIN semana s ON s.id = d.semana_fk
                        JOIN atletas a ON a.id = s.atleta_fk
                        WHERE a.id = :atletaId
                          AND a.atleta_medico_fk = :medicoId
                        """)
        List<TreinamentoModel> listarDoAtletaDoMedico(@Param("atletaId") Long atletaId, @Param("medicoId") Long medicoId);

        @NativeQuery("""
                        SELECT t.*
                        FROM treinamento t
                        JOIN dia d ON d.id = t.dia_fk
                        JOIN semana s ON s.id = d.semana_fk
                        JOIN atletas a ON a.id = s.atleta_fk
                        WHERE t.id = :treinamentoId
                          AND a.id = :atletaId
                          AND a.atleta_medico_fk = :medicoId
                        FOR UPDATE OF t
                        """)
        Optional<TreinamentoModel> buscarDoAtletaDoMedico(@Param("treinamentoId") Long treinamentoId,
                        @Param("atletaId") Long atletaId, @Param("medicoId") Long medicoId);

        @NativeQuery("""
                        SELECT t.*
                        FROM treinamento t
                        JOIN dia d ON d.id = t.dia_fk
                        JOIN semana s ON s.id = d.semana_fk
                        JOIN atletas a ON a.id = s.atleta_fk
                        JOIN perfis_modelo p ON p.id = a.id
                        WHERE t.id = :treinoId
                          AND p.cpf = :cpf
                        FOR UPDATE OF t
                        """)
        Optional<TreinamentoModel> buscarDoAtletaParaAtualizar(@Param("treinoId") Long treinoId, @Param("cpf") String cpf);

        @NativeQuery("""
                        SELECT t.*
                        FROM treinamento t
                        JOIN dia d ON d.id = t.dia_fk
                        JOIN semana s ON s.id = d.semana_fk
                        JOIN atletas a ON a.id = s.atleta_fk
                        JOIN treinadores tr ON tr.id = a.atleta_trenador_fk
                        JOIN perfis_modelo p ON p.id = tr.id
                        WHERE t.id = :treinoId
                          AND s.id = :semanaId
                          AND p.cpf = :cpf
                        FOR UPDATE OF t
                        """)
        Optional<TreinamentoModel> buscarDoTreinadorParaAtualizar(@Param("treinoId") Long treinoId,
                        @Param("semanaId") Long semanaId, @Param("cpf") String cpf);
}
