package com.api.Projeto_3.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity 
public class ExameSangue implements Serializable {
     private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atleta_id", nullable = false)
    private Long atletaId;

    @Column(name = "data_coleta")
    private LocalDate dataColeta;

    @Column(name = "tipo_amostra", length = 20)
    private String tipoAmostra;

    @Column(precision = 5, scale = 2)
    private BigDecimal hemoglobina;

    @Column(precision = 5, scale = 2)
    private BigDecimal hematocrito;

    @Column(precision = 5, scale = 2)
    private BigDecimal hemacias;

    @Column(precision = 5, scale = 2)
    private BigDecimal reticulocitos;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "analise_ia", columnDefinition = "TEXT")
    private String analiseIa;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "exameSangue", cascade = CascadeType.PERSIST)
    private List<SangueSubs> substancias = new ArrayList<>();

    public ExameSangue(Long id, Long atletaId, LocalDate dataColeta, String tipoAmostra, BigDecimal hemoglobina,
            BigDecimal hematocrito, BigDecimal hemacias, BigDecimal reticulocitos, String observacoes, String analiseIa,
            LocalDateTime criadoEm, List<SangueSubs> substancias) {
        this.id = id;
        this.atletaId = atletaId;
        this.dataColeta = dataColeta;
        this.tipoAmostra = tipoAmostra;
        this.hemoglobina = hemoglobina;
        this.hematocrito = hematocrito;
        this.hemacias = hemacias;
        this.reticulocitos = reticulocitos;
        this.observacoes = observacoes;
        this.analiseIa = analiseIa;
        this.criadoEm = criadoEm;
        this.substancias = substancias;
    }

    
    public ExameSangue() {
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getAnaliseIa() {
        return analiseIa;
    }

    public void setAnaliseIa(String analiseIa) {
        this.analiseIa = analiseIa;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<SangueSubs> getSubstancias() {
        return substancias;
    }

    public void setSubstancias(List<SangueSubs> substancias) {
        this.substancias = substancias;
    }


    @Override
    public String toString() {
        return "ExameSangue [id=" + id + ", atletaId=" + atletaId + ", dataColeta=" + dataColeta + ", tipoAmostra="
                + tipoAmostra + ", hemoglobina=" + hemoglobina + ", hematocrito=" + hematocrito + ", hemacias="
                + hemacias + ", reticulocitos=" + reticulocitos + ", observacoes=" + observacoes + ", analiseIa="
                + analiseIa + ", criadoEm=" + criadoEm + ", substancias=" + substancias + "]";
    }


    
    
}
