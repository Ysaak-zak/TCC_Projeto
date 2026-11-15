package com.api.Projeto_3.dtos.enumDtos;

public enum EnumResullBinario {
    
    NEGATIVO("NEGATIVO", "Negativo"),
    POSITIVO("POSITIVO", "Positivo");

    private final String sigla;
    private final String name;

    private EnumResullBinario(String sigla, String name) {
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
