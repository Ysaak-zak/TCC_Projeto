package com.api.Projeto_3.model;

import java.io.Serializable;
import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DialectOverride.GeneratedColumn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "EXAMA_URINA")
public class EmaxeUrina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date data_hora_coleta;
    private Date  dataHoraRecebimento;
    
    @CreationTimestamp
    @Column(name = "dataHoraLiberdado")
    private Date dataHoraLiberacao;
    
    private double tempeturaAmostraCelsius;
    private String cor_amostra;
    private double ph;
    private double densidade_epecifica;
    private double creatinina_urinaria;
    private String test_adulteracao;
    private String metodologia_triagem;
    private String status_amostra;
    private String resultado_sbstancia;
    private String valorCorteCuoff;
    
}