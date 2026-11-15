package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import com.api.Projeto_3.dtos.DtosModelos.ExameUrinaDtos;
import com.api.Projeto_3.repository.UrinaRespository;
import dev.langchain4j.exception.TimeoutException;
import dev.langchain4j.model.chat.ChatModel;

class ExamesServiceTest {
    @Test
    void informaQueExameFoiSalvoQuandoAnaliseExcedeTempoLimite() {
        var repository = mock(UrinaRespository.class);
        var atletaRepository = mock(com.api.Projeto_3.repository.AtletaRespository.class);
        var atleta = new com.api.Projeto_3.model.AtletaModelo();
        atleta.setId(1L);
        when(atletaRepository.findById(1L)).thenReturn(java.util.Optional.of(atleta));
        var model = mock(ChatModel.class);
        var service = new UrinaExamesService();
        ReflectionTestUtils.setField(service, "urinaRepository", repository);
        ReflectionTestUtils.setField(service, "chatModel", model);
        ReflectionTestUtils.setField(service, "atletaRepository", atletaRepository);
        var exame = new ExameUrinaDtos();
        exame.setAtletaId(1L);
        exame.setDataColeta(java.time.LocalDate.of(2026, 11, 14));
        exame.setTemperatura(36.0);
        exame.setPh(6.0);
        exame.setDensidade(1.020);
        exame.setCreatinina(100.0);
        exame.setCor("AMARELO_CLARO");
        exame.setGlicose("NEGATIVO");
        exame.setProteinas("TRACOS");
        exame.setSangue("NEGATIVO");
        exame.setObservacoes("Observação preenchida pelo médico.");
        exame.setAnabolizantes("NAO_REALIZADO");
        exame.setEstimulantes("NAO_REALIZADO");
        exame.setDiureticos("NAO_REALIZADO");
        when(model.chat(anyString())).thenThrow(new TimeoutException("request timed out"));
        String resposta = service.realizandoExame(exame);
        assertTrue(resposta.contains("Exame salvo com sucesso"));
        assertTrue(resposta.contains("não foi concluída"));
        verify(repository, times(1)).save(argThat(salvo ->
                salvo.getAtleta() == atleta
                && salvo.getProteinas() == com.api.Projeto_3.model.enums.EnumProteinas.TRACOS
                && salvo.getSangue() == com.api.Projeto_3.model.enums.EnumSangueUrina.NEGATIVO
                && "Observação preenchida pelo médico.".equals(salvo.getObservacoes())));
        verify(model, times(1)).chat(anyString());
    }

    @Test
    void rejeitaAtletaAusenteOuInexistenteSemSalvarNemConsultarChat() {
        var repository = mock(UrinaRespository.class);
        var atletas = mock(com.api.Projeto_3.repository.AtletaRespository.class);
        var chat = mock(ChatModel.class);
        var service = new UrinaExamesService();
        ReflectionTestUtils.setField(service, "urinaRepository", repository);
        ReflectionTestUtils.setField(service, "atletaRepository", atletas);
        ReflectionTestUtils.setField(service, "chatModel", chat);
        var exame = new ExameUrinaDtos();
        for (Long id : new Long[] {null, 0L, -1L, 999L}) {
            exame.setAtletaId(id);
            var erro = org.junit.jupiter.api.Assertions.assertThrows(
                    org.springframework.web.server.ResponseStatusException.class,
                    () -> service.realizandoExame(exame));
            org.junit.jupiter.api.Assertions.assertEquals(id != null && id == 999L ? 404 : 400,
                    erro.getStatusCode().value());
        }
        verifyNoInteractions(repository, chat);
    }
}
