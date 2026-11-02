package com.api.Projeto_3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.model.AtletaModelo;

import jakarta.transaction.Transactional;

@Service 
public class AtletaService {
    @Autowired
    AtletaRespository jpa;

    public AtletaModelo buscarAtletaPorCpf(String cpf) {
        return jpa.findByCpf(cpf).stream().findFirst().orElse(null);
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
