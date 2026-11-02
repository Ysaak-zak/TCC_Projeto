package com.api.Projeto_3.dtos.interfaceCustomer;

import java.time.LocalDate;

public interface MedicoCustomer {

    Long getId();
    String getName();
    LocalDate getDataNascimento();
    String getCpf();
    String getRg();
    String getEmail();
    String getSenha();
    String getTelefoneFixo();
    String getTelefoneZap();
    String getPesoMigrama();
    String getAlturaCetimentro();
    String getGenero();
    String getSangue();
    String getBairro();
    String getCidade();
    String getMaeNome();
    String getMaeEmail();
    String getMaeTelefone();
    String getPaiName();
    String getPaiEmail();
    String getPaiTelefone();
}
