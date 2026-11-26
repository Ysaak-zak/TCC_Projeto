package com.api.Projeto_3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.Projeto_3.model.SemanaModel;

public interface SemanaResponitory extends JpaRepository<SemanaModel, Long> {
    @Query(value = """
            SELECT s.*
            FROM semana s
            INNER JOIN atletas a ON a.id = s.atleta_fk
            INNER JOIN treinadores t ON t.id = a.atleta_trenador_fk
            INNER JOIN perfis_modelo p ON p.id = t.id
            WHERE p.cpf = :cpf
            ORDER BY s.id DESC
            """, nativeQuery = true)
    List<SemanaModel> listarPorTreinador(@Param("cpf") String cpf);
}
