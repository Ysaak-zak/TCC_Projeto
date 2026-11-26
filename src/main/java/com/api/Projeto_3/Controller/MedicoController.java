package com.api.Projeto_3.Controller;

import java.util.List;

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

import com.api.Projeto_3.dtos.DtosModelos.PerfilsDtos;
import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.model.enums.EnumGenero;
import com.api.Projeto_3.model.enums.EnumSague;
import com.api.Projeto_3.model.enums.EnumUf;
import com.api.Projeto_3.service.AtletaService;
import com.api.Projeto_3.service.MedicoService;
import com.api.Projeto_3.service.PerfilService;

import jakarta.validation.Valid;

@Controller
public class MedicoController {

    @Autowired
    PerfilService service;

    @Autowired
    MedicoService medicoService;

    @Autowired
    AtletaService atletaService;

    @GetMapping({"/cadastro/medico", "/cadastro/medico/{id:\\d+}"})
    public String getCadastroMedico(PerfilsDtos medicosDtos, Model model) {
        carregarDadosCadastro(model);
        model.addAttribute("medicosDtos", medicosDtos);
        return "publicPlages/cadastroMedico";
    }

    @PostMapping({"/cadastro/medico/save", "/cadastro/medico/save/{id:\\d+}"})
    public String postMedicoInsert(@ModelAttribute("medicosDtos") @Valid PerfilsDtos medicosDtos, BindingResult result, Model model) {

        if (result.hasErrors()) {
            carregarDadosCadastro(model);
            return "publicPlages/cadastroMedico";
        }
        service.insertMeidco(medicosDtos);

        return "redirect:/home";
    }

    @GetMapping("/medico")
    public String getMedico(@AuthenticationPrincipal UserDetails userLogado, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

        List<ListDasAtleta> listaAtletas = medicoService.listTab();

        model.addAttribute("medico", medico);
        model.addAttribute("listaAtletas", listaAtletas);
        model.addAttribute("atleta_sem_Vinculacao", atletaService.quntAtletaSemVinculadosMedico());

        return "medicoPages/vincularAtleta";
    }

    @GetMapping("/medico/perfil")
    public String getPerfilMedico(@AuthenticationPrincipal UserDetails userLogado, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());
        model.addAttribute("perfil", medico);
        return "medicoPages/PerfilMedico";
    }

    @GetMapping("/medico/atletas-vinculados")
    public String getMedicoAtletasVinculados(@AuthenticationPrincipal UserDetails userLogado, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());
        model.addAttribute("medico", medico);
        model.addAttribute("nomeMedico", medicoService.buscarNomes(userLogado.getUsername()));
        model.addAttribute("lisNome", medicoService.mostrandoAtletaNomes(medico.getId()));
        model.addAttribute("atletas", medicoService.atletasVinculados(medico.getId()));

        return "medicoPages/listaAtletasVinculados";
    }

    @GetMapping("/medico/exame-urina")
    public String getExameUrina(@AuthenticationPrincipal UserDetails userLogado, Model model) {

        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());
        model.addAttribute("medico", medico);
        model.addAttribute("atletas", medicoService.atletasVinculados(medico.getId()));
        return "medicoPages/TestUrina";
    }

    @GetMapping("/medico/exame-sangue")
    public String getExameSangue(@AuthenticationPrincipal UserDetails userLogado, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());
        model.addAttribute("medico", medico);
        model.addAttribute("atletas", medicoService.atletasVinculados(medico.getId()));
        return "medicoPages/TesSangue";
    }

    @PostMapping("/medico/{medicoId}/vincular-atleta/{atletaId}")
    public String vincularAtleta(@PathVariable("medicoId") Long medicoId,  @PathVariable("atletaId") Long atletaId) {
        atletaService.insertFkAMedico(atletaId, medicoId);
        return "redirect:/medico";
    }

    @PostMapping("/medico/atletas/{atletaId}/desvincular")
    public String desvincularAtleta(@AuthenticationPrincipal UserDetails userLogado, @PathVariable Long atletaId) {
        

        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

       medicoService.desvincularAtleta(medico.getId(), atletaId);

        return "redirect:/medico/atletas-vinculados?desvinculado";
    }

    @GetMapping("/medico/atletas/{atletaId}/treinamentos")
    public String getTreinamentosDoAtleta(@AuthenticationPrincipal UserDetails userLogado,  @PathVariable Long atletaId, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());
        AtletaModelo atleta = medicoService.buscarAtletaVinculado(medico.getId(), atletaId);
        
        model.addAttribute("medico", medico);
        model.addAttribute("atleta", atleta);
        model.addAttribute("treinamentos", medicoService.treinamentosDoAtleta(medico.getId(), atletaId));

        return "medicoPages/treinamentosAtleta";
    }

    @PostMapping("/medico/atletas/{atletaId}/treinamentos/{treinamentoId}/cancelar")
    public String cancelarTreinamento(@AuthenticationPrincipal UserDetails userLogado,
                                      @PathVariable Long atletaId, @PathVariable Long treinamentoId) {

        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

        String tela = "redirect:/medico/atletas/" + atletaId + "/treinamentos";
        if (medicoService.cancelarTreinamento(medico.getId(), atletaId, treinamentoId)) {
            return tela + "?cancelado";
        }
        return tela + "?erroCancelamento";
    }

    private void carregarDadosCadastro(Model model) {
        model.addAttribute("listaUfs", EnumUf.values());
        model.addAttribute("listSague", EnumSague.values());
        model.addAttribute("listEnumGenero", EnumGenero.values());
    }
}
