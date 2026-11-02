package com.api.Projeto_3.model;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.junit.jupiter.api.Test;

class TreinamentoMappingTest {
    @Test
    void relacionamentosFormamUmMapeamentoValidoSemBanco() {
        var registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.boot.allow_jdbc_metadata_access", false)
                .build();
        try {
            var sources = new MetadataSources(registry);
            for (Class<?> entity : new Class<?>[] {
                    PerfisModelo.class, AtletaModelo.class, TreinadorModel.class,
                    MedicoModelo.class, MoradiaModel.class, AfiliacaoModelo.class,
                    RolesModel.class, SemanaModel.class, DiaModel.class, TreinamentoModel.class,
                    ExercicioModel.class }) {
                sources.addAnnotatedClass(entity);
            }
            ((MetadataImplementor) sources.buildMetadata()).validate();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
