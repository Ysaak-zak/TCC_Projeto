package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Proxy;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.api.Projeto_3.model.RecomendacaoAlimentar;
import com.api.Projeto_3.repository.RecomendacaoAlimentarRepository;

class AtletaServiceAlimentacaoTest {

    @Test
    void buscaRecomendacoesDoAtletaPorDataMaisRecente() {
        List<RecomendacaoAlimentar> recomendacoes = List.of(new RecomendacaoAlimentar());
        AtletaService service = new AtletaService();
        service.recomendacaoRepository = (RecomendacaoAlimentarRepository) Proxy.newProxyInstance(
                RecomendacaoAlimentarRepository.class.getClassLoader(),
                new Class<?>[] { RecomendacaoAlimentarRepository.class },
                (proxy, method, args) -> {
                    if (method.getName().equals("findByAtletaIdOrderByDataInicioDesc") && args[0].equals(42L)) {
                        return recomendacoes;
                    }
                    throw new UnsupportedOperationException(method.getName());
                });

        assertSame(recomendacoes, service.buscarRecomendacoesAlimentares(42L));
    }
}
