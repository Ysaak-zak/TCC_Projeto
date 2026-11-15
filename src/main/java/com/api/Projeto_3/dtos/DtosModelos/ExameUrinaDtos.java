package com.api.Projeto_3.dtos.DtosModelos;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import com.api.Projeto_3.dtos.chat.AlertaTriagemDto;
import com.api.Projeto_3.dtos.chat.ProcessamentoChat;

public class ExameUrinaDtos {

    private Long atletaId;

    private LocalDate dataColeta;

    private Double temperatura;

    private String cor;

    private Double ph;

    private Double densidade;

    private Double creatinina;

    private String proteinas;

    private String glicose;

    private String sangue;

    private String anabolizantes;

    private String estimulantes;

    private String diureticos;

    private String observacoes;

    private ProcessamentoChat analise;

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

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Double getPh() {
        return ph;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public Double getDensidade() {
        return densidade;
    }

    public void setDensidade(Double densidade) {
        this.densidade = densidade;
    }

    public Double getCreatinina() {
        return creatinina;
    }

    public void setCreatinina(Double creatinina) {
        this.creatinina = creatinina;
    }

    public String getProteinas() {
        return proteinas;
    }

    public void setProteinas(String proteinas) {
        this.proteinas = proteinas;
    }

    public String getGlicose() {
        return glicose;
    }

    public void setGlicose(String glicose) {
        this.glicose = glicose;
    }

    public String getSangue() {
        return sangue;
    }

    public void setSangue(String sangue) {
        this.sangue = sangue;
    }

    public String getAnabolizantes() {
        return anabolizantes;
    }

    public void setAnabolizantes(String anabolizantes) {
        this.anabolizantes = anabolizantes;
    }

    public String getEstimulantes() {
        return estimulantes;
    }

    public void setEstimulantes(String estimulantes) {
        this.estimulantes = estimulantes;
    }

    public String getDiureticos() {
        return diureticos;
    }

    public void setDiureticos(String diureticos) {
        this.diureticos = diureticos;
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

    public List<AlertaTriagemDto> getAlertasTriagem() {
        var alertas = new ArrayList<AlertaTriagemDto>();
        adicionarAlerta(alertas, "Anabolizantes", anabolizantes);
        adicionarAlerta(alertas, "Estimulantes", estimulantes);
        adicionarAlerta(alertas, "Diuréticos", diureticos);
        return List.copyOf(alertas);
    }

    private void adicionarAlerta(List<AlertaTriagemDto> alertas,
            String classe, String status) {
        if ("SUSPEITO".equals(status)) {
            alertas.add(new AlertaTriagemDto(classe, "Suspeito na triagem"));
        } else if ("INCONCLUSIVO".equals(status)) {
            alertas.add(new AlertaTriagemDto(classe, "Inconclusivo"));
        }
    }

    @Override
    public String toString() {
        return "ExameUrinaDtos [atletaId=" + atletaId + ", dataColeta=" + dataColeta + ", temperatura=" + temperatura
                + ", cor=" + cor + ", ph=" + ph + ", densidade=" + densidade + ", creatinina=" + creatinina
                + ", proteinas=" + proteinas + ", glicose=" + glicose + ", sangue=" + sangue + ", anabolizantes="
                + anabolizantes + ", estimulantes=" + estimulantes + ", diureticos=" + diureticos + ", observacoes="
                + observacoes + "]";
    }
}
