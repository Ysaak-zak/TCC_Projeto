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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "EXERCICIO")
public class ExercicioModel implements Serializable {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private double duracacao = 0;
    private String descricao;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "treinamento_fk", nullable = false)
    @ToString.Exclude
    private TreinamentoModel treinamento_fk;
}
