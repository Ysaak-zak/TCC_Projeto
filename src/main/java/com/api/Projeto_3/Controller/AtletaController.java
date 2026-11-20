package com.api.Projeto_3.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.Projeto_3.dtos.DtosModelos.PerfilsDtos;
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


        @GetMapping({"/cadastro/atleta", "/cadastro/atleta/{id:\\d+}"})
        public String getCadastroAtleta(PerfilsDtos atletaDtos, Model model) {
            carregarDadosCadastro(model);
            model.addAttribute("atletaDtos", atletaDtos);
            return "publicPlages/cadastroAtleta";
        }
        

        @PostMapping({"/cadastro/atleta/save", "/cadastro/atleta/save/{id:\\d+}"})
        public String postAtletaInsert(@ModelAttribute("atletaDtos") @Valid PerfilsDtos atletaDtos,
        BindingResult result,  Model model
        ) {

            if (result.hasErrors()) { 
                carregarDadosCadastro(model);
                return "publicPlages/cadastroAtleta"; 
            }
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

        private void carregarDadosCadastro(Model model) {
            model.addAttribute("listaUfs", EnumUf.values());
            model.addAttribute("listSague", EnumSague.values());
            model.addAttribute("listEnumGenero", EnumGenero.values());
        }
        
  }
