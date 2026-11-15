package com.api.Projeto_3.dtos.DtosModelos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.api.Projeto_3.dtos.chat.ProcessamentoChat;

public class ExmeSagueDtos {

    private Long atletaId;
    private LocalDate dataColeta;
    private String tipoAmostra;
    private BigDecimal hemoglobina;
    private BigDecimal hematocrito;
    private BigDecimal hemacias;
    private BigDecimal reticulocitos;
    private String observacoes;
    private ProcessamentoChat analise;
    private List<SangueSubsDtos> substancias = new ArrayList<>();

    public Long getAtletaId() {
        return atletaId;
    }

    public void setAtletaId(Long atletaId) {
        this.atletaId = atletaId;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }

    public String getTipoAmostra() {
        return tipoAmostra;
    }

    public void setTipoAmostra(String tipoAmostra) {
        this.tipoAmostra = tipoAmostra;
    }

    public BigDecimal getHemoglobina() {
        return hemoglobina;
    }

    public void setHemoglobina(BigDecimal hemoglobina) {
        this.hemoglobina = hemoglobina;
    }

    public BigDecimal getHematocrito() {
        return hematocrito;
    }

    public void setHematocrito(BigDecimal hematocrito) {
        this.hematocrito = hematocrito;
    }

    public BigDecimal getHemacias() {
        return hemacias;
    }

    public void setHemacias(BigDecimal hemacias) {
        this.hemacias = hemacias;
    }

    public BigDecimal getReticulocitos() {
        return reticulocitos;
    }

    public void setReticulocitos(BigDecimal reticulocitos) {
        this.reticulocitos = reticulocitos;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public ProcessamentoChat getAnalise() {
        return analise;
    }

    public void setAnalise(ProcessamentoChat analise) {
        this.analise = analise;
    }

    public List<SangueSubsDtos> getSubstancias() {
        return substancias;
    }

    public void setSubstancias(List<SangueSubsDtos> substancias) {
        this.substancias = substancias;
    }
}
