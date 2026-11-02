package com.api.Projeto_3.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import com.api.Projeto_3.dtos.model.CriarSemanaDto;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.SemanaResponitory;
import com.api.Projeto_3.service.TrenamentosSerive;

class TreinamentoControllerTest {
    @Test
    void salvaSemanaDoAtletaVinculadoUsandoCreSemana() {
        var service = mock(TrenamentosSerive.class);
        var atletas = mock(AtletaRespository.class);
        var semanas = mock(SemanaResponitory.class);
        var atleta = new AtletaModelo();
        atleta.setId(10L);
        when(atletas.listarPorTreinador("cpf")).thenReturn(List.of(atleta));
        var form = new CriarSemanaDto();
        form.setNome(" Semana 1 ");
        form.setAtletaId(10L);
        var result = new BeanPropertyBindingResult(form, "semanaForm");
        var controller = new TreinamentoController(service, atletas, semanas);
        assertEquals("redirect:/treinador/treinos", controller.criarSemana(form, result,
                User.withUsername("cpf").password("senha").roles("TREINADOR").build(),
                new ExtendedModelMap(), new RedirectAttributesModelMap()));
        var captor = ArgumentCaptor.forClass(SemanaModel.class);
        verify(service).creSemana(captor.capture());
        assertSame(atleta, captor.getValue().getAtleta_fk());
        assertEquals("Semana 1", captor.getValue().getNome());
    }

    @Test
    void rejeitaAtletaNaoVinculadoSemSalvar() {
        var service = mock(TrenamentosSerive.class);
        var atletas = mock(AtletaRespository.class);
        var semanas = mock(SemanaResponitory.class);
        when(atletas.listarPorTreinador("cpf")).thenReturn(List.of());
        var form = new CriarSemanaDto();
        form.setNome("Semana 1");
        form.setAtletaId(99L);
        var result = new BeanPropertyBindingResult(form, "semanaForm");
        assertEquals("treinadorPages/treimamento/semana",
                new TreinamentoController(service, atletas, semanas).criarSemana(form, result,
                User.withUsername("cpf").password("senha").roles("TREINADOR").build(),
                new ExtendedModelMap(), new RedirectAttributesModelMap()));
        assertTrue(result.hasFieldErrors("atletaId"));
        verifyNoInteractions(service);
    }
    @Test
    void salvaExercicioMantendoSemanaNoRedirecionamentoEReexibeErros() {
        var service = mock(TrenamentosSerive.class);
        var atletas = mock(AtletaRespository.class);
        var semanas = mock(SemanaResponitory.class);
        var controller = new TreinamentoController(service, atletas, semanas);
        var usuario = User.withUsername("cpf").password("senha").roles("TREINADOR").build();
        var form = new com.api.Projeto_3.dtos.model.CriarExercicioDto();
        form.setSemanaId(1L);
        form.setTreinamentoId(2L);
        form.setNome("Corrida");
        form.setDuracao(30);
        var result = new BeanPropertyBindingResult(form, "exercicioForm");
        var redirect = new RedirectAttributesModelMap();
        assertEquals("redirect:/treinador/treinos/exercicios",
                controller.criarExercicio(form, result, usuario, new ExtendedModelMap(), redirect));
        verify(service).criarExercicio(form, "cpf");
        assertEquals("1", redirect.get("semanaId"));
        doThrow(new IllegalArgumentException("Treinamento inválido"))
                .when(service).criarExercicio(form, "cpf");
        var model = new ExtendedModelMap();
        assertEquals("treinadorPages/treimamento/Exercicio",
                controller.criarExercicio(form, result, usuario, model, redirect));
        assertTrue(result.hasFieldErrors("treinamentoId"));
        assertTrue(model.containsAttribute("semanas"));
        assertTrue(model.containsAttribute("treinamentos"));
        assertEquals("Corrida", form.getNome());
    }
}
