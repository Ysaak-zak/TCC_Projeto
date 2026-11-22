package com.api.Projeto_3.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.dtos.DtosModelos.RecomendacaoAlimentarDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.model.RecomendacaoAlimentar;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.MedicoRespository;
import com.api.Projeto_3.repository.RecomendacaoAlimentarRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlimentoService {
    private final MedicoRespository medicoRepository;
    private final AtletaRespository atletaRepository;
    private final RecomendacaoAlimentarRepository recomendacaoRepository;

    @Transactional(readOnly = true)
    public AtletaModelo buscarAtletaVinculado(Long medicoId, Long atletaId) {
        AtletaModelo atleta = atletaRepository.findById(atletaId).orElse(null);
        if (atleta == null) {
            return null;
        }

        MedicoModelo medicoDoAtleta = atleta.getMedico_fk();
        if (medicoDoAtleta == null || !medicoId.equals(medicoDoAtleta.getId())) {
            return null;
        }

        return atleta;
    }

    public boolean dataRevisaoInvalida(RecomendacaoAlimentarDto dados) {
        LocalDate dataInicio = dados.getDataInicio();
        LocalDate dataRevisao = dados.getDataRevisao();
        if (dataInicio == null || dataRevisao == null) {
            return false;
        }

        return dataRevisao.isBefore(dataInicio);
    }

    @Transactional(readOnly = true)
    public List<RecomendacaoAlimentar> recomendacoes(Long medicoId, Long atletaId) {
        return recomendacaoRepository.findByAtletaIdAndMedicoIdOrderByDataInicioDesc(atletaId, medicoId);
    }

    @Transactional
    public boolean recomendarAlimentacao(Long medicoId, Long atletaId, RecomendacaoAlimentarDto dados) {
        if (dados.getDataInicio() == null || dataRevisaoInvalida(dados)) {
            return false;
        }

        MedicoModelo medico = medicoRepository.findById(medicoId).orElse(null);
        if (medico == null) {
            return false;
        }

        AtletaModelo atleta = buscarAtletaVinculado(medicoId, atletaId);
        if (atleta == null) {
            return false;
        }

        RecomendacaoAlimentar recomendacao = new RecomendacaoAlimentar();
        recomendacao.setMedico(medico);
        recomendacao.setAtleta(atleta);
        recomendacao.setRefeicao(dados.getRefeicao().trim());
        recomendacao.setObjetivo(dados.getObjetivo().trim());
        recomendacao.setOrientacao(dados.getOrientacao().trim());
        recomendacao.setDataInicio(dados.getDataInicio());
        recomendacao.setDataRevisao(dados.getDataRevisao());

        recomendacaoRepository.save(recomendacao);
        return true;
    }
}
