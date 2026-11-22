package com.api.Projeto_3.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "EXERCICIO")
public class ExercicioModel implements Serializable {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private double duracacao = 0;
    private String descricao;
    private Boolean concluido;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "treinamento_fk", nullable = false)
    @ToString.Exclude
    private TreinamentoModel treinamento_fk;

    
    public ExercicioModel(Long id, String nome, double duracacao, String descricao, TreinamentoModel treinamento_fk) {
        this.id = id;
        this.nome = nome;
        this.duracacao = duracacao;
        this.descricao = descricao;
        this.treinamento_fk = treinamento_fk;
    }


    public ExercicioModel() {
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


    public double getDuracacao() {
        return duracacao;
    }


    public void setDuracacao(double duracacao) {
        this.duracacao = duracacao;
    }


    public String getDescricao() {
        return descricao;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluido() {
        return Boolean.TRUE.equals(concluido);
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }


    public TreinamentoModel getTreinamento_fk() {
        return treinamento_fk;
    }


    public void setTreinamento_fk(TreinamentoModel treinamento_fk) {
        this.treinamento_fk = treinamento_fk;
    }


    

    
}
