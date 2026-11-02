package com.api.Projeto_3.dtos.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarExercicioDto {
    //@NotNull(message = "Selecione a semana.")
    private Long semanaId;
    //@NotNull(message = "Selecione um treinamento da semana.")
    private Long treinamentoId;
    //@NotBlank(message = "Informe o nome do exercício.")
    //@Size(max = 255)
    private String nome;
    //@Size(max = 255, message = "A descrição deve ter até 255 caracteres.")
    private String descricao;
    //@NotNull(message = "Informe a duração em minutos.")
    //@Positive(message = "A duração deve ser maior que zero.")
    private Integer duracao;
}
