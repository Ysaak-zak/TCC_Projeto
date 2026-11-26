package com.api.Projeto_3.Controller;

import com.api.Projeto_3.dtos.DtosModelos.ExameSagueDtos;
import com.api.Projeto_3.dtos.DtosModelos.ExameUrinaDtos;
import com.api.Projeto_3.service.SangueTest;
import com.api.Projeto_3.service.UrinaExamesService;
import org.springframework.stereotype.Controller;
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
    public String salvarExameUrina(@ModelAttribute("exameUrina") ExameUrinaDtos exame, RedirectAttributes redirectAttributes) {
        ExameUrinaDtos resultado = urinaService.realizarExameParaPagina(exame);
        redirectAttributes.addFlashAttribute("exameUrina", resultado);
        return "redirect:/medico/exame-urina/resultado";
    }

    @GetMapping("/exame-urina/resultado")
    public String resultadoExameUrina() {
        return "medicoPages/result";
    }

    @PostMapping("/exame-sangue/salvar")
    public String salvarExameSangue(@ModelAttribute("exameSangue") ExameSagueDtos exame, RedirectAttributes redirectAttributes) {
        ExameSagueDtos resultado = sangueService.realizarExameParaPagina(exame);
        redirectAttributes.addFlashAttribute("exameSangue", resultado);
        return "redirect:/medico/exame-sangue/resultado";
    }

    @GetMapping("/exame-sangue/resultado")
    public String resultadoExameSangue() {
        return "medicoPages/resultadoSangue";
    }

    @ControllerAdvice(assignableTypes = MedicoController.class)
    public static class ExameUrinaModelAdvice {
        @ModelAttribute("exameUrina")
        public ExameUrinaDtos exameUrina() {
            return new ExameUrinaDtos();
        }
    }
}
