package com.api.Projeto_3.dtos.DtosModelos;



public class ResultadoProgressoTreinoDto {
    private final Long semanaId;
    private final boolean sucesso;
    private final String mensagem;
 
    public ResultadoProgressoTreinoDto(Long semanaId, boolean sucesso, String mensagem) {
        this.semanaId = semanaId;
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

   
    public Long getSemanaId() {
        return semanaId;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }


    @Override
    public String toString() {
        return "ResultadoProgressoTreinoDto [semanaId=" + semanaId + ", sucesso=" + sucesso + ", mensagem=" + mensagem
                + "]";
    }


    
    
    

}
