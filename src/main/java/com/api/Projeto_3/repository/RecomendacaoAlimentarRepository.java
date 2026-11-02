package com.api.Projeto_3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.Projeto_3.model.RecomendacaoAlimentar;

public interface RecomendacaoAlimentarRepository extends JpaRepository<RecomendacaoAlimentar, Long> {
    List<RecomendacaoAlimentar> findByAtletaIdAndMedicoIdOrderByDataInicioDesc(Long atletaId, Long medicoId);
}
