package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.api.Projeto_3.dtos.DtosModelos.ExameUrinaDtos;

class AlertaTriagemTest {
    @Test
    void destacaSomenteClassesSuspeitasOuInconclusivasMesmoSemRespostaDaIa() {
        var exame = new ExameUrinaDtos();
        exame.setAnabolizantes("SUSPEITO");
        exame.setEstimulantes("INCONCLUSIVO");
        exame.setDiureticos("NEGATIVO");

        var alertas = exame.getAlertasTriagem();
        assertEquals(2, alertas.size());
        assertEquals("Anabolizantes", alertas.get(0).classe());
        assertEquals("Suspeito na triagem", alertas.get(0).status());
        assertEquals("Estimulantes", alertas.get(1).classe());
        assertEquals("Inconclusivo", alertas.get(1).status());
        assertNull(exame.getAnalise());
    }

    @Test
    void naoCriaAlertaParaCamposAusentesNegativosOuNaoRealizados() {
        var exame = new ExameUrinaDtos();
        assertTrue(exame.getAlertasTriagem().isEmpty());
        exame.setAnabolizantes("NAO_REALIZADO");
        exame.setEstimulantes("NEGATIVO");
        exame.setDiureticos("");
        assertTrue(exame.getAlertasTriagem().isEmpty());
    }
}
