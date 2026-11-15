package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import org.junit.jupiter.api.Test;
import com.api.Projeto_3.repository.AtletaRespository;

class MedicoDesvincularAtletaTest {
    private MedicoService serviceComResultado(int linhasAlteradas) {
        MedicoService service = new MedicoService();
        service.jpaAtleta = (AtletaRespository) Proxy.newProxyInstance(
                AtletaRespository.class.getClassLoader(),
                new Class<?>[] { AtletaRespository.class },
                (proxy, method, args) -> {
                    assertEquals("desvincularMedico", method.getName());
                    assertEquals(10L, args[0]);
                    assertEquals(2L, args[1]);
                    return linhasAlteradas;
                });
        return service;
    }

    @Test
    void desvinculaUsandoAtletaEMedicoInformados() {
        assertTrue(serviceComResultado(1).desvincularAtleta(2L, 10L));
    }

    @Test
    void retornaFalhaQuandoNenhumVinculoFoiRemovido() {
        assertFalse(serviceComResultado(0).desvincularAtleta(2L, 10L));
    }
}
