package com.api.Projeto_3.service;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Proxy;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.api.Projeto_3.dtos.DtosModelos.*;
import com.api.Projeto_3.model.*;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.SemanaResponitory;

class TreinoComExerciciosTest {
    @Test
    void salvaTreinoEExerciciosComVinculosNaMesmaSemana() {
        var semana = new SemanaModel();
        semana.setId(1L);
        int[] salvos = {0};
        var service = new TrenamentosSerive();
        service.snJpa = (SemanaResponitory) Proxy.newProxyInstance(
            SemanaResponitory.class.getClassLoader(), new Class<?>[]{SemanaResponitory.class},
            (proxy, method, args) -> {
                if (method.getName().equals("listarPorTreinador")) return List.of(semana);
                if (method.getName().equals("save")) { salvos[0]++; return args[0]; }
                throw new AssertionError(method.getName());
            });
        var form = new CriarTreinoDto();
        form.setSemanaId(1L);
        form.setDia(DiasEnum.SEGUNDA);
        form.setNome("Treino");
        form.setCategoria("Força");
        var exercicio = new ExercicioDoTreinoDto();
        exercicio.setNome(" Agachamento ");
        exercicio.setDuracao(15);
        form.setExercicios(List.of(exercicio));
        service.criarTreino(form, "cpf");
        var treino = semana.getDias().get(0).getTreinamentos().get(0);
        assertEquals(1, salvos[0]);
        assertEquals(1, treino.getExercicios().size());
        var salvo = treino.getExercicios().get(0);
        assertSame(treino, salvo.getTreinamento_fk());
        assertEquals("Agachamento", salvo.getNome());
        assertEquals(15, salvo.getDuracacao());
    }

    @Test
    void validaNomeEDuracaoDosExerciciosNoFormularioDeTreino() {
        try (var factory = jakarta.validation.Validation.buildDefaultValidatorFactory()) {
            var form = new CriarTreinoDto();
            form.setExercicios(List.of(new ExercicioDoTreinoDto()));
            assertEquals(2, factory.getValidator().validate(form).size());
            form.getExercicios().get(0).setNome("Corrida");
            form.getExercicios().get(0).setDuracao(10);
            assertTrue(factory.getValidator().validate(form).isEmpty());
        }
    }
}
