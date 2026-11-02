package com.api.Projeto_3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.api.Projeto_3.model.enums.*;

@Entity
@Table(name = "MEDICOS")
public class MedicoModelo extends PerfisModelo {

    @OneToMany(mappedBy = "medico_fk")
    private List<AtletaModelo> atletas = new ArrayList<>();

    public MedicoModelo(){
        super();
    }

    public List<AtletaModelo> getAtletas() {
        return atletas;
    }

    public void setAtletas(List<AtletaModelo> atletas) {
        this.atletas = atletas;
    }

 public MedicoModelo(Long id, String fotoImg, String name, LocalDate dataNascimento, String cpf, String rg,
                 String email, String senha,String telefoneFixo,String telefoneZap,String pesoMigrama,
                String alturaCetimentro, EnumUf uf, EnumGenero sexo, EnumSague sangue, 
                 AfiliacaoModelo pais, MoradiaModel moradia, RolesModel role) { 

    super(id, fotoImg, name, dataNascimento, cpf, rg, email, senha, 
          telefoneFixo, telefoneZap, pesoMigrama, alturaCetimentro, uf, sexo, sangue, pais, moradia, role);
}
}
