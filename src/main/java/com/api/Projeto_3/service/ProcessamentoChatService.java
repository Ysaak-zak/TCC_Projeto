package com.api.Projeto_3.service;

import java.util.List;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;
import com.api.Projeto_3.dtos.chat.ProcessamentoChat;

@Service
public class ProcessamentoChatService {
    private final JsonMapper mapper = JsonMapper.builder().build();

    public ProcessamentoChat separarResposta(String resposta) {
        if (resposta == null || resposta.isBlank()) {
            return new ProcessamentoChat("Análise não disponível.", List.of(), List.of());
        }
        String json = resposta.strip();
        // Alguns modelos envolvem o JSON em um bloco Markdown.
        if (json.startsWith("```")) {
            json = json.replaceFirst("^```(?:json)?\\s*", "")
                    .replaceFirst("\\s*```$", "").strip();
        }
        try {
            var root = mapper.readTree(json);
            if (!root.isObject() || !root.path("resumo").isTextual()
                    || !listaDeTextos(root.path("pontosAtencao"))
                    || !listaDeTextos(root.path("recomendacoes"))) {
                return textoOriginal(resposta);
            }
            return mapper.readValue(json, ProcessamentoChat.class);
        } catch (RuntimeException exception) {
            return textoOriginal(resposta);
        }
    }

    private boolean listaDeTextos(tools.jackson.databind.JsonNode node) {
        if (!node.isArray()) return false;
        for (var item : node) {
            if (!item.isTextual()) return false;
        }
        return true;
    }

    private ProcessamentoChat textoOriginal(String resposta) {
        return new ProcessamentoChat(resposta, List.of(), List.of());
    }
}
