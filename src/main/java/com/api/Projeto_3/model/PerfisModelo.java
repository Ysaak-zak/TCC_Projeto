package com.api.Projeto_3.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.api.Projeto_3.dtos.DtosModelos.RoleDtos;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@ToString
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


	

}