package com.api.Projeto_3.model;


import java.io.Serializable;
import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.api.Projeto_3.dtos.enumDtos.EnumTriagemAntiAntidopin;
import com.api.Projeto_3.model.enums.EnumCorUrina;
import com.api.Projeto_3.model.enums.EnumGlicose;
import com.api.Projeto_3.model.enums.EnumProteinas;
import com.api.Projeto_3.model.enums.EnumSangueUrina;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "EXAMA_URINA")
public class EmaxeUrina implements Serializable {
     private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atleta_id")
    private AtletaModelo atleta;

    public AtletaModelo getAtleta() {
        return atleta;
    }

    public void setAtleta(AtletaModelo atleta) {
        this.atleta = atleta;
    }
   
    @Column(name = "data_hora_coleta", nullable = false)
    private Date data_hora_coleta;
    private Date dataHoraRecebimento;

    @CreationTimestamp
    @Column(name = "dataHoraLiberdado")
    private Date dataHoraLiberacao;

    @Column(unique = false, length = 8, scale = 2)
    private double tempeturaAmostraCelsius;

    @Column(unique = false, length = 5)
    private double ph;

    @Column(unique = false, length = 8, scale = 2)
    private double densidade_epecifica;

    @Column(unique = false, length = 8, scale = 2)
    private double creatinina_urinaria;

    @Enumerated
    @Column(name = "glicose", nullable = false)
    private EnumGlicose glicose;

    @Enumerated(EnumType.STRING)
    @Column(name = "proteinas")
    private EnumProteinas proteinas;

    @Enumerated(EnumType.STRING)
    @Column(name = "sangue")
    private EnumSangueUrina sangue;

    @Column(name = "observacoes", columnDefinition = "text")
    private String observacoes;

   
     @Enumerated(EnumType.STRING)
    @Column(name = "cor_amostra", nullable = false)
    private EnumCorUrina cor_amostra;


    @Enumerated(EnumType.STRING)
    @Column(name = "anabolizante", nullable = false)
    private EnumTriagemAntiAntidopin anabolizante;

     @Enumerated(EnumType.STRING)
    @Column(name = "estimulantes", nullable = false)
    private EnumTriagemAntiAntidopin estimulantes;

     @Enumerated(EnumType.STRING)
    @Column(name = "dirutecios", nullable = false)
    private EnumTriagemAntiAntidopin diureticos;



    @Column(unique = false, length = 50)
    private String valorCorteCuoff;

    public EnumProteinas getProteinas() {
        return proteinas;
    }

    public void setProteinas(EnumProteinas proteinas) {
        this.proteinas = proteinas;
    }

    public EnumSangueUrina getSangue() {
        return sangue;
    }

    public void setSangue(EnumSangueUrina sangue) {
        this.sangue = sangue;
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



    public Date getData_hora_coleta() {
        return data_hora_coleta;
    }



    public void setData_hora_coleta(Date data_hora_coleta) {
        this.data_hora_coleta = data_hora_coleta;
    }



    public Date getDataHoraRecebimento() {
        return dataHoraRecebimento;
    }



    public void setDataHoraRecebimento(Date dataHoraRecebimento) {
        this.dataHoraRecebimento = dataHoraRecebimento;
    }



    public Date getDataHoraLiberacao() {
        return dataHoraLiberacao;
    }



    public void setDataHoraLiberacao(Date dataHoraLiberacao) {
        this.dataHoraLiberacao = dataHoraLiberacao;
    }



    public double getTempeturaAmostraCelsius() {
        return tempeturaAmostraCelsius;
    }



    public void setTempeturaAmostraCelsius(double tempeturaAmostraCelsius) {
        this.tempeturaAmostraCelsius = tempeturaAmostraCelsius;
    }



    public double getPh() {
        return ph;
    }



    public void setPh(double ph) {
        this.ph = ph;
    }



    public double getDensidade_epecifica() {
        return densidade_epecifica;
    }



    public void setDensidade_epecifica(double densidade_epecifica) {
        this.densidade_epecifica = densidade_epecifica;
    }



    public double getCreatinina_urinaria() {
        return creatinina_urinaria;
    }



    public void setCreatinina_urinaria(double creatinina_urinaria) {
        this.creatinina_urinaria = creatinina_urinaria;
    }



    public EnumGlicose getGlicose() {
        return glicose;
    }



    public void setGlicose(EnumGlicose glicose) {
        this.glicose = glicose;
    }



    public EnumCorUrina getCor_amostra() {
        return cor_amostra;
    }



    public void setCor_amostra(EnumCorUrina cor_amostra) {
        this.cor_amostra = cor_amostra;
    }



    public EnumTriagemAntiAntidopin getAnabolizante() {
        return anabolizante;
    }



    public void setAnabolizante(EnumTriagemAntiAntidopin anabolizante) {
        this.anabolizante = anabolizante;
    }



    public EnumTriagemAntiAntidopin getEstimulantes() {
        return estimulantes;
    }



    public void setEstimulantes(EnumTriagemAntiAntidopin estimulantes) {
        this.estimulantes = estimulantes;
    }



    public EnumTriagemAntiAntidopin getDiureticos() {
        return diureticos;
    }



    public void setDiureticos(EnumTriagemAntiAntidopin diureticos) {
        this.diureticos = diureticos;
    }



    public String getValorCorteCuoff() {
        return valorCorteCuoff;
    }



    public void setValorCorteCuoff(String valorCorteCuoff) {
        this.valorCorteCuoff = valorCorteCuoff;
    }



    @Override
    public String toString() {
        return "EmaxeUrina [id=" + id + ", data_hora_coleta=" + data_hora_coleta + ", dataHoraRecebimento="
                + dataHoraRecebimento + ", dataHoraLiberacao=" + dataHoraLiberacao + ", tempeturaAmostraCelsius="
                + tempeturaAmostraCelsius + ", ph=" + ph + ", densidade_epecifica=" + densidade_epecifica
                + ", creatinina_urinaria=" + creatinina_urinaria + ", glicose=" + glicose + ", cor_amostra="
                + cor_amostra + ", anabolizante=" + anabolizante + ", estimulantes=" + estimulantes + ", diureticos="
                + diureticos + ", valorCorteCuoff=" + valorCorteCuoff + "]";
    }


    

}
