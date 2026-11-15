package com.api.Projeto_3.model;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.junit.jupiter.api.Test;

class ExameUrinaMappingTest {
    @Test
    void exameUrinaPossuiMapeamentoValidoSemColunasDuplicadas() {
        var registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.boot.allow_jdbc_metadata_access", false)
                .build();
        try {
            var sources = new MetadataSources(registry);
            for (var entidade : new Class<?>[] {EmaxeUrina.class, AtletaModelo.class, PerfisModelo.class,
                    TreinadorModel.class, MedicoModelo.class, FichaMedicaModel.class, SemanaModel.class,
                    DiaModel.class, TreinamentoModel.class, ExercicioModel.class, AfiliacaoModelo.class,
                    MoradiaModel.class, RolesModel.class, ExameSangue.class, RecomendacaoAlimentar.class}) {
                sources.addAnnotatedClass(entidade);
            }
            var metadata = (MetadataImplementor) sources.buildMetadata();
            metadata.validate();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
