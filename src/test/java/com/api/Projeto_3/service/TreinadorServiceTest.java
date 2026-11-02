package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.api.Projeto_3.dtos.interfaceCustomer.TreinadorCustomer;
import com.api.Projeto_3.repository.TreinadorRespository;

class TreinadorServiceTest {
    private TreinadorService serviceReturning(TreinadorCustomer result) {
        TreinadorService service = new TreinadorService();
        service.jpaTreinador = (TreinadorRespository) Proxy.newProxyInstance(
                TreinadorRespository.class.getClassLoader(),
                new Class<?>[] { TreinadorRespository.class },
                (proxy, method, args) -> {
                    assertEquals("listTreinador", method.getName());
                    assertEquals("12345678900", args[0]);
                    return result;
                });
        return service;
    }

    @Test
    void treinadorAusenteRetorna404() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> serviceReturning(null).buscarTreinadorPorId("12345678900"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void treinadorEncontradoPreservaProjecao() {
        TreinadorCustomer treinador = (TreinadorCustomer) Proxy.newProxyInstance(
                TreinadorCustomer.class.getClassLoader(),
                new Class<?>[] { TreinadorCustomer.class },
                (proxy, method, args) -> null);
        assertSame(treinador, serviceReturning(treinador).buscarTreinadorPorId("12345678900"));
    }
}
