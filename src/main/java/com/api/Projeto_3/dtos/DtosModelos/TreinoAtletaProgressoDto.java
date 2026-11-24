package com.api.Projeto_3.dtos.DtosModelos;

import java.util.ArrayList;
import java.util.List;

import com.api.Projeto_3.dtos.enumDtos.AcaoProgressoTreino;

public class TreinoAtletaProgressoDto {
    private AcaoProgressoTreino acao;
    private List<Long> exercicioIds = new ArrayList<>();
    
    
    public TreinoAtletaProgressoDto() {
    }


    public TreinoAtletaProgressoDto(AcaoProgressoTreino acao, List<Long> exercicioIds) {
        this.acao = acao;
        this.exercicioIds = exercicioIds;
    }


    public AcaoProgressoTreino getAcao() {
        return acao;
    }


    public void setAcao(AcaoProgressoTreino acao) {
        this.acao = acao;
    }


    public List<Long> getExercicioIds() {
        return exercicioIds;
    }


    public void setExercicioIds(List<Long> exercicioIds) {
        this.exercicioIds = exercicioIds;
    }


    @Override
    public String toString() {
        return "TreinoAtletaProgressoDto [acao=" + acao + ", exercicioIds=" + exercicioIds + "]";
    }

    
    

    
}
