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

class DashTemplateTest {
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
            var view = resolver.resolveViewName("atletaPages/dashAtleta", Locale.forLanguageTag("pt-BR"));
            var dash = new com.api.Projeto_3.dtos.model.DashDto();
            dash.setNomeAtleta("Ana");
            var model = new HashMap<String, Object>();
            model.put("dash", dash);
            model.put("_csrf", new org.springframework.security.web.csrf.DefaultCsrfToken("header", "_csrf", "token"));
            var response = new MockHttpServletResponse();
            view.render(model, new MockHttpServletRequest(servlet), response);
            var html = response.getContentAsString();
            assertTrue(html.contains("Nenhum treino cadastrado nesta semana."));
            assertTrue(html.contains("Ana"));
            var semana = new SemanaModel();
            semana.setId(1L);
            semana.setNome("Semana 1");
            dash.setSemanas(List.of(semana));
            dash.setSemanaId(1L);
            var treino = new com.api.Projeto_3.dtos.model.DashTreinoDto();
            treino.setNome("Corrida");
            treino.setDuracao(45L);
            dash.setTreinos(List.of(treino));
            var ponto = new com.api.Projeto_3.dtos.model.DashPontoDto();
            ponto.setX("Seg");
            ponto.setY(45);
            dash.setAtividadeSemanal(List.of(ponto));
            response = new MockHttpServletResponse();
            view.render(model, new MockHttpServletRequest(servlet), response);
            html = response.getContentAsString();
            assertTrue(html.contains("Corrida"));
            assertTrue(html.contains("45 min"));
            assertTrue(html.contains("selected=\"selected\""));
            assertTrue(html.contains("\"y\":45"));
        }
    }
}
