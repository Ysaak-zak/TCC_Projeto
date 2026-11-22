package com.api.Projeto_3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;



@Table(name = "SEMANA")
@Entity
public class SemanaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

 
    @ManyToOne(optional = false)
    @JoinColumn(name = "atleta_fk", nullable = false)
    private AtletaModelo atleta_fk;

    @OneToMany(mappedBy = "semana_fk", cascade = CascadeType.ALL)
    private List<DiaModel> dias = new ArrayList<>();


     public void adicionarDia(DiaModel dia) {

        if (dias.size() >= 7) {
            throw new IllegalStateException(
                "Uma semana não pode possuir mais de 7 dias."
            );
        }

        dias.add(dia);
    }


     public SemanaModel(Long id, String nome, String descricao, AtletaModelo atleta_fk, List<DiaModel> dias) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.atleta_fk = atleta_fk;
        this.dias = dias;
     }


     public SemanaModel() {
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


     public AtletaModelo getAtleta_fk() {
         return atleta_fk;
     }


     public void setAtleta_fk(AtletaModelo atleta_fk) {
         this.atleta_fk = atleta_fk;
     }


     public List<DiaModel> getDias() {
         return dias;
     }


     public void setDias(List<DiaModel> dias) {
         this.dias = dias;
     }


     @Override
     public String toString() {
        return "SemanaModel [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", atleta_fk=" + atleta_fk
                + ", dias=" + dias + "]";
     }

    
     
     
    

}
