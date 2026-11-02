package com.api.Projeto_3.dtos.model;

import java.io.Serializable;

import com.api.Projeto_3.model.AfiliacaoModelo;
import com.api.Projeto_3.model.PerfisModelo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AfiliacaoDtos implements Serializable {
    
    private static final long serialVersionUID = 1L;
 


    private  Long id;
    
   // @NotBlank(message = "O nome da mãe não poder ser vazio")
    //@Size(min = 5 , message = "O nome da mãe dever ser maior que 5 cacachteres")
    private  String maeNome;
    
    //@NotBlank(message = "O telefone não pode ser vazio")
    private  String maeTelefone;

    //@Email(message = "Email da mãe , não poder  invalido")
    //@NotBlank(message = "Email da mãe não pode ser vazio")
    private  String maeEmail;

    //@NotBlank(message = "O nome do pai não poder ser vazio")
    //@Size(min = 5 , message = "O nome da pai dever ser maior que 5 cacachteres")
    private  String paiName;

    //@NotBlank(message = "O telefone não pode ser vazio")
    private  String paiTelefone;
    
    
    //@Email(message = "Email da pai , não poder  invalido")
    //@NotBlank(message = "Email do pai  não pode ser vazio")
    private  String paiEmail;
    private PerfisModelo perfis;

      public AfiliacaoDtos(){
    
    
    }

    public AfiliacaoDtos(AfiliacaoModelo model) {   
        this.id = model.getId();
        this.maeNome = model.getMaeNome();
        this.maeTelefone = model.getMaeTelefone();
        this.maeEmail = model.getMaeEmail();
        this.paiName = model.getPaiName(); 
        this.paiTelefone = model.getPaiTelefone();
        this.paiEmail = model.getPaiEmail();
        this.perfis = model.getPerfis(); 
    }

}