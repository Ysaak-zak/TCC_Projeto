package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProcessamentoChatServiceTest {
    private final ProcessamentoChatService service = new ProcessamentoChatService();

    @Test
    void separaRespostaEmCamposDoDto() {
        var analise = service.separarResposta("""
                {"resumo":"Resultado informado", "pontosAtencao":["Conferir coleta"],
                 "recomendacoes":["Avaliação médica"]}
                """);
        assertEquals("Resultado informado", analise.resumo());
        assertEquals(List.of("Conferir coleta"), analise.pontosAtencao());
        assertEquals(List.of("Avaliação médica"), analise.recomendacoes());
    }

    @Test
    void aceitaJsonEnvolvidoEmMarkdown() {
        var analise = service.separarResposta("""
                ```json
                {"resumo":"Resultado", "pontosAtencao":[], "recomendacoes":[]}
                ```
                """);
        assertEquals("Resultado", analise.resumo());
        assertTrue(analise.pontosAtencao().isEmpty());
        assertTrue(analise.recomendacoes().isEmpty());
    }

    @Test
    void preservaRespostaQuandoNaoSegueFormatoEsperado() {
        for (String resposta : List.of("Tempo limite excedido.",
                "{\"resumo\":\"Resultado\",\"pontosAtencao\":[42],\"recomendacoes\":[]}")) {
            var analise = service.separarResposta(resposta);
            assertEquals(resposta, analise.resumo());
            assertTrue(analise.pontosAtencao().isEmpty());
            assertTrue(analise.recomendacoes().isEmpty());
        }
    }

    @Test
    void informaAusenciaDeResposta() {
        assertEquals("Análise não disponível.", service.separarResposta(null).resumo());
        assertEquals("Análise não disponível.", service.separarResposta(" ").resumo());
    }
}
