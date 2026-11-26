package com.api.Projeto_3.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.Projeto_3.dtos.chat.ProcessamentoChat;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Service
public class ProcessamentoChatService {
    private static final String MARCADOR_DE_CODIGO = "```";
    private static final ProcessamentoChat ANALISE_INDISPONIVEL =
            new ProcessamentoChat("Análise não disponível.", List.of(), List.of());

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
            JsonNode analise = leitorJson.readTree(retirarMarcadoresDeCodigo(texto));
            if (!textoValido(analise.path("resumo"))
                    || !listaDeTextosValida(analise.path("pontosAtencao"))
                    || !listaDeTextosValida(analise.path("recomendacoes"))) {
                return ANALISE_INDISPONIVEL;
            }

            return leitorJson.treeToValue(analise, ProcessamentoChat.class);
        } catch (JacksonException exception) {
            return ANALISE_INDISPONIVEL;
        }
    }

    private String retirarMarcadoresDeCodigo(String texto) {
        int tamanhoDoMarcador = MARCADOR_DE_CODIGO.length();
        if (texto.length() < 2 * tamanhoDoMarcador
                || !texto.startsWith(MARCADOR_DE_CODIGO)
                || !texto.endsWith(MARCADOR_DE_CODIGO)) {
            return texto;
        }

        String conteudo = texto.substring(tamanhoDoMarcador, texto.length() - tamanhoDoMarcador);
        if (conteudo.regionMatches(true, 0, "json", 0, 4)) {
            conteudo = conteudo.substring(4);
        }
        return conteudo.strip();
    }

    private boolean listaDeTextosValida(JsonNode lista) {
        return lista.isArray() && lista.valueStream().allMatch(this::textoValido);
    }

    private boolean textoValido(JsonNode campo) {
        if (!campo.isString()) {
            return false;
        }
        String texto = campo.stringValue().strip();
        return !texto.isEmpty() && !pareceJsonOuCodigo(texto);
    }

    private boolean pareceJsonOuCodigo(String texto) {
        return texto.startsWith("{") || texto.startsWith("[") || texto.contains(MARCADOR_DE_CODIGO);
    }
}
