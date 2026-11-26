package com.api.Projeto_3.dtos.DtosModelos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


public class CriarExercicioDto {
    @NotNull(message = "Selecione a semana.")
    private Long semanaId;
    @NotNull(message = "Selecione um treinamento da semana.")
    private Long treinamentoId;
    @NotBlank(message = "Informe o nome do exercício.")
    @Size(max = 255)
    private String nome;
    @Size(max = 255, message = "A descrição deve ter até 255 caracteres.")
    private String descricao;
    @NotNull(message = "Informe a duração em minutos.")
    @Positive(message = "A duração deve ser maior que zero.")
    private Integer duracao;
    public Long getSemanaId() {
        return semanaId;
    }
    public void setSemanaId(Long semanaId) {
        this.semanaId = semanaId;
    }
    public Long getTreinamentoId() {
        return treinamentoId;
    }
    public void setTreinamentoId(Long treinamentoId) {
        this.treinamentoId = treinamentoId;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Integer getDuracao() {
        return duracao;
    }
    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    
}
