package com.api.Projeto_3.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.dtos.model.RecomendacaoAlimentarDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.model.RecomendacaoAlimentar;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.MedicoRespository;
import com.api.Projeto_3.repository.RecomendacaoAlimentarRepository;

@Service
public class MedicoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

    @Autowired
    MedicoRespository jpaMedico;

    @Autowired
    RecomendacaoAlimentarRepository recomendacaoRepository;

    @Autowired
    AtletaRespository jpaAtleta;

    @Transactional(readOnly = true)
    public MedicoModelo buscarMedicoPorId(String cpf) {
        logger.info("Buscando dados do médico através do cpf {}", cpf);
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
        logger.info("Listando atletas sem médico vinculado");
        List<ListDasAtleta> list = jpaAtleta.atletListCusMedico();
        if (list == null || list.isEmpty()) {
            logger.warn("Nenhum atleta disponível para vínculo encontrado");
        } else {
            logger.info("Total de atletas disponíveis: {}", list.size());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<AtletaModelo> atletasVinculados(Long medicoId) {
        return jpaAtleta.findByMedicoFk_IdOrderByName(medicoId);
    }

    @Transactional(readOnly = true)
    public List<RecomendacaoAlimentar> recomendacoes(Long medicoId, Long atletaId) {
        return recomendacaoRepository.findByAtletaIdAndMedicoIdOrderByDataInicioDesc(atletaId, medicoId);
    }

    @Transactional
    public boolean recomendarAlimentacao(Long medicoId, Long atletaId, RecomendacaoAlimentarDto dados) {
        if (dados.getDataInicio() == null) {
            return false;
        }
        MedicoModelo medico = jpaMedico.findById(medicoId).orElse(null);
        AtletaModelo atleta = jpaAtleta.findById(atletaId).orElse(null);
        if (medico == null || atleta == null || atleta.getMedico_fk() == null
                || !medicoId.equals(atleta.getMedico_fk().getId())) {
            return false;
        }
        if (dados.getDataRevisao() != null && dados.getDataRevisao().isBefore(dados.getDataInicio())) {
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
