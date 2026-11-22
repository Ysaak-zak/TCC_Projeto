package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.api.Projeto_3.dtos.DtosModelos.TreinoAtletaProgressoDto;
import com.api.Projeto_3.dtos.DtosModelos.TreinoAtletaProgressoDto.Acao;
import com.api.Projeto_3.dtos.DtosModelos.TreinosAtletaDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.DiaModel;
import com.api.Projeto_3.model.ExercicioModel;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.TreinamentoModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.DashRepository;
import com.api.Projeto_3.repository.TreinamentoRespository;

class TreinosAtletaServiceTest {
    private final AtletaService atletas = mock(AtletaService.class);
    private final DashRepository semanas = mock(DashRepository.class);
    private final TreinamentoRespository treinos = mock(TreinamentoRespository.class);
    private final TreinosAtletaService service = new TreinosAtletaService(atletas, semanas, treinos);
    private final TreinamentoModel treino = new TreinamentoModel();
    private final ExercicioModel corrida = new ExercicioModel();
    private final ExercicioModel mobilidade = new ExercicioModel();

    @BeforeEach
    void prepararAgenda() {
        var atleta = new AtletaModelo();
        atleta.setName("Ana");
        var semana = new SemanaModel();
        semana.setId(1L);
        var dia = new DiaModel();
        dia.setNome(DiasEnum.SEGUNDA);
        dia.setSemana_fk(semana);
        treino.setId(2L);
        treino.setDia_fk(dia);
        corrida.setId(3L);
        mobilidade.setId(4L);
        treino.setExercicios(List.of(corrida, mobilidade));
        dia.setTreinamentos(List.of(treino));
        semana.setDias(List.of(dia));
        when(atletas.buscarAtletaPorCpf("cpf")).thenReturn(atleta);
        when(semanas.buscarSemanasDoAtleta("cpf")).thenReturn(List.of(semana));
        when(treinos.buscarDoAtletaParaAtualizar(2L, "cpf")).thenReturn(Optional.of(treino));
    }

    @Test
    void mostraTodosOsDiasComOsTreinosEExerciciosDaSemana() {
        var terca = new DiaModel();
        terca.setNome(DiasEnum.TERCA);
        var outroTreino = new TreinamentoModel();
        outroTreino.setId(5L);
        outroTreino.setDia_fk(terca);
        when(treinos.listarDaSemanaDoAtleta(1L, "cpf")).thenReturn(List.of(outroTreino, treino));

        var agenda = service.buscarAgenda("cpf", null);
        assertEquals(1L, agenda.getSemanaId());
        var dias = agenda.getDias();
        assertEquals(List.of(DiasEnum.values()), dias.stream().map(TreinosAtletaDto.Dia::getNome).toList());
        assertEquals(2, dias.get(0).getTreinos().get(0).getExercicios().size());
        assertEquals(5L, dias.get(1).getTreinos().get(0).getId());
        assertTrue(dias.get(6).getTreinos().isEmpty());
        assertThrows(ResponseStatusException.class, () -> service.buscarAgenda("cpf", 99L));
    }

    @Test
    void exigeConcluirTodosOsExerciciosSemAceitarItensDeOutroTreino() {
        var concluirTreino = formulario(Acao.CONCLUIR_TREINO);
        assertFalse(service.registrarProgresso("cpf", 2L, concluirTreino).isSucesso());

        var selecaoInvalida = formulario(Acao.CONCLUIR_EXERCICIOS, 3L, 99L);
        assertFalse(service.registrarProgresso("cpf", 2L, selecaoInvalida).isSucesso());
        assertFalse(corrida.isConcluido());

        assertTrue(service.registrarProgresso("cpf", 2L, formulario(Acao.CONCLUIR_EXERCICIOS, 3L)).isSucesso());
        assertTrue(corrida.isConcluido());
        assertFalse(mobilidade.isConcluido());
        assertFalse(service.registrarProgresso("cpf", 2L, concluirTreino).isSucesso());

        assertTrue(service.registrarProgresso("cpf", 2L, formulario(Acao.CONCLUIR_EXERCICIOS, 4L)).isSucesso());
        assertTrue(service.registrarProgresso("cpf", 2L, concluirTreino).isSucesso());
        assertTrue(treino.isConcluido());
    }

    @Test
    void rejeitaTreinoCanceladoOuPertencenteAOutroAtleta() {
        var formulario = formulario(Acao.CONCLUIR_EXERCICIOS, 3L);
        assertThrows(ResponseStatusException.class, () -> service.registrarProgresso("outro-cpf", 2L, formulario));
        treino.setCancelado(true);
        assertFalse(service.registrarProgresso("cpf", 2L, formulario).isSucesso());
        assertFalse(corrida.isConcluido());
    }

    private TreinoAtletaProgressoDto formulario(Acao acao, Long... exercicios) {
        var dto = new TreinoAtletaProgressoDto();
        dto.setAcao(acao);
        dto.setExercicioIds(List.of(exercicios));
        return dto;
    }
}
