package com.api.Projeto_3.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

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

    @Autowired
    MedicoRespository jpaMedico;

    @Autowired
    AtletaRespository jpaAtleta;

    @Autowired
    TreinamentoRespository jpaTreinamento;

    public static final int MOTIVO_CANCELAMENTO_MAX = 255;

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
        if (atleta == null) {
            return null;
        }

        MedicoModelo medicoVinculado = atleta.getMedico_fk();
        if (medicoVinculado == null || !medicoId.equals(medicoVinculado.getId())) {
            return null;
        }

        return atleta;
    }

    @Transactional(readOnly = true)
    public List<TreinamentoModel> treinamentosDoAtleta(Long medicoId, Long atletaId) {
        List<TreinamentoModel> treinos = jpaTreinamento.listarDoAtletaDoMedico(atletaId, medicoId);

        Comparator<TreinamentoModel> semanasMaisRecentesPrimeiro = Comparator.comparing(
                treino -> treino.getDia_fk().getSemana_fk().getId(), Comparator.reverseOrder());
        Comparator<LocalTime> horariosAusentesNoFim = Comparator.nullsLast(Comparator.naturalOrder());

        Comparator<TreinamentoModel> ordemDosTreinos = semanasMaisRecentesPrimeiro
                .thenComparing(treino -> treino.getDia_fk().getNome())
                .thenComparing(treino -> treino.getHorarioInicio(), horariosAusentesNoFim)
                .thenComparing(treino -> treino.getId());

        return treinos.stream()
                .sorted(ordemDosTreinos)
                .toList();
    }

    @Transactional
    public boolean cancelarTreinamento(Long medicoId, Long atletaId, Long treinamentoId, String motivo) {
        TreinamentoModel treino = jpaTreinamento.buscarDoAtletaDoMedico(treinamentoId, atletaId, medicoId)
                .orElse(null);

        if (treino == null || Boolean.TRUE.equals(treino.getCancelado())) {
            return false;
        }

        String motivoLimpo = motivo == null ? "" : motivo.trim();
        if (motivoLimpo.isEmpty() || motivoLimpo.length() > MOTIVO_CANCELAMENTO_MAX) {
            return false;
        }

        treino.setCancelado(true);
        treino.setMotivoCancelamento(motivoLimpo);
        treino.setCanceladoEm(LocalDateTime.now());
        treino.setCanceladoPor(jpaMedico.getReferenceById(medicoId));
        jpaTreinamento.save(treino);
        return true;
    }

}
