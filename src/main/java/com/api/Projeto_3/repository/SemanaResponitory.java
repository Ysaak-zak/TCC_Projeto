package com.api.Projeto_3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.api.Projeto_3.model.SemanaModel;

public interface SemanaResponitory extends JpaRepository<SemanaModel, Long> {
    @Query("select s from SemanaModel s join fetch s.atleta_fk a where a.treinador_fk.cpf = :cpf order by s.id desc")
    List<SemanaModel> listarPorTreinador(String cpf);
}
