package com.api.Projeto_3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.Projeto_3.model.SemanaModel;

public interface DashRepository extends JpaRepository<SemanaModel, Long> {
    @Query("select s from SemanaModel s where s.atleta_fk.cpf = :cpf order by s.id desc")
    List<SemanaModel> buscarSemanasDoAtleta(@Param("cpf") String cpf);
}
