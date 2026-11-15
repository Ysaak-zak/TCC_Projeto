package com.api.Projeto_3.model.enums;


public enum EnumTriagemAntiAntidopin {

    NAO_REALIZADO("NAO_REALIZADO", "Não realizado"),
    NEGATIVO("NEGATIVO", "Negativo na triagem"),
    SUSPEITO("SUSPEITO", "Suspeito na triagem"),
    INCONCLUSIVO("INCONCLUSIVO", "Inconclusivo");

    private final String sigla;
    private final String name;

    private EnumTriagemAntiAntidopin(String sigla, String name) {
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