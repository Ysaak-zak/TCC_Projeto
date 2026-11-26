package com.api.Projeto_3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import com.api.Projeto_3.model.PerfisModelo;

public interface PerfilRepository extends JpaRepository<PerfisModelo, Long> {
    Optional<PerfisModelo> findByCpf(String cpf);

    @NativeQuery("""
            SELECT id
            FROM perfis_modelo
            WHERE regexp_replace(cpf, '[^0-9]', '', 'g') =
                  regexp_replace(:cpf, '[^0-9]', '', 'g')
            LIMIT 1
            """)
    Optional<Long> findIdByCpfSemPontuacao(@Param("cpf") String cpf);
}
