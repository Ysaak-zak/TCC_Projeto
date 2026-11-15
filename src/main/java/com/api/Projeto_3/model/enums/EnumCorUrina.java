package com.api.Projeto_3.model.enums;

public enum EnumCorUrina {
    INCOLOR("Incolor", "INC"),
    AMARELO_CLARO("Amarelo-claro", "ACL"),
    AMARELO_PALHA("Amarelo-palha", "APL"),
    AMARELO("Amarelo", "AMA"),
    AMARELO_ESCURO("Amarelo-escuro", "AES"),
    AMBAR("Âmbar", "AMB"),
    LARANJA("Laranja", "LAR"),
    ROSA("Rosa", "ROS"),
    VERMELHA("Vermelha", "VER"),
    MARROM("Marrom", "MAR"),
    VERDE("Verde", "VRD"),
    AZUL("Azul", "AZU"),
    BRANCA_LEITOSA("Branca ou leitosa", "BRL");

     private final String rotulo;
    private final String abreviacao;


    private EnumCorUrina(String rotulo, String abreviacao) {
        this.rotulo = rotulo;
        this.abreviacao = abreviacao;
    }


    public String getRotulo() {
        return rotulo;
    }


    public String getAbreviacao() {
        return abreviacao;
    }

    
    
}

