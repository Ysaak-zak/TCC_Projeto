package com.api.Projeto_3.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import com.api.Projeto_3.model.enums.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ATLETAS")
public class AtletaModelo extends PerfisModelo {

    @OneToMany(mappedBy = "atleta_fk", cascade = CascadeType.ALL)
    private List<SemanaModel> semanas = new ArrayList<>();

    public List<SemanaModel> getSemanas() {
        return semanas;
    }

    public void setSemanas(List<SemanaModel> semanas) {
        this.semanas = semanas;
    }

    @ManyToOne
    @JoinColumn(name = "atleta_trenador_fk")
    private TreinadorModel treinador_fk;

    @ManyToOne
    @JoinColumn(name = "atleta_medico_fk")
    private MedicoModelo medico_fk;

    public AtletaModelo(){
        super();
    }

    public TreinadorModel getTreinador_fk() {
        return treinador_fk;
    }

    public void setTreinador_fk(TreinadorModel treinador_fk) {
        this.treinador_fk = treinador_fk;
    }

    public MedicoModelo getMedico_fk() {
        return medico_fk;
    }

    public void setMedico_fk(MedicoModelo medico_fk) {
        this.medico_fk = medico_fk;
    }

  public AtletaModelo(Long id, String fotoImg, String name, LocalDate dataNascimento, String cpf, String rg,
                 String email, String senha, String telefoneFixo, String telefoneZap, String pesoMigrama,
                 String alturaCetimentro, EnumUf uf, EnumGenero sexo, EnumSague sangue, 
                 AfiliacaoModelo pais, MoradiaModel moradia, RolesModel role) { 

    super(id, fotoImg, name, dataNascimento, cpf, rg, email, senha, 
          telefoneFixo, telefoneZap, pesoMigrama, alturaCetimentro, uf, sexo, sangue, pais, moradia, role);
}


}
