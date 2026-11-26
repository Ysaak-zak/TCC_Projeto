package com.api.Projeto_3.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.repository.AlimentosAtletaRepository;
import com.api.Projeto_3.service.AtletaService;



@Controller
@RequestMapping("/atleta/alimentos")
public class AlimentosAtletaController {

    @Autowired 
    private  AtletaService atletaService;
   
    @Autowired
    private  AlimentosAtletaRepository alimentosRepository;

    @GetMapping
    public String mostrar(@AuthenticationPrincipal UserDetails usuario, Model model) {
        AtletaModelo atleta = buscarAtleta(usuario);
        model.addAttribute("nomeAtleta", atleta.getName());
        model.addAttribute("alimentos", alimentosRepository.listarPorAtleta(atleta.getId()));
        return "atletaPages/alimentos";
    }

    @PostMapping("/{id}/concluir")
    public String concluir(@AuthenticationPrincipal UserDetails usuario, @PathVariable Long id) {
        AtletaModelo atleta = buscarAtleta(usuario);
        if (alimentosRepository.marcarConcluido(id, atleta.getId()) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recomendação alimentar não encontrada");
        }
        return "redirect:/atleta/alimentos";
    }

    private AtletaModelo buscarAtleta(UserDetails usuario) {
        AtletaModelo atleta = atletaService.buscarAtletaPorCpf(usuario.getUsername());
        if (atleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atleta não encontrado");
        }
        return atleta;
    }
}
