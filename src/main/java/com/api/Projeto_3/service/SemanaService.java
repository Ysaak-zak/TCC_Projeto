package com.api.Projeto_3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.Projeto_3.dtos.interfaceCustomer.SemanaDtos;
import com.api.Projeto_3.model.DiaModel;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.SemanaResponitory;

@Service 
public class SemanaService {
    
       private  final SemanaResponitory senJpa;
         private static final Logger logger = LoggerFactory.getLogger(SemanaService.class);
 
        public  void  creiSemana(SemanaDtos se){
        SemanaModel en = new SemanaModel();

        en.setDescricao(se.getDiscricao());
        en.setAtleta_fk(null);
           
        for(DiasEnum diaNomes : DiasEnum.values()){
            DiaModel dia = new DiaModel();
            dia.setNome(diaNomes);
            en.adicionarDia(dia);
            
        }
        logger.info("Semana criada com sucesso",en.toString());
        senJpa.save(en);    
    }








        SemanaService(SemanaResponitory senJpa) {
        this.senJpa = senJpa;
    }
       
}
