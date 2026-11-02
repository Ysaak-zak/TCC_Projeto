package com.api.Projeto_3.dtos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarSemanaDto {
    //@NotBlank(message = "Informe o nome da semana.")
    //@Size(max = 255, message = "O nome deve ter até 255 caracteres.")
    private String nome;

    //@Size(max = 255, message = "A descrição deve ter até 255 caracteres.")
    private String descricao;

    //@NotNull(message = "Selecione um atleta vinculado.")
    private Long atletaId;
}
