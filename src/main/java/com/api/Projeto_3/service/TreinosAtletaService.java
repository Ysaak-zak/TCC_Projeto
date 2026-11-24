package com.api.Projeto_3.service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.dtos.DtosModelos.DiaAgendaDto;
import com.api.Projeto_3.dtos.DtosModelos.ExercicioAgendaDto;
import com.api.Projeto_3.dtos.DtosModelos.ResultadoProgressoTreinoDto;
import com.api.Projeto_3.dtos.DtosModelos.SemanaAgendaDto;
import com.api.Projeto_3.dtos.DtosModelos.TreinoAgendaDto;
import com.api.Projeto_3.dtos.DtosModelos.TreinoAtletaProgressoDto;
import com.api.Projeto_3.dtos.DtosModelos.TreinosAtletaDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.DiaModel;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.TreinamentoModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.AgendaTreinamentoRepository.LinhaDaAgenda;
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
        List<SemanaModel> semanas = semanaRepository.buscarSemanasDoAtleta(cpf);

        List<SemanaAgendaDto> semanasDaAgenda = new ArrayList<>();
        for (SemanaModel semana : semanas) {
            semanasDaAgenda.add(mapearSemana(semana));
        }

        Optional<Long> semanaSelecionada = semanas.stream()
                .map(SemanaModel::getId)
                .filter(id -> semanaId == null || semanaId.equals(id))
                .findFirst();
        List<DiaAgendaDto> dias = semanaSelecionada
                .map(id -> montarDias(treinoRepository.listarDaSemanaDoAtleta(id, cpf)))
                .orElseGet(List::of);
        return new TreinosAtletaDto(atleta.getName(), semanaSelecionada.orElse(null),
                List.copyOf(semanasDaAgenda), dias);
    }

    @Transactional
    public ResultadoProgressoTreinoDto registrarProgresso(String cpf, Long treinoId, TreinoAtletaProgressoDto formulario) {
        TreinamentoModel treino = treinoRepository.buscarDoAtletaParaAtualizar(treinoId, cpf).orElseThrow();
        return switch (formulario.getAcao()) {
            case CONCLUIR_EXERCICIOS -> concluirExercicios(treino, formulario.getExercicioIds());
            case CONCLUIR_TREINO -> concluirTreino(treino);
        };
    }

    private ResultadoProgressoTreinoDto concluirExercicios(TreinamentoModel treino, List<Long> exercicioIds) {
        Set<Long> idsSelecionados = new HashSet<>(exercicioIds);
        treino.getExercicios().stream()
                .filter(exercicio -> idsSelecionados.contains(exercicio.getId()))
                .forEach(exercicio -> exercicio.setConcluido(true));
        return resultado(treino, "Exercícios selecionados concluídos.");
    }

    private ResultadoProgressoTreinoDto concluirTreino(TreinamentoModel treino) {
        treino.setConcluido(true);
        return resultado(treino, "Treino concluído. O dashboard desta semana foi atualizado.");
    }

    private ResultadoProgressoTreinoDto resultado(TreinamentoModel treino, String mensagem) {
        DiaModel dia = treino.getDia_fk();
        SemanaModel semana = dia.getSemana_fk();
        return new ResultadoProgressoTreinoDto(semana.getId(), true, mensagem);
    }

    private SemanaAgendaDto mapearSemana(SemanaModel semana) {
        SemanaAgendaDto semanaDaAgenda = new SemanaAgendaDto();
        semanaDaAgenda.setId(semana.getId());
        semanaDaAgenda.setNome(semana.getNome());
        return semanaDaAgenda;
    }

    private List<DiaAgendaDto> montarDias(List<LinhaDaAgenda> linhas) {
        Map<DiasEnum, DiaAgendaDto> dias = new EnumMap<>(DiasEnum.class);
        for (DiasEnum nome : DiasEnum.values()) {
            DiaAgendaDto dia = new DiaAgendaDto();
            dia.setNome(nome);
            dia.setTreinos(new ArrayList<>());
            dias.put(nome, dia);
        }

        Map<Long, TreinoAgendaDto> treinos = new HashMap<>();
        for (LinhaDaAgenda linha : linhas) {
            TreinoAgendaDto treino = treinos.computeIfAbsent(linha.treinoId(), id -> {
                TreinoAgendaDto novoTreino = mapearTreino(linha);
                dias.get(DiasEnum.valueOf(linha.dia())).getTreinos().add(novoTreino);
                return novoTreino;
            });
            Optional.ofNullable(linha.exercicioId())
                    .ifPresent(id -> treino.getExercicios().add(mapearExercicio(linha)));
        }

        return List.copyOf(dias.values());
    }

    private TreinoAgendaDto mapearTreino(LinhaDaAgenda linha) {
        TreinoAgendaDto treino = new TreinoAgendaDto();
        treino.setId(linha.treinoId());
        treino.setNome(linha.treinoNome());
        treino.setCategoria(linha.treinoCategoria());
        treino.setDescricao(linha.treinoDescricao());
        treino.setHorarioInicio(linha.horarioInicio());
        treino.setHorarioFim(linha.horarioFim());
        treino.setCancelado(linha.treinoCancelado());
        treino.setConcluido(linha.treinoConcluido());
        treino.setExercicios(new ArrayList<>());
        return treino;
    }

    private ExercicioAgendaDto mapearExercicio(LinhaDaAgenda linha) {
        ExercicioAgendaDto exercicio = new ExercicioAgendaDto();
        exercicio.setId(linha.exercicioId());
        exercicio.setNome(linha.exercicioNome());
        exercicio.setDescricao(linha.exercicioDescricao());
        exercicio.setDuracao(linha.exercicioDuracao());
        exercicio.setConcluido(linha.exercicioConcluido());
        return exercicio;
    }
}
