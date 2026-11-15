package com.api.Projeto_3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.Projeto_3.model.EmaxeUrina;

@Repository 
public interface UrinaRespository  extends JpaRepository<EmaxeUrina,Long>   {
    List<EmaxeUrina> findByAtleta_Id(Long atletaId);
}
