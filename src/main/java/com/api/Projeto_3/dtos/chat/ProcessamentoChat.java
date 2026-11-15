package com.api.Projeto_3.dtos.chat;

import java.util.List;


public record ProcessamentoChat(
        String resumo,
        List<String> pontosAtencao,
        List<String> recomendacoes
) {
    public ProcessamentoChat {
        resumo = resumo == null ? "" : resumo;
        pontosAtencao = pontosAtencao == null ? List.of() : List.copyOf(pontosAtencao);
        recomendacoes = recomendacoes == null ? List.of() : List.copyOf(recomendacoes);
    }
}
