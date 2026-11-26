package com.api.Projeto_3.Controller;

import com.api.Projeto_3.dtos.DtosModelos.CriarExercicioDto;
import com.api.Projeto_3.dtos.DtosModelos.CriarSemanaDto;
import com.api.Projeto_3.dtos.DtosModelos.CriarTreinoDto;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.SemanaResponitory;
import com.api.Projeto_3.service.TrenamentosSerive;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/treinador/treinos")
public class TreinamentoController {
    @Autowired 
    private  TrenamentosSerive service;
    
    @Autowired 
    private  AtletaRespository atletas;
    
    @Autowired 
    private  SemanaResponitory semanas;

    @GetMapping
    public String getTreino(@AuthenticationPrincipal UserDetails usuario, Model model) {
        model.addAttribute("treinoForm", new CriarTreinoDto());
        return mostrarTreinos(usuario, model);
    }

    @PostMapping
    public String criarTreino(@Valid @ModelAttribute("treinoForm") CriarTreinoDto form, BindingResult result, @AuthenticationPrincipal UserDetails usuario, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return mostrarTreinos(usuario, model);
        }

        service.criarTreino(form, usuario.getUsername());
        redirect.addFlashAttribute("sucesso", "Treino salvo para " + form.getDia().getRotulo() + ".");
        return "redirect:/treinador/treinos";
    }

    @GetMapping("/semanas")
    public String getSemanas(@AuthenticationPrincipal UserDetails usuario, Model model) {
        model.addAttribute("semanaForm", new CriarSemanaDto());
        return mostrarSemanas(usuario, model);
    }

    @PostMapping("/semanas")
    public String criarSemana(@Valid @ModelAttribute("semanaForm") CriarSemanaDto form, BindingResult result, @AuthenticationPrincipal UserDetails usuario, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return mostrarSemanas(usuario, model);
        }

        var atletaSelecionado = atletas.listarPorTreinador(usuario.getUsername()).stream()
                .filter(atleta -> atleta.getId().equals(form.getAtletaId()))
                .findFirst();


        SemanaModel semana = new SemanaModel();
        semana.setNome(form.getNome().trim());
        semana.setDescricao(form.getDescricao());
        semana.setAtleta_fk(atletaSelecionado.get());

        service.creSemana(semana);
        redirect.addFlashAttribute("sucesso", "Semana criada com sucesso!");
        return "redirect:/treinador/treinos";
    }

    @GetMapping("/exercicios")
    public String getExercicios() {
        return "redirect:/treinador/treinos#exercicios";
    }

    @PostMapping("/exercicios")
    public String criarExercicio(@Valid @ModelAttribute("exercicioForm") CriarExercicioDto form,
            BindingResult result, @AuthenticationPrincipal UserDetails usuario,
            RedirectAttributes redirect) {
        return "redirect:/treinador/treinos#exercicios";
    }

    private String mostrarTreinos(UserDetails usuario, Model model) {
        model.addAttribute("semanas", semanas.listarPorTreinador(usuario.getUsername()));
        model.addAttribute("diasSemana", DiasEnum.values());
        return "treinadorPages/treimamento/Treinamento";
    }

    private String mostrarSemanas(UserDetails usuario, Model model) {
        model.addAttribute("atletas", atletas.listarPorTreinador(usuario.getUsername()));
        model.addAttribute("semanas", semanas.listarPorTreinador(usuario.getUsername()));
        return "treinadorPages/treimamento/semana";
    }

   
}
