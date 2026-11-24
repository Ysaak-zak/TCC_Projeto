package com.api.Projeto_3.dtos.DtosModelos;

import java.time.LocalTime;
import java.util.List;



public class TreinoAgendaDto {
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private boolean cancelado;
    private boolean concluido;
    private List<ExercicioAgendaDto> exercicios = List.of();

    public long getTotalExerciciosConcluidos() {
        long total = 0;
        for (ExercicioAgendaDto exercicio : exercicios) {
            if (exercicio.isConcluido()) {
                total++;
            }
        }
        return total;
    }

    public boolean isPodeConcluir() {
        return !cancelado && !concluido && getTotalExerciciosConcluidos() == exercicios.size();
    }

    public TreinoAgendaDto(Long id, String nome, String categoria, String descricao, LocalTime horarioInicio,
            LocalTime horarioFim, boolean cancelado, boolean concluido, List<ExercicioAgendaDto> exercicios) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.cancelado = cancelado;
        this.concluido = concluido;
        this.exercicios = exercicios;
    }

    public TreinoAgendaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public List<ExercicioAgendaDto> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<ExercicioAgendaDto> exercicios) {
        this.exercicios = exercicios;
    }

    @Override
    public String toString() {
        return "TreinoAgendaDto [id=" + id + ", nome=" + nome + ", categoria=" + categoria + ", descricao=" + descricao
                + ", horarioInicio=" + horarioInicio + ", horarioFim=" + horarioFim + ", cancelado=" + cancelado
                + ", concluido=" + concluido + ", exercicios=" + exercicios + "]";
    }

    
}
