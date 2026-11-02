package com.api.Projeto_3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.Projeto_3.model.MedicoModelo;

@Repository
public interface MedicoRespository extends JpaRepository<MedicoModelo, Long> {

    MedicoModelo findByCpf(String cpf);

    @Query(value = "SELECT per.per_nome FROM perfis_modelo AS per "
            + " INNER JOIN medicos med ON med.id = per.id "
            + " WHERE per.cpf = :cpf ", nativeQuery = true)
    String buscarNomePorCpf(String cpf);
}
