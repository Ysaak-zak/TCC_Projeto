package com.api.Projeto_3.dtos.model;

import com.api.Projeto_3.model.MoradiaModel;
import com.api.Projeto_3.model.PerfisModelo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@ToString

public class MoradiaDto {
    private long id; 

    //@NotBlank(message = "cep não pode ser zero")
    private String cep;
    
    //@NotBlank(message = "avenida não pode ser zero")
    private String avenida;

    //@NotBlank(message = "sn não pode ser zero")
    private String sn;

    //@NotBlank(message = "Bairro  não pode ser zero")
    private String bairro;

    //@NotBlank(message = "Digite o nome da cidade")
    private String cidade;
    private PerfisModelo perfil;


     public MoradiaDto() {
        
    }
        
public MoradiaDto(MoradiaModel entiy) {
        this.id = entiy.getId();
        this.cep = entiy.getCep();
        this.avenida = entiy.getAvenida();
        this.sn = entiy.getSn();
        this.bairro = entiy.getBairro();
        this.cidade = entiy.getCidade();
    }
    

}