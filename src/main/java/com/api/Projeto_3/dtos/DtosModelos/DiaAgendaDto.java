package com.api.Projeto_3.dtos.DtosModelos;

import java.util.ArrayList;
import java.util.List;

import com.api.Projeto_3.model.enums.DiasEnum;



public class DiaAgendaDto {
    private DiasEnum nome;
    private List<TreinoAgendaDto> treinos = new ArrayList<>();
    
    public DiaAgendaDto() {
    
    }

    public DiaAgendaDto(DiasEnum nome, List<TreinoAgendaDto> treinos) {
        this.nome = nome;
        this.treinos = treinos;
    }

    public DiasEnum getNome() {
        return nome;
    }

    public void setNome(DiasEnum nome) {
        this.nome = nome;
    }

    public List<TreinoAgendaDto> getTreinos() {
        return treinos;
    }

    public void setTreinos(List<TreinoAgendaDto> treinos) {
        this.treinos = treinos;
    }

    @Override
    public String toString() {
        return "DiaAgendaDto [nome=" + nome + ", treinos=" + treinos + "]";
    }

    

}
