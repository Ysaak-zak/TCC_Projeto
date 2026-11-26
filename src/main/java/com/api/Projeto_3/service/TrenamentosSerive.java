package com.api.Projeto_3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.Projeto_3.model.ExercicioModel;
import com.api.Projeto_3.dtos.DtosModelos.CriarExercicioDto;
import com.api.Projeto_3.dtos.DtosModelos.CriarTreinoDto;
import com.api.Projeto_3.model.DiaModel;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.TreinamentoModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.SemanaResponitory;
import com.api.Projeto_3.repository.TreinamentoRespository;

import jakarta.transaction.Transactional;

@Service
public class TrenamentosSerive {
    @Autowired
    SemanaResponitory snJpa;

    @Autowired
    TreinamentoRespository treinamentoRepository;

    @Transactional
    public void criarTreino(CriarTreinoDto forma, String cpf) {
        SemanaModel semana = buscarSemana(forma.getSemanaId(), cpf);
        DiaModel dia = buscarOuCriarDia(semana, forma.getDia());
        TreinamentoModel treino = montarTreino(forma, dia);

        if (forma.getExercicios() != null) {
            for (var item : forma.getExercicios()) {
                ExercicioModel exercicio = new ExercicioModel();
                exercicio.setNome(item.getNome().trim());
                exercicio.setDescricao(item.getDescricao());
                exercicio.setDuracacao(item.getDuracao());
                exercicio.setTreinamento_fk(treino);
                treino.getExercicios().add(exercicio);
            }
        }

        dia.getTreinamentos().add(treino);
        snJpa.save(semana);
    }

    @Transactional
    public java.util.List<TreinamentoModel> listarTreinosDaSemana(Long semanaId, String cpf) {
        
        return buscarSemana(semanaId, cpf).getDias().stream()
                .flatMap(dia -> dia.getTreinamentos().stream())
                .filter(treino -> !Boolean.TRUE.equals(treino.getCancelado()) && !treino.isConcluido())
                .toList();
    }

    @Transactional
    public void criarExercicio(CriarExercicioDto form, String cpf) {
        TreinamentoModel treino = treinamentoRepository.buscarDoTreinadorParaAtualizar(form.getTreinamentoId(), form.getSemanaId(), cpf)
                .orElseThrow(() -> new IllegalArgumentException( "Selecione um treinamento pertencente à semana escolhida."));
                
        ExercicioModel exercicio = new ExercicioModel();
        exercicio.setNome(form.getNome().trim());
        exercicio.setDescricao(form.getDescricao());
        exercicio.setDuracacao(form.getDuracao());
        exercicio.setTreinamento_fk(treino);
        treino.getExercicios().add(exercicio);
        treinamentoRepository.save(treino);
    }

    public void creSemana(SemanaModel semana) {
        snJpa.save(semana);
    }

    private SemanaModel buscarSemana(Long semanaId, String cpf) {
        for (SemanaModel semana : snJpa.listarPorTreinador(cpf)) {
            if (semana.getId().equals(semanaId)) {
                return semana;
            }
        }

        throw new IllegalArgumentException("Selecione uma semana de um atleta vinculado a você.");
    }

    private DiaModel buscarOuCriarDia(SemanaModel semana, DiasEnum nomeDia) {
        for (DiaModel dia : semana.getDias()) {
            if (dia.getNome() == nomeDia) {
                return dia;
            }
        }

        DiaModel dia = new DiaModel();
        dia.setNome(nomeDia);
        dia.setSemana_fk(semana);
        semana.adicionarDia(dia);
        return dia;
    }

    private TreinamentoModel montarTreino(CriarTreinoDto forma, DiaModel dia) {
        TreinamentoModel treino = new TreinamentoModel();
        treino.setNome(forma.getNome().trim());
        treino.setCategoria(forma.getCategoria().trim());
        treino.setDescricao(forma.getDescricao());
        treino.setHorarioInicio(forma.getHorarioInicio());
        treino.setHorarioFim(forma.getHorarioFim());
        treino.setDia_fk(dia);
        return treino;
    }
}
