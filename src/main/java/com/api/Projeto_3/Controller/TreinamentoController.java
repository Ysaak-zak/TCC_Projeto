package com.api.Projeto_3.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import com.api.Projeto_3.dtos.model.CriarSemanaDto;
import com.api.Projeto_3.dtos.model.CriarExercicioDto;
import org.springframework.web.bind.annotation.RequestParam;
import com.api.Projeto_3.dtos.model.CriarTreinoDto;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.SemanaResponitory;
import com.api.Projeto_3.service.TrenamentosSerive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/treinador/treinos")
public class TreinamentoController {
    private final TrenamentosSerive service;
    private final AtletaRespository atletas;
    private final SemanaResponitory semanas;

    public TreinamentoController(TrenamentosSerive service, AtletaRespository atletas,
            SemanaResponitory semanas) {
        this.service = service;
        this.atletas = atletas;
        this.semanas = semanas;
    }


    @GetMapping
    public String getTreino(@AuthenticationPrincipal UserDetails usuario, Model model) {
        model.addAttribute("treinoForm", new CriarTreinoDto());
        carregarTreino(usuario, model);
        return "treinadorPages/treimamento/Treinamento";
    }

    private void carregarTreino(UserDetails usuario, Model model) {
        model.addAttribute("semanas", semanas.listarPorTreinador(usuario.getUsername()));
        model.addAttribute("diasSemana", DiasEnum.values());
    }

    @PostMapping
    public String criarTreino(@Valid @ModelAttribute("treinoForm") CriarTreinoDto form,
            BindingResult result, @AuthenticationPrincipal UserDetails usuario,
            Model model, RedirectAttributes redirect) {
        if (!result.hasErrors()) {
            try {
                service.criarTreino(form, usuario.getUsername());
                redirect.addFlashAttribute("sucesso", "Treino salvo para " + form.getDia().getRotulo() + ".");
                return "redirect:/treinador/treinos";
            } catch (IllegalArgumentException e) {
                result.rejectValue("semanaId", "semana.invalida", e.getMessage());
            }
        }
        carregarTreino(usuario, model);
        return "treinadorPages/treimamento/Treinamento";
    }

    @GetMapping("/semanas")
    public String getSemanas(@AuthenticationPrincipal UserDetails usuario, Model model) {
        model.addAttribute("semanaForm", new CriarSemanaDto());
        carregarSemanas(usuario, model);
        return "treinadorPages/treimamento/semana";
    }

    @PostMapping("/semanas")
    public String criarSemana(@Valid @ModelAttribute("semanaForm") CriarSemanaDto form,
            BindingResult result, @AuthenticationPrincipal UserDetails usuario,
            Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            carregarSemanas(usuario, model);
            return "treinadorPages/treimamento/semana";
        }

        for (var atleta : atletas.listarPorTreinador(usuario.getUsername())) {
            if (atleta.getId().equals(form.getAtletaId())) {
                SemanaModel semana = new SemanaModel();
                semana.setNome(form.getNome().trim());
                semana.setDescricao(form.getDescricao());
                semana.setAtleta_fk(atleta);

                service.creSemana(semana);

                redirect.addFlashAttribute("sucesso", "Semana criada com sucesso!");
                return "redirect:/treinador/treinos";
            }
        }

        result.rejectValue("atletaId", "atleta.invalido", "Selecione um atleta vinculado a você.");
        carregarSemanas(usuario, model);
        return "treinadorPages/treimamento/semana";
    }

    private void carregarSemanas(UserDetails usuario, Model model) {
        model.addAttribute("atletas", atletas.listarPorTreinador(usuario.getUsername()));
        model.addAttribute("semanas", semanas.listarPorTreinador(usuario.getUsername()));
    }

    @GetMapping("/exercicios")
    public String getExercicios(@RequestParam(required = false) Long semanaId,
            @AuthenticationPrincipal UserDetails usuario, Model model) {
        CriarExercicioDto form = new CriarExercicioDto();
        form.setSemanaId(semanaId);
        model.addAttribute("exercicioForm", form);
        carregarExercicios(form, usuario, model);
        return "treinadorPages/treimamento/Exercicio";
    }

    private void carregarExercicios(CriarExercicioDto form, UserDetails usuario, Model model) {
        model.addAttribute("semanas", semanas.listarPorTreinador(usuario.getUsername()));
        try {
            model.addAttribute("treinamentos", service.listarTreinosDaSemana(form.getSemanaId(), usuario.getUsername()));
        } catch (IllegalArgumentException e) {
            model.addAttribute("treinamentos", java.util.List.of());
            model.addAttribute("erroSelecao", e.getMessage());
        }
    }

    @PostMapping("/exercicios")
    public String criarExercicio(@Valid @ModelAttribute("exercicioForm") CriarExercicioDto form,
            BindingResult result, @AuthenticationPrincipal UserDetails usuario,
            Model model, RedirectAttributes redirect) {
        if (!result.hasErrors()) {
            try {
                service.criarExercicio(form, usuario.getUsername());
                redirect.addFlashAttribute("sucesso", "Exercício adicionado ao treinamento com sucesso!");
                redirect.addAttribute("semanaId", form.getSemanaId());
                return "redirect:/treinador/treinos/exercicios";
            } catch (IllegalArgumentException e) {
                result.rejectValue("treinamentoId", "treinamento.invalido", e.getMessage());
            }
        }
        carregarExercicios(form, usuario, model);
        return "treinadorPages/treimamento/Exercicio";
    }
}
