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


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

}
