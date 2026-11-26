package com.api.Projeto_3.dtos.DtosModelos;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.api.Projeto_3.model.enums.DiasEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CriarTreinoDto {
    @Valid
    @Size(max = 50, message = "Adicione até 50 exercícios por treino.")
    private List<ExercicioDoTreinoDto> exercicios = new ArrayList<>();

    @NotNull(message = "Selecione a semana do atleta.")
    private Long semanaId;

    @NotNull(message = "Selecione o dia da semana.")
    private DiasEnum dia;
    
    @NotBlank(message = "Informe o nome do treino.")
    @Size(max = 255)
    private String nome;

    @NotBlank(message = "Informe a categoria.")
    @Size(max = 255)
    private String categoria;
    
    @Size(max = 255, message = "A descrição deve ter até 255 caracteres.")
    private String descricao;
    
    @NotNull(message = "Informe o horário de início.")
    private LocalTime horarioInicio;
    
    @NotNull(message = "Informe o horário de fim.")
    private LocalTime horarioFim;

    @AssertTrue(message = "O horário final deve ser posterior ao inicial, entre 06:00 e 22:00.")
    public boolean isHorarioValido() {
        return horarioInicio == null || horarioFim == null ||
            (!horarioInicio.isBefore(LocalTime.of(6, 0)) &&
             !horarioFim.isAfter(LocalTime.of(22, 0)) && horarioFim.isAfter(horarioInicio));
    }

    public List<ExercicioDoTreinoDto> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<ExercicioDoTreinoDto> exercicios) {
        this.exercicios = exercicios;
    }

    public Long getSemanaId() {
        return semanaId;
    }

    public void setSemanaId(Long semanaId) {
        this.semanaId = semanaId;
    }

    public DiasEnum getDia() {
        return dia;
    }

    public void setDia(DiasEnum dia) {
        this.dia = dia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }
}
