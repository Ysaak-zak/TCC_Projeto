package com.api.Projeto_3.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

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

class DashServiceTest {
    @Test
    void somaApenasDuracoesValidasERegistraMinutosDosTreinosConcluidos() {
        TreinamentoModel treinoValido = criarTreino("Musculação", LocalTime.of(8, 0), LocalTime.of(9, 0));
        TreinamentoModel treinoInvertido = criarTreino("Cardio", LocalTime.of(21, 0), LocalTime.of(7, 0));
        TreinamentoModel treinoSemHorarioFinal = criarTreino("Cardio", LocalTime.of(9, 0), null);
        DiaModel segunda = new DiaModel(1L, DiasEnum.SEGUNDA, null, List.of(treinoValido));
        DiaModel terca = new DiaModel(2L, DiasEnum.TERCA, null, List.of(treinoInvertido, treinoSemHorarioFinal));
        SemanaModel semana = new SemanaModel(3L, "Semana 3", null, null, List.of(segunda, terca));

        DashService service = criarServico(semana);
        DashDto painel = service.buscarDashboard("cpf-atleta", 3L);

        assertThat(painel.getTempoTotal()).isEqualTo(60);
        assertThat(painel.getMediaDiaria()).isEqualTo(60.0 / 7);
        assertThat(painel.getTreinos()).extracting(DashTreinoDto::getDuracao).containsExactly(60L, null, null);
        assertThat(painel.getAtividadeSemanal()).extracting(DashPontoDto::getY)
                .containsExactly(60L, 0L, 0L, 0L, 0L, 0L, 0L);
        assertThat(painel.getAtividadeSemanal()).extracting(DashPontoDto::getYConcluido).containsOnly(0L);
        assertThat(painel.getCategorias()).extracting(DashPontoDto::getY).containsExactly(1L, 2L);

        treinoValido.setConcluido(true);
        treinoInvertido.setConcluido(true);
        DashDto painelAtualizado = service.buscarDashboard("cpf-atleta", 3L);

        assertThat(painelAtualizado.getAtividadeSemanal()).extracting(DashPontoDto::getYConcluido)
                .containsExactly(60L, 0L, 0L, 0L, 0L, 0L, 0L);
        assertThat(painelAtualizado.getCategorias()).extracting(DashPontoDto::getYConcluido).containsExactly(1L, 1L);
    }

    @Test
    void barraRefleteExerciciosSalvosAteOLimiteDoTempoProgramado() {
        TreinamentoModel treino = criarTreino("Musculação", LocalTime.of(8, 0), LocalTime.of(9, 0));
        ExercicioModel primeiro = criarExercicio(20);
        ExercicioModel segundo = criarExercicio(50);
        treino.setExercicios(List.of(primeiro, segundo));

        DiaModel segunda = new DiaModel(1L, DiasEnum.SEGUNDA, null, List.of(treino));
        SemanaModel semana = new SemanaModel(3L, "Semana 3", null, null, List.of(segunda));
        DashService service = criarServico(semana);

        primeiro.setConcluido(true);
        DashDto comProgresso = service.buscarDashboard("cpf-atleta", 3L);
        assertThat(comProgresso.getAtividadeSemanal().get(0).getY()).isEqualTo(60);
        assertThat(comProgresso.getAtividadeSemanal().get(0).getYConcluido()).isEqualTo(20);
        assertThat(comProgresso.getCategorias().get(0).getYConcluido()).isZero();

        segundo.setConcluido(true);
        DashDto comTodosExercicios = service.buscarDashboard("cpf-atleta", 3L);
        assertThat(comTodosExercicios.getAtividadeSemanal().get(0).getYConcluido()).isEqualTo(60);

        treino.setCancelado(true);
        DashDto comCancelamento = service.buscarDashboard("cpf-atleta", 3L);
        assertThat(comCancelamento.getAtividadeSemanal().get(0).getY()).isZero();
        assertThat(comCancelamento.getAtividadeSemanal().get(0).getYConcluido()).isZero();
    }

    private DashService criarServico(SemanaModel semana) {
        MethodInterceptor buscarSemanas = chamada -> List.of(semana);
        DashRepository repository = (DashRepository) new ProxyFactory(DashRepository.class, buscarSemanas).getProxy();
        AtletaService atletaService = new AtletaService() {
            @Override
            public AtletaModelo buscarAtletaPorCpf(String cpf) {
                return null;
            }
        };
        return new DashService(repository, atletaService);
    }

    private ExercicioModel criarExercicio(double duracao) {
        ExercicioModel exercicio = new ExercicioModel();
        exercicio.setDuracacao(duracao);
        return exercicio;
    }

    private TreinamentoModel criarTreino(String categoria, LocalTime inicio, LocalTime fim) {
        TreinamentoModel treino = new TreinamentoModel();
        treino.setCategoria(categoria);
        treino.setHorarioInicio(inicio);
        treino.setHorarioFim(fim);
        return treino;
    }
}
