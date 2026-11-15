package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.api.Projeto_3.dtos.DtosModelos.ExameSagueDtos;
import com.api.Projeto_3.dtos.DtosModelos.SangueSubsDtos;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.ExameSangue;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.SangueRespository;

import dev.langchain4j.model.chat.ChatModel;

class SangueTestServiceTest {
    @Test
    void salvaExameComSubstanciasEProcessaRespostaDaIa() {
        var repository = mock(SangueRespository.class);
        var atletas = mock(AtletaRespository.class);
        var chat = mock(ChatModel.class);
        var service = new SangueTest();
        ReflectionTestUtils.setField(service, "sangueRepository", repository);
        ReflectionTestUtils.setField(service, "atletaRepository", atletas);
        ReflectionTestUtils.setField(service, "chatModel", chat);
        ReflectionTestUtils.setField(service, "processamentoChatService", new ProcessamentoChatService());
        var atleta = new AtletaModelo();
        atleta.setId(7L);
        when(atletas.findById(7L)).thenReturn(Optional.of(atleta));
        when(repository.save(any(ExameSangue.class))).thenAnswer(call -> {
            ExameSangue entity = call.getArgument(0);
            entity.setId(12L);
            return entity;
        });
        String resposta = "{\"resumo\":\"Triagem inconclusiva\",\"pontosAtencao\":[],\"recomendacoes\":[]}";
        when(chat.chat(anyString())).thenReturn(resposta);
        var dto = new ExameSagueDtos();
        dto.setAtletaId(7L);
        dto.setHemoglobina(new BigDecimal("15.50"));
        var substancia = new SangueSubsDtos();
        substancia.setNomeSubstancia("Estanozolol");
        substancia.setResultadoTriagem("INCONCLUSIVO");
        dto.setSubstancias(List.of(substancia, new SangueSubsDtos()));

        assertSame(dto, service.realizarExameParaPagina(dto));
        assertEquals(12L, dto.getId());
        assertEquals(resposta, dto.getAnaliseIa());
        assertEquals("Triagem inconclusiva", dto.getAnalise().resumo());
        var captor = org.mockito.ArgumentCaptor.forClass(ExameSangue.class);
        verify(repository, times(2)).save(captor.capture());
        var salvo = captor.getValue();
        assertEquals(7L, salvo.getAtletaId());
        assertEquals(dto.getHemoglobina(), salvo.getHemoglobina());
        assertEquals(resposta, salvo.getAnaliseIa());
        assertEquals(1, salvo.getSubstancias().size());
        assertSame(salvo, salvo.getSubstancias().get(0).getExameSangue());
        verify(chat).chat(argThat((String prompt) -> prompt.contains("Estanozolol")
                && prompt.contains("INCONCLUSIVO") && prompt.contains("15.50")));
    }
}
