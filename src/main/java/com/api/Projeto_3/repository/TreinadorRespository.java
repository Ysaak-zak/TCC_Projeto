package com.api.Projeto_3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.Projeto_3.dtos.interfaceCustomer.CustomerNome;
import com.api.Projeto_3.dtos.interfaceCustomer.TreinadorCustomer;
import com.api.Projeto_3.dtos.model.PerfilsDtos;
import com.api.Projeto_3.model.TreinadorModel;

@Repository
public interface TreinadorRespository  extends JpaRepository<TreinadorModel,Long> {

    PerfilsDtos findByCpf(String cpf);

    @Query(value = """
        SELECT per.id AS id, per.per_nome AS name,
               per.data_nascimento AS dataNascimento, per.cpf AS cpf,
               per.rg AS rg, per.email AS email, per.senha AS senha,
               per.telefone_fixo AS telefoneFixo, per.telefone_zap AS telefoneZap,
               per.peso_migrama AS pesoMigrama, per.altura_cetimentro AS alturaCetimentro,
               per.sexo AS genero, per.sangue AS sangue,
               mor.bairro AS bairro, mor.cidade AS cidade,
               afl.mae_nome AS maeNome, afl.mae_email AS maeEmail,
               afl.mae_telefone AS maeTelefone, afl.pai_name AS paiName,
               afl.pai_email AS paiEmail, afl.pai_telefone AS paiTelefone
        FROM perfis_modelo per
        INNER JOIN treinadores tre ON tre.id = per.id
        LEFT JOIN moradia mor ON per.mord = mor.id
        LEFT JOIN afliacao afl ON per.fal_fk = afl.id
        WHERE per.cpf = :cpf
        """, nativeQuery = true)
    public TreinadorCustomer listTreinador(String cpf);


    @Query(value = " SELECT per.per_nome FROM perfis_modelo AS per " 
                 +" INNER JOIN treinadores tre ON tre.id = per.id "
                 +" WHERE per.cpf = :cpf "
                 , nativeQuery = true)
    public CustomerNome buscarNomes(String cpf);



    @Modifying
    @Query(value = " UPDATE perfis_modelo " + 
                   " SET foto_url = :caminho_Arquivo " +
                   " FROM treinadores tre  " + 
                   " WHERE tre.id = perfis_modelo.id " + 
                   " AND perfis_modelo.id = :id ; ", nativeQuery =  true)
    public void salvandoFotoPerfil(String caminho_Arquivo , Long id);
    
    

    
}
