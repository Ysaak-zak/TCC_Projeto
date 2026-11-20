package com.api.Projeto_3.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.Projeto_3.dtos.DtosModelos.PerfilsDtos;
import com.api.Projeto_3.dtos.interfaceCustomer.CustomerNome;
import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.dtos.interfaceCustomer.TreinadorCustomer;
import com.api.Projeto_3.model.enums.EnumGenero;
import com.api.Projeto_3.model.enums.EnumSague;
import com.api.Projeto_3.model.enums.EnumUf;
import com.api.Projeto_3.service.AtletaService;
import com.api.Projeto_3.service.PerfilService;
import com.api.Projeto_3.service.TreinadorService;

import jakarta.validation.Valid;

@Controller
public class TreinadorController {

    @Autowired
    PerfilService service;

    @Autowired
    TreinadorService serviceTre;

    @Autowired
    AtletaService atl_servic;

    private String id_username = null;

    @GetMapping({"/cadastro/treinador", "/cadastro/treinador/{id:\\d+}"})
    public String getCadastroTreinador(PerfilsDtos treinadorDtos, Model model) {
        carregarDadosCadastro(model);
        model.addAttribute("treinadorDtos", treinadorDtos);
        return "publicPlages/cadastroTreinador";
    }

    @PostMapping({"/cadastro/treinador/save", "/cadastro/treinador/save/{id:\\d+}"})
    public String postTreinadorInsert(@ModelAttribute("treinadorDtos") @Valid PerfilsDtos treinadorDtos, BindingResult result, Model model) {

        if (result.hasErrors()) {
            carregarDadosCadastro(model);
            return "publicPlages/cadastroTreinador";
        }


        service.insertTreinador(treinadorDtos);
        return "redirect:/login";
    }

    @GetMapping("/treinador")
    public String getTreinador(@AuthenticationPrincipal UserDetails userLogado, Model model) {

        TreinadorCustomer treinador = serviceTre.buscarTreinadorPorId(userLogado.getUsername());
        List<ListDasAtleta> listDas = serviceTre.listTab();

        this.id_username = userLogado.getUsername();

        model.addAttribute("treinador", treinador);
        model.addAttribute("listaAtletas", listDas);
        model.addAttribute("treinador_id_Vincule", treinador.getId());

        final int v = atl_servic.quntAtletaVinculadosTreinador(treinador.getId());
        model.addAttribute("vinculados_count", v);

        final int vSem = atl_servic.quntAtletaSemVinculadosTreinador();
        model.addAttribute("atleta_sem_Vinculacao", vSem);

        return "treinadorPages/dash.html";
    }

    @GetMapping("/treinador/perfil")
    public String getTreinadorPerfil(@AuthenticationPrincipal UserDetails userLogado, Model model) {

        TreinadorCustomer treinador = serviceTre.buscarTreinadorPorId(userLogado.getUsername());
        CustomerNome treinadorNome = serviceTre.buscarNomes(userLogado.getUsername());

        model.addAttribute("treinador", treinador);
        model.addAttribute("nomeTreinador", treinadorNome);

        return "treinadorPages/Perfil.html";
    }

    @GetMapping("/treinador/atletas-vinculados")
    public String getTreinadorAtletasVinculados(@AuthenticationPrincipal UserDetails userLogado, Model model) {

        TreinadorCustomer treinador = serviceTre.buscarTreinadorPorId(userLogado.getUsername());
        List<ListDasAtleta> atletasVinculados = serviceTre.listarAtletasVinculados(treinador.getId());

        model.addAttribute("treinador", treinador);
        model.addAttribute("atletasVinculados", atletasVinculados);
        model.addAttribute("vinculados_count", atletasVinculados.size());

        return "treinadorPages/listaAtletasVinculados";
    }

    @PostMapping("/treinador/{treinadorId}/vincular-atleta/{atletaId}")
    public String vincularAtleta(@PathVariable("treinadorId") Long treinadorId,
            @PathVariable("atletaId") Long atletaId) {
        atl_servic.insertFkATreinador(atletaId, treinadorId);
        return "redirect:/treinador";
    }
    @PostMapping("/treinador/atletas/{atletaId}/desvincular")
    public String desvincularAtleta(@AuthenticationPrincipal UserDetails userLogado,
                                   @PathVariable("atletaId") Long atletaId) {
        if (userLogado == null) {
            return "redirect:/login";
        }

        TreinadorCustomer treinador = serviceTre.buscarTreinadorPorId(userLogado.getUsername());
        if (!serviceTre.desvincularAtleta(treinador.getId(), atletaId)) {
            return "redirect:/treinador/atletas-vinculados?atletaIndisponivel";
        }

        return "redirect:/treinador/atletas-vinculados?desvinculado";
    }

    private void carregarDadosCadastro(Model model) {
        model.addAttribute("listaUfs", EnumUf.values());
        model.addAttribute("listSague", EnumSague.values());
        model.addAttribute("listEnumGenero", EnumGenero.values());
    }

}
