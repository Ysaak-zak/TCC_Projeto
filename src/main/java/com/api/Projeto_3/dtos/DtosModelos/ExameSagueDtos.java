package com.api.Projeto_3.dtos.DtosModelos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.api.Projeto_3.dtos.chat.ProcessamentoChat;
import java.util.ArrayList;
import java.util.List;

public class ExameSagueDtos {

    private Long id;

    private LocalDate dataColeta;

    private String tipoAmostra;

    private BigDecimal hemacias;

    private String analiseIa;

    private ProcessamentoChat analise;

    private LocalDateTime criadoEm;


    private Long atletaId;

    private BigDecimal hemoglobina;

    private BigDecimal hematocrito;

    private BigDecimal reticulocitos;

    private List<SangueSubsDtos> substancias = new ArrayList<>();

    private String observacoes;

    public Long getAtletaId() {
        return atletaId;
    }

    public void setAtletaId(Long atletaId) {
        this.atletaId = atletaId;
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

    public BigDecimal getReticulocitos() {
        return reticulocitos;
    }

    public void setReticulocitos(BigDecimal reticulocitos) {
        this.reticulocitos = reticulocitos;
    }

    public List<SangueSubsDtos> getSubstancias() {
        return substancias;
    }

    public void setSubstancias(List<SangueSubsDtos> substancias) {
        this.substancias = substancias;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getHemacias() {
        return hemacias;
    }

    public void setHemacias(BigDecimal hemacias) {
        this.hemacias = hemacias;
    }

    public String getAnaliseIa() {
        return analiseIa;
    }

    public void setAnaliseIa(String analiseIa) {
        this.analiseIa = analiseIa;
    }

    public ProcessamentoChat getAnalise() {
        return analise;
    }

    public void setAnalise(ProcessamentoChat analise) {
        this.analise = analise;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public String toString() {
        return "ExameSagueDtos [id=" + id + ", dataColeta=" + dataColeta + ", tipoAmostra=" + tipoAmostra
                + ", hemacias=" + hemacias + ", analiseIa=" + analiseIa + ", analise=" + analise + ", criadoEm="
                + criadoEm + ", atletaId=" + atletaId + ", hemoglobina=" + hemoglobina + ", hematocrito=" + hematocrito
                + ", reticulocitos=" + reticulocitos + ", substancias=" + substancias + ", observacoes=" + observacoes
                + "]";
    }


    
}
