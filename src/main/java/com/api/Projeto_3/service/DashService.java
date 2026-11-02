package com.api.Projeto_3.service;

import java.time.Duration;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.api.Projeto_3.dtos.model.DashDto;
import com.api.Projeto_3.dtos.model.DashPontoDto;
import com.api.Projeto_3.dtos.model.DashTreinoDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.DiaModel;
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
        AtletaModelo atleta = atletaService.buscarAtletaPorCpf(cpf);
        if (atleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atleta não encontrado.");
        }

        DashDto dash = new DashDto();
        dash.setNomeAtleta(atleta.getName());
        List<SemanaModel> semanas = dashRepository.buscarSemanasDoAtleta(cpf);
        dash.setSemanas(semanas);
        for (DiasEnum dia : DiasEnum.values()) {
            dash.getAtividadeSemanal().add(criarPonto(dia.getAbreviacao(), 0));
        }

        SemanaModel selecionada = buscarSemana(semanas, semanaId);
        if (selecionada == null) {
            return dash;
        }

        dash.setSemanaId(selecionada.getId());
        dash.setNomeSemana(selecionada.getNome());
        int somaSatisfacao = 0;
        int treinosAvaliados = 0;
        for (DiaModel dia : selecionada.getDias()) {
            for (TreinamentoModel treino : dia.getTreinamentos()) {
                DashTreinoDto item = criarTreinoDoHistorico(treino);
                dash.getTreinos().add(item);
                if (item.getSatisfacao() != null) {
                    somaSatisfacao += item.getSatisfacao();
                    treinosAvaliados++;
                }

                if (item.getDuracao() != null) {
                    dash.setTempoTotal(dash.getTempoTotal() + item.getDuracao());
                    somarTempoDoDia(dash.getAtividadeSemanal(), dia.getNome(), item.getDuracao());
                }
                somarCategoria(dash.getCategorias(), item.getCategoria());
            }
        }
        // Média considerando os sete dias da semana, inclusive dias sem treino.
        dash.setMediaDiaria(dash.getTempoTotal() / 7.0);
        if (treinosAvaliados > 0) {
            dash.setSatisfacaoMedia((double) somaSatisfacao / treinosAvaliados);
        }
        return dash;
    }

    // Sem filtro, mostra a semana mais recente. A lista contém só semanas do atleta.
    private SemanaModel buscarSemana(List<SemanaModel> semanas, Long semanaId) {
        if (semanaId == null) {
            if (semanas.isEmpty()) {
                return null;
            }
            return semanas.get(0);
        }

        for (SemanaModel semana : semanas) {
            if (semana.getId().equals(semanaId)) {
                return semana;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Semana não encontrada.");
    }

    private DashTreinoDto criarTreinoDoHistorico(TreinamentoModel treino) {
        DashTreinoDto treinoDoHistorico = new DashTreinoDto();
        treinoDoHistorico.setId(treino.getId());
        treinoDoHistorico.setSatisfacao(treino.getSatisfacao());
        treinoDoHistorico.setNome(treino.getNome());
        treinoDoHistorico.setDescricao(treino.getDescricao());
        treinoDoHistorico.setDuracao(calcularDuracao(treino));

        if (treino.getCategoria() == null) {
            treinoDoHistorico.setCategoria("Sem categoria");
        } else {
            treinoDoHistorico.setCategoria(treino.getCategoria());
        }

        return treinoDoHistorico;
    }

    private void somarTempoDoDia(List<DashPontoDto> dias, DiasEnum dia, long minutos) {
        for (DashPontoDto ponto : dias) {
            if (ponto.getX().equals(dia.getAbreviacao())) {
                ponto.setY(ponto.getY() + minutos);
                return;
            }
        }
    }

    private Long calcularDuracao(TreinamentoModel treino) {
        if (treino.getHorarioInicio() == null || treino.getHorarioFim() == null) {
            return null;
        }
        long minutos = Duration.between(treino.getHorarioInicio(), treino.getHorarioFim()).toMinutes();
        if (minutos < 0) {
            return null;
        }
        return minutos;
    }

    // Cada treino acrescenta 1 à sua categoria no gráfico.
    private void somarCategoria(List<DashPontoDto> categorias, String categoria) {
        for (DashPontoDto ponto : categorias) {
            if (ponto.getX().equals(categoria)) {
                ponto.setY(ponto.getY() + 1);
                return;
            }
        }
        categorias.add(criarPonto(categoria, 1));
    }

    private DashPontoDto criarPonto(String nome, long valor) {
        DashPontoDto ponto = new DashPontoDto();
        // O gráfico usa x para o nome e y para o valor.
        ponto.setX(nome);
        ponto.setY(valor);
        return ponto;
    }
}
