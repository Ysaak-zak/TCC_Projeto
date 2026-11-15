package com.api.Projeto_3.dtos.enumDtos;


public enum EnumResulBioqu {

    NEGATIVO("NEGATIVO", "Negativo"),
    TRACOS("TRACOS", "Traços"),
    POSITIVO("POSITIVO", "Positivo");

    private final String sigla;
    private final String name;

    private EnumResulBioqu(String sigla, String name) {
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