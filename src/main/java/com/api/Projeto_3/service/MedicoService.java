package com.api.Projeto_3.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.dtos.DtosModelos.TreinamentoMedicoDto;
import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.model.TreinamentoModel;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.MedicoRespository;
import com.api.Projeto_3.repository.TreinamentoRespository;

@Service
public class MedicoService {

    @Autowired
    MedicoRespository jpaMedico;

    @Autowired
    AtletaRespository jpaAtleta;

    @Autowired
    TreinamentoRespository jpaTreinamento;

    @Transactional(readOnly = true)
    public MedicoModelo buscarMedicoPorId(String cpf) {
        return jpaMedico.findByCpf(cpf);
    }

    @Transactional(readOnly = true)
    public String buscarNomes(String cpf) {
        return jpaMedico.buscarNomePorCpf(cpf);
    }

    @Transactional(readOnly = true)
    public List<String> mostrandoAtletaNomes(Long idMedico) {
        return jpaAtleta.listAtletasVinculadosMedico(idMedico);
    }

    @Transactional(readOnly = true)
    public List<ListDasAtleta> listTab() {
        return jpaAtleta.atletListCusMedico();
    }

    @Transactional(readOnly = true)
    public List<AtletaModelo> atletasVinculados(Long medicoId) {
        return jpaAtleta.findByMedicoFk_IdOrderByName(medicoId);
    }

    @Transactional
    public boolean desvincularAtleta(Long medicoId, Long atletaId) {
        return jpaAtleta.desvincularMedico(atletaId, medicoId) > 0;
    }

    @Transactional(readOnly = true)
    public AtletaModelo buscarAtletaVinculado(Long medicoId, Long atletaId) {
        AtletaModelo atleta = jpaAtleta.findById(atletaId).orElse(null);
        
        return atleta;
    }

    @Transactional(readOnly = true)
    public List<TreinamentoMedicoDto> treinamentosDoAtleta(Long medicoId, Long atletaId) {
        return jpaTreinamento.listarDoAtletaDoMedico(atletaId, medicoId);
    }

    @Transactional
    public boolean cancelarTreinamento(Long medicoId, Long atletaId, Long treinamentoId) {
        TreinamentoModel treino = jpaTreinamento.buscarDoAtletaDoMedico(treinamentoId, atletaId, medicoId)
                .orElse(null);

        treino.setCancelado(true);
        treino.setCanceladoEm(LocalDateTime.now());
        treino.setCanceladoPor(jpaMedico.getReferenceById(medicoId));
        jpaTreinamento.save(treino);
        return true;
    }

}
