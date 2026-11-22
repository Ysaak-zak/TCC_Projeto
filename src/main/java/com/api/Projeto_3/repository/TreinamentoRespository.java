package com.api.Projeto_3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.Projeto_3.model.TreinamentoModel;
import jakarta.persistence.LockModeType;

@Repository
public interface TreinamentoRespository extends JpaRepository<TreinamentoModel, Long> {

    @Query("""
            select distinct t from TreinamentoModel t
            join fetch t.dia_fk d
            join d.semana_fk s
            join s.atleta_fk a
            left join fetch t.exercicios
            where s.id = :semanaId and a.cpf = :cpf
            """)
    List<TreinamentoModel> listarDaSemanaDoAtleta(@Param("semanaId") Long semanaId, @Param("cpf") String cpf);

    // Todos os treinamentos de um atleta, desde que ele esteja vinculado ao médico informado.
    // A ordem final (semana, dia da semana, horário) é feita no MedicoService.
    @Query("select t from TreinamentoModel t " +
           "join fetch t.dia_fk d " +
           "join fetch d.semana_fk s " +
           "join fetch s.atleta_fk a " +
           "left join fetch t.canceladoPor " +
           "where a.id = :atletaId and a.medico_fk.id = :medicoId")
    List<TreinamentoModel> listarDoAtletaDoMedico(@Param("atletaId") Long atletaId,
                                                  @Param("medicoId") Long medicoId);

    // Um treinamento específico, só se ele pertencer a um atleta vinculado ao médico informado.
    // Se o treino for de outro atleta (ou de atleta de outro médico), o resultado é vazio.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from TreinamentoModel t " +
           "join t.dia_fk d " +
           "join d.semana_fk s " +
           "join s.atleta_fk a " +
           "where t.id = :treinamentoId and a.id = :atletaId and a.medico_fk.id = :medicoId")
    Optional<TreinamentoModel> buscarDoAtletaDoMedico(@Param("treinamentoId") Long treinamentoId,
                                                      @Param("atletaId") Long atletaId,
                                                      @Param("medicoId") Long medicoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t from TreinamentoModel t
            join t.dia_fk d
            join d.semana_fk s
            join s.atleta_fk a
            where t.id = :treinoId and a.cpf = :cpf
            """)
    Optional<TreinamentoModel> buscarDoAtletaParaAtualizar(@Param("treinoId") Long treinoId,
                                                          @Param("cpf") String cpf);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t from TreinamentoModel t
            join t.dia_fk d
            join d.semana_fk s
            join s.atleta_fk a
            where t.id = :treinoId and s.id = :semanaId and a.treinador_fk.cpf = :cpf
            """)
    Optional<TreinamentoModel> buscarDoTreinadorParaAtualizar(@Param("treinoId") Long treinoId,
                                                             @Param("semanaId") Long semanaId,
                                                             @Param("cpf") String cpf);
}
