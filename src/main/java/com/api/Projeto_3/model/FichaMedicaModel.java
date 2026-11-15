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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "FICHAMEIDCA")
public class FichaMedicaModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id; 
    @OneToOne(mappedBy = "atlFixaMedic" , cascade = CascadeType.ALL)
    private AtletaModelo atl;

    @OneToMany
    @JoinColumn(name = "urinaExame")
    private List<EmaxeUrina> exameUrinaAtl = new ArrayList<>();

}

