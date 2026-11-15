package com.api.Projeto_3.dtos.DtosModelos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExercicioDoTreinoDto {
    @NotBlank(message = "Informe o nome do exercício.")
    @Size(max = 255)
    private String nome;
    @Size(max = 255)
    private String descricao;
    @NotNull(message = "Informe a duração do exercício.")
    @Positive(message = "A duração do exercício deve ser maior que zero.")
    private Integer duracao;
}
