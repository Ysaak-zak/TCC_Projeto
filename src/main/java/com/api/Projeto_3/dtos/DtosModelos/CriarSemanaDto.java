package com.api.Projeto_3.dtos.DtosModelos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CriarSemanaDto {
    @NotBlank(message = "Informe o nome da semana.")
    @Size(max = 255, message = "O nome deve ter até 255 caracteres.")
    private String nome;

    @Size(max = 255, message = "A descrição deve ter até 255 caracteres.")
    private String descricao;

    @NotNull(message = "Selecione um atleta vinculado.")
    private Long atletaId;

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

    public Long getAtletaId() {
        return atletaId;
    }

    public void setAtletaId(Long atletaId) {
        this.atletaId = atletaId;
    }
}
