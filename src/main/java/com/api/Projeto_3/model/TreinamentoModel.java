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
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "TREINAMENTO")
@Entity
public class TreinamentoModel implements Serializable {
    private Integer satisfacao;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private java.time.LocalDate dataInicio;
    private java.time.LocalDate dataFim;
    private java.time.LocalTime horarioInicio;
    private java.time.LocalTime horarioFim;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dia_fk", nullable = false)
    @ToString.Exclude
    private DiaModel dia_fk;

    @OneToMany(mappedBy = "treinamento_fk", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ExercicioModel> exercicios = new ArrayList<>();

    @OneToMany(mappedBy = "treimanento_fk" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TreinadorModel> listTreinador = new ArrayList<>();


    public Integer getSatisfacao() {
        return satisfacao;
    }

    public void setSatisfacao(Integer satisfacao) {
        this.satisfacao = satisfacao;
    }
}
