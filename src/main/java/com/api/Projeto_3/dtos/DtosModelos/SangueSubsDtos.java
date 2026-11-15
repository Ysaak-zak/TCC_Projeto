package com.api.Projeto_3.dtos.DtosModelos;

import java.math.BigDecimal;

public class SangueSubsDtos {

    private String nomeSubstancia;
    private String categoriaSubstancia;
    private String resultadoTriagem;
    private BigDecimal concentracao;
    private String unidadeMedida;
    private String metodoAnalise;
    private String resultadoConfirmacao;

    public String getNomeSubstancia() {
        return nomeSubstancia;
    }

    public void setNomeSubstancia(String nomeSubstancia) {
        this.nomeSubstancia = nomeSubstancia;
    }

    public String getCategoriaSubstancia() {
        return categoriaSubstancia;
    }

    public void setCategoriaSubstancia(String categoriaSubstancia) {
        this.categoriaSubstancia = categoriaSubstancia;
    }

    public String getResultadoTriagem() {
        return resultadoTriagem;
    }

    public void setResultadoTriagem(String resultadoTriagem) {
        this.resultadoTriagem = resultadoTriagem;
    }

    public BigDecimal getConcentracao() {
        return concentracao;
    }

    public void setConcentracao(BigDecimal concentracao) {
        this.concentracao = concentracao;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public String getMetodoAnalise() {
        return metodoAnalise;
    }

    public void setMetodoAnalise(String metodoAnalise) {
        this.metodoAnalise = metodoAnalise;
    }

    public String getResultadoConfirmacao() {
        return resultadoConfirmacao;
    }

    public void setResultadoConfirmacao(String resultadoConfirmacao) {
        this.resultadoConfirmacao = resultadoConfirmacao;
    }
}
