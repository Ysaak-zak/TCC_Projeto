package com.api.Projeto_3.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.api.Projeto_3.dtos.interfaceCustomer.CustomerNome;
import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.dtos.interfaceCustomer.TreinadorCustomer;
import com.api.Projeto_3.dtos.model.PerfilsDtos;
import com.api.Projeto_3.dtos.model.RoleDtos;
import com.api.Projeto_3.model.enums.EnumGenero;
import com.api.Projeto_3.model.enums.EnumSague;
import com.api.Projeto_3.model.enums.EnumUf;
import com.api.Projeto_3.service.AtletaService;
import com.api.Projeto_3.service.PerfilService;
import com.api.Projeto_3.service.TreinadorService;

import jakarta.validation.Valid;

@Controller
public class TreinadorController {

    @Autowired
    PerfilService service;

    @Autowired
    TreinadorService serviceTre;

    @Autowired AtletaService atl_servic;


    private String id_username = null;


        @GetMapping("/cadastro/treinador/{id}")
        public String getCadastroTreinador(@PathVariable("id") Long id, PerfilsDtos treinadorDtos, Model model) {
        carregarDadosCadastro(id, model);
        model.addAttribute("treinadorDtos", treinadorDtos);
        return "publicPlages/cadastroTreinador";
    }

@PostMapping("/cadastro/treinador/save/{id}")
public String postTreinadorInsert(@PathVariable("id")   Long id , @ModelAttribute("treinadorDtos") @Valid PerfilsDtos treinadorDtos , BindingResult result,  Model model) {
    
    if(result.hasErrors()){
        carregarDadosCadastro(id, model);
        return "publicPlages/cadastroTreinador"; 
    }  
    
     if(treinadorDtos.getRoles() == null){
        treinadorDtos.setRoles(new RoleDtos());
    }

    treinadorDtos.getRoles().setId(id);

     service.insertTreinador(treinadorDtos);
    return "redirect:/login";
}

 @GetMapping("/treinador")
public String getTreinador(@AuthenticationPrincipal UserDetails userLogado, Model model) {
    
   
    TreinadorCustomer treinador = serviceTre.buscarTreinadorPorId(userLogado.getUsername());
    List<ListDasAtleta> listDas = serviceTre.listTab();

    this.id_username = userLogado.getUsername();

    model.addAttribute("treinador", treinador);
    model.addAttribute("listaAtletas", listDas);

    model.addAttribute("treinador_id_Vincule",treinador.getId());

    final int v = atl_servic.quntAtletaVinculadosTreinador(treinador.getId());
    model.addAttribute("vinculados_count",v);

    final int vSem = atl_servic.quntAtletaSemVinculadosTreinador();
    model.addAttribute("atleta_sem_Vinculacao",vSem);



    return "treinadorPages/dash.html";
}

 
    

 

 @GetMapping("/treinador/perfil")
public String getTreinadorPerfil(@AuthenticationPrincipal UserDetails userLogado, Model model) {
    
    TreinadorCustomer treinador = serviceTre.buscarTreinadorPorId(userLogado.getUsername());
    CustomerNome treinadorNome =  serviceTre.buscarNomes(userLogado.getUsername());

    model.addAttribute("treinador", treinador);
    model.addAttribute("nomeTreinador", treinadorNome);

    return "treinadorPages/Perfil.html";
}

@PostMapping("/treinador/{treinadorId}/vincular-atleta/{atletaId}")
public String vincularAtleta(@PathVariable("treinadorId") Long treinadorId, @PathVariable("atletaId") Long atletaId) {
   atl_servic.insertFkATreinador(atletaId, treinadorId);

    return "redirect:/treinador";
}

private void carregarDadosCadastro(Long id, Model model) {
    model.addAttribute("id", id);
    model.addAttribute("listaUfs", EnumUf.values());
    model.addAttribute("listSague", EnumSague.values());
    model.addAttribute("listEnumGenero", EnumGenero.values());
    model.addAttribute("roleId", service.infoRoles(id));
    model.addAttribute("roleNome", service.buscarNomeRoles(id));
}

}
