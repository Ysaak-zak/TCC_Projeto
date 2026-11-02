package com.api.Projeto_3.dtos.model;

import java.util.ArrayList;
import java.util.List;
import com.api.Projeto_3.model.SemanaModel;

public class DashDto {
    private Double satisfacaoMedia;
    private String nomeAtleta;
    private Long semanaId;
    private String nomeSemana = "Sem semanas";
    private long tempoTotal;
    private double mediaDiaria;
    private List<SemanaModel> semanas = new ArrayList<>();
    private List<DashTreinoDto> treinos = new ArrayList<>();
    private List<DashPontoDto> atividadeSemanal = new ArrayList<>();
    private List<DashPontoDto> categorias = new ArrayList<>();

    public String getNomeAtleta() {
        return nomeAtleta;
    }

    public void setNomeAtleta(String nomeAtleta) {
        this.nomeAtleta = nomeAtleta;
    }

    public Long getSemanaId() {
        return semanaId;
    }

    public void setSemanaId(Long semanaId) {
        this.semanaId = semanaId;
    }

    public String getNomeSemana() {
        return nomeSemana;
    }

    public void setNomeSemana(String nomeSemana) {
        this.nomeSemana = nomeSemana;
    }

    public long getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(long tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public double getMediaDiaria() {
        return mediaDiaria;
    }

    public void setMediaDiaria(double mediaDiaria) {
        this.mediaDiaria = mediaDiaria;
    }

    public List<SemanaModel> getSemanas() {
        return semanas;
    }

    public void setSemanas(List<SemanaModel> semanas) {
        this.semanas = semanas;
    }

    public List<DashTreinoDto> getTreinos() {
        return treinos;
    }

    public void setTreinos(List<DashTreinoDto> treinos) {
        this.treinos = treinos;
    }

    public List<DashPontoDto> getAtividadeSemanal() {
        return atividadeSemanal;
    }

    public void setAtividadeSemanal(List<DashPontoDto> atividadeSemanal) {
        this.atividadeSemanal = atividadeSemanal;
    }

    public List<DashPontoDto> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<DashPontoDto> categorias) {
        this.categorias = categorias;
    }
    public Double getSatisfacaoMedia() {
        return satisfacaoMedia;
    }

    public void setSatisfacaoMedia(Double satisfacaoMedia) {
        this.satisfacaoMedia = satisfacaoMedia;
    }
}
