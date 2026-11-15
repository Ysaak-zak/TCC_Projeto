package com.api.Projeto_3.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.api.Projeto_3.dtos.DtosModelos.ExameSagueDtos;
import com.api.Projeto_3.service.SangueTest;

class ExameSangueControllerTest {
    @Test
    void recebeFormularioEAbreResultado() throws Exception {
        var service = mock(SangueTest.class);
        when(service.realizarExameParaPagina(any())).thenAnswer(call -> call.getArgument(0));
        var controller = new ChatMedicoExame();
        ReflectionTestUtils.setField(controller, "sangueService", service);
        var mvc = MockMvcBuilders.standaloneSetup(controller).build();

        var response = mvc.perform(post("/medico/exame-sangue/salvar")
                .param("atletaId", "7")
                .param("substancias[0].nomeSubstancia", "Substância pesquisada"))
                .andExpect(redirectedUrl("/medico/exame-sangue/resultado"))
                .andReturn();
        var exame = (ExameSagueDtos) response.getFlashMap().get("exameSangue");
        assertEquals(7L, exame.getAtletaId());
        assertEquals("Substância pesquisada", exame.getSubstancias().get(0).getNomeSubstancia());
        verify(service).realizarExameParaPagina(exame);
        mvc.perform(get("/medico/exame-sangue/resultado").flashAttrs(response.getFlashMap()))
                .andExpect(view().name("medicoPages/resultadoSangue"));
        mvc.perform(get("/medico/exame-sangue/resultado"))
                .andExpect(redirectedUrl("/medico/exame-sangue"));
    }
}
