package com.api.Projeto_3.dtos.DtosModelos;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RecomendacaoAlimentarDto {
    @NotBlank
    @Size(max = 40)
    private String refeicao;

    @NotBlank
    @Size(max = 120)
    private String objetivo;

    @NotBlank
    @Size(max = 2000)
    private String orientacao;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataRevisao;

    public RecomendacaoAlimentarDto(@NotBlank @Size(max = 40) String refeicao,
            @NotBlank @Size(max = 120) String objetivo, @NotBlank @Size(max = 2000) String orientacao,
            @NotNull LocalDate dataInicio, LocalDate dataRevisao) {
        this.refeicao = refeicao;
        this.objetivo = objetivo;
        this.orientacao = orientacao;
        this.dataInicio = dataInicio;
        this.dataRevisao = dataRevisao;
    }

    public RecomendacaoAlimentarDto() {
    }

    public String getRefeicao() {
        return refeicao;
    }

    public void setRefeicao(String refeicao) {
        this.refeicao = refeicao;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataRevisao() {
        return dataRevisao;
    }

    public void setDataRevisao(LocalDate dataRevisao) {
        this.dataRevisao = dataRevisao;
    }

    @Override
    public String toString() {
        return "RecomendacaoAlimentarDto [refeicao=" + refeicao + ", objetivo=" + objetivo + ", orientacao="
                + orientacao + ", dataInicio=" + dataInicio + ", dataRevisao=" + dataRevisao + "]";
    }

    
    
}
