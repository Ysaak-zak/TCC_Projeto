package com.api.Projeto_3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.Projeto_3.dtos.model.CriarTreinoDto;
import com.api.Projeto_3.dtos.model.CriarExercicioDto;
import com.api.Projeto_3.model.ExercicioModel;
import com.api.Projeto_3.model.DiaModel;
import com.api.Projeto_3.model.SemanaModel;
import com.api.Projeto_3.model.TreinamentoModel;
import com.api.Projeto_3.model.enums.DiasEnum;
import com.api.Projeto_3.repository.SemanaResponitory;

import jakarta.transaction.Transactional;

@Service
public class TrenamentosSerive {
    @Autowired
    SemanaResponitory snJpa;

    @Transactional
    public void criarTreino(CriarTreinoDto forma, String cpf) {
        SemanaModel semana = buscarSemana(forma.getSemanaId(), cpf);
        DiaModel dia = buscarOuCriarDia(semana, forma.getDia());
        TreinamentoModel treino = montarTreino(forma, dia);

        dia.getTreinamentos().add(treino);
        snJpa.save(semana);
    }

    @Transactional
    public java.util.List<TreinamentoModel> listarTreinosDaSemana(Long semanaId, String cpf) {
        if (semanaId == null) return java.util.List.of();
        return buscarSemana(semanaId, cpf).getDias().stream()
                .flatMap(dia -> dia.getTreinamentos().stream()).toList();
    }

    @Transactional
    public void criarExercicio(CriarExercicioDto form, String cpf) {
        SemanaModel semana = buscarSemana(form.getSemanaId(), cpf);
        TreinamentoModel treino = semana.getDias().stream()
                .flatMap(dia -> dia.getTreinamentos().stream())
                .filter(item -> item.getId().equals(form.getTreinamentoId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(
                        "Selecione um treinamento pertencente à semana escolhida."));
        ExercicioModel exercicio = new ExercicioModel();
        exercicio.setNome(form.getNome().trim());
        exercicio.setDescricao(form.getDescricao());
        exercicio.setDuracacao(form.getDuracao());
        exercicio.setTreinamento_fk(treino);
        treino.getExercicios().add(exercicio);
        snJpa.save(semana);
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
