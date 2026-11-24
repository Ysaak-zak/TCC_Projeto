package com.api.Projeto_3.dtos.DtosModelos;
import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.model.TreinadorModel;
import com.api.Projeto_3.model.enums.EnumGenero;
import com.api.Projeto_3.model.enums.EnumSague;
import com.api.Projeto_3.model.enums.EnumUf;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



public class PerfilsDtos {
   private Long id;  

   //@NotBlank(message = "O campo de nome não pode vazio !!!")
   //@Size(min = 5  , max = 50 , message = "O nome de ser maior que 5 letreas")
    private String name;
    
    private MultipartFile fotoImg;

    //@NotNull(message = "A data de nascimento é obrigatória")
    //@PastOrPresent(message = "A data não pode estar no futuro")
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    //@CPF(message = "CPF inválido")
    private String cpf;

 
    //@NotBlank(message = "O campo não pode estar vazio")
    private String rg;

    //@Email(message = "Deve ser um email valido !!")
    //@NotBlank(message = "O emial não ser vazio")
    private String email;

    //@Size(min = 5 , message = "Senha ter mas de  5 cacteres")
    private String senha;
    
    //@NotBlank(message = "O campo não pode estar vazio")
    private String telefoneFixo;
    
    //@NotBlank(message = "O campo não pode estar vazio")
    private String telefoneZap;
    
    //@NotBlank(message = "O campo não pode estar vazio")
    private String pesoMigrama;

    //@NotBlank(message = "O campo não pode estar vazio")
    private String alturaCetimentro;  

    
    //@Valid
    private AfiliacaoDtos pais_fk;  

    //@Valid
    private MoradiaDto moradia_fk;
    
    private EnumUf ufDtos;
    
    private EnumGenero genero;

    private EnumSague sangue;

    private RoleDtos roles;


    public PerfilsDtos() {
        
    }

    public  PerfilsDtos(AtletaModelo atl) {

            this.id = atl.getId();
            this.name = atl.getName();
            this.dataNascimento = atl.getDataNascimento();
            this.cpf = atl.getCpf();
            this.rg = atl.getRg();
            this.email = atl.getEmail();
            this.senha = atl.getSenha();
            this.telefoneFixo = atl.getTelefoneFixo();
            this.telefoneZap = atl.getTelefoneZap();
            this.pesoMigrama =  atl.getPesoMigrama();
            this.alturaCetimentro = atl.getAlturaCetimentro();
            this.pais_fk = new AfiliacaoDtos(atl.getPais_fk()); 
            this.moradia_fk = new MoradiaDto(atl.getMoradia_fk());
            this.ufDtos = atl.getUf();
            this.genero = atl.getSexo();
            this.sangue = atl.getSangue();
            this.roles = new RoleDtos(atl.getRole());
    }

    public  PerfilsDtos(MedicoModelo med) {
            this.id = med.getId();
            this.name = med.getName();
            this.dataNascimento = med.getDataNascimento();
            this.cpf = med.getCpf();
            this.rg = med.getRg();
            this.email = med.getEmail();
            this.senha = med.getSenha();
            this.telefoneFixo = med.getTelefoneFixo();
            this.telefoneZap = med.getTelefoneZap();
            this.pesoMigrama =  med.getPesoMigrama();
            this.alturaCetimentro = med.getAlturaCetimentro();
            this.pais_fk = new AfiliacaoDtos(med.getPais_fk()); 
            this.moradia_fk = new MoradiaDto(med.getMoradia_fk());
            this.ufDtos = med.getUf();
            this.genero = med.getSexo();
            this.sangue = med.getSangue();
            this.roles = new RoleDtos(med.getRole());
    }


        public  PerfilsDtos(TreinadorModel trei) {
            this.id = trei.getId();
            this.name = trei.getName();
            this.dataNascimento = trei.getDataNascimento();
            this.cpf = trei.getCpf();
            this.rg = trei.getRg();
            this.email = trei.getEmail();
            this.senha = trei.getSenha();
            this.telefoneFixo = trei.getTelefoneFixo();
            this.telefoneZap = trei.getTelefoneZap();
            this.pesoMigrama =  trei.getPesoMigrama();
            this.alturaCetimentro = trei.getAlturaCetimentro();
            this.pais_fk = new AfiliacaoDtos(trei.getPais_fk()); 
            this.moradia_fk = new MoradiaDto(trei.getMoradia_fk());
            this.ufDtos = trei.getUf();
            this.genero = trei.getSexo();
            this.sangue = trei.getSangue();
            this.roles = new RoleDtos(trei.getRole());
    }

        public PerfilsDtos(Long id, String name, MultipartFile fotoImg, LocalDate dataNascimento, String cpf, String rg,
                        String email, String senha, String telefoneFixo, String telefoneZap, String pesoMigrama,
                        String alturaCetimentro, AfiliacaoDtos pais_fk, MoradiaDto moradia_fk, EnumUf ufDtos,
                        EnumGenero genero, EnumSague sangue, RoleDtos roles) {
                this.id = id;
                this.name = name;
                this.fotoImg = fotoImg;
                this.dataNascimento = dataNascimento;
                this.cpf = cpf;
                this.rg = rg;
                this.email = email;
                this.senha = senha;
                this.telefoneFixo = telefoneFixo;
                this.telefoneZap = telefoneZap;
                this.pesoMigrama = pesoMigrama;
                this.alturaCetimentro = alturaCetimentro;
                this.pais_fk = pais_fk;
                this.moradia_fk = moradia_fk;
                this.ufDtos = ufDtos;
                this.genero = genero;
                this.sangue = sangue;
                this.roles = roles;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public MultipartFile getFotoImg() {
                return fotoImg;
        }

        public void setFotoImg(MultipartFile fotoImg) {
                this.fotoImg = fotoImg;
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

        public AfiliacaoDtos getPais_fk() {
                return pais_fk;
        }

        public void setPais_fk(AfiliacaoDtos pais_fk) {
                this.pais_fk = pais_fk;
        }

        public MoradiaDto getMoradia_fk() {
                return moradia_fk;
        }

        public void setMoradia_fk(MoradiaDto moradia_fk) {
                this.moradia_fk = moradia_fk;
        }

        public EnumUf getUfDtos() {
                return ufDtos;
        }

        public void setUfDtos(EnumUf ufDtos) {
                this.ufDtos = ufDtos;
        }

        public EnumGenero getGenero() {
                return genero;
        }

        public void setGenero(EnumGenero genero) {
                this.genero = genero;
        }

        public EnumSague getSangue() {
                return sangue;
        }

        public void setSangue(EnumSague sangue) {
                this.sangue = sangue;
        }

        public RoleDtos getRoles() {
                return roles;
        }

        public void setRoles(RoleDtos roles) {
                this.roles = roles;
        }

        @Override
        public String toString() {
                return "PerfilsDtos [id=" + id + ", name=" + name + ", fotoImg=" + fotoImg + ", dataNascimento="
                                + dataNascimento + ", cpf=" + cpf + ", rg=" + rg + ", email=" + email + ", senha="
                                + senha + ", telefoneFixo=" + telefoneFixo + ", telefoneZap=" + telefoneZap
                                + ", pesoMigrama=" + pesoMigrama + ", alturaCetimentro=" + alturaCetimentro
                                + ", pais_fk=" + pais_fk + ", moradia_fk=" + moradia_fk + ", ufDtos=" + ufDtos
                                + ", genero=" + genero + ", sangue=" + sangue + ", roles=" + roles + "]";
        }


        
        
    

}