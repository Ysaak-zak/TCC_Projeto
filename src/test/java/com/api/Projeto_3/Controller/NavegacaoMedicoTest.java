package com.api.Projeto_3.Controller;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import com.api.Projeto_3.dtos.DtosModelos.ExameUrinaDtos;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.service.MedicoService;

class NavegacaoMedicoTest {
    @Test
    void renderizaMenusDosExamesComLinksEPaginaAtual() throws Exception {
        var servlet = new MockServletContext();
        try (var context = new StaticWebApplicationContext()) {
            context.setServletContext(servlet);
            context.refresh();
            servlet.setAttribute(org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
            var templates = new ClassLoaderTemplateResolver();
            templates.setPrefix("templates/");
            templates.setSuffix(".html");
            templates.setCharacterEncoding("UTF-8");
            var engine = new SpringTemplateEngine();
            engine.setTemplateResolver(templates);
            var resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(engine);
            resolver.setApplicationContext(context);
            resolver.setCharacterEncoding("UTF-8");
            var atleta = new AtletaModelo();
            atleta.setId(1L);
            atleta.setName("Ana");
            var model = new HashMap<String, Object>();
            model.put("atletas", List.of(atleta));
            model.put("exameUrina", new ExameUrinaDtos());
            for (String pagina : List.of("TestUrina", "TesSangue", "result")) {
                var response = new MockHttpServletResponse();
                resolver.resolveViewName("medicoPages/" + pagina, Locale.forLanguageTag("pt-BR"))
                        .render(model, new MockHttpServletRequest(servlet), response);
                String html = response.getContentAsString();
                for (String rota : List.of("/medico", "/medico/atletas-vinculados", "/medico/alimentacao",
                        "/medico/exame-urina", "/medico/exame-sangue", "/medico/perfil")) {
                    assertTrue(html.contains("href=\"" + rota + "\""), pagina + ": " + rota);
                }
                assertEquals(1, html.split("aria-current=\"page\"", -1).length - 1, pagina);
                String rotaAtual = pagina.equals("TesSangue") ? "/medico/exame-sangue" : "/medico/exame-urina";
                assertTrue(java.util.regex.Pattern.compile("href=\"" + rotaAtual + "\"[^>]*aria-current=\"page\"", java.util.regex.Pattern.DOTALL)
                        .matcher(html).find(), pagina);
                if (pagina.equals("TesSangue")) assertTrue(html.contains("Ana"));
            }
        }
    }

    @Test
    void abreExameDeSangueComAtletasDoMedicoEExigeAutenticacao() throws Exception {
        var controller = new MedicoController();
        assertEquals("redirect:/login", controller.getExameSangue(null, new ExtendedModelMap()));
        var medico = new MedicoModelo();
        medico.setId(7L);
        var atleta = new AtletaModelo();
        controller.medicoService = new MedicoService() {
            @Override public MedicoModelo buscarMedicoPorId(String cpf) { return medico; }
            @Override public List<AtletaModelo> atletasVinculados(Long id) {
                assertEquals(7L, id);
                return List.of(atleta);
            }
        };
        var usuario = User.withUsername("cpf").password("senha").roles("MEDICO").build();
        var model = new ExtendedModelMap();
        assertEquals("medicoPages/TesSangue", controller.getExameSangue(usuario, model));
        assertEquals(List.of(atleta), model.get("atletas"));
        var metodo = MedicoController.class.getMethod("getExameSangue",
                org.springframework.security.core.userdetails.UserDetails.class, org.springframework.ui.Model.class);
        assertArrayEquals(new String[] {"/medico/exame-sangue"}, metodo.getAnnotation(GetMapping.class).value());
        controller.medicoService = new MedicoService() {
            @Override public MedicoModelo buscarMedicoPorId(String cpf) { return null; }
        };
        assertEquals("redirect:/login?error", controller.getExameSangue(usuario, new ExtendedModelMap()));
    }
}
