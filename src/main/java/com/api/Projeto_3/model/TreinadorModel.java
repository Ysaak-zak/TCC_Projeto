package com.api.Projeto_3.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.api.Projeto_3.model.enums.EnumGenero;
import com.api.Projeto_3.model.enums.EnumSague;
import com.api.Projeto_3.model.enums.EnumUf;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TREINADORES")
public class TreinadorModel  extends PerfisModelo{

    public TreinadorModel() {
        super();
    }
public TreinadorModel(Long id, String fotoImg, String name, LocalDate dataNascimento, String cpf, String rg,
                 String email, String senha, String telefoneFixo, String telefoneZap, String pesoMigrama,
                 String alturaCetimentro, EnumUf uf, EnumGenero sexo, EnumSague sangue, 
                 AfiliacaoModelo pais, MoradiaModel moradia, RolesModel role) { 

    super(id, fotoImg, name, dataNascimento, cpf, rg, email, senha, 
          telefoneFixo, telefoneZap, pesoMigrama, alturaCetimentro, uf, sexo, sangue, pais, moradia, role);
}

    @ManyToOne
    @JoinColumn(name = "treimanento_fk")
    private TreinamentoModel treimanento_fk;

    @OneToMany(mappedBy = "treinador_fk")
    private List<AtletaModelo> atletas = new ArrayList<>();

    public TreinamentoModel getTreimanento_fk() {
        return treimanento_fk;
    }
    public void setTreimanento_fk(TreinamentoModel treimanento_fk) {
        this.treimanento_fk = treimanento_fk;
    }
    public List<AtletaModelo> getAtletas() {
        return atletas;
    }
    public void setAtletas(List<AtletaModelo> atletas) {
        this.atletas = atletas;
    }
    @Override
    public String toString() {
        return "TreinadorModel [treimanento_fk=" + treimanento_fk + ", atletas=" + atletas + "]";
    }


    
    
}
