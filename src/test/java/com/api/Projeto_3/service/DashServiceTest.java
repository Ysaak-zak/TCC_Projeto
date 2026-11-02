package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import com.api.Projeto_3.dtos.model.DashDto;
import com.api.Projeto_3.model.*;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.DashRepository;

class DashServiceTest {
    private final DashRepository repository = mock(DashRepository.class);
    private final AtletaService atletas = mock(AtletaService.class);
    private final DashService service = new DashService(repository, atletas);

    private void prepararAtleta() {
        AtletaModelo atleta = new AtletaModelo();
        atleta.setName("Ana");
        when(atletas.buscarAtletaPorCpf("123")).thenReturn(atleta);
    }

    @Test
    void retornaDashboardVazioQuandoNaoExistemSemanas() {
        prepararAtleta();
        when(repository.buscarSemanasDoAtleta("123")).thenReturn(List.of());
        DashDto dash = service.buscarDashboard("123", null);
        assertEquals("Ana", dash.getNomeAtleta());
        assertEquals(0, dash.getTempoTotal());
        assertEquals(7, dash.getAtividadeSemanal().size());
        assertTrue(dash.getTreinos().isEmpty());
        assertNull(dash.getSemanaId());
        assertNull(dash.getSatisfacaoMedia());
    }

    @Test
    void calculaTempoMediaECategoriasDaSemanaSelecionada() {
        prepararAtleta();
        SemanaModel semana = new SemanaModel();
        semana.setId(1L);
        semana.setNome("Semana 1");
        DiaModel dia = new DiaModel();
        dia.setNome(DiasEnum.SEGUNDA);
        TreinamentoModel treino = new TreinamentoModel();
        treino.setCategoria("Força");
        treino.setSatisfacao(4);
        treino.setHorarioInicio(LocalTime.of(8, 0));
        treino.setHorarioFim(LocalTime.of(9, 10));
        TreinamentoModel semHorario = new TreinamentoModel();
        semHorario.setCategoria("Força");
        dia.setTreinamentos(List.of(treino, semHorario));
        semana.setDias(List.of(dia));
        SemanaModel recente = new SemanaModel();
        recente.setId(2L);
        when(repository.buscarSemanasDoAtleta("123")).thenReturn(List.of(recente, semana));
        DashDto dash = service.buscarDashboard("123", 1L);
        assertEquals(4.0, dash.getSatisfacaoMedia());
        assertEquals(4, dash.getTreinos().get(0).getSatisfacao());
        assertEquals(70, dash.getTempoTotal());
        assertEquals(10.0, dash.getMediaDiaria());
        assertEquals(70, dash.getAtividadeSemanal().get(0).getY());
        assertEquals(0, dash.getAtividadeSemanal().get(1).getY());
        assertEquals(2, dash.getCategorias().get(0).getY());
        assertNull(dash.getTreinos().get(1).getDuracao());
        assertEquals(2L, service.buscarDashboard("123", null).getSemanaId());
    }

    @Test
    void rejeitaSemanaQueNaoPertenceAoAtleta() {
        prepararAtleta();
        when(repository.buscarSemanasDoAtleta("123")).thenReturn(List.of());
        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.buscarDashboard("123", 999L));
        assertEquals(404, erro.getStatusCode().value());
    }
}
