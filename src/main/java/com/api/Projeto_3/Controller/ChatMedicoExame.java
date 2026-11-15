package com.api.Projeto_3.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.api.Projeto_3.dtos.DtosModelos.ExameUrinaDtos;
import com.api.Projeto_3.service.UrinaExamesService;
import com.api.Projeto_3.service.SangueTest;
import com.api.Projeto_3.dtos.DtosModelos.ExameSagueDtos;

@Controller
@RequestMapping("/medico")
public class ChatMedicoExame {
    @Autowired
    private UrinaExamesService examesService;

    @Autowired
    private SangueTest sangueService;

    @ControllerAdvice(assignableTypes = MedicoController.class)
    public static class ExameUrinaModelAdvice {
        @ModelAttribute("exameUrina")
        public ExameUrinaDtos exameUrina() {
            return new ExameUrinaDtos();
        }
    }

    @PostMapping({"/exame/urina", "/exame-urina/salvar"})
    public String salvarExameUrina(@ModelAttribute("exameUrina") ExameUrinaDtos exame,
            RedirectAttributes redirectAttributes) {
        exame.setAnabolizantes(triagemOuNaoRealizado(exame.getAnabolizantes()));
        exame.setEstimulantes(triagemOuNaoRealizado(exame.getEstimulantes()));
        exame.setDiureticos(triagemOuNaoRealizado(exame.getDiureticos()));
        ExameUrinaDtos resultado = examesService.realizarExameParaPagina(exame);
        redirectAttributes.addFlashAttribute("exameUrina", resultado);
        return "redirect:/medico/exame-urina/resultado";
    }

    @GetMapping("/exame-urina/resultado")
    public String resultadoExameUrina(org.springframework.ui.Model model) {
        if (!model.containsAttribute("exameUrina")) return "redirect:/medico/exame-urina";
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
    public String resultadoExameSangue(org.springframework.ui.Model model) {
        if (!model.containsAttribute("exameSangue")) return "redirect:/medico/exame-sangue";
        return "medicoPages/resultadoSangue";
    }

    private String triagemOuNaoRealizado(String valor) {
        return valor == null || valor.isBlank() ? "NAO_REALIZADO" : valor.trim();
    }
}
