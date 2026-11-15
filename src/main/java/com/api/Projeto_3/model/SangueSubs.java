package com.api.Projeto_3.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SANGUE_SUBSTANCIA")
public class SangueSubs implements Serializable {

     private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exame_sangue_id", nullable = false)
    @JsonIgnore
    private ExameSangue exameSangue;

    @Column(name = "nome_substancia", length = 100, nullable = false)
    private String nomeSubstancia;

    @Column(name = "categoria_substancia", length = 100)
    private String categoriaSubstancia;

    @Column(name = "resultado_triagem", length = 30)
    private String resultadoTriagem;

    @Column(precision = 12, scale = 4)
    private BigDecimal concentracao;

    @Column(name = "unidade_medida", length = 20)
    private String unidadeMedida;

    @Column(name = "metodo_analise", length = 100)
    private String metodoAnalise;

    @Column(name = "resultado_confirmacao", length = 30)
    private String resultadoConfirmacao;

    public SangueSubs(Long id, ExameSangue exameSangue, String nomeSubstancia, String categoriaSubstancia,
            String resultadoTriagem, BigDecimal concentracao, String unidadeMedida, String metodoAnalise,
            String resultadoConfirmacao) {
        this.id = id;
        this.exameSangue = exameSangue;
        this.nomeSubstancia = nomeSubstancia;
        this.categoriaSubstancia = categoriaSubstancia;
        this.resultadoTriagem = resultadoTriagem;
        this.concentracao = concentracao;
        this.unidadeMedida = unidadeMedida;
        this.metodoAnalise = metodoAnalise;
        this.resultadoConfirmacao = resultadoConfirmacao;
    }

    public SangueSubs() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExameSangue getExameSangue() {
        return exameSangue;
    }

    public void setExameSangue(ExameSangue exameSangue) {
        this.exameSangue = exameSangue;
    }

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

    @Override
    public String toString() {
        return "SangueSubs [id=" + id + ", exameSangue=" + exameSangue + ", nomeSubstancia=" + nomeSubstancia
                + ", categoriaSubstancia=" + categoriaSubstancia + ", resultadoTriagem=" + resultadoTriagem
                + ", concentracao=" + concentracao + ", unidadeMedida=" + unidadeMedida + ", metodoAnalise="
                + metodoAnalise + ", resultadoConfirmacao=" + resultadoConfirmacao + "]";
    }

    
    
    
}
