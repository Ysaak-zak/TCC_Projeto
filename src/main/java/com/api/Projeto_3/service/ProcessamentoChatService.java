package com.api.Projeto_3.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import com.api.Projeto_3.dtos.chat.ProcessamentoChat;

@Service
public class ProcessamentoChatService {
    private static final String MARCADOR_DE_CODIGO = "```";
    private static final ProcessamentoChat ANALISE_INDISPONIVEL = new ProcessamentoChat("Análise não disponível.", List.of(), List.of());

    private final JsonMapper leitorJson = JsonMapper.builder().build();

    public ProcessamentoChat separarResposta(String resposta) {
        if (resposta == null || resposta.isBlank()) {
            return ANALISE_INDISPONIVEL;
        }

        String texto = resposta.strip();
        if (!pareceJsonOuCodigo(texto)) {
            return new ProcessamentoChat(resposta, List.of(), List.of());
        }

        try {
            String json = retirarMarcadoresDeCodigo(texto);
            JsonNode analiseJson = leitorJson.readTree(json);
            JsonNode resumo = analiseJson.path("resumo");
            JsonNode pontosDeAtencao = analiseJson.path("pontosAtencao");
            JsonNode recomendacoes = analiseJson.path("recomendacoes");

            boolean camposValidos = textoValido(resumo)
                    && listaDeTextosValida(pontosDeAtencao)
                    && listaDeTextosValida(recomendacoes);
            if (!camposValidos) {
                return ANALISE_INDISPONIVEL;
            }

            return leitorJson.treeToValue(analiseJson, ProcessamentoChat.class);
        } catch (JacksonException exception) {
            return ANALISE_INDISPONIVEL;
        }
    }

    private String retirarMarcadoresDeCodigo(String texto) {
        int tamanhoDoMarcador = MARCADOR_DE_CODIGO.length();
        boolean temMarcadores = texto.length() >= 2 * tamanhoDoMarcador
                && texto.startsWith(MARCADOR_DE_CODIGO)
                && texto.endsWith(MARCADOR_DE_CODIGO);
        if (!temMarcadores) {
            return texto;
        }

        int fimDoConteudo = texto.length() - tamanhoDoMarcador;
        String conteudo = texto.substring(tamanhoDoMarcador, fimDoConteudo);
        if (conteudo.length() >= 4 && conteudo.substring(0, 4).equalsIgnoreCase("json")) {
            conteudo = conteudo.substring(4);
        }
        return conteudo.strip();
    }

    private boolean listaDeTextosValida(JsonNode lista) {
        if (!lista.isArray()) {
            return false;
        }
        for (JsonNode item : lista) {
            if (!textoValido(item)) {
                return false;
            }
        }
        return true;
    }

    private boolean textoValido(JsonNode campo) {
        if (!campo.isTextual()) {
            return false;
        }
        String texto = campo.asString().strip();
        return !texto.isEmpty() && !pareceJsonOuCodigo(texto);
    }

    private boolean pareceJsonOuCodigo(String texto) {
        return texto.startsWith("{") || texto.startsWith("[") || texto.contains(MARCADOR_DE_CODIGO);
    }
}
