package com.api.Projeto_3.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Table(name = "TREINAMENTO")
@Entity
public class TreinamentoModel implements Serializable {
    private Integer satisfacao;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private java.time.LocalDate dataInicio;
    private java.time.LocalDate dataFim;
    private java.time.LocalTime horarioInicio;
    private java.time.LocalTime horarioFim;
    private Boolean concluido;
    private Boolean cancelado;
    private java.time.LocalDateTime canceladoEm;

    @ManyToOne
    @JoinColumn(name = "cancelado_por_fk")
    @ToString.Exclude
    private MedicoModelo canceladoPor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dia_fk", nullable = false)
    @ToString.Exclude
    private DiaModel dia_fk;

    @OneToMany(mappedBy = "treinamento_fk", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ExercicioModel> exercicios = new ArrayList<>();

    @OneToMany(mappedBy = "treimanento_fk" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TreinadorModel> listTreinador = new ArrayList<>();

    

    public TreinamentoModel() {
    }

    public Integer getSatisfacao() {
        return satisfacao;
    }

    public void setSatisfacao(Integer satisfacao) {
        this.satisfacao = satisfacao;
    }

    public TreinamentoModel(Integer satisfacao, Long id, String nome, String categoria, String descricao,
            LocalDate dataInicio, LocalDate dataFim, LocalTime horarioInicio, LocalTime horarioFim, DiaModel dia_fk,
            List<ExercicioModel> exercicios, List<TreinadorModel> listTreinador) {
        this.satisfacao = satisfacao;
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.dia_fk = dia_fk;
        this.exercicios = exercicios;
        this.listTreinador = listTreinador;
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

    public java.time.LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(java.time.LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public java.time.LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(java.time.LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public java.time.LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(java.time.LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public java.time.LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(java.time.LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public boolean isConcluido() {
        return Boolean.TRUE.equals(concluido);
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public java.time.LocalDateTime getCanceladoEm() {
        return canceladoEm;
    }

    public void setCanceladoEm(java.time.LocalDateTime canceladoEm) {
        this.canceladoEm = canceladoEm;
    }

    public MedicoModelo getCanceladoPor() {
        return canceladoPor;
    }

    public void setCanceladoPor(MedicoModelo canceladoPor) {
        this.canceladoPor = canceladoPor;
    }

    public DiaModel getDia_fk() {
        return dia_fk;
    }

    public void setDia_fk(DiaModel dia_fk) {
        this.dia_fk = dia_fk;
    }

    public List<ExercicioModel> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<ExercicioModel> exercicios) {
        this.exercicios = exercicios;
    }

    public List<TreinadorModel> getListTreinador() {
        return listTreinador;
    }

    public void setListTreinador(List<TreinadorModel> listTreinador) {
        this.listTreinador = listTreinador;
    }

    @Override
    public String toString() {
        return "TreinamentoModel [satisfacao=" + satisfacao + ", id=" + id + ", nome=" + nome + ", categoria="
                + categoria + ", descricao=" + descricao + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim
                + ", horarioInicio=" + horarioInicio + ", horarioFim=" + horarioFim + ", dia_fk=" + dia_fk
                + ", exercicios=" + exercicios + ", listTreinador=" + listTreinador + "]";
    }

    
    
    
}
