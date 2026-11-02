package com.api.Projeto_3.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.Projeto_3.dtos.model.PerfilsDtos;
import com.api.Projeto_3.dtos.model.RoleDtos;
import com.api.Projeto_3.model.enums.EnumGenero;
import com.api.Projeto_3.model.enums.EnumSague;
import com.api.Projeto_3.model.enums.EnumUf;
import com.api.Projeto_3.service.PerfilService;
import com.api.Projeto_3.service.AtletaService;

import jakarta.validation.Valid;


@Controller
public class AtletaController {

     @Autowired
    PerfilService service;

    @Autowired
    AtletaService atletaService;


        @GetMapping("/cadastro/atleta/{id}")
        public String getCadastroAtleta(@PathVariable("id") Long id, PerfilsDtos atletaDtos, Model model) {
            carregarDadosCadastro(id, model);
            model.addAttribute("atletaDtos", atletaDtos);
            return "publicPlages/cadastroAtleta";
        }
        

        @PostMapping("/cadastro/atleta/save/{id}")
        public String postAtletaInsert( @PathVariable("id") Long id, @ModelAttribute("atletaDtos") @Valid PerfilsDtos atletaDtos,
        BindingResult result,  Model model
        ) {

            if (result.hasErrors()) { 
                carregarDadosCadastro(id, model);
                return "publicPlages/cadastroAtleta"; 
            }
            if (atletaDtos.getRoles() == null) {
                atletaDtos.setRoles(new RoleDtos()); 
            }
            atletaDtos.getRoles().setId(id);
            service.InsertAtleta(atletaDtos);
            return "redirect:/home"; 
        }




        @GetMapping("/atleta/perfil")
        public String getPerfilAtleta(@AuthenticationPrincipal UserDetails userLogado, Model model) {
            var atleta = atletaService.buscarAtletaPorCpf(userLogado.getUsername());
            if (atleta == null) {
                return "redirect:/login?error";
            }
            model.addAttribute("perfil", atleta);
            return "atletaPages/PerfilAtleta";
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
