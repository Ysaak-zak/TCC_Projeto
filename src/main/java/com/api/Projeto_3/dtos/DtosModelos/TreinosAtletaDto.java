package com.api.Projeto_3.dtos.DtosModelos;

import java.time.LocalTime;
import java.util.List;

import com.api.Projeto_3.model.enums.DiasEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreinosAtletaDto {
    private String nomeAtleta;
    private Long semanaId;
    private List<Semana> semanas = List.of();
    private List<Dia> dias = List.of();

    @Getter
    @Setter
    public static class Semana {
        private Long id;
        private String nome;
    }

    @Getter
    @Setter
    public static class Dia {
        private DiasEnum nome;
        private List<Treino> treinos = List.of();
    }

    @Getter
    @Setter
    public static class Treino {
        private Long id;
        private String nome;
        private String categoria;
        private String descricao;
        private LocalTime horarioInicio;
        private LocalTime horarioFim;
        private boolean cancelado;
        private boolean concluido;
        private List<Exercicio> exercicios = List.of();

        public long getTotalExerciciosConcluidos() {
            return exercicios.stream().filter(Exercicio::isConcluido).count();
        }

        public boolean isPodeConcluir() {
            return !cancelado && !concluido && exercicios.stream().allMatch(Exercicio::isConcluido);
        }
    }

    @Getter
    @Setter
    public static class Exercicio {
        private Long id;
        private String nome;
        private String descricao;
        private double duracao;
        private boolean concluido;
    }
}
