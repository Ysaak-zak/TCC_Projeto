package com.api.Projeto_3.repository;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;

class AgendaTreinamentoRepositoryImpl implements AgendaTreinamentoRepository {

    private final JdbcClient jdbc;

    AgendaTreinamentoRepositoryImpl(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<LinhaDaAgenda> listarDaSemanaDoAtleta(Long semanaId, String cpf) {
        return jdbc.sql("""
                SELECT d.nome AS dia,
                       t.id AS treino_id,
                       t.nome AS treino_nome,
                       t.categoria AS treino_categoria,
                       t.descricao AS treino_descricao,
                       t.horario_inicio,
                       t.horario_fim,
                       t.cancelado AS treino_cancelado,
                       t.concluido AS treino_concluido,
                       e.id AS exercicio_id,
                       e.nome AS exercicio_nome,
                       e.descricao AS exercicio_descricao,
                       e.duracacao AS exercicio_duracao,
                       e.concluido AS exercicio_concluido
                FROM treinamento t
                JOIN dia d ON d.id = t.dia_fk
                JOIN semana s ON s.id = d.semana_fk
                JOIN atletas a ON a.id = s.atleta_fk
                JOIN perfis_modelo p ON p.id = a.id
                LEFT JOIN exercicio e ON e.treinamento_fk = t.id
                WHERE s.id = :semanaId
                  AND p.cpf = :cpf
                ORDER BY d.nome, t.horario_inicio NULLS LAST, t.id, e.id
                """)
                .param("semanaId", semanaId)
                .param("cpf", cpf)
                .query(LinhaDaAgenda.class)
                .list();
    }
}
