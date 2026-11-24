package com.api.Projeto_3.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles_tb")
public class RolesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    @OneToMany
    private List<PerfisModelo> perfils = new ArrayList<>();
    
    public RolesModel(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    

    public RolesModel() {
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }



    @Override
    public String toString() {
        return "RolesModel [id=" + id + ", authority=" + authority + ", perfils=" + perfils + "]";
    }



    public List<PerfisModelo> getPerfils() {
        return perfils;
    }



    public void setPerfils(List<PerfisModelo> perfils) {
        this.perfils = perfils;
    }

    
    
}
