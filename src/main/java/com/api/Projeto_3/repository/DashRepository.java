package com.api.Projeto_3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.Projeto_3.model.SemanaModel;

public interface DashRepository extends JpaRepository<SemanaModel, Long> {
    @Query(value = """
            SELECT s.*
            FROM semana s
            JOIN atletas a ON a.id = s.atleta_fk
            JOIN perfis_modelo p ON p.id = a.id
            WHERE p.cpf = :cpf
            ORDER BY s.id DESC
            """, nativeQuery = true)
    List<SemanaModel> buscarSemanasDoAtleta(@Param("cpf") String cpf);
}
