package com.api.Projeto_3.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.model.TreinamentoModel;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.MedicoRespository;
import com.api.Projeto_3.repository.TreinamentoRespository;

@Service
public class MedicoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

    @Autowired
    MedicoRespository jpaMedico;

    @Autowired
    AtletaRespository jpaAtleta;

    @Autowired
    TreinamentoRespository jpaTreinamento;

    // Tamanho do campo 'motivoCancelamento' (VARCHAR(255) padrão do Hibernate).
    public static final int MOTIVO_CANCELAMENTO_MAX = 255;

    public enum ResultadoCancelamento {
        CANCELADO, JA_CANCELADO, MOTIVO_INVALIDO, NAO_ENCONTRADO
    }

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

    @Transactional
    public boolean desvincularAtleta(Long medicoId, Long atletaId) {
        return jpaAtleta.desvincularMedico(atletaId, medicoId) > 0;
    }

    // Só devolve o atleta se ele estiver vinculado a ESTE médico; caso contrário, null.
    @Transactional(readOnly = true)
    public AtletaModelo buscarAtletaVinculado(Long medicoId, Long atletaId) {
        return jpaAtleta.findById(atletaId)
                .filter(a -> a.getMedico_fk() != null && medicoId.equals(a.getMedico_fk().getId()))
                .orElse(null);
    }

    // Treinamentos do atleta: semana mais recente primeiro, depois dia da semana e horário de início.
    @Transactional(readOnly = true)
    public List<TreinamentoModel> treinamentosDoAtleta(Long medicoId, Long atletaId) {
        List<TreinamentoModel> lista = jpaTreinamento.listarDoAtletaDoMedico(atletaId, medicoId);
        return lista.stream()
                .sorted(Comparator
                        .comparing((TreinamentoModel t) -> t.getDia_fk().getSemana_fk().getId(), Comparator.reverseOrder())
                        .thenComparing(t -> t.getDia_fk().getNome())
                        .thenComparing(TreinamentoModel::getHorarioInicio, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(TreinamentoModel::getId))
                .toList();
    }

    // Cancelamento "suave": o treino continua no banco, marcado como cancelado (com motivo, data e médico).
    // Só funciona para treino de atleta vinculado ao médico que está cancelando.
    @Transactional
    public ResultadoCancelamento cancelarTreinamento(Long medicoId, Long atletaId, Long treinamentoId, String motivo) {
        TreinamentoModel treino = jpaTreinamento.buscarDoAtletaDoMedico(treinamentoId, atletaId, medicoId).orElse(null);
        if (treino == null) {
            logger.warn("Médico {} tentou cancelar o treinamento {} que não é de um atleta vinculado a ele", medicoId, treinamentoId);
            return ResultadoCancelamento.NAO_ENCONTRADO;
        }
        if (Boolean.TRUE.equals(treino.getCancelado())) {
            return ResultadoCancelamento.JA_CANCELADO;
        }

        String motivoLimpo = motivo == null ? "" : motivo.trim();
        if (motivoLimpo.isEmpty() || motivoLimpo.length() > MOTIVO_CANCELAMENTO_MAX) {
            return ResultadoCancelamento.MOTIVO_INVALIDO;
        }

        treino.setCancelado(true);
        treino.setMotivoCancelamento(motivoLimpo);
        treino.setCanceladoEm(LocalDateTime.now());
        treino.setCanceladoPor(jpaMedico.getReferenceById(medicoId));
        jpaTreinamento.save(treino);
        logger.info("Treinamento {} do atleta {} cancelado pelo médico {}", treinamentoId, atletaId, medicoId);
        return ResultadoCancelamento.CANCELADO;
    }

}
