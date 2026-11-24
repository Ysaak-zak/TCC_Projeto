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

   

    public FichaMedicaModel() {
    }



    public FichaMedicaModel(Long id, AtletaModelo atl, List<EmaxeUrina> exameUrinaAtl) {
        this.id = id;
        this.atl = atl;
        this.exameUrinaAtl = exameUrinaAtl;
    }



    public static long getSerialversionuid() {
        return serialVersionUID;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public AtletaModelo getAtl() {
        return atl;
    }



    public void setAtl(AtletaModelo atl) {
        this.atl = atl;
    }



    public List<EmaxeUrina> getExameUrinaAtl() {
        return exameUrinaAtl;
    }



    public void setExameUrinaAtl(List<EmaxeUrina> exameUrinaAtl) {
        this.exameUrinaAtl = exameUrinaAtl;
    }



    @Override
    public String toString() {
        return "FichaMedicaModel [id=" + id + ", atl=" + atl + ", exameUrinaAtl=" + exameUrinaAtl + "]";
    }

    
    
    

    
}

