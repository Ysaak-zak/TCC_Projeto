#!/usr/bin/env bash
set -euo pipefail

cd -- "$(dirname -- "${BASH_SOURCE[0]}")"

for comando in ollama curl; do
    if ! command -v "$comando" >/dev/null 2>&1; then
        echo "Instale $comando antes de iniciar o projeto." >&2
        exit 1
    fi
done

export OLLAMA_HOST="127.0.0.1:11434"
export APP_OLLAMA_BASE_URL="http://127.0.0.1:11434"
export APP_OLLAMA_MODEL_NAME="${APP_OLLAMA_MODEL_NAME:-llama3.2}"
ollama_pid=""

limpar() {
    if [[ -n "$ollama_pid" ]]; then
        kill "$ollama_pid" 2>/dev/null || true
        wait "$ollama_pid" 2>/dev/null || true
    fi
}
trap limpar EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

api_pronta() {
    curl --fail --silent --max-time 2 "$APP_OLLAMA_BASE_URL/api/tags" >/dev/null 2>&1
}

if api_pronta; then
    echo "Ollama já está rodando. Usando o serviço existente."
else
    mkdir -p logs
    echo "Iniciando Ollama (log: logs/ollama.log)..."
    ollama serve >logs/ollama.log 2>&1 &
    ollama_pid=$!
    for ((tentativa = 0; tentativa < 30; tentativa++)); do
        if api_pronta; then
            break
        fi
        if ! kill -0 "$ollama_pid" 2>/dev/null; then
            echo "Ollama não iniciou. Confira logs/ollama.log." >&2
            exit 1
        fi
        sleep 1
    done
    if ! api_pronta; then
        echo "Ollama não respondeu. Confira logs/ollama.log." >&2
        exit 1
    fi
fi

if ! ollama show "$APP_OLLAMA_MODEL_NAME" >/dev/null 2>&1; then
    echo "Modelo ausente. Execute: ollama pull $APP_OLLAMA_MODEL_NAME" >&2
    exit 1
fi

echo "Iniciando projeto com o modelo $APP_OLLAMA_MODEL_NAME. Use Ctrl+C para parar."
./mvnw spring-boot:run "$@"
