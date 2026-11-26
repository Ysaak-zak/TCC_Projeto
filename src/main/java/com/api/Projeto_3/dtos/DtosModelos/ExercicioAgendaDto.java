package com.api.Projeto_3.dtos.DtosModelos;



public class ExercicioAgendaDto {
    private Long id;
    private String nome;
    private String descricao;
    private double duracao;
    private boolean concluido;
   
    public ExercicioAgendaDto(Long id, String nome, String descricao, double duracao, boolean concluido) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
        this.concluido = concluido;
    }

    public ExercicioAgendaDto() {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getDuracao() {
        return duracao;
    }

    public void setDuracao(double duracao) {
        this.duracao = duracao;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    @Override
    public String toString() {
        return "ExercicioAgendaDto [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", duracao=" + duracao
                + ", concluido=" + concluido + "]";
    }

    
}
