package com.api.Projeto_3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.api.Projeto_3.model.enums.DiasEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;




@Entity
@Table(name = "DIA")
public class DiaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiasEnum nome;


    @ManyToOne
    @JoinColumn(name = "semana_fk", nullable = false)
 
    private SemanaModel semana_fk;

    @OneToMany(mappedBy = "dia_fk", cascade = CascadeType.ALL)
    
    private List<TreinamentoModel> treinamentos = new ArrayList<>();

    

    public DiaModel() {
    }

    public DiaModel(Long id, DiasEnum nome, SemanaModel semana_fk, List<TreinamentoModel> treinamentos) {
        this.id = id;
        this.nome = nome;
        this.semana_fk = semana_fk;
        this.treinamentos = treinamentos;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiasEnum getNome() {
        return nome;
    }

    public void setNome(DiasEnum nome) {
        this.nome = nome;
    }

    public SemanaModel getSemana_fk() {
        return semana_fk;
    }

    public void setSemana_fk(SemanaModel semana_fk) {
        this.semana_fk = semana_fk;
    }

    public List<TreinamentoModel> getTreinamentos() {
        return treinamentos;
    }

    public void setTreinamentos(List<TreinamentoModel> treinamentos) {
        this.treinamentos = treinamentos;
    }

    @Override
    public String toString() {
        return "DiaModel [id=" + id + ", nome=" + nome + ", semana_fk=" + semana_fk + ", treinamentos=" + treinamentos
                + "]";
    }




    

}

