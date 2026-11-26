package com.api.Projeto_3.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.Projeto_3.dtos.DtosModelos.DashDto;
import com.api.Projeto_3.dtos.DtosModelos.DashPontoDto;
import com.api.Projeto_3.dtos.DtosModelos.DashTreinoDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.DiaModel;
import com.api.Projeto_3.model.ExercicioModel;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.TreinamentoModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.DashRepository;

@Service
public class DashService {
    private final DashRepository dashRepository;
    private final AtletaService atletaService;

    public DashService(DashRepository dashRepository, AtletaService atletaService) {
        this.dashRepository = dashRepository;
        this.atletaService = atletaService;
    }

    @Transactional(readOnly = true)
    public DashDto buscarDashboard(String cpf, Long semanaId) {
        DashDto dashboard = new DashDto();
        dashboard.setSemanas(dashRepository.buscarSemanasDoAtleta(cpf));
        dashboard.setNomeAtleta(Optional.ofNullable(atletaService.buscarAtletaPorCpf(cpf))
                .map(AtletaModelo::getName)
                .orElse(null));

        for (DiasEnum dia : DiasEnum.values()) {
            dashboard.getAtividadeSemanal().add(criarPonto(dia.getAbreviacao()));
        }

        dashboard.getSemanas().stream()
                .filter(semana -> semanaId == null || semanaId.equals(semana.getId()))
                .findFirst()
                .ifPresent(semana -> preencherDadosDaSemana(dashboard, semana));
        return dashboard;
    }

    private void preencherDadosDaSemana(DashDto dashboard, SemanaModel semana) {
        dashboard.setSemanaId(semana.getId());
        dashboard.setNomeSemana(semana.getNome());

        List<DashPontoDto> atividadeSemanal = dashboard.getAtividadeSemanal();
        Map<String, DashPontoDto> categorias = new LinkedHashMap<>();
        long tempoTotal = 0;
        int somaDasNotas = 0;
        int totalDeNotas = 0;

        for (DiaModel dia : semana.getDias()) {
            DashPontoDto atividade = atividadeSemanal.get(dia.getNome().ordinal());
            for (TreinamentoModel treinamento : dia.getTreinamentos()) {
                DashTreinoDto treino = criarTreinoDoHistorico(treinamento);
                dashboard.getTreinos().add(treino);
                if (treino.isCancelado()) {
                    continue;
                }

                long duracao = Objects.requireNonNullElse(treino.getDuracao(), 0L);
                tempoTotal += duracao;
                somarNoGrafico(atividade, duracao, calcularMinutosConcluidos(treinamento, duracao));

                DashPontoDto categoria = categorias.computeIfAbsent(treino.getCategoria(), this::criarPonto);
                somarNoGrafico(categoria, 1, treino.isConcluido() ? 1 : 0);

                Integer nota = treino.getSatisfacao();
                if (nota != null) {
                    somaDasNotas += nota;
                    totalDeNotas++;
                }
            }
        }

        dashboard.getCategorias().addAll(categorias.values());
        dashboard.setTempoTotal(tempoTotal);
        dashboard.setMediaDiaria((double) tempoTotal / atividadeSemanal.size());
        if (totalDeNotas > 0) {
            dashboard.setSatisfacaoMedia((double) somaDasNotas / totalDeNotas);
        }
    }

    private DashTreinoDto criarTreinoDoHistorico(TreinamentoModel treino) {
        LocalTime inicio = treino.getHorarioInicio();
        LocalTime fim = treino.getHorarioFim();
        Long duracao = inicio != null && fim != null && fim.isAfter(inicio)
                ? Duration.between(inicio, fim).toMinutes()
                : null;

        DashTreinoDto treinoDoHistorico = new DashTreinoDto();
        treinoDoHistorico.setId(treino.getId());
        treinoDoHistorico.setSatisfacao(treino.getSatisfacao());
        treinoDoHistorico.setNome(treino.getNome());
        treinoDoHistorico.setDescricao(treino.getDescricao());
        treinoDoHistorico.setDuracao(duracao);
        treinoDoHistorico.setCancelado(Boolean.TRUE.equals(treino.getCancelado()));
        treinoDoHistorico.setConcluido(treino.isConcluido());
        treinoDoHistorico.setCategoria(treino.getCategoria());
        return treinoDoHistorico;
    }

    private long calcularMinutosConcluidos(TreinamentoModel treino, long duracaoProgramada) {
        if (treino.isConcluido() || duracaoProgramada == 0) {
            return duracaoProgramada;
        }

        double minutosConcluidos = treino.getExercicios().stream()
                .filter(ExercicioModel::isConcluido)
                .mapToDouble(ExercicioModel::getDuracacao)
                .filter(duracao -> Double.isFinite(duracao) && duracao > 0)
                .sum();
        return Math.min(duracaoProgramada, Math.round(minutosConcluidos));
    }

    private void somarNoGrafico(DashPontoDto ponto, long valor, long valorConcluido) {
        ponto.setY(ponto.getY() + valor);
        ponto.setYConcluido(ponto.getYConcluido() + valorConcluido);
    }

    private DashPontoDto criarPonto(String nome) {
        DashPontoDto ponto = new DashPontoDto();
        ponto.setX(nome);
        return ponto;
    }
}
