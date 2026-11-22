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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/treinador/treinos")
public class TreinamentoController {
    private static final String PAGINA_TREINOS = "treinadorPages/treimamento/Treinamento";
    private static final String PAGINA_SEMANAS = "treinadorPages/treimamento/semana";
    private static final String REDIRECIONAR_TREINOS = "redirect:/treinador/treinos";
    private static final String REDIRECIONAR_EXERCICIOS = REDIRECIONAR_TREINOS + "#exercicios";

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
        carregarDadosDosTreinos(usuario, model);
        return PAGINA_TREINOS;
    }

    @PostMapping
    public String criarTreino(@Valid @ModelAttribute("treinoForm") CriarTreinoDto form,
            BindingResult result, @AuthenticationPrincipal UserDetails usuario,
            Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            carregarDadosDosTreinos(usuario, model);
            return PAGINA_TREINOS;
        }

        service.criarTreino(form, usuario.getUsername());
        redirect.addFlashAttribute("sucesso", "Treino salvo para " + form.getDia().getRotulo() + ".");
        return REDIRECIONAR_TREINOS;
    }

    @GetMapping("/semanas")
    public String getSemanas(@AuthenticationPrincipal UserDetails usuario, Model model) {
        model.addAttribute("semanaForm", new CriarSemanaDto());
        carregarDadosDasSemanas(usuario, model);
        return PAGINA_SEMANAS;
    }

    @PostMapping("/semanas")
    public String criarSemana(@Valid @ModelAttribute("semanaForm") CriarSemanaDto form,
            BindingResult result, @AuthenticationPrincipal UserDetails usuario,
            Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            carregarDadosDasSemanas(usuario, model);
            return PAGINA_SEMANAS;
        }

        var atletaSelecionado = atletas.listarPorTreinador(usuario.getUsername()).stream()
                .filter(atleta -> atleta.getId().equals(form.getAtletaId()))
                .findFirst();

        if (atletaSelecionado.isEmpty()) {
            result.rejectValue("atletaId", "atleta.invalido", "Selecione um atleta vinculado a você.");
            carregarDadosDasSemanas(usuario, model);
            return PAGINA_SEMANAS;
        }

        SemanaModel semana = new SemanaModel();
        semana.setNome(form.getNome().trim());
        semana.setDescricao(form.getDescricao());
        semana.setAtleta_fk(atletaSelecionado.get());

        service.creSemana(semana);
        redirect.addFlashAttribute("sucesso", "Semana criada com sucesso!");
        return REDIRECIONAR_TREINOS;
    }

    @GetMapping("/exercicios")
    public String getExercicios(@RequestParam(required = false) Long semanaId,
            @AuthenticationPrincipal UserDetails usuario, Model model) {
        return REDIRECIONAR_EXERCICIOS;
    }

    @PostMapping("/exercicios")
    public String criarExercicio(@Valid @ModelAttribute("exercicioForm") CriarExercicioDto form,
            BindingResult result, @AuthenticationPrincipal UserDetails usuario,
            Model model, RedirectAttributes redirect) {
        if (!result.hasErrors()) {
            service.criarExercicio(form, usuario.getUsername());
            redirect.addFlashAttribute("sucesso", "Exercício adicionado ao treinamento com sucesso!");
            redirect.addAttribute("semanaId", form.getSemanaId());
        }
        return REDIRECIONAR_EXERCICIOS;
    }

    private void carregarDadosDosTreinos(UserDetails usuario, Model model) {
        model.addAttribute("semanas", semanas.listarPorTreinador(usuario.getUsername()));
        model.addAttribute("diasSemana", DiasEnum.values());
    }

    private void carregarDadosDasSemanas(UserDetails usuario, Model model) {
        model.addAttribute("atletas", atletas.listarPorTreinador(usuario.getUsername()));
        model.addAttribute("semanas", semanas.listarPorTreinador(usuario.getUsername()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarSelecaoInvalida(IllegalArgumentException erro) {
        return ResponseEntity.badRequest().body(erro.getMessage());
    }
}
