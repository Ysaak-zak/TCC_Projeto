package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.api.Projeto_3.dtos.model.CriarTreinoDto;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.SemanaResponitory;
import jakarta.validation.Validation;

class TrenamentosSeriveTest {
    private CriarTreinoDto formulario() {
        var form = new CriarTreinoDto();
        form.setSemanaId(1L);
        form.setDia(DiasEnum.SEGUNDA);
        form.setNome(" Corrida ");
        form.setCategoria(" Aeróbico ");
        form.setDescricao("Ritmo leve");
        form.setHorarioInicio(LocalTime.of(8, 0));
        form.setHorarioFim(LocalTime.of(9, 0));
        return form;
    }

    @Test
    void criaDiaComVinculosEReutilizaParaProximoTreino() {
        var repo = mock(SemanaResponitory.class);
        var semana = new SemanaModel();
        semana.setId(1L);
        when(repo.listarPorTreinador("cpf")).thenReturn(List.of(semana));
        var service = new TrenamentosSerive();
        service.snJpa = repo;
        var form = formulario();
        service.criarTreino(form, "cpf");
        service.criarTreino(form, "cpf");
        assertEquals(1, semana.getDias().size());
        var dia = semana.getDias().get(0);
        assertSame(semana, dia.getSemana_fk());
        assertEquals(DiasEnum.SEGUNDA, dia.getNome());
        assertEquals(2, dia.getTreinamentos().size());
        var treino = dia.getTreinamentos().get(0);
        assertSame(dia, treino.getDia_fk());
        assertEquals("Corrida", treino.getNome());
        assertEquals("Aeróbico", treino.getCategoria());
        assertEquals(form.getHorarioFim(), treino.getHorarioFim());
        verify(repo, times(2)).save(semana);
    }

    @Test
    void rejeitaSemanaDeOutroTreinador() {
        var repo = mock(SemanaResponitory.class);
        when(repo.listarPorTreinador("cpf")).thenReturn(List.of());
        var service = new TrenamentosSerive();
        service.snJpa = repo;
        assertThrows(IllegalArgumentException.class, () -> service.criarTreino(formulario(), "cpf"));
        verify(repo, never()).save(any());
    }

    @Test
    void validaDiaEHorariosSemExigirDatas() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var form = formulario();
            assertTrue(validator.validate(form).isEmpty());
            form.setDia(null);
            assertFalse(validator.validate(form).isEmpty());
            form.setDia(DiasEnum.DOMINGO);
            form.setHorarioFim(LocalTime.of(7, 0));
            assertFalse(form.isHorarioValido());
            form.setHorarioFim(LocalTime.of(23, 0));
            assertFalse(form.isHorarioValido());
        }
    }
    @Test
    void vinculaExercicioAoTreinoDaSemanaERejeitaOutroTreino() {
        var repo = mock(SemanaResponitory.class);
        var semana = new SemanaModel();
        semana.setId(1L);
        var dia = new com.api.Projeto_3.model.DiaModel();
        var treino = new com.api.Projeto_3.model.TreinamentoModel();
        treino.setId(5L);
        dia.getTreinamentos().add(treino);
        semana.getDias().add(dia);
        when(repo.listarPorTreinador("cpf")).thenReturn(List.of(semana));
        var service = new TrenamentosSerive();
        service.snJpa = repo;
        var form = new com.api.Projeto_3.dtos.model.CriarExercicioDto();
        form.setSemanaId(1L);
        form.setTreinamentoId(5L);
        form.setNome(" Corrida ");
        form.setDuracao(30);
        service.criarExercicio(form, "cpf");
        var exercicio = treino.getExercicios().get(0);
        assertSame(treino, exercicio.getTreinamento_fk());
        assertEquals("Corrida", exercicio.getNome());
        assertEquals(30, exercicio.getDuracacao());
        assertEquals(List.of(treino), service.listarTreinosDaSemana(1L, "cpf"));
        form.setTreinamentoId(999L);
        assertThrows(IllegalArgumentException.class, () -> service.criarExercicio(form, "cpf"));
        form.setSemanaId(999L);
        assertThrows(IllegalArgumentException.class, () -> service.criarExercicio(form, "cpf"));
        verify(repo, times(1)).save(semana);
        assertEquals(1, treino.getExercicios().size());
    }

    @Test
    void exigeSemanaTreinamentoNomeEDuracaoPositiva() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var form = new com.api.Projeto_3.dtos.model.CriarExercicioDto();
            assertEquals(4, validator.validate(form).size());
            form.setSemanaId(1L);
            form.setTreinamentoId(2L);
            form.setNome("Corrida");
            form.setDuracao(0);
            assertFalse(validator.validate(form).isEmpty());
            form.setDuracao(30);
            assertTrue(validator.validate(form).isEmpty());
        }
    }
}
