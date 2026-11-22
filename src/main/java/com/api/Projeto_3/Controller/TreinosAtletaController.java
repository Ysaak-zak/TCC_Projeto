package com.api.Projeto_3.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.Projeto_3.dtos.DtosModelos.TreinoAtletaProgressoDto;
import com.api.Projeto_3.service.TreinosAtletaService;

@Controller
@RequestMapping("/atleta/treinos")
public class TreinosAtletaController {
    private final TreinosAtletaService service;

    public TreinosAtletaController(TreinosAtletaService service) {
        this.service = service;
    }

    @GetMapping
    public String mostrar(@AuthenticationPrincipal UserDetails usuario,
                          @RequestParam(required = false) Long semanaId, Model model) {
        model.addAttribute("agenda", service.buscarAgenda(usuario.getUsername(), semanaId));
        return "atletaPages/treinos";
    }

    @PostMapping("/{treinoId}")
    public String registrar(@AuthenticationPrincipal UserDetails usuario, @PathVariable Long treinoId,
                            @ModelAttribute TreinoAtletaProgressoDto formulario, RedirectAttributes redirect) {
        var resultado = service.registrarProgresso(usuario.getUsername(), treinoId, formulario);
        redirect.addAttribute("semanaId", resultado.getSemanaId());
        redirect.addFlashAttribute("treinoComRetorno", treinoId);
        redirect.addFlashAttribute("resultado", resultado);
        return "redirect:/atleta/treinos#treino-" + treinoId;
    }
}
