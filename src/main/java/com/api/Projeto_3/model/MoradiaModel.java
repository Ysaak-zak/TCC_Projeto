package com.api.Projeto_3.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "MORADIA")
public class MoradiaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cep;

    private String avenida;

    private String sn;

    private String bairro;

    private String cidade;

    @OneToOne(mappedBy = "moradia_fk")
    private PerfisModelo perfil;

   

    

    public MoradiaModel() {
    
    }





    public MoradiaModel(long id, String cep, String avenida, String sn, String bairro, String cidade,
            PerfisModelo perfil) {
        this.id = id;
        this.cep = cep;
        this.avenida = avenida;
        this.sn = sn;
        this.bairro = bairro;
        this.cidade = cidade;
        this.perfil = perfil;
    }





    public static long getSerialversionuid() {
        return serialVersionUID;
    }





    public long getId() {
        return id;
    }





    public void setId(long id) {
        this.id = id;
    }





    public String getCep() {
        return cep;
    }





    public void setCep(String cep) {
        this.cep = cep;
    }





    public String getAvenida() {
        return avenida;
    }





    public void setAvenida(String avenida) {
        this.avenida = avenida;
    }





    public String getSn() {
        return sn;
    }





    public void setSn(String sn) {
        this.sn = sn;
    }





    public String getBairro() {
        return bairro;
    }





    public void setBairro(String bairro) {
        this.bairro = bairro;
    }





    public String getCidade() {
        return cidade;
    }





    public void setCidade(String cidade) {
        this.cidade = cidade;
    }





    public PerfisModelo getPerfil() {
        return perfil;
    }





    public void setPerfil(PerfisModelo perfil) {
        this.perfil = perfil;
    }





    @Override
    public String toString() {
        return "MoradiaModel [id=" + id + ", cep=" + cep + ", avenida=" + avenida + ", sn=" + sn + ", bairro=" + bairro
                + ", cidade=" + cidade + ", perfil=" + perfil + "]";
    }



    

}
