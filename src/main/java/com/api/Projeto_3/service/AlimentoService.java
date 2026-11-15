package com.api.Projeto_3.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.dtos.DtosModelos.RecomendacaoAlimentarDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.model.RecomendacaoAlimentar;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.MedicoRespository;
import com.api.Projeto_3.repository.RecomendacaoAlimentarRepository;

@Service
@RequiredArgsConstructor
public class AlimentoService {
    private final MedicoRespository jpaMedico;
    private final AtletaRespository jpaAtleta;
    private final RecomendacaoAlimentarRepository recomendacaoRepository;

    @Transactional(readOnly = true)
    public AtletaModelo buscarAtletaVinculado(Long medicoId, Long atletaId) {
        return jpaAtleta.findById(atletaId)
                .filter(atleta -> atleta.getMedico_fk() != null
                        && medicoId.equals(atleta.getMedico_fk().getId()))
                .orElse(null);
    }

    public boolean dataRevisaoInvalida(RecomendacaoAlimentarDto dados) {
        return dados.getDataInicio() != null && dados.getDataRevisao() != null
                && dados.getDataRevisao().isBefore(dados.getDataInicio());
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
        MedicoModelo medico = jpaMedico.findById(medicoId).orElse(null);
        AtletaModelo atleta = buscarAtletaVinculado(medicoId, atletaId);
        if (medico == null || atleta == null) {
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
