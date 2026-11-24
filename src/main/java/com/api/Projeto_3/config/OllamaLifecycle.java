
package com.api.Projeto_3.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
@ConditionalOnProperty(name = "app.ollama.start-local", havingValue = "true", matchIfMissing = true)
public class OllamaLifecycle {

    private Process processo;

    @PostConstruct
    public void iniciar() throws IOException {

        try {
            var conexao = (HttpURLConnection) URI
                    .create("http://localhost:11434/api/tags")
                    .toURL()
                    .openConnection();

            conexao.setConnectTimeout(1000);
            conexao.setReadTimeout(1000);

            if (conexao.getResponseCode() == 200) {
                conexao.disconnect();
                return;
            }

            conexao.disconnect();

        } catch (IOException e) {
           
        }

        processo = new ProcessBuilder("ollama", "serve")
                .inheritIO()
                .start();
    }

    @PreDestroy
    public void parar() {
        if (processo != null && processo.isAlive()) {
            processo.destroy();
        }
    }
}
