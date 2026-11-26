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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.Projeto_3.dtos.DtosModelos.RecomendacaoAlimentarDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.service.AlimentoService;
import com.api.Projeto_3.service.MedicoService;

import jakarta.validation.Valid;

@Controller

@RequestMapping("/medico/alimentacao")
public class AlimentoController {

    @Autowired 
    private  MedicoService medicoService;
    
    @Autowired 
    private  AlimentoService alimentoService;

    @GetMapping
    public String listarAtletas(@AuthenticationPrincipal UserDetails userLogado, Model model) {

        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

        var atletas = medicoService.atletasVinculados(medico.getId());
        if (!atletas.isEmpty()) {
            return "redirect:/medico/alimentacao/" + atletas.get(0).getId();
        }

        model.addAttribute("medico", medico);
        model.addAttribute("atletas", atletas);
        model.addAttribute("modoAlimentacao", true);
        return "medicoPages/listaAtletasVinculados";
    }

    @GetMapping("/{atletaId}")
    public String getAlimentacao(@AuthenticationPrincipal UserDetails userLogado,  @PathVariable Long atletaId,@RequestParam(defaultValue = "false") boolean salvo, Model model) {

        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

        AtletaModelo atleta = alimentoService.buscarAtletaVinculado(medico.getId(), atletaId);
        if (atleta == null) {
            return "redirect:/medico/atletas-vinculados?atletaIndisponivel";
        }

        model.addAttribute("recomendacao", new RecomendacaoAlimentarDto());
        return mostrarFormulario(medico.getId(), atleta, salvo, model);
    }

    @PostMapping("/{atletaId}")
    public String salvarAlimentacao(@AuthenticationPrincipal UserDetails userLogado,   @PathVariable Long atletaId,  @ModelAttribute("recomendacao") @Valid RecomendacaoAlimentarDto recomendacao, BindingResult result, Model model) {
        if (userLogado == null) {
            return "redirect:/login";
        }

        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());
        if (medico == null) {
            return "redirect:/login?error";
        }

        if (alimentoService.dataRevisaoInvalida(recomendacao)) {
            result.rejectValue("dataRevisao", "dataRevisao.invalida", "A revisão deve ser na data inicial ou depois.");
        }

        if (result.hasErrors()) {
            AtletaModelo atleta = alimentoService.buscarAtletaVinculado(medico.getId(), atletaId);
            if (atleta == null) {
                return "redirect:/medico/atletas-vinculados?atletaIndisponivel";
            }
            return mostrarFormulario(medico.getId(), atleta, false, model);
        }

        if (!alimentoService.recomendarAlimentacao(medico.getId(), atletaId, recomendacao)) {
            return "redirect:/medico/atletas-vinculados?atletaIndisponivel";
        }

        return "redirect:/medico/alimentacao/" + atletaId + "?salvo=true";
    }

    private String mostrarFormulario(Long medicoId, AtletaModelo atleta, boolean salvo, Model model) {
        model.addAttribute("atleta", atleta);
        model.addAttribute("atletas", medicoService.atletasVinculados(medicoId));
        model.addAttribute("recomendacoes", alimentoService.recomendacoes(medicoId, atleta.getId()));
        model.addAttribute("salvo", salvo);
        return "medicoPages/alimentacao";
    }
}
