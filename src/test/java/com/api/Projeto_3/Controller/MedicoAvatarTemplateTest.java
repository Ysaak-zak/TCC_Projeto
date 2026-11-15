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

class MedicoAvatarTemplateTest {
    @Test
    void renderizaAvatarComNomeVazioNuloEPreenchido() throws Exception {
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
            for (String nome : new String[] {"", null, "Beatriz"}) {
                var atleta = new AtletaModelo();
                atleta.setId(1L);
                atleta.setName(nome);
                var model = new HashMap<String, Object>();
                model.put("atleta", atleta);
                model.put("atletas", List.of(atleta));
                model.put("recomendacao", new com.api.Projeto_3.dtos.DtosModelos.RecomendacaoAlimentarDto());
                model.put("recomendacoes", List.of());
                model.put("salvo", false);
                for (String pagina : List.of("medicoPages/listaAtletasVinculados", "medicoPages/alimentacao", "medicoPages/TestUrina")) {
                    var view = resolver.resolveViewName(pagina, Locale.forLanguageTag("pt-BR"));
                    var response = new MockHttpServletResponse();
                    view.render(model, new MockHttpServletRequest(servlet), response);
                    String html = response.getContentAsString();
                    String marcador = !pagina.endsWith("alimentacao") ? "data-avatar=\"atleta\"" : "class=\"athlete-avatar\"";
                    String inicial = nome == null || nome.isEmpty() ? "A" : "B";
                    assertTrue(html.contains(marcador + ">" + inicial + "</span>"), pagina + ": " + nome);
                }
            }
        }
    }
}
