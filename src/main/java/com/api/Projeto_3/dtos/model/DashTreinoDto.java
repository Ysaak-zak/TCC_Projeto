package com.api.Projeto_3.dtos.model;

public class DashTreinoDto {
    private Integer satisfacao;
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private Long duracao;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getDuracao() {
        return duracao;
    }

    public void setDuracao(Long duracao) {
        this.duracao = duracao;
    }
    public Integer getSatisfacao() {
        return satisfacao;
    }

    public void setSatisfacao(Integer satisfacao) {
        this.satisfacao = satisfacao;
    }
}
