package com.api.Projeto_3.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.model.AtletaModelo;




@Repository
public interface AtletaRespository extends  JpaRepository<AtletaModelo,Long>  {

    List<AtletaModelo> findByCpf(String cpf);

    @Query("select a from AtletaModelo a where a.medico_fk.id = :medicoId order by a.name")
    List<AtletaModelo> findByMedicoFk_IdOrderByName(@Param("medicoId") Long medicoId);

    @Query("select a from AtletaModelo a where a.treinador_fk.cpf = :cpf order by a.name")
    List<AtletaModelo> listarPorTreinador(@Param("cpf") String cpf);


@Query(value = "SELECT atl.id , per.per_nome , per.cpf , per.sexo , per.email , mor.cidade " +
               "FROM perfis_modelo AS per " +
               "INNER JOIN atletas atl ON per.id = atl.id " +
               "INNER JOIN moradia mor ON per.mord = mor.id" +
               " WHERE atl.atleta_trenador_fk IS null " , nativeQuery = true)
List<ListDasAtleta> atletListCus();


@Modifying
@Query(value = "UPDATE atletas AS atl " +
                   "SET atleta_trenador_fk = :treinadorId " +
                   "WHERE atl.id = :atletaId", 
           nativeQuery = true)
void vincularTreinador(@Param("atletaId") Long atletaId, @Param("treinadorId") Long treinadorId);


@Query(value=" SELECT COUNT(*) " +
             " FROM perfis_modelo AS per " +
             " INNER JOIN atletas atl ON per.id = atl.id  " +
             "  WHERE atl.atleta_trenador_fk = :treinadorId "
            , nativeQuery = true)
int atletaVinculados(Long treinadorId);

@Query(value=" SELECT COUNT(*) " +
             " FROM perfis_modelo AS per " +
             " INNER JOIN atletas atl ON per.id = atl.id  " +
             "  WHERE atl.atleta_trenador_fk IS null "
            , nativeQuery = true)
int atletaSemVinculados();

        @Query(value = "  SELECT per.per_nome  FROM perfis_modelo AS per " +
                        " INNER JOIN atletas atl ON atl.id = per.id " + 
                        " INNER JOIN treinadores tre  ON tre.id = atl.atleta_trenador_fk " + 
                         "  WHERE tre.id = :indTreinador " , nativeQuery = true )
        List<String> listAtletasVinculados(Long indTreinador);

     

        @Query(value = "SELECT atl.id , per.per_nome , per.cpf , per.sexo , per.email , mor.cidade " +
                       "FROM perfis_modelo AS per " +
                       "INNER JOIN atletas atl ON per.id = atl.id " +
                       "INNER JOIN moradia mor ON per.mord = mor.id" +
                       " WHERE atl.atleta_medico_fk IS null " , nativeQuery = true)
        List<ListDasAtleta> atletListCusMedico();

        @Modifying
        @Query(value = "UPDATE atletas AS atl " +
                           "SET atleta_medico_fk = :medicoId " +
                           "WHERE atl.id = :atletaId",
                   nativeQuery = true)
        void vincularMedico(@Param("atletaId") Long atletaId, @Param("medicoId") Long medicoId);

        @Query(value=" SELECT COUNT(*) " +
                     " FROM perfis_modelo AS per " +
                     " INNER JOIN atletas atl ON per.id = atl.id  " +
                     "  WHERE atl.atleta_medico_fk = :medicoId "
                    , nativeQuery = true)
        int atletaVinculadosMedico(Long medicoId);

        @Query(value=" SELECT COUNT(*) " +
                     " FROM perfis_modelo AS per " +
                     " INNER JOIN atletas atl ON per.id = atl.id  " +
                     "  WHERE atl.atleta_medico_fk IS null "
                    , nativeQuery = true)
        int atletaSemVinculadosMedico();

        @Query(value = "  SELECT per.per_nome  FROM perfis_modelo AS per " +
                        " INNER JOIN atletas atl ON atl.id = per.id " +
                        " INNER JOIN medicos med  ON med.id = atl.atleta_medico_fk " +
                         "  WHERE med.id = :indMedico " , nativeQuery = true )
        List<String> listAtletasVinculadosMedico(Long indMedico);

}
