package com.api.Projeto_3.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.api.Projeto_3.service.DashService;

@Controller
public class DashController {
    private final DashService dashService;

    public DashController(DashService dashService) {
        this.dashService = dashService;
    }

    @GetMapping("/atleta")
    public String mostrarDashboard(@AuthenticationPrincipal UserDetails usuario,
            @RequestParam(name = "semanaId", required = false) Long semanaId, Model model) {
        model.addAttribute("dash", dashService.buscarDashboard(usuario.getUsername(), semanaId));
        return "atletaPages/dashAtleta";
    }
}
