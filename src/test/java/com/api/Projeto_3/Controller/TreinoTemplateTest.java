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
import com.api.Projeto_3.dtos.model.CriarTreinoDto;
import com.api.Projeto_3.model.*;
import com.api.Projeto_3.model.enums.DiasEnum;

class TreinoTemplateTest {
    @Test
    void renderizaDiasSelecaoEEstadoVazio() throws Exception {
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
            var view = resolver.resolveViewName("treinadorPages/treimamento/Treinamento", Locale.forLanguageTag("pt-BR"));
            var form = new CriarTreinoDto();
            form.setDia(DiasEnum.QUARTA);
            var model = new HashMap<String, Object>();
            model.put("treinoForm", form);
            model.put("diasSemana", DiasEnum.values());
            model.put("semanas", List.of());
            var response = new MockHttpServletResponse();
            view.render(model, new MockHttpServletRequest(servlet), response);
            var html = response.getContentAsString();
            assertTrue(html.contains("Comece criando uma semana"));
            assertTrue(html.contains("disabled=\"disabled\""));
            assertEquals(7, html.split("type=\"radio\"", -1).length - 1);
            assertTrue(java.util.regex.Pattern.compile("<input(?=[^>]*value=\"QUARTA\")(?=[^>]*checked=\"checked\")[^>]*>").matcher(html).find());
            var atleta = new AtletaModelo();
            atleta.setName("Atleta exemplo");
            var semana = new SemanaModel();
            semana.setId(1L);
            semana.setNome("Semana 1");
            semana.setAtleta_fk(atleta);
            model.put("semanas", List.of(semana));
            response = new MockHttpServletResponse();
            view.render(model, new MockHttpServletRequest(servlet), response);
            html = response.getContentAsString();
            assertTrue(html.contains("Semana 1 — Atleta exemplo"));
            assertFalse(html.contains("disabled=\"disabled\""));
        }
    }
}
