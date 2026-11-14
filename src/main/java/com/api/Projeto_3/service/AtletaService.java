package com.api.Projeto_3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.RecomendacaoAlimentar;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.RecomendacaoAlimentarRepository;

import jakarta.transaction.Transactional;

@Service 
public class AtletaService {
    @Autowired
    AtletaRespository jpa;

    @Autowired
    RecomendacaoAlimentarRepository recomendacaoRepository;

    public AtletaModelo buscarAtletaPorCpf(String cpf) {
        return jpa.findByCpf(cpf).stream().findFirst().orElse(null);
    }

    public List<RecomendacaoAlimentar> buscarRecomendacoesAlimentares(Long atletaId) {
        return recomendacaoRepository.findByAtletaIdOrderByDataInicioDesc(atletaId);
    }


    @Transactional
    public void insertFkATreinador(Long atletaid , Long treinaid){
        jpa.vincularTreinador(atletaid, treinaid);
    }

    @Transactional 
    public  int quntAtletaVinculadosTreinador(Long idTreinador){
        return jpa.atletaVinculados(idTreinador);
    }

     @Transactional 
    public  int quntAtletaSemVinculadosTreinador(){
        return jpa.atletaSemVinculados();
    }

    @Transactional
    public void insertFkAMedico(Long atletaid , Long medicoId){
        jpa.vincularMedico(atletaid, medicoId);
    }

    @Transactional 
    public int quntAtletaVinculadosMedico(Long idMedico){
        return jpa.atletaVinculadosMedico(idMedico);
    }

    @Transactional 
    public int quntAtletaSemVinculadosMedico(){
        return jpa.atletaSemVinculadosMedico();
    }

}
