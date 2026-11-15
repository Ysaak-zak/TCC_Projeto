package com.api.Projeto_3.dtos.enumDtos;

public enum EnumCorUrina {

    AMARELO_CLARO("AMARELO_CLARO", "Amarelo claro"),
    AMARELO("AMARELO", "Amarelo"),
    AMARELO_ESCURO("AMARELO_ESCURO", "Amarelo escuro"),
    AVERMELHADO("AVERMELHADO", "Avermelhado"),
    OUTRO("OUTRO", "Outro");

    private final String sigla;
    private final String name;

    private EnumCorUrina(String sigla, String name) {
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
