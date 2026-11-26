package com.api.Projeto_3.service;

import java.util.Collections;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException.ConstraintKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.Projeto_3.dtos.DtosModelos.AfiliacaoDtos;
import com.api.Projeto_3.dtos.DtosModelos.MoradiaDto;
import com.api.Projeto_3.dtos.DtosModelos.PerfilsDtos;
import com.api.Projeto_3.model.AfiliacaoModelo;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.model.MoradiaModel;
import com.api.Projeto_3.model.PerfisModelo;
import com.api.Projeto_3.model.RolesModel;
import com.api.Projeto_3.model.TreinadorModel;
import com.api.Projeto_3.model.enums.RoleAuthority;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.MedicoRespository;
import com.api.Projeto_3.repository.PerfilRepository;
import com.api.Projeto_3.repository.RoleRepository;
import com.api.Projeto_3.repository.TreinadorRespository;

import jakarta.transaction.Transactional;

@Service
public class PerfilService implements UserDetailsService {
        
    @Autowired
    PerfilRepository perfilJpa;
    
    @Autowired
    AtletaRespository respository;

    @Autowired
    MedicoRespository jpaMedico;

    @Autowired
    TreinadorRespository jpaTreinador;

    @Autowired
    RoleRepository roles;

    @Autowired
    private PasswordEncoder hast;

    //SAVALNDO PERFILS
    @Transactional
    public PerfilsDtos InsertAtleta(PerfilsDtos dtos){
        AtletaModelo atl = new AtletaModelo();           
        
        preencherDadosBasicos(atl,dtos);
        atl.setRole(buscarRole(RoleAuthority.ROLE_ATLETA));
        salvarAfiliado(dtos.getPais_fk(), atl);
        salvarMoradia(dtos.getMoradia_fk(), atl);
        
        AtletaModelo salvo = respository.save(atl);

        respository.save(salvo);
        return new PerfilsDtos(salvo);
    }

    @Transactional
    public PerfilsDtos insertMeidco( PerfilsDtos dos){
        MedicoModelo med = new MedicoModelo();
        
        preencherDadosBasicos(med, dos);
        med.setRole(buscarRole(RoleAuthority.ROLE_MEDICO));
        salvarAfiliado(dos.getPais_fk(), med);
        salvarMoradia(dos.getMoradia_fk(), med);

        MedicoModelo salveMeidco = jpaMedico.save(med);

        return new PerfilsDtos(salveMeidco);
    }

     @Transactional
    public PerfilsDtos insertTreinador( PerfilsDtos dos){
        TreinadorModel trei = new TreinadorModel();
        
        preencherDadosBasicos(trei, dos);
        trei.setRole(buscarRole(RoleAuthority.ROLE_TREINADOR));
        salvarAfiliado(dos.getPais_fk(), trei);
        salvarMoradia(dos.getMoradia_fk(), trei);

        TreinadorModel salveTrei = jpaTreinador.save(trei);

        return new PerfilsDtos(salveTrei);
    }

    public boolean isCpfDuplicado(DataIntegrityViolationException erro, String cpf) {
        return erro.getCause() instanceof ConstraintViolationException restricao
                && restricao.getKind() == ConstraintKind.UNIQUE
                && perfilJpa.existsByCpf(cpf);
    }

    private RolesModel buscarRole(RoleAuthority authority) {
        return roles.findFirstByAuthorityOrderByIdAsc(authority.name())
                .orElseThrow(() -> new IllegalStateException("Role não encontrada: " + authority));
    }

    private void   preencherDadosBasicos(PerfisModelo perMod, PerfilsDtos dtos){
            perMod.setName(dtos.getName());
            perMod.setDataNascimento(dtos.getDataNascimento());
            perMod.setCpf(dtos.getCpf());
            perMod.setRg(dtos.getRg());
            perMod.setEmail(dtos.getEmail());

            perMod.setSenha(this.hast.encode(dtos.getSenha()));

            perMod.setTelefoneFixo(dtos.getTelefoneFixo());
            perMod.setTelefoneZap(dtos.getTelefoneZap());
            perMod.setPesoMigrama(dtos.getPesoMigrama());
            perMod.setUf(dtos.getUfDtos());
           
            perMod.setAlturaCetimentro(dtos.getAlturaCetimentro());
            perMod.setSangue(dtos.getSangue());
            perMod.setSexo(dtos.getGenero());

     }

    private void salvarAfiliado(AfiliacaoDtos afi , PerfisModelo perMod){
         AfiliacaoModelo pais = new AfiliacaoModelo();

            pais.setMaeNome(afi.getMaeNome());
            pais.setMaeTelefone(afi.getMaeTelefone());
            pais.setMaeEmail(afi.getMaeEmail());
            pais.setPaiName(afi.getPaiName());
            pais.setPaiTelefone(afi.getPaiTelefone());
            pais.setPaiEmail(afi.getPaiEmail());

            perMod.setPais_fk(pais);

    }
    
    
    private void salvarMoradia(MoradiaDto morDtos , PerfisModelo perMod){
            MoradiaModel mor = new MoradiaModel();

            mor.setCep(morDtos.getCep());
            mor.setAvenida(morDtos.getAvenida());
            mor.setSn(morDtos.getSn());
            mor.setBairro(morDtos.getBairro());
            mor.setCidade(morDtos.getCidade());
            
           perMod.setMoradia_fk(mor);

    }


@Override
public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
    PerfisModelo usuario = perfilJpa.findByCpf(cpf)
            .or(() -> perfilJpa.findIdByCpfSemPontuacao(cpf).flatMap(perfilJpa::findById))
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com CPF: " + cpf));

    String authority = normalizarAuthority(usuario.getRole().getAuthority());

    return User.builder()
            .username(usuario.getCpf())
            .password(usuario.getSenha())
            .authorities(new SimpleGrantedAuthority(authority))
            .build();
}

private String normalizarAuthority(String authority) {
    String role = authority == null ? "" : authority.trim().toUpperCase();
    String roleSemSimbolos = role.replaceAll("[^A-Z]", "");

    if (roleSemSimbolos.contains("ATLETA")) {
        return "ROLE_ATLETA";
    }
    if (roleSemSimbolos.contains("MEDICO")) {
        return "ROLE_MEDICO";
    }
    if (roleSemSimbolos.contains("TREINADOR")) {
        return "ROLE_TREINADOR";
    }

    if (role.startsWith("ROLE_")) {
        return role;
    }
    return "ROLE_" + role;
}

}
