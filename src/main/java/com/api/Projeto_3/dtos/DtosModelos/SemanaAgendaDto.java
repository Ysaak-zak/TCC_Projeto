package com.api.Projeto_3.dtos.DtosModelos;


public class SemanaAgendaDto {
    private Long id;
    private String nome;
   
    public SemanaAgendaDto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public SemanaAgendaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "SemanaAgendaDto [id=" + id + ", nome=" + nome + "]";
    }

    


    
}
