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
import com.api.Projeto_3.dtos.model.CriarExercicioDto;
import com.api.Projeto_3.model.*;
import com.api.Projeto_3.model.enums.DiasEnum;

class ExercicioTemplateTest {
    @Test
    void renderizaSelecaoDaSemanaTreinoEEstadoVazio() throws Exception {
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
            var view = resolver.resolveViewName("treinadorPages/treimamento/Exercicio", Locale.forLanguageTag("pt-BR"));
            var form = new CriarExercicioDto();
            
            var model = new HashMap<String, Object>();
            model.put("exercicioForm", form);
            model.put("treinamentos", List.of());
            model.put("semanas", List.of());
            var response = new MockHttpServletResponse();
            view.render(model, new MockHttpServletRequest(servlet), response);
            var html = response.getContentAsString();
            assertTrue(html.contains("criando uma semana"));
            assertTrue(html.contains("disabled=\"disabled\""));
            var atleta = new AtletaModelo();
            atleta.setName("Atleta exemplo");
            var semana = new SemanaModel();
            semana.setId(1L);
            semana.setNome("Semana 1");
            semana.setAtleta_fk(atleta);
            model.put("semanas", List.of(semana));
            form.setSemanaId(1L);
            var dia = new DiaModel();
            dia.setNome(DiasEnum.SEGUNDA);
            var treino = new TreinamentoModel();
            treino.setId(2L);
            treino.setNome("Treino teste");
            treino.setDia_fk(dia);
            treino.setHorarioInicio(java.time.LocalTime.of(8, 0));
            model.put("treinamentos", List.of(treino));
            response = new MockHttpServletResponse();
            view.render(model, new MockHttpServletRequest(servlet), response);
            html = response.getContentAsString();
            assertTrue(html.contains("Semana 1 — Atleta exemplo"));
            assertTrue(html.contains("Treino teste"));
            assertFalse(html.contains("disabled=\"disabled\""));
        }
    }
}
