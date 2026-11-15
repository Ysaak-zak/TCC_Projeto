package com.api.Projeto_3.model.enums;

public enum EnumGlicose {
    NAO_REALIZADO("NAO_REALIZADO", "Não realizado"),
    NEGATIVO("NEGATIVO", "Negativo"),
    TRACOS("TRACOS", "Traços"),
    POSITIVO("POSITIVO", "Positivo"),
    POSITIVO_LEVE("POSITIVO LEVE", "Positivo leve (+)"),
    INCONCLUSIVO("INCONCLUSIVO", "Inconclusivo"),
    AMOSTRA_INVALIDA("AMOSTRA INVALIDA", "Amostra inválida");

    private final String sigla;
    private final String name;

    private EnumGlicose(String sigla, String name) {
        this.sigla = sigla;
        this.name = name;
    }

    public String getSigla() {
        return sigla;
    }

    public String getName() {
        return name;
    }
}
