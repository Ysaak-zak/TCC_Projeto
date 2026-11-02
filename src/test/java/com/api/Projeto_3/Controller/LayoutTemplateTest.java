package com.api.Projeto_3.Controller;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import com.api.Projeto_3.model.*;

class LayoutTemplateTest {
    @Test
    void renderizaLoginEPaineisComOsFragmentos() throws Exception {
        var servlet = new MockServletContext();
        try (var context = new StaticWebApplicationContext()) {
            context.setServletContext(servlet);
            context.refresh();
            servlet.setAttribute(org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
            var template = new ClassLoaderTemplateResolver();
            template.setPrefix("templates/");
            template.setSuffix(".html");
            template.setCharacterEncoding("UTF-8");
            var engine = new SpringTemplateEngine();
            engine.setTemplateResolver(template);
            var resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(engine);
            resolver.setApplicationContext(context);
            resolver.setCharacterEncoding("UTF-8");
            var model = new HashMap<String, Object>();
            model.put("_csrf", new org.springframework.security.web.csrf.DefaultCsrfToken("header", "_csrf", "token"));
            var treinador = new TreinadorModel();
            treinador.setId(1L);
            treinador.setName("Treinador exemplo");
            model.put("treinador", treinador);
            model.put("listaAtletas", List.of());
            model.put("vinculados_count", 1);
            model.put("atleta_sem_Vinculacao", 0);
            for (String pagina : List.of("publicPlages/Login", "treinadorPages/dash", "medicoPages/vincularAtleta")) {
                var view = resolver.resolveViewName(pagina, Locale.forLanguageTag("pt-BR"));
                var response = new MockHttpServletResponse();
                view.render(model, new MockHttpServletRequest(servlet), response);
                String html = response.getContentAsString();
                assertTrue(html.contains("/js/tailwind-theme.js"));
                if (pagina.equals("publicPlages/Login")) {
                    assertTrue(html.contains("Acessar Portal"));
                    assertTrue(html.contains("name=\"_csrf\""));
                } else {
                    assertTrue(html.contains("Nenhum atleta novo por aqui"));
                    assertTrue(html.contains("/logout"));
                    assertTrue(html.contains("id=\"search\""));
                }
            }
        }
    }
}
