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
import org.springframework.web.bind.annotation.RequestParam;

import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.dtos.model.PerfilsDtos;
import com.api.Projeto_3.dtos.model.RecomendacaoAlimentarDto;
import com.api.Projeto_3.dtos.model.RoleDtos;
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

    @GetMapping("/cadastro/medico/{id}")
    public String getCadastroMedico(@PathVariable("id") Long id, PerfilsDtos medicosDtos, Model model) {
        carregarDadosCadastro(id, model);
        model.addAttribute("medicosDtos", medicosDtos);
        return "publicPlages/cadastroMedico";
    }

    @PostMapping("/cadastro/medico/save/{id}")
    public String postMedicoInsert(@PathVariable("id") Long id, @ModelAttribute("medicosDtos") @Valid PerfilsDtos medicosDtos, BindingResult result, Model model) {

        if (result.hasErrors()) {
            carregarDadosCadastro(id, model);
            return "publicPlages/cadastroMedico";
        }
        if (medicosDtos.getRoles() == null) {
            medicosDtos.setRoles(new RoleDtos());
        }
        medicosDtos.getRoles().setId(id);
        service.insertMeidco(medicosDtos);

        return "redirect:/login";
    }

    @GetMapping("/medico")
    public String getMedico(@AuthenticationPrincipal UserDetails userLogado, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

        if (medico == null) {
            return "redirect:/login?error";
        }

        List<ListDasAtleta> listaAtletas = medicoService.listTab();

        model.addAttribute("medico", medico);
        model.addAttribute("listaAtletas", listaAtletas);
        model.addAttribute("atleta_sem_Vinculacao", atletaService.quntAtletaSemVinculadosMedico());

        return "medicoPages/vincularAtleta";
    }

    @GetMapping("/medico/perfil")
    public String getPerfilMedico(@AuthenticationPrincipal UserDetails userLogado, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

        if (medico == null) {
            return "redirect:/login?error";
        }

        model.addAttribute("perfil", medico);
        return "medicoPages/PerfilMedico";
    }

    @GetMapping("/medico/atletas-vinculados")
    public String getMedicoAtletasVinculados(@AuthenticationPrincipal UserDetails userLogado, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

        if (medico == null) {
            return "redirect:/login?error";
        }

        model.addAttribute("medico", medico);
        model.addAttribute("nomeMedico", medicoService.buscarNomes(userLogado.getUsername()));
        model.addAttribute("lisNome", medicoService.mostrandoAtletaNomes(medico.getId()));
        model.addAttribute("atletas", medicoService.atletasVinculados(medico.getId()));

        return "medicoPages/listaAtletasVinculados";
    }

    @GetMapping("/medico/alimentacao/{atletaId}")
    public String getAlimentacao(@AuthenticationPrincipal UserDetails userLogado,
                                 @PathVariable Long atletaId,
                                 @RequestParam(defaultValue = "false") boolean salvo, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());

        if (medico == null) return "redirect:/login?error";
        var atleta = medicoService.atletasVinculados(medico.getId()).stream()
                .filter(item -> atletaId.equals(item.getId())).findFirst().orElse(null);
        if (atleta == null) return "redirect:/medico/atletas-vinculados?atletaIndisponivel";
        model.addAttribute("atleta", atleta);
        model.addAttribute("recomendacao", new RecomendacaoAlimentarDto());
        model.addAttribute("recomendacoes", medicoService.recomendacoes(medico.getId(), atletaId));
        model.addAttribute("salvo", salvo);
        return "medicoPages/alimentacao";
    }

    @PostMapping("/medico/alimentacao/{atletaId}")
    public String salvarAlimentacao(@AuthenticationPrincipal UserDetails userLogado,
                                    @PathVariable Long atletaId,
                                    @ModelAttribute("recomendacao") @Valid RecomendacaoAlimentarDto recomendacao,
                                    BindingResult result, Model model) {
        MedicoModelo medico = medicoService.buscarMedicoPorId(userLogado.getUsername());
        if (medico == null) return "redirect:/login?error";
        if (recomendacao.getDataInicio() != null && recomendacao.getDataRevisao() != null
                && recomendacao.getDataRevisao().isBefore(recomendacao.getDataInicio())) {
            result.rejectValue("dataRevisao", "dataRevisao.invalida", "A revisão deve ser na data inicial ou depois.");
        }
        var atleta = medicoService.atletasVinculados(medico.getId()).stream()
                .filter(item -> atletaId.equals(item.getId())).findFirst().orElse(null);
        if (atleta == null) return "redirect:/medico/atletas-vinculados?atletaIndisponivel";
        if (result.hasErrors()) {
            model.addAttribute("atleta", atleta);
            model.addAttribute("recomendacoes", medicoService.recomendacoes(medico.getId(), atletaId));
            model.addAttribute("salvo", false);
            return "medicoPages/alimentacao";
        }
        if (!medicoService.recomendarAlimentacao(medico.getId(), atletaId, recomendacao)) {
            return "redirect:/medico/atletas-vinculados?atletaIndisponivel";
        }
        return "redirect:/medico/alimentacao/" + atletaId + "?salvo=true";
    }

    @PostMapping("/medico/{medicoId}/vincular-atleta/{atletaId}")
    public String vincularAtleta(@PathVariable("medicoId") Long medicoId,
                                @PathVariable("atletaId") Long atletaId) {
        atletaService.insertFkAMedico(atletaId, medicoId);
        return "redirect:/medico";
    }

    private void carregarDadosCadastro(Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("listaUfs", EnumUf.values());
        model.addAttribute("listSague", EnumSague.values());
        model.addAttribute("listEnumGenero", EnumGenero.values());
        model.addAttribute("roleId", service.infoRoles(id));
        model.addAttribute("roleNome", service.buscarNomeRoles(id));
    }
}
