package com.api.Projeto_3.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.api.Projeto_3.dtos.interfaceCustomer.CustomerNome;
import com.api.Projeto_3.dtos.interfaceCustomer.ListDasAtleta;
import com.api.Projeto_3.dtos.interfaceCustomer.TreinadorCustomer;
import com.api.Projeto_3.repository.AtletaRespository;
import com.api.Projeto_3.repository.TreinadorRespository;

@Service
public class TreinadorService {
    private static final Path PASTA_LOCAL = Path.of("src/main/resources/static");

    @Autowired
    TreinadorRespository jpaTreinador;

    @Autowired
    AtletaRespository jpaAtleta;

    @Transactional(readOnly = true)
    public TreinadorCustomer buscarTreinadorPorId(String cpf) {
        TreinadorCustomer treinador = jpaTreinador.listTreinador(cpf);
        if (treinador == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treinador não encontrado");
        }

        return treinador;
    }

    @Transactional(readOnly = true)
    public CustomerNome buscarNomes(String cpf) {
        return jpaTreinador.buscarNomes(cpf);
    }

    @Transactional(readOnly = true)
    public List<String> mosntrando_atleta_nomes(Long treinadorId) {
        return jpaAtleta.listAtletasVinculados(treinadorId);
    }

    @Transactional(readOnly = true)
    public List<ListDasAtleta> listTab() {
        return jpaAtleta.atletListCus();
    }

    @Transactional(readOnly = true)
    public List<ListDasAtleta> listarAtletasVinculados(Long treinadorId) {
        return jpaAtleta.atletListVinculadosTreinador(treinadorId);
    }

    @Transactional
    public boolean desvincularAtleta(Long treinadorId, Long atletaId) {
        return jpaAtleta.desvincularTreinador(atletaId, treinadorId) > 0;
    }

    public void salvarImagemNaPasta(MultipartFile arquivo, Long treinadorId) throws IOException {
        Files.createDirectories(PASTA_LOCAL);

        String nomeDoArquivo = arquivo.getOriginalFilename();
        Path caminhoDoArquivo = PASTA_LOCAL.resolve(nomeDoArquivo);
        Files.write(caminhoDoArquivo, arquivo.getBytes());

        jpaTreinador.salvandoFotoPerfil(nomeDoArquivo, treinadorId);
    }
}
