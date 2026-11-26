package com.api.Projeto_3.dtos.interfaceCustomer;

import java.time.LocalTime;

public interface LinhaDaAgendaCustomer {
    String getDia();
    Long getTreinoId();
    String getTreinoNome();
    String getTreinoCategoria();
    String getTreinoDescricao();
    LocalTime getHorarioInicio();
    LocalTime getHorarioFim();
    boolean isTreinoCancelado();
    boolean isTreinoConcluido();

    Long getExercicioId();
    String getExercicioNome();
    String getExercicioDescricao();
    double getExercicioDuracao();
    boolean isExercicioConcluido();
}
