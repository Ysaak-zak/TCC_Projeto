package com.api.Projeto_3.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.api.Projeto_3.dtos.interfaceCustomer.CustomerNome;
import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.dtos.interfaceCustomer.TreinadorCustomer;

import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.PerfilRepository;
import com.api.Projeto_3.repository.RoleRepository;
import com.api.Projeto_3.repository.TreinadorRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class TreinadorService {

      private static final Logger logger = LoggerFactory.getLogger(TreinadorService.class);

      private static final String PASTA_LOCAL = "src/main/resources/static/";

    @Autowired
    PerfilRepository perfilJpa;
    
    @Autowired
    TreinadorRespository jpaTreinador;

    @Autowired
    AtletaRespository jpaAtleta;

    @Autowired
    RoleRepository roles;

    @Transactional(readOnly = true)
    public TreinadorCustomer buscarTreinadorPorId(String cpf) {
        logger.info("buscando dados do Treinador atreves do " + cpf );
        TreinadorCustomer treinador = jpaTreinador.listTreinador(cpf);
        if (treinador == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treinador não encontrado");
        }
        return treinador;
    }

    @Transactional(readOnly = true)
    public CustomerNome buscarNomes(String cpf){
          logger.info("buscando tudos os atletas ");
        return jpaTreinador.buscarNomes(cpf);
    }

     @Transactional(readOnly = true)
    public List<String> mosntrando_atleta_nomes(Long idTrei){  
        return jpaAtleta.listAtletasVinculados(idTrei);
    }

     @Transactional(readOnly = true)
    public List<ListDasAtleta> listTab() {
        logger.info("Listando todos os atletas");
        List<ListDasAtleta> list = jpaAtleta.atletListCus();
        if (list == null || list.isEmpty()) {
            logger.warn("Nenhum atleta encontrado");
        } else {
            logger.info("Total de atletas: {}", list.size());
        }
        return list;
    }


    public void salvarImagemNaPasta(MultipartFile arquivo , Long id) throws IOException {
        Path caminhoPasta = Paths.get(PASTA_LOCAL);
        if (!Files.exists(caminhoPasta)) {
            Files.createDirectories(caminhoPasta);
        }
        
        String nomeDoArquivo = arquivo.getOriginalFilename();
        
        Path caminhoCompleto = caminhoPasta.resolve(nomeDoArquivo);
        Files.write(caminhoCompleto, arquivo.getBytes());

        jpaTreinador.salvandoFotoPerfil(nomeDoArquivo, id);

    }


}
