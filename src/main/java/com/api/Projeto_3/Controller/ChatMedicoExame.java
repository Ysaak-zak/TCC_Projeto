package com.api.Projeto_3.Controller;

import com.api.Projeto_3.dtos.DtosModelos.ExameSagueDtos;
import com.api.Projeto_3.dtos.DtosModelos.ExameUrinaDtos;
import com.api.Projeto_3.service.SangueTest;
import com.api.Projeto_3.service.UrinaExamesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medico")
public class ChatMedicoExame {
    private final UrinaExamesService urinaService;
    private final SangueTest sangueService;

    public ChatMedicoExame(UrinaExamesService urinaService, SangueTest sangueService) {
        this.urinaService = urinaService;
        this.sangueService = sangueService;
    }

    @PostMapping({"/exame/urina", "/exame-urina/salvar"})
    public String salvarExameUrina(@ModelAttribute("exameUrina") ExameUrinaDtos exame,
            RedirectAttributes redirectAttributes) {
        exame.setAnabolizantes(normalizarTriagem(exame.getAnabolizantes()));
        exame.setEstimulantes(normalizarTriagem(exame.getEstimulantes()));
        exame.setDiureticos(normalizarTriagem(exame.getDiureticos()));
        ExameUrinaDtos resultado = urinaService.realizarExameParaPagina(exame);
        redirectAttributes.addFlashAttribute("exameUrina", resultado);
        return "redirect:/medico/exame-urina/resultado";
    }

    @GetMapping("/exame-urina/resultado")
    public String resultadoExameUrina(Model model) {
        if (!model.containsAttribute("exameUrina")) {
            return "redirect:/medico/exame-urina";
        }
        return "medicoPages/result";
    }

    @PostMapping("/exame-sangue/salvar")
    public String salvarExameSangue(@ModelAttribute("exameSangue") ExameSagueDtos exame,
            RedirectAttributes redirectAttributes) {
        ExameSagueDtos resultado = sangueService.realizarExameParaPagina(exame);
        redirectAttributes.addFlashAttribute("exameSangue", resultado);
        return "redirect:/medico/exame-sangue/resultado";
    }

    @GetMapping("/exame-sangue/resultado")
    public String resultadoExameSangue(Model model) {
        if (!model.containsAttribute("exameSangue")) {
            return "redirect:/medico/exame-sangue";
        }
        return "medicoPages/resultadoSangue";
    }

    private String normalizarTriagem(String valor) {
        return valor == null || valor.isBlank() ? "NAO_REALIZADO" : valor.trim();
    }

    @ControllerAdvice(assignableTypes = MedicoController.class)
    public static class ExameUrinaModelAdvice {
        @ModelAttribute("exameUrina")
        public ExameUrinaDtos exameUrina() {
            return new ExameUrinaDtos();
        }
    }
}
