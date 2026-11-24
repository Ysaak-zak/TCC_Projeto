package com.api.Projeto_3.model;

import java.time.LocalDate;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.Projeto_3.model.enums.EnumGenero;
import com.api.Projeto_3.model.enums.EnumSague;
import com.api.Projeto_3.model.enums.EnumUf;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PerfisModelo implements UserDetails {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id; 

   @Column(name = "foto_url")
    private String fotoImg;

    @Column(name = "per_nome" , length =50)
    private String name;

  
   @Column(columnDefinition = "DATE")
    private LocalDate dataNascimento;

    @Column(unique = true , nullable = false)
    private String cpf;

    private String rg;
    
    private String email;

    private String senha;


    private String telefoneFixo;

    private String telefoneZap;

    private String pesoMigrama;

    private String  alturaCetimentro;

    @Enumerated(EnumType.STRING)
    private EnumUf uf;

    @Enumerated(EnumType.STRING)
    private EnumGenero sexo;
    
     @Enumerated(EnumType.STRING)
    private EnumSague sangue;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fal_fk" , unique = true )
    private AfiliacaoModelo pais_fk;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mord")
    private MoradiaModel moradia_fk;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RolesModel role;


    public PerfisModelo() {
    }


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
	}


	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
	}


	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
	}

    

    public PerfisModelo(Long id, String fotoImg, String name, LocalDate dataNascimento, String cpf, String rg,
            String email, String senha, String telefoneFixo, String telefoneZap, String pesoMigrama,
            String alturaCetimentro, EnumUf uf, EnumGenero sexo, EnumSague sangue, AfiliacaoModelo pais_fk,
            MoradiaModel moradia_fk, RolesModel role) {
        this.id = id;
        this.fotoImg = fotoImg;
        this.name = name;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.rg = rg;
        this.email = email;
        this.senha = senha;
        this.telefoneFixo = telefoneFixo;
        this.telefoneZap = telefoneZap;
        this.pesoMigrama = pesoMigrama;
        this.alturaCetimentro = alturaCetimentro;
        this.uf = uf;
        this.sexo = sexo;
        this.sangue = sangue;
        this.pais_fk = pais_fk;
        this.moradia_fk = moradia_fk;
        this.role = role;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getFotoImg() {
        return fotoImg;
    }


    public void setFotoImg(String fotoImg) {
        this.fotoImg = fotoImg;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public LocalDate getDataNascimento() {
        return dataNascimento;
    }


    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }


    public String getCpf() {
        return cpf;
    }


    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public String getRg() {
        return rg;
    }


    public void setRg(String rg) {
        this.rg = rg;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getSenha() {
        return senha;
    }


    public void setSenha(String senha) {
        this.senha = senha;
    }


    public String getTelefoneFixo() {
        return telefoneFixo;
    }


    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }


    public String getTelefoneZap() {
        return telefoneZap;
    }


    public void setTelefoneZap(String telefoneZap) {
        this.telefoneZap = telefoneZap;
    }


    public String getPesoMigrama() {
        return pesoMigrama;
    }


    public void setPesoMigrama(String pesoMigrama) {
        this.pesoMigrama = pesoMigrama;
    }


    public String getAlturaCetimentro() {
        return alturaCetimentro;
    }


    public void setAlturaCetimentro(String alturaCetimentro) {
        this.alturaCetimentro = alturaCetimentro;
    }


    public EnumUf getUf() {
        return uf;
    }


    public void setUf(EnumUf uf) {
        this.uf = uf;
    }


    public EnumGenero getSexo() {
        return sexo;
    }


    public void setSexo(EnumGenero sexo) {
        this.sexo = sexo;
    }


    public EnumSague getSangue() {
        return sangue;
    }


    public void setSangue(EnumSague sangue) {
        this.sangue = sangue;
    }


    public AfiliacaoModelo getPais_fk() {
        return pais_fk;
    }


    public void setPais_fk(AfiliacaoModelo pais_fk) {
        this.pais_fk = pais_fk;
    }


    public MoradiaModel getMoradia_fk() {
        return moradia_fk;
    }


    public void setMoradia_fk(MoradiaModel moradia_fk) {
        this.moradia_fk = moradia_fk;
    }


    public RolesModel getRole() {
        return role;
    }


    public void setRole(RolesModel role) {
        this.role = role;
    }


    @Override
    public String toString() {
        return "PerfisModelo [id=" + id + ", fotoImg=" + fotoImg + ", name=" + name + ", dataNascimento="
                + dataNascimento + ", cpf=" + cpf + ", rg=" + rg + ", email=" + email + ", senha=" + senha
                + ", telefoneFixo=" + telefoneFixo + ", telefoneZap=" + telefoneZap + ", pesoMigrama=" + pesoMigrama
                + ", alturaCetimentro=" + alturaCetimentro + ", uf=" + uf + ", sexo=" + sexo + ", sangue=" + sangue
                + ", pais_fk=" + pais_fk + ", moradia_fk=" + moradia_fk + ", role=" + role + "]";
    }

    

	

}