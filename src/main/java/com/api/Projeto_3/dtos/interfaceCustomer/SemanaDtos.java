package com.api.Projeto_3.dtos.interfaceCustomer;

public class SemanaDtos {
    private  Long id;
    private  String Discricao;

    
    
    
    public SemanaDtos(Long id, String discricao) {
        this.id = id;
        Discricao = discricao;
    }


 
    public SemanaDtos() {
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getDiscricao() {
        return Discricao;
    }



    public void setDiscricao(String discricao) {
        Discricao = discricao;
    }



    @Override
    public String toString() {
        return "SemanaDtos [id=" + id + ", Discricao=" + Discricao + "]";
    }
 
 
    
    
}
