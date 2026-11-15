package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import org.junit.jupiter.api.Test;
import com.api.Projeto_3.repository.AtletaRespository;

class TreinadorDesvincularAtletaTest {
    private TreinadorService serviceComResultado(int linhasAlteradas) {
        TreinadorService service = new TreinadorService();
        service.jpaAtleta = (AtletaRespository) Proxy.newProxyInstance(
                AtletaRespository.class.getClassLoader(),
                new Class<?>[] { AtletaRespository.class },
                (proxy, method, args) -> {
                    assertEquals("desvincularTreinador", method.getName());
                    assertEquals(10L, args[0]);
                    assertEquals(2L, args[1]);
                    return linhasAlteradas;
                });
        return service;
    }

    @Test
    void desvinculaUsandoAtletaETreinadorInformados() {
        assertTrue(serviceComResultado(1).desvincularAtleta(2L, 10L));
    }

    @Test
    void retornaFalhaQuandoNenhumVinculoFoiRemovido() {
        assertFalse(serviceComResultado(0).desvincularAtleta(2L, 10L));
    }
}
