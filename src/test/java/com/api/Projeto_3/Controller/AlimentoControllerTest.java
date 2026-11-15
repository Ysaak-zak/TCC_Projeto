package com.api.Projeto_3.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ExtendedModelMap;
import com.api.Projeto_3.model.AtletaModelo;
import com.api.Projeto_3.model.MedicoModelo;
import com.api.Projeto_3.service.MedicoService;

class AlimentoControllerTest {
    @Test
    void alimentacaoListaAtletasDoMedicoLogado() {
        MedicoModelo medico = new MedicoModelo();
        medico.setId(2L);
        AtletaModelo atleta = new AtletaModelo();
        atleta.setId(10L);
        List<AtletaModelo> atletas = List.of(atleta);
        MedicoService service = new MedicoService() {
            @Override
            public MedicoModelo buscarMedicoPorId(String cpf) {
                assertEquals("123", cpf);
                return medico;
            }

            @Override
            public List<AtletaModelo> atletasVinculados(Long medicoId) {
                assertEquals(2L, medicoId);
                return atletas;
            }
        };
        AlimentoController controller = new AlimentoController(service, null);
        ExtendedModelMap model = new ExtendedModelMap();
        var usuario = User.withUsername("123").password("senha").roles("MEDICO").build();

        assertEquals("redirect:/medico/alimentacao/10", controller.listarAtletas(usuario, model));
    }

    @Test
    void usuarioAusenteVaiParaLogin() {
        AlimentoController controller = new AlimentoController(null, null);
        assertEquals("redirect:/login", controller.listarAtletas(null, new ExtendedModelMap()));
    }

    @Test
    void medicoAusenteVaiParaLoginComErro() {
        MedicoService service = new MedicoService() {
            @Override
            public MedicoModelo buscarMedicoPorId(String cpf) {
                return null;
            }
        };
        AlimentoController controller = new AlimentoController(service, null);
        var usuario = User.withUsername("123").password("senha").roles("MEDICO").build();
        assertEquals("redirect:/login?error", controller.listarAtletas(usuario, new ExtendedModelMap()));
    }
}
