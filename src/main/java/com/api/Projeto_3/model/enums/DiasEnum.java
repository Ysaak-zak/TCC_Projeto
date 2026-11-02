package com.api.Projeto_3.model.enums;

public enum DiasEnum {
    SEGUNDA("Segunda-feira", "Seg"),
    TERCA("Terça-feira", "Ter"),
    QUARTA("Quarta-feira", "Qua"),
    QUINTA("Quinta-feira", "Qui"),
    SEXTA("Sexta-feira", "Sex"),
    SABADO("Sábado", "Sáb"),
    DOMINGO("Domingo", "Dom");

    private final String rotulo;
    private final String abreviacao;

    DiasEnum(String rotulo, String abreviacao) {
        this.rotulo = rotulo;
        this.abreviacao = abreviacao;
    }
    public String getRotulo() { return rotulo; }
    public String getAbreviacao() { return abreviacao; }
}
