package com.api.Projeto_3.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.StandardEnvironment;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.openai.spring.OpenAiAutoConfiguration;

class OllamaConfigTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withInitializer(context -> context.getEnvironment().getPropertySources()
                    .remove(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME))
            .withInitializer(new ConfigDataApplicationContextInitializer())
            .withConfiguration(AutoConfigurations.of(OpenAiAutoConfiguration.class))
            .withUserConfiguration(OllamaConfig.class, OllamaLifecycle.class)
            .withPropertyValues("app.ollama.start-local=false");

    @Test
    void criaApenasOModeloOllamaQuandoAChaveEstaAusente() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(ChatModel.class);
            assertThat(context.getBean(ChatModel.class)).isInstanceOf(OllamaChatModel.class);
            assertThat(context).doesNotHaveBean(OllamaLifecycle.class);
        });
    }

    @Test
    void criaModeloOpenAiQuandoAChaveEstaConfigurada() {
        runner.withPropertyValues("OPENAI_API_KEY=chave-ficticia").run(context -> {
            assertThat(context).hasSingleBean(OpenAiChatModel.class);
            assertThat(context.getBean(ChatModel.class)).isInstanceOf(OllamaChatModel.class);
        });
    }
}
