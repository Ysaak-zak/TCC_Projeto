package com.api.Projeto_3.dtos.model;

import java.time.LocalTime;
import com.api.Projeto_3.model.enums.DiasEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarTreinoDto {
    //@NotNull(message = "Selecione a semana do atleta.")
    private Long semanaId;

    //@NotNull(message = "Selecione o dia da semana.")
    private DiasEnum dia;
    
    //@NotBlank(message = "Informe o nome do treino.")
    //@Size(max = 255)
    
    private String nome;
    //@NotBlank(message = "Informe a categoria.")
    
    //@Size(max = 255)
    private String categoria;
    
    //@Size(max = 255, message = "A descrição deve ter até 255 caracteres.")
    private String descricao;
    
    //@NotNull(message = "Informe o horário de início.")
    private LocalTime horarioInicio;
    
    //@NotNull(message = "Informe o horário de fim.")
    private LocalTime horarioFim;

    //@AssertTrue(message = "O horário final deve ser posterior ao inicial, entre 06:00 e 22:00.")
    public boolean isHorarioValido() {
        return horarioInicio == null || horarioFim == null ||
            (!horarioInicio.isBefore(LocalTime.of(6, 0)) &&
             !horarioFim.isAfter(LocalTime.of(22, 0)) && horarioFim.isAfter(horarioInicio));
    }

}
