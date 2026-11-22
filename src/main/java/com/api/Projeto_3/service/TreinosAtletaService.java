package com.api.Projeto_3.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.Projeto_3.dtos.DtosModelos.TreinoAtletaProgressoDto;
import com.api.Projeto_3.dtos.DtosModelos.TreinoAtletaProgressoDto.Resultado;
import com.api.Projeto_3.dtos.DtosModelos.TreinosAtletaDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.ExercicioModel;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.TreinamentoModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.DashRepository;
import com.api.Projeto_3.repository.TreinamentoRespository;

@Service
public class TreinosAtletaService {
    private final AtletaService atletaService;
    private final DashRepository semanaRepository;
    private final TreinamentoRespository treinoRepository;

    public TreinosAtletaService(AtletaService atletaService, DashRepository semanaRepository,
                               TreinamentoRespository treinoRepository) {
        this.atletaService = atletaService;
        this.semanaRepository = semanaRepository;
        this.treinoRepository = treinoRepository;
    }

    @Transactional(readOnly = true)
    public TreinosAtletaDto buscarAgenda(String cpf, Long semanaId) {
        AtletaModelo atleta = atletaService.buscarAtletaPorCpf(cpf);
        if (atleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atleta não encontrado.");
        }

        List<SemanaModel> semanas = semanaRepository.buscarSemanasDoAtleta(cpf);
        TreinosAtletaDto agenda = new TreinosAtletaDto();
        agenda.setNomeAtleta(atleta.getName());
        agenda.setSemanas(semanas.stream().map(this::mapearSemana).toList());

        SemanaModel semana = selecionarSemana(semanas, semanaId);
        if (semana == null) {
            return agenda;
        }

        agenda.setSemanaId(semana.getId());
        List<TreinamentoModel> treinos = treinoRepository.listarDaSemanaDoAtleta(semana.getId(), cpf);
        agenda.setDias(Arrays.stream(DiasEnum.values()).map(dia -> mapearDia(dia, treinos)).toList());
        return agenda;
    }

    @Transactional
    public Resultado registrarProgresso(String cpf, Long treinoId, TreinoAtletaProgressoDto formulario) {
        TreinamentoModel treino = treinoRepository.buscarDoAtletaParaAtualizar(treinoId, cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Treino não encontrado."));
        if (Boolean.TRUE.equals(treino.getCancelado())) {
            return resultado(treino, false, "Este treino foi cancelado pelo médico.");
        }

        if (formulario.getAcao() == null) {
            return resultado(treino, false, "Selecione uma ação para continuar.");
        }

        return switch (formulario.getAcao()) {
            case CONCLUIR_EXERCICIOS -> concluirExercicios(treino, formulario.getExercicioIds());
            case CONCLUIR_TREINO -> concluirTreino(treino);
        };
    }

    private Resultado concluirExercicios(TreinamentoModel treino, List<Long> exercicioIds) {
        if (treino.isConcluido()) {
            return resultado(treino, true, "Este treino já está concluído.");
        }
        if (exercicioIds == null || exercicioIds.isEmpty()) {
            return resultado(treino, false, "Marque pelo menos um exercício.");
        }

        Set<Long> idsSelecionados = new HashSet<>(exercicioIds);
        List<ExercicioModel> selecionados = treino.getExercicios().stream()
                .filter(exercicio -> idsSelecionados.contains(exercicio.getId()))
                .toList();
        if (selecionados.size() != idsSelecionados.size()) {
            return resultado(treino, false, "Selecione apenas exercícios deste treino.");
        }

        selecionados.forEach(exercicio -> exercicio.setConcluido(true));
        return resultado(treino, true, "Exercícios selecionados concluídos.");
    }

    private Resultado concluirTreino(TreinamentoModel treino) {
        if (treino.getExercicios().stream().anyMatch(exercicio -> !exercicio.isConcluido())) {
            return resultado(treino, false, "Conclua todos os exercícios antes de concluir o treino.");
        }

        treino.setConcluido(true);
        return resultado(treino, true, "Treino concluído. O dashboard desta semana foi atualizado.");
    }

    private Resultado resultado(TreinamentoModel treino, boolean sucesso, String mensagem) {
        return new Resultado(treino.getDia_fk().getSemana_fk().getId(), sucesso, mensagem);
    }

    private SemanaModel selecionarSemana(List<SemanaModel> semanas, Long semanaId) {
        if (semanaId == null) {
            return semanas.isEmpty() ? null : semanas.get(0);
        }
        return semanas.stream()
                .filter(semana -> semana.getId().equals(semanaId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semana não encontrada."));
    }

    private TreinosAtletaDto.Semana mapearSemana(SemanaModel semana) {
        TreinosAtletaDto.Semana dto = new TreinosAtletaDto.Semana();
        dto.setId(semana.getId());
        dto.setNome(semana.getNome());
        return dto;
    }

    private TreinosAtletaDto.Dia mapearDia(DiasEnum nome, List<TreinamentoModel> treinos) {
        TreinosAtletaDto.Dia dto = new TreinosAtletaDto.Dia();
        dto.setNome(nome);
        dto.setTreinos(treinos.stream()
                .filter(treino -> treino.getDia_fk().getNome() == nome)
                .sorted(Comparator.comparing(TreinamentoModel::getHorarioInicio,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(TreinamentoModel::getId))
                .map(this::mapearTreino)
                .toList());
        return dto;
    }

    private TreinosAtletaDto.Treino mapearTreino(TreinamentoModel treino) {
        TreinosAtletaDto.Treino dto = new TreinosAtletaDto.Treino();
        dto.setId(treino.getId());
        dto.setNome(treino.getNome());
        dto.setCategoria(treino.getCategoria());
        dto.setDescricao(treino.getDescricao());
        dto.setHorarioInicio(treino.getHorarioInicio());
        dto.setHorarioFim(treino.getHorarioFim());
        dto.setCancelado(Boolean.TRUE.equals(treino.getCancelado()));
        dto.setConcluido(treino.isConcluido());
        dto.setExercicios(treino.getExercicios().stream()
                .sorted(Comparator.comparing(ExercicioModel::getId))
                .map(this::mapearExercicio)
                .toList());
        return dto;
    }

    private TreinosAtletaDto.Exercicio mapearExercicio(ExercicioModel exercicio) {
        TreinosAtletaDto.Exercicio dto = new TreinosAtletaDto.Exercicio();
        dto.setId(exercicio.getId());
        dto.setNome(exercicio.getNome());
        dto.setDescricao(exercicio.getDescricao());
        dto.setDuracao(exercicio.getDuracacao());
        dto.setConcluido(exercicio.isConcluido());
        return dto;
    }
}
