package com.api.Projeto_3.Controller;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.service.MedicoService;

class ExameUrinaControllerTest {
    @Test
    void enviaRespostaDoChatParaPaginaDeResultado() throws Exception {
        var service = org.mockito.Mockito.mock(com.api.Projeto_3.service.UrinaExamesService.class);
        org.mockito.Mockito.when(service.realizarExameParaPagina(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> {
                    var exame = invocation.getArgument(0, com.api.Projeto_3.dtos.DtosModelos.ExameUrinaDtos.class);
                    exame.setAnalise(new com.api.Projeto_3.dtos.chat.ProcessamentoChat(
                            "Resposta real do chat", List.of("Conferir dados"), List.of("Avaliação médica")));
                    return exame;
                });
        var controller = new ChatMedicoExame(service, null);
        var mvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(controller).build();
        var response = mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .post("/medico/exame/urina").param("observacoes", "Observação original"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .redirectedUrl("/medico/exame-urina/resultado"))
                .andReturn();
        var exame = (com.api.Projeto_3.dtos.DtosModelos.ExameUrinaDtos) response.getFlashMap().get("exameUrina");
        assertEquals("Resposta real do chat", exame.getAnalise().resumo());
        assertEquals("Observação original", exame.getObservacoes());
        assertEquals("NAO_REALIZADO", exame.getAnabolizantes());
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/medico/exame-urina/resultado").flashAttrs(response.getFlashMap()))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .view().name("medicoPages/result"));
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/medico/exame-urina/resultado"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .redirectedUrl("/medico/exame-urina"));
    }

    @Test
    void disponibilizaDtoDoFormularioPeloAdvice() throws Exception {
        var controller = new MedicoController();
        controller.medicoService = new MedicoService() {
            @Override
            public MedicoModelo buscarMedicoPorId(String cpf) {
                var medico = new MedicoModelo();
                medico.setId(7L);
                return medico;
            }

            @Override
            public List<AtletaModelo> atletasVinculados(Long medicoId) {
                return List.of();
            }
        };
        var usuario = User.withUsername("cpf").password("senha").roles("MEDICO").build();
        var mvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ChatMedicoExame.ExameUrinaModelAdvice())
                .setCustomArgumentResolvers(new org.springframework.web.method.support.HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
                        return parameter.getParameterType() == org.springframework.security.core.userdetails.UserDetails.class;
                    }

                    @Override
                    public Object resolveArgument(org.springframework.core.MethodParameter parameter,
                            org.springframework.web.method.support.ModelAndViewContainer container,
                            org.springframework.web.context.request.NativeWebRequest request,
                            org.springframework.web.bind.support.WebDataBinderFactory factory) {
                        return usuario;
                    }
                }).build();

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/medico/exame-urina"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.view().name("medicoPages/TestUrina"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.model()
                        .attributeExists("exameUrina"));
    }

    @Test
    void abreExameComAtletasDoMedicoAutenticado() throws Exception {
        var medico = new MedicoModelo();
        medico.setId(7L);
        var atleta = new AtletaModelo();
        var atletas = List.of(atleta);
        var controller = new MedicoController();
        controller.medicoService = new MedicoService() {
            @Override
            public MedicoModelo buscarMedicoPorId(String cpf) {
                assertEquals("cpf-medico", cpf);
                return medico;
            }
            @Override
            public List<AtletaModelo> atletasVinculados(Long medicoId) {
                assertEquals(7L, medicoId);
                return atletas;
            }
        };
        var model = new ExtendedModelMap();
        var usuario = User.withUsername("cpf-medico").password("senha").roles("MEDICO").build();
        assertEquals("medicoPages/TestUrina", controller.getExameUrina(usuario, model));
        assertSame(medico, model.get("medico"));
        assertSame(atletas, model.get("atletas"));
        var method = MedicoController.class.getMethod("getExameUrina",
                org.springframework.security.core.userdetails.UserDetails.class, org.springframework.ui.Model.class);
        assertArrayEquals(new String[] {"/medico/exame-urina"}, method.getAnnotation(GetMapping.class).value());
    }

    @Test
    void redirecionaQuandoNaoHaUsuarioOuCadastroMedico() {
        var controller = new MedicoController();
        assertEquals("redirect:/login", controller.getExameUrina(null, new ExtendedModelMap()));
        controller.medicoService = new MedicoService() {
            @Override
            public MedicoModelo buscarMedicoPorId(String cpf) { return null; }
        };
        var usuario = User.withUsername("cpf").password("senha").roles("MEDICO").build();
        var model = new ExtendedModelMap();
        assertEquals("redirect:/login?error", controller.getExameUrina(usuario, model));
        assertTrue(model.isEmpty());
    }
}
