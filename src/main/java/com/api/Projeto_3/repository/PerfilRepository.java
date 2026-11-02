package com.api.Projeto_3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.Projeto_3.model.PerfisModelo;


public interface PerfilRepository extends JpaRepository <PerfisModelo , Long> {
       Optional<PerfisModelo> findByCpf(String cpf);

       @Query(value = "SELECT * FROM perfis_modelo WHERE regexp_replace(cpf, '[^0-9]', '', 'g') = regexp_replace(:cpf, '[^0-9]', '', 'g') LIMIT 1", nativeQuery = true)
       Optional<PerfisModelo> findByCpfSemPontuacao(String cpf);

}
