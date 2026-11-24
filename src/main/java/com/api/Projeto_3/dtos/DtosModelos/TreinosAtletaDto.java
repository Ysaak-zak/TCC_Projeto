package com.api.Projeto_3.dtos.DtosModelos;

import java.util.ArrayList;
import java.util.List;


public class TreinosAtletaDto {
    private String nomeAtleta;
    private Long semanaId;
    private List<SemanaAgendaDto> semanas = new ArrayList<>();
    private List<DiaAgendaDto> dias = new ArrayList<>();
   
    public TreinosAtletaDto(String nomeAtleta, Long semanaId, List<SemanaAgendaDto> semanas, List<DiaAgendaDto> dias) {
        this.nomeAtleta = nomeAtleta;
        this.semanaId = semanaId;
        this.semanas = semanas;
        this.dias = dias;
    }

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

    public List<SemanaAgendaDto> getSemanas() {
        return semanas;
    }

    public void setSemanas(List<SemanaAgendaDto> semanas) {
        this.semanas = semanas;
    }

    public List<DiaAgendaDto> getDias() {
        return dias;
    }

    public void setDias(List<DiaAgendaDto> dias) {
        this.dias = dias;
    }

    @Override
    public String toString() {
        return "TreinosAtletaDto [nomeAtleta=" + nomeAtleta + ", semanaId=" + semanaId + ", semanas=" + semanas
                + ", dias=" + dias + "]";
    }

    
    
}
