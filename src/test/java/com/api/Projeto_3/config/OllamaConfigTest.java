package com.api.Projeto_3.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

class OllamaConfigTest {

    @Test
    void criaChatModelComOsDoisClientesHttpNoClasspath() {
        try (var context = new AnnotationConfigApplicationContext(OllamaConfig.class)) {
            assertInstanceOf(OllamaChatModel.class, context.getBean(ChatModel.class));
        }
    }
}
