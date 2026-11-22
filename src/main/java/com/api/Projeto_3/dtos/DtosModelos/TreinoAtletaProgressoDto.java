package com.api.Projeto_3.dtos.DtosModelos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreinoAtletaProgressoDto {
    private Acao acao;
    private List<Long> exercicioIds = new ArrayList<>();

    public enum Acao {
        CONCLUIR_EXERCICIOS, CONCLUIR_TREINO
    }

    @Getter
    @AllArgsConstructor
    public static class Resultado {
        private final Long semanaId;
        private final boolean sucesso;
        private final String mensagem;
    }
}
