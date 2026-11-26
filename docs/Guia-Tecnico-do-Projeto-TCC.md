# Guia técnico do Projeto TCC

O Projeto TCC reúne o acompanhamento de atletas, a organização de treinos e o registro de avaliações em uma aplicação web. Atletas consultam sua agenda e registram o progresso. Treinadores planejam atividades. Médicos acompanham atletas vinculados, registram exames e orientações alimentares e podem cancelar treinos.

O Spring Boot entrega as telas ao navegador, usa PostgreSQL para persistência e consulta o Ollama nas análises de exames. Na publicação, o GitHub Actions compila o JAR e envia os arquivos de execução para a VPS. A referência do código é a revisão `2783a82` da branch `producao`.

## Sumário

1. [Front-end](#capítulo-1-front-end)
2. [Back-end](#capítulo-2-back-end)
3. [Docker e entrega contínua na VPS](#capítulo-3-docker-e-entrega-contínua-na-vps)

## Capítulo 1 Front-end

### 1.1 Renderização e organização

O Thymeleaf renderiza o HTML no servidor. Um controller recebe a requisição, carrega os dados no `Model` do Spring MVC e retorna o nome da vista. Os arquivos em `src/main/resources/templates` usam atributos como `th:text` para exibição de dados, `th:each` para listas, `th:if` para condições e `th:action` para endereçar formulários.

O Spring Boot serve os arquivos de `src/main/resources/static` pelos caminhos `/css`, `/js` e `/img`. O Maven inclui templates e recursos estáticos no mesmo JAR. O deploy utiliza os arquivos versionados e, por isso, a VPS executa a aplicação sem uma etapa de compilação de front-end.

| Área | Templates e ações |
| --- | --- |
| `publicPlages` | `home.html` apresenta a aplicação. `Login.html` envia CPF e senha. `cadastroAtleta.html`, `cadastroMedico.html` e `cadastroTreinador.html` renderizam os formulários dos três perfis. |
| `atletaPages` | `dashAtleta.html` exibe indicadores e histórico. `treinos.html` mostra a agenda e recebe o progresso. `PerfilAtleta.html` exibe os dados do perfil. |
| `treinadorPages` | `dash.html` lista atletas disponíveis para vínculo. `listaAtletasVinculados.html` permite consultar e desfazer vínculos. `Perfil.html` exibe o perfil. Em `treimamento/`, `semana.html` cadastra semanas e `Treinamento.html` cadastra treinos e seus exercícios. |
| `medicoPages` | `vincularAtleta.html` é a tela inicial. `listaAtletasVinculados.html`, `PerfilMedico.html` e `treinamentosAtleta.html` cobrem acompanhamento e cancelamento. `alimentacao.html` mantém orientações. `TestUrina.html` e `TesSangue.html` recebem exames. `result.html` e `resultadoSangue.html` mostram os resultados. |

Atleta e treinador reutilizam seus fragmentos `navegacao.html` com `th:replace`. Nas pastas de treino e da área médica também há arquivos como `Exercicio.html`, `atletasVinculados.html` e `resultSangue.html`. A rota GET de exercícios redireciona para a seção `#exercicios` da tela de treinos. As vistas médicas em uso são as retornadas por `MedicoController` e `ChatMedicoExame`. [Templates Thymeleaf](../src/main/resources/templates/)

### 1.2 Jornadas por perfil

1. **Visitante.** A página `/home` dá acesso ao login e aos três cadastros. O formulário de login envia `cpf` e `password` por POST para `/login`. Após a autenticação, a aplicação abre a área correspondente ao perfil.
2. **Atleta.** O painel `/atleta` filtra os dados por `semanaId`. O template apresenta tempo programado, média diária, satisfação quando registrada, gráficos de atividade e categorias e o histórico de treinos. Em `/atleta/treinos`, o atleta escolhe a semana, consulta os dias e envia a conclusão dos exercícios selecionados ou do treino.
3. **Treinador.** O painel `/treinador` apresenta a lista de atletas disponíveis. Após vincular um atleta, o treinador cria uma semana para ele em `/treinador/treinos/semanas` e cadastra o treino em `/treinador/treinos`. Pode inserir exercícios no formulário de treino ou acrescentá-los depois pela seção de exercícios.
4. **Médico.** O painel `/medico` permite vincular atletas. A partir da lista de vinculados, o médico consulta treinos, registra um motivo de cancelamento e acessa alimentação e exames. Os relatórios mostram os dados informados e o resultado textual do processamento. O botão de impressão abre o diálogo do navegador.

Formulários de alteração usam POST e enviam o token CSRF, que o Spring Security compara com o valor esperado pela sessão. Quando o controller valida um formulário com `BindingResult`, o template pode exibir os erros junto aos campos. Redirecionamentos e mensagens temporárias informam o resultado de ações como cadastro de treino e registro de progresso.

### 1.3 Estilos e comportamento no navegador

| Recurso | Uso no projeto |
| --- | --- |
| `static/css/atleta-tailwind.css` | CSS do atleta compilado com Tailwind CSS 4.2.1 e versionado. O arquivo de entrada `src/main/tailwind/atleta.css` aponta para os templates de `atletaPages`. |
| `static/js/vendor/tailwindcss-3.4.17.js` | Distribuição local do Tailwind para páginas públicas, médicas e do treinador. A configuração de cores e tipografia está em `tailwind-theme.js` e, para telas de treino do treinador, em `tailwind-treinador-treinos.js`. |
| `treinador-dashboard.js` | Filtra, pelo nome e CPF, as linhas de atletas que o servidor já enviou à página. |
| `treinador-treinamento.js` | Adiciona e remove campos de exercícios no formulário e atualiza os índices que o Spring usa para vincular a lista. |
| `treinador-exercicio.js` e `acoes-medico.js` | Em `Exercicio.html`, o primeiro limpa o treino selecionado ao trocar a semana. O segundo trata impressão, navegação por seletor e confirmações em formulários médicos. |

Os gráficos do painel atual do atleta usam os valores de `DashDto` no HTML e estilos CSS, inclusive as proporções das barras. Recursos como imagens e ícones locais ficam em `static/img`. Alguns templates também referenciam Google Fonts ou imagens externas para a apresentação. [Recursos estáticos](../src/main/resources/static/)

### 1.4 Alterar e conferir uma tela

Para modificar uma tela, localize o controller pela rota e confira os atributos que ele fornece ao template. Ao alterar as classes Tailwind da área do atleta, regenere o CSS versionado na raiz do repositório. Para essa tarefa, instale localmente Node.js, npm e o CLI da mesma versão usada no CSS. [Tailwind CLI](https://tailwindcss.com/docs/installation/tailwind-cli)

```bash
npm install --no-save --package-lock=false \
  tailwindcss@4.2.1 @tailwindcss/cli@4.2.1
npx @tailwindcss/cli \
  -i src/main/tailwind/atleta.css \
  -o src/main/resources/static/css/atleta-tailwind.css \
  --minify
```

Com a aplicação em execução, abra `http://127.0.0.1:8080/home`, entre com o perfil correspondente e confira a navegação, o formulário e o retorno da ação. Verifique o layout em largura de celular e desktop. Use dados fictícios nas verificações de cadastro, vínculo e exames.

### 1.5 Código das telas e dos formulários

#### 1.5.1 Formulário de login

```html
<form th:action="@{/login}" method="post" class="flex flex-col gap-5">
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">

    <div>
        <label for="cpf" class="mb-2 block text-sm font-semibold text-slate-800">CPF</label>
        <input id="cpf" name="cpf" type="text" inputmode="numeric" autocomplete="username"
               placeholder="000.000.000-00" required
               class="w-full rounded-lg border border-slate-300 bg-slate-50 px-4 py-3 focus:border-sky-700 focus:outline-sky-700">
    </div>

    <div>
        <label for="password" class="mb-2 block text-sm font-semibold text-slate-800">Senha</label>
        <input id="password" name="password" type="password" autocomplete="current-password" required
               class="w-full rounded-lg border border-slate-300 bg-slate-50 px-4 py-3 focus:border-sky-700 focus:outline-sky-700">
    </div>

    <button type="submit" class="rounded-lg bg-slate-800 px-4 py-3 font-bold text-white hover:bg-sky-800 focus-visible:outline-sky-700">
        Entrar
    </button>
</form>
```

O Thymeleaf resolve `th:action="@{/login}"` para o endereço que recebe o POST. O campo oculto usa o nome e o valor do token CSRF fornecido pelo Spring Security. Os atributos `name="cpf"` e `name="password"` devem corresponder aos parâmetros definidos na configuração de login da seção 2.8. O `required` fornece uma validação de preenchimento no navegador. O servidor autentica o CPF e a senha enviados. [Login.html linhas 26–45](../src/main/resources/templates/publicPlages/Login.html#L26-L45)

#### 1.5.2 Navegação e gráfico do atleta

```html
    <link rel="stylesheet" th:href="@{/css/atleta-tailwind.css}">
</head>
<body class="flex min-h-screen flex-col font-sans xl:flex-row bg-black text-white">
    <div th:replace="~{atletaPages/navegacao :: menu('painel', ${dash.nomeAtleta})}"></div>
```

O `<link>` usa o CSS que acompanha o JAR. `th:replace` inclui o fragmento `menu` da área do atleta e passa a opção ativa `painel` e o nome do atleta. O controller prepara o objeto `dash` antes de renderizar a página. [dashAtleta.html linhas 8–11](../src/main/resources/templates/atletaPages/dashAtleta.html#L8-L11)

```css
@import "tailwindcss" source(none);
@source "../resources/templates/atletaPages";
```

`source(none)` desliga a detecção automática de arquivos pelo Tailwind. `@source` aponta explicitamente para os templates do atleta. O comando da seção 1.4 gera o CSS lido pelo `<link>` da página. [atleta.css linhas 1–2](../src/main/tailwind/atleta.css#L1-L2)

```html
<li class="relative flex h-40 min-w-0 flex-1 flex-col items-center justify-end" th:each="dia : ${dash.atividadeSemanal}"
    th:with="proporcao=${1.0 * dia.y / maiorTempo}, parteConcluida=${dia.y > 0 ? 1.0 * dia.yConcluido / dia.y : 0}"
    th:style="|--proporcao: ${proporcao}; --parte-concluida: ${parteConcluida}|"
    th:title="|${dia.yConcluido} de ${dia.y} min concluídos|">
    <span class="order-1 h-6 text-xs text-gray-300 sm:text-sm" th:text="${dia.x}">Seg</span>
```

O `th:each` gera uma barra por ponto de `dash.atividadeSemanal`. `dia.y` guarda os minutos programados e `dia.yConcluido` guarda os minutos de treinos concluídos. A primeira proporção compara o dia com o maior valor da semana. A segunda representa a parte concluída da barra e fica em zero se o dia tiver zero minutos programados. `th:style` passa as duas proporções ao CSS. [dashAtleta.html linhas 66–70](../src/main/resources/templates/atletaPages/dashAtleta.html#L66-L70)

#### 1.5.3 Registro de exercícios e de treino

```html
<form th:action="@{/atleta/treinos/{id}(id=${treino.id})}" method="post" class="flex flex-col gap-3">
    <fieldset th:unless="${#lists.isEmpty(treino.exercicios)}"
              class="flex min-w-0 flex-col divide-y divide-gray-800 border-y border-gray-800">
        <legend class="sr-only" th:text="|Exercícios de ${treino.nome}|">Exercícios</legend>
        <label th:each="exercicio : ${treino.exercicios}"
               class="flex min-h-11 items-center gap-3 py-2 has-[:enabled]:cursor-pointer">
            <input type="checkbox" name="exercicioIds" th:value="${exercicio.id}"
                   th:checked="${exercicio.concluido}" th:disabled="${exercicio.concluido or treino.concluido or treino.cancelado}"
                   class="size-5 shrink-0 accent-emerald-500">
```

O Thymeleaf insere `treino.id` no endereço do POST. Cada checkbox envia o ID do exercício com o nome `exercicioIds`. O template marca os exercícios concluídos e desabilita o controle quando o exercício ou o treino já foi concluído ou quando o treino foi cancelado. [treinos.html linhas 67–75](../src/main/resources/templates/atletaPages/treinos.html#L67-L75)

```html
    <div th:unless="${treino.cancelado or treino.concluido}" class="flex flex-wrap items-center gap-2">
        <button th:if="${treino.totalExerciciosConcluidos < #lists.size(treino.exercicios)}"
                type="submit" name="acao" value="CONCLUIR_EXERCICIOS"
                class="min-h-11 cursor-pointer rounded-md bg-purple-700 px-3 text-sm font-bold hover:bg-purple-800">
            Salvar exercícios marcados
        </button>
        <button type="submit" name="acao" value="CONCLUIR_TREINO" th:disabled="${!treino.podeConcluir}"
                class="min-h-11 cursor-pointer rounded-md bg-emerald-700 px-3 text-sm font-bold
                    enabled:hover:bg-emerald-800 disabled:cursor-not-allowed disabled:bg-gray-800 disabled:text-gray-300">
            Concluir treino
        </button>
    </div>
</form>
```

Os botões usam o mesmo parâmetro `acao` com valores diferentes. `CONCLUIR_EXERCICIOS` envia os IDs marcados e `CONCLUIR_TREINO` solicita a conclusão do treino. `treino.podeConcluir` vem de um método do DTO da agenda. O formulário, o DTO e o `switch` do serviço usam os mesmos valores. [treinos.html linhas 86–98](../src/main/resources/templates/atletaPages/treinos.html#L86-L98)

#### 1.5.4 Campos de exercícios no formulário do treinador

```javascript
const listaExercicios = document.getElementById('lista-exercicios');
const botaoAdicionar = document.getElementById('adicionar-exercicio');
const modeloExercicio = document.getElementById('modelo-exercicio');
const avisoSemExercicios = document.getElementById('sem-exercicios');
const limiteExercicios = 50;

function atualizarExercicios() {
    const exercicios = listaExercicios.querySelectorAll('.exercicio-item');

    exercicios.forEach((exercicio, indice) => {
        for (const campo of exercicio.querySelectorAll('input[data-campo]')) {
            const nome = campo.dataset.campo;
            const rotulo = exercicio.querySelector(`label[data-campo="${nome}"]`);

            campo.name = `exercicios[${indice}].${nome}`;
            campo.id = `exercicio-${indice}-${nome}`;
            rotulo.htmlFor = campo.id;
        }
    });

    avisoSemExercicios.hidden = exercicios.length > 0;
    botaoAdicionar.disabled = exercicios.length >= limiteExercicios;
}
```

`atualizarExercicios()` percorre as linhas na ordem em que aparecem na tela. A partir de `data-campo`, constrói nomes como `exercicios[0].nome`, que o Spring MVC usa para preencher a lista do DTO. Também atualiza o `id` de cada campo e o `htmlFor` do rótulo correspondente. O método exibe o aviso quando não há exercícios e aplica o limite de 50 na interface. [treinador-treinamento.js linhas 1–23](../src/main/resources/static/js/treinador-treinamento.js#L1-L23)

```javascript
botaoAdicionar.addEventListener('click', () => {
    const novoExercicio = modeloExercicio.content.firstElementChild.cloneNode(true);
    listaExercicios.append(novoExercicio);
    atualizarExercicios();
    novoExercicio.querySelector('input').focus();
});

listaExercicios.addEventListener('click', (evento) => {
    const botaoRemover = evento.target.closest('[data-remover-exercicio]');
    if (botaoRemover) {
        botaoRemover.closest('.exercicio-item').remove();
        atualizarExercicios();
    }
});

atualizarExercicios();
```

O botão de adicionar clona um elemento do `<template>`, insere-o na lista, renumera os campos e leva o foco ao primeiro `input`. O listener da lista identifica cliques em botões de remoção, inclusive nas linhas adicionadas depois. Ao remover uma linha, refaz os índices para o próximo envio. A última chamada inicializa os índices quando a página abre. [treinador-treinamento.js linhas 25–40](../src/main/resources/static/js/treinador-treinamento.js#L25-L40)

#### 1.5.5 Ações no painel médico

```javascript
for (const botao of document.querySelectorAll('[data-imprimir]')) {
    botao.addEventListener('click', () => window.print());
}

for (const seletor of document.querySelectorAll('select[data-navegar-selecao]')) {
    seletor.addEventListener('change', () => {
        if (seletor.value) {
            window.location.assign(seletor.value);
        }
    });
}

for (const formulario of document.querySelectorAll('form[data-confirmacao]')) {
    formulario.addEventListener('submit', (evento) => {
        if (!window.confirm(formulario.dataset.confirmacao)) {
            evento.preventDefault();
        }
    });
}
```

O primeiro laço liga os botões `data-imprimir` ao diálogo de impressão do navegador. O segundo usa o valor do item selecionado como endereço de navegação. O terceiro lê o texto de `data-confirmacao` e usa `preventDefault()` quando o usuário recusa a ação. O controller recebe o formulário quando o navegador conclui o envio. [acoes-medico.js linhas 1–19](../src/main/resources/static/js/acoes-medico.js#L1-L19)

## Capítulo 2 Back-end

### 2.1 Tecnologias e fluxo da requisição

`Projeto3Application` inicia o Spring Boot 4.0.3. O Spring MVC trata as rotas e renderiza vistas Thymeleaf, o Spring Security autentica e autoriza as requisições e o Spring Data JPA usa o Hibernate para persistir os dados. O driver JDBC conecta ao PostgreSQL e os componentes LangChain4j configuram o cliente do Ollama. O `pom.xml` define Java 17 como alvo de compilação. O build do projeto e o ambiente de produção descritos aqui usam JDK ou JRE 21 conforme a etapa. O Maven Wrapper do projeto aponta para Maven 3.9.12. [pom.xml](../pom.xml) · [Maven Wrapper](../.mvn/wrapper/maven-wrapper.properties)

```text
Navegador → Spring Security → Controller → Service → Repository
                                    ↓          ↓         ↓
                              Thymeleaf     Ollama   PostgreSQL
```

O controller recebe parâmetros e formulários, usa DTOs para organizar os dados e aciona os serviços. Os serviços executam a lógica de negócio e controlam as transações. Os repositórios recuperam ou persistem entidades e projeções. O controller pode devolver uma vista com o `Model` preenchido ou um redirecionamento. A interação com o Ollama ocorre nos serviços de exames.

| Pacote em `com.api.Projeto_3` | Responsabilidade |
| --- | --- |
| `Controller` | Rotas HTTP, dados da vista, vinculação de formulários e redirecionamentos. |
| `service` | Cadastro e autenticação, agenda, progresso, vínculos, exames e alimentação. |
| `repository` | Persistência JPA, consultas filtradas, projeções e consulta JDBC da agenda. |
| `model` e `model.enums` | Entidades, relacionamentos e valores de domínio persistidos. |
| `dtos` | Formulários, modelos de leitura, projeções e resposta estruturada de exames. |
| `config` | Segurança, carga inicial de permissões e conexão com o Ollama. |

### 2.2 Contas, autenticação e permissões

`WebSecurityConfig` disponibiliza páginas públicas, cadastros, login e arquivos estáticos. A regra de acesso às áreas `/atleta/**`, `/medico/**` e `/treinador/**` usa, respectivamente, `ROLE_ATLETA`, `ROLE_MEDICO` e `ROLE_TREINADOR`. As entradas `/atleta`, `/medico` e `/treinador` seguem as mesmas permissões. No login por formulário, o sistema usa CPF e senha. Após autenticar, o Spring Security redireciona o usuário para a área do perfil. O logout padrão redireciona para `/home`.

`PerfilService` cadastra os três tipos de perfil com senha codificada em BCrypt. No login, `PerfilRepository` procura o CPF cadastrado. Se a primeira consulta falhar, uma consulta compara apenas os dígitos do CPF. O serviço normaliza a autoridade existente e entrega ao Spring Security um `UserDetails` com a senha codificada e a permissão.

`RoleDataInitializer` executa na inicialização. Em uma transação, `RoleRepository` bloqueia a tabela `roles_tb` durante a verificação e cria as autoridades que estiverem ausentes. `WebSecurityConfig` mantém proteção CSRF para os POSTs de cadastro, progresso, vínculo, alimentação e exames e tem uma exceção específica configurada para `/medico/chat/urina`. Os POSTs que processam os exames nas telas médicas usam os caminhos da seção 2.3. [Configuração](../src/main/java/com/api/Projeto_3/config/)

### 2.3 Controllers e rotas

| Controller | Rotas e operações |
| --- | --- |
| `PublicController` | GET `/` redireciona para `/home` após passar pela regra geral de autenticação. GET `/home` e GET `/login` devolvem as vistas públicas. O POST de login é tratado pelo Spring Security. |
| `AtletaController` | GET `/cadastro/atleta` abre o cadastro. POST `/cadastro/atleta/save` grava o perfil. GET `/atleta/perfil` exibe a conta autenticada. |
| `DashController` | GET `/atleta` usa o CPF da autenticação e o parâmetro opcional `semanaId` para carregar o painel. |
| `TreinosAtletaController` | GET `/atleta/treinos` carrega a agenda. POST `/atleta/treinos/{treinoId}` registra a conclusão e redireciona para o treino na página. |
| `TreinadorController` | GET `/cadastro/treinador` e POST `/cadastro/treinador/save` atendem o cadastro e aceitam também uma variante com ID numérico no caminho. GET `/treinador`, `/treinador/perfil` e `/treinador/atletas-vinculados` atendem a área autenticada. O POST de vínculo recebe os IDs do treinador e do atleta. O POST `/treinador/atletas/{atletaId}/desvincular` encerra o vínculo. |
| `TreinamentoController` | GET e POST em `/treinador/treinos` exibem e registram treinos. GET e POST no sufixo `/semanas` exibem e registram semanas. No sufixo `/exercicios`, GET redireciona para a seção de exercícios e POST acrescenta um exercício. |
| `MedicoController` | GET `/cadastro/medico` e POST `/cadastro/medico/save` atendem o cadastro e aceitam uma variante com ID numérico no caminho. A área autenticada usa GET `/medico`, `/medico/perfil` e `/medico/atletas-vinculados`. Também atende os formulários de exames, o vínculo, o desvínculo, a consulta e o cancelamento de treinos. |
| `AlimentoController` | GET `/medico/alimentacao` seleciona um atleta vinculado. GET e POST `/medico/alimentacao/{atletaId}` exibem o histórico e recebem uma recomendação alimentar. |
| `ChatMedicoExame` | POST `/medico/exame-urina/salvar` ou `/medico/exame/urina` processa urina. POST `/medico/exame-sangue/salvar` processa sangue. GET `/medico/exame-urina/resultado` e GET `/medico/exame-sangue/resultado` devolvem as vistas de relatório. |

Os formulários médicos abrem em GET `/medico/exame-urina` e GET `/medico/exame-sangue`. GET `/medico/atletas/{atletaId}/treinamentos` consulta treinos de um atleta vinculado. O cancelamento usa POST nesse caminho com o sufixo `/{treinamentoId}/cancelar`. As rotas de vínculo são POST `/medico/{medicoId}/vincular-atleta/{atletaId}` e POST `/treinador/{treinadorId}/vincular-atleta/{atletaId}`. O médico encerra o vínculo por POST `/medico/atletas/{atletaId}/desvincular`. [Controllers](../src/main/java/com/api/Projeto_3/Controller/)

### 2.4 Serviços e regras de negócio

| Serviço | Comportamento |
| --- | --- |
| `PerfilService` | Localiza a conta para autenticação, associa a autoridade e persiste os perfis com moradia e dados familiares. |
| `AtletaService` | Procura atletas pelo CPF, efetua vínculos e devolve contagens de atletas vinculados e disponíveis. |
| `TreinadorService` | Consulta o treinador e seus atletas por projeções, lista candidatos a vínculo e trata o desvínculo. |
| `MedicoService` | Consulta o médico, os vínculos e os treinos de atletas acompanhados. No cancelamento, registra motivo, data e médico responsável. O motivo tem até 255 caracteres. |
| `SemanaService` | Oferece o cadastro de semana a partir de `SemanaDtos`. O controller atual de treinos usa `TrenamentosSerive.creSemana` para gravar a semana do atleta selecionado. |
| `TrenamentosSerive` | Valida o vínculo com o treinador, localiza ou cria o dia da semana e associa o treino e os exercícios. Ao adicionar um exercício posteriormente, verifica se o treino pertence à semana escolhida e se ainda está ativo. |
| `TreinosAtletaService` | Monta a agenda agrupando dias, treinos e exercícios. Atualiza a conclusão dos exercícios selecionados ou a conclusão do treino do atleta autenticado. |
| `DashService` | Calcula minutos programados e concluídos por dia, totais por categoria, média diária e satisfação quando disponível. Mantém treinos cancelados no histórico e os exclui dos indicadores. |
| `AlimentoService` | Confere o vínculo médico, carrega o histórico e grava a orientação, sua data inicial e a data de revisão. A revisão informada deve ser igual ou posterior ao início. |
| `UrinaExamesService`, `SangueTest` e `ProcessamentoChatService` | Persistem os dados de exames, chamam o Ollama e organizam a análise enviada à interface. |

No fluxo de treino, o treinador escolhe um atleta vinculado, cria uma semana e registra um treino em um dia. O atleta consulta a agenda, cuja busca usa o CPF autenticado. As atualizações de progresso e os cancelamentos usam transações e consultas com bloqueio de linha para o treino selecionado. O painel calcula os indicadores cada vez que carrega os dados da semana.

`TreinadorService` também contém `salvarImagemNaPasta`, que escreve em `src/main/resources/static` e atualiza a referência da foto no banco. Os controllers atuais concentram as operações de perfil na consulta e no cadastro. O volume Docker de uploads usa o caminho `/app/uploads`. [Serviços](../src/main/java/com/api/Projeto_3/service/)

### 2.5 Modelo de dados e repositórios

| Entidades | Relações e dados |
| --- | --- |
| `PerfisModelo`, `AtletaModelo`, `MedicoModelo` e `TreinadorModel` | Os perfis especializados estendem a entidade base por herança JPA `JOINED`. O perfil tem CPF único, senha, contatos e uma autoridade. O atleta pode ter vínculo com um treinador e um médico. |
| `RolesModel`, `AfiliacaoModelo` e `MoradiaModel` | Guardam a autoridade, os dados familiares e o endereço associados ao perfil. |
| `SemanaModel`, `DiaModel`, `TreinamentoModel` e `ExercicioModel` | Uma semana pertence a um atleta, contém dias e cada dia contém treinos. O treino guarda categoria, horários, satisfação e estados de conclusão e cancelamento. Seus exercícios têm descrição, duração e conclusão. |
| `FichaMedicaModel`, `EmaxeUrina`, `ExameSangue` e `SangueSubs` | A ficha modela o relacionamento médico do atleta com registros de urina. `EmaxeUrina` também tem referência ao atleta, usada no cadastro atual. `ExameSangue` registra o ID do atleta, os dados do exame e a análise. Cada substância pesquisada pode armazenar triagem e confirmação separadamente. |
| `RecomendacaoAlimentar` | Relaciona uma orientação a um médico e um atleta. Guarda refeição, objetivo, texto da orientação e datas de início e revisão. |

Os enums de domínio representam autoridades, dias da semana, UF, gênero, tipo sanguíneo e campos de exames. Os DTOs têm enums próprios para valores recebidos em formulários, como as opções da triagem e `AcaoProgressoTreino`. [Entidades e enums](../src/main/java/com/api/Projeto_3/model/)

| Repositórios | Uso |
| --- | --- |
| `PerfilRepository` e `RoleRepository` | Buscam CPF com ou sem pontuação e suportam a inicialização das autoridades. |
| `AtletaRespository`, `TreinadorRespository` e `MedicoRespository` | Consultam perfis, vínculos e dados de listas. O repositório de atletas atualiza os vínculos com médico e treinador. |
| `DashRepository` e `SemanaResponitory` | Consultam as semanas do atleta e as semanas dos atletas associados ao treinador. |
| `TreinamentoRespository` e `AgendaTreinamentoRepository` | Buscam treinos pelas relações de médico, atleta e treinador. A implementação `AgendaTreinamentoRepositoryImpl` usa `JdbcClient` para projetar as linhas da agenda, inclusive treinos sem exercício. |
| `UrinaRespository`, `SangueRespository` e `RecomendacaoAlimentarRepository` | Persistem os exames e as orientações alimentares. O último consulta o histórico por médico e atleta. |

O Hibernate atualiza o esquema na inicialização com `spring.jpa.hibernate.ddl-auto=update`. O PostgreSQL mantém o estado gravado entre execuções. A seção 3.8 contém o procedimento de backup usado para proteger esse estado antes de uma atualização. [Repositórios](../src/main/java/com/api/Projeto_3/repository/) · [Configuração do banco](../src/main/resources/application.properties)

### 2.6 DTOs e integração com o Ollama

| Grupo | Componentes principais |
| --- | --- |
| Cadastro e conta | `PerfilsDtos`, `AfiliacaoDtos`, `MoradiaDto` e `RoleDtos`. |
| Criação e progresso de treinos | `CriarSemanaDto`, `CriarTreinoDto`, `CriarExercicioDto`, `ExercicioDoTreinoDto`, `SemanaDtos`, `TreinoAtletaProgressoDto` e `ResultadoProgressoTreinoDto`. |
| Agenda e indicadores | `TreinosAtletaDto`, `SemanaAgendaDto`, `DiaAgendaDto`, `TreinoAgendaDto`, `ExercicioAgendaDto`, `DashDto`, `DashPontoDto` e `DashTreinoDto`. |
| Exames e alimentação | `ExameUrinaDtos`, `ExameSagueDtos`, `SangueSubsDtos`, `RecomendacaoAlimentarDto` e o tipo adicional `ExmeSagueDtos`. As rotas atuais de sangue recebem `ExameSagueDtos`. |
| Projeções e análise | `TreinadorCustomer`, `MedicoCustomer`, `CustomerNome` e `ListDasAtleta` em `interfaceCustomer`. `ProcessamentoChat` e `AlertaTriagemDto` em `chat`. |

O atendimento médico começa pelo formulário Thymeleaf. `ChatMedicoExame` encaminha o DTO ao serviço e usa um atributo temporário para transportar o resultado ao GET do relatório. No exame de urina, o serviço persiste os dados e usa a análise no resultado da página. No exame de sangue, o serviço salva o registro e as substâncias, consulta o modelo e persiste também o texto da análise.

`OllamaConfig` registra um `ChatModel` primário do LangChain4j com cliente HTTP do JDK e limite padrão de 180 segundos por chamada. O modelo recebe dados do exame e responde com um resumo, pontos de atenção e recomendações. `ProcessamentoChatService` valida a estrutura JSON e prepara o `ProcessamentoChat` para a tela. A análise é parte de uma simulação acadêmica de triagem. [DTOs](../src/main/java/com/api/Projeto_3/dtos/) · [OllamaConfig.java](../src/main/java/com/api/Projeto_3/config/OllamaConfig.java)

### 2.7 Configuração e execução no host

Com PostgreSQL acessível e Java 21 instalado, configure as variáveis de conexão antes de iniciar o Spring. `SPRING_DATASOURCE_URL` tem o padrão `jdbc:postgresql://localhost:5432/PROJETO_TCC`, `SPRING_DATASOURCE_USERNAME` tem o padrão `postgres` e `SPRING_DATASOURCE_PASSWORD` recebe a senha do banco. `SERVER_PORT` usa 8080 por padrão.

`APP_OLLAMA_BASE_URL` indica a URL do Ollama. `APP_OLLAMA_MODEL_NAME` seleciona o modelo. No ambiente local, `OllamaLifecycle` pode iniciar `ollama serve` quando o serviço ainda está indisponível. O Compose define `APP_OLLAMA_START_LOCAL=false` porque usa um serviço próprio para o Ollama. O script `iniciar.sh` verifica o executável e a API, exige que o modelo escolhido já esteja baixado e inicia o Maven Wrapper.

Com o Ollama acessível em `localhost:11434`, baixe o modelo e execute o script na raiz do projeto. Informe a URL e o usuário reais do seu PostgreSQL. Digite a senha quando `read` aguardar a entrada.

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/PROJETO_TCC
export SPRING_DATASOURCE_USERNAME=postgres
read -rsp 'Digite a senha do PostgreSQL ' SPRING_DATASOURCE_PASSWORD
printf '\n'
export SPRING_DATASOURCE_PASSWORD
ollama pull qwen2.5:1.5b
APP_OLLAMA_MODEL_NAME=qwen2.5:1.5b bash iniciar.sh
```

Para empacotar após alterar o código, execute `./mvnw -B -ntp clean package`. O JAR executável do Spring Boot é criado em `target/`. Para executar os três serviços juntos com Docker, siga a seção 3.2. [application.properties](../src/main/resources/application.properties) · [iniciar.sh](../iniciar.sh)

### 2.8 Código de inicialização, autenticação e perfis

#### 2.8.1 Ponto de entrada da aplicação

```java
@SpringBootApplication
public class Projeto3Application {

    public static void main(String[] args) {
        SpringApplication.run(Projeto3Application.class, args);
    }

}
```

A anotação `@SpringBootApplication` habilita a configuração automática e a busca de componentes no pacote da aplicação. `main` passa a classe de entrada e os argumentos do processo para `SpringApplication.run`. A partir daí, o Spring inicializa componentes como os controllers, os serviços, os repositórios e os beans de configuração. [Projeto3Application.java linhas 6–13](../src/main/java/com/api/Projeto_3/Projeto3Application.java#L6-L13)

#### 2.8.2 Criação das permissões na inicialização

```java
@Override
@Transactional
public void run(String... args) {
    roleRepository.bloquearCriacaoDeRoles();
    for (RoleAuthority authority : RoleAuthority.values()) {
        String nomeRole = authority.name();
        if (!roleRepository.existsByAuthority(nomeRole)) {
            RolesModel role = new RolesModel();
            role.setAuthority(nomeRole);
            roleRepository.save(role);
        }
    }
}
```

`run` é chamado porque a classe implementa `CommandLineRunner`. A transação começa antes de `bloquearCriacaoDeRoles()`. O laço consulta pelo nome de cada valor de `RoleAuthority` e grava somente as permissões ausentes. Isso permite iniciar novamente com as mesmas permissões cadastradas. [RoleDataInitializer.java linhas 20–32](../src/main/java/com/api/Projeto_3/config/RoleDataInitializer.java#L20-L32)

```java
@Modifying
@Query(value = "LOCK TABLE roles_tb IN SHARE ROW EXCLUSIVE MODE", nativeQuery = true)
void bloquearCriacaoDeRoles();
```

`bloquearCriacaoDeRoles()` emite SQL nativo no PostgreSQL. O bloqueio da tabela permanece até o fim da transação e coordena inicializações concorrentes que tentariam inserir a mesma autoridade. [RoleRepository.java linhas 19–21](../src/main/java/com/api/Projeto_3/repository/RoleRepository.java#L19-L21)

#### 2.8.3 Regras e processamento do login

```java
http
.authorizeHttpRequests((req) -> req

    .requestMatchers("/home", "/cadastro/**", "/favicon.ico", "/error").permitAll()
    .requestMatchers("/css/**", "/img/**", "/js/**", "/iconsTrei/**").permitAll()
    .requestMatchers("/login").permitAll()

    .requestMatchers("/medico/chat/urina").permitAll()

    .requestMatchers("/atleta", "/atleta/**").hasRole("ATLETA")
    .requestMatchers("/medico", "/medico/**").hasRole("MEDICO")
    .requestMatchers("/treinador", "/treinador/**").hasRole("TREINADOR")

    .anyRequest().authenticated()
)

.csrf(csrf -> csrf
    .ignoringRequestMatchers("/medico/chat/urina")
)

.formLogin(form -> form
    .loginPage("/login")
    .loginProcessingUrl("/login")
    .usernameParameter("cpf")
    .passwordParameter("password")
```

Nesse trecho de `secuguraFilter`, o Spring avalia as regras de rota na ordem declarada. `permitAll()` libera as entradas públicas e os recursos estáticos. `hasRole("ATLETA")` verifica a autoridade `ROLE_ATLETA`, com o mesmo padrão para médico e treinador. A exceção configurada em `csrf` é a de `/medico/chat/urina`. `usernameParameter` e `passwordParameter` ligam o filtro aos nomes enviados em `Login.html`. [WebSecurityConfig.java linhas 20–44](../src/main/java/com/api/Projeto_3/config/WebSecurityConfig.java#L20-L44)

```java
.successHandler((req, res, aut) -> {

    String autRoles = aut.getAuthorities()
        .iterator().next().getAuthority();

    switch (autRoles) {
        case "ROLE_ATLETA":
            res.sendRedirect("/atleta");
            break;

        case "ROLE_MEDICO":
            res.sendRedirect("/medico");
            break;

        case "ROLE_TREINADOR":
            res.sendRedirect("/treinador");
            break;

        default:
            res.sendRedirect("/home");
            break;
    }
})
```

O `successHandler` é chamado depois da autenticação. Ele lê a autoridade do usuário autenticado e escolhe o endereço de destino. O `default` retorna à página inicial. No restante do método, `logoutSuccessUrl` define a saída para `/home` e `http.build()` entrega a cadeia de filtros ao Spring. [WebSecurityConfig.java linhas 45–67](../src/main/java/com/api/Projeto_3/config/WebSecurityConfig.java#L45-L67)

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

`passwordEncoder()` fornece o codificador BCrypt usado no cadastro e na checagem das senhas durante a autenticação. [WebSecurityConfig.java linhas 79–82](../src/main/java/com/api/Projeto_3/config/WebSecurityConfig.java#L79-L82)

#### 2.8.4 Cadastro e busca da conta

```java
@PostMapping("/cadastro/atleta/save")
public String postAtletaInsert(@ModelAttribute("atletaDtos") @Valid PerfilsDtos atletaDtos,
BindingResult result,  Model model
) {

    if (result.hasErrors()) {
        carregarDadosCadastro(model);
        return "publicPlages/cadastroAtleta";
    }
    service.InsertAtleta(atletaDtos);
    return "redirect:/home";
}
```

`postAtletaInsert` usa `@ModelAttribute` para preencher `atletaDtos` com o POST. Em caso de erro, recarrega os dados auxiliares do formulário e retorna a vista de cadastro. Quando o formulário pode seguir, chama o serviço de perfil e redireciona para `/home`. [AtletaController.java linhas 41–52](../src/main/java/com/api/Projeto_3/Controller/AtletaController.java#L41-L52)

```java
@Transactional
public PerfilsDtos InsertAtleta(PerfilsDtos dtos){
    AtletaModelo atl = new AtletaModelo();

    preencherDadosBasicos(atl,dtos);
    atl.setRole(buscarRole(RoleAuthority.ROLE_ATLETA));
    salvarAfiliado(dtos.getPais_fk(), atl);
    salvarMoradia(dtos.getMoradia_fk(), atl);

    AtletaModelo salvo = respository.save(atl);

    respository.save(salvo);
    return new PerfilsDtos(salvo);
}
```

`InsertAtleta` transforma o DTO de cadastro em `AtletaModelo`. `preencherDadosBasicos` copia os campos e codifica a senha com `PasswordEncoder.encode`. `buscarRole` seleciona a permissão do atleta. `salvarAfiliado` e `salvarMoradia` montam os objetos associados. A gravação ocorre dentro da transação e o método devolve um DTO construído a partir da entidade salva. Os métodos `insertMeidco` e `insertTreinador` seguem essa sequência com a entidade e a autoridade do perfil correspondente. [PerfilService.java linhas 55–68](../src/main/java/com/api/Projeto_3/service/PerfilService.java#L55-L68)

```java
@Override
public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
    PerfisModelo usuario = perfilJpa.findByCpf(cpf)
            .or(() -> perfilJpa.findByCpfSemPontuacao(cpf))
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com CPF: " + cpf));

    String authority = normalizarAuthority(usuario.getRole().getAuthority());

    return User.builder()
            .username(usuario.getCpf())
            .password(usuario.getSenha())
            .authorities(new SimpleGrantedAuthority(authority))
            .build();
}
```

`loadUserByUsername` recebe o CPF informado na tela. `findByCpf` procura uma correspondência exata e `findByCpfSemPontuacao` é chamado se a primeira consulta vier vazia. Ausência de conta produz `UsernameNotFoundException`. `normalizarAuthority` converte as autoridades conhecidas para os nomes usados pelo filtro. `User.builder()` devolve ao Spring Security o CPF registrado, o hash persistido e a autoridade. [PerfilService.java linhas 152–165](../src/main/java/com/api/Projeto_3/service/PerfilService.java#L152-L165)

#### 2.8.5 Configuração da aplicação

```properties
spring.application.name=Projeto-3

server.port=${SERVER_PORT:8080}

spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/PROJETO_TCC}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

langchain4j.ollama.chat-model.base-url=${APP_OLLAMA_BASE_URL:http://localhost:11434}
langchain4j.ollama.chat-model.model-name=${APP_OLLAMA_MODEL_NAME:qwen2.5:1.5b}
```

`SERVER_PORT` define a porta do Spring e usa 8080 quando a variável está ausente. As propriedades da fonte de dados usam as variáveis `SPRING_DATASOURCE_*`. O Compose fornece a URL JDBC e as credenciais pela configuração do serviço. `ddl-auto=update` pede ao Hibernate que atualize o esquema durante a inicialização. `show-sql=false` desativa a saída SQL configurada pelo Hibernate. As duas últimas linhas também declaram URL e modelo para a integração LangChain4j. O cliente usado nos serviços de exames é o bean primário de `OllamaConfig`. [application.properties linhas 2–14](../src/main/resources/application.properties#L2-L14)

### 2.9 Código do planejamento, da agenda e do painel

#### 2.9.1 Criar uma semana para um atleta

```java
@PostMapping("/semanas")
public String criarSemana(@Valid @ModelAttribute("semanaForm") CriarSemanaDto form,
        BindingResult result, @AuthenticationPrincipal UserDetails usuario,
        Model model, RedirectAttributes redirect) {
    if (result.hasErrors()) {
        carregarDadosDasSemanas(usuario, model);
        return PAGINA_SEMANAS;
    }

    var atletaSelecionado = atletas.listarPorTreinador(usuario.getUsername()).stream()
            .filter(atleta -> atleta.getId().equals(form.getAtletaId()))
            .findFirst();

    if (atletaSelecionado.isEmpty()) {
        result.rejectValue("atletaId", "atleta.invalido", "Selecione um atleta vinculado a você.");
        carregarDadosDasSemanas(usuario, model);
        return PAGINA_SEMANAS;
    }

    SemanaModel semana = new SemanaModel();
    semana.setNome(form.getNome().trim());
    semana.setDescricao(form.getDescricao());
    semana.setAtleta_fk(atletaSelecionado.get());

    service.creSemana(semana);
    redirect.addFlashAttribute("sucesso", "Semana criada com sucesso!");
    return REDIRECIONAR_TREINOS;
}
```

`criarSemana` recebe o formulário, o resultado da validação e a conta autenticada. Se o formulário tiver erros, `carregarDadosDasSemanas` reconstrói as opções da tela antes de exibir o formulário. A lista buscada pelo CPF do treinador restringe a seleção aos seus atletas. `rejectValue` associa o erro ao campo `atletaId`. Após encontrar o atleta, o método cria a entidade, associa o atleta e chama `creSemana` para gravar. `addFlashAttribute` transporta a mensagem de sucesso para o GET seguinte. [TreinamentoController.java linhas 75–102](../src/main/java/com/api/Projeto_3/Controller/TreinamentoController.java#L75-L102)

#### 2.9.2 Receber e persistir o treino

```java
@PostMapping
public String criarTreino(@Valid @ModelAttribute("treinoForm") CriarTreinoDto form,
        BindingResult result, @AuthenticationPrincipal UserDetails usuario,
        Model model, RedirectAttributes redirect) {
    if (result.hasErrors()) {
        carregarDadosDosTreinos(usuario, model);
        return PAGINA_TREINOS;
    }

    service.criarTreino(form, usuario.getUsername());
    redirect.addFlashAttribute("sucesso", "Treino salvo para " + form.getDia().getRotulo() + ".");
    return REDIRECIONAR_TREINOS;
}
```

`criarTreino` recebe os campos da tela em `CriarTreinoDto`, incluindo a lista de exercícios. O `BindingResult` reúne os erros da vinculação e das restrições ativas. Se houver erro, a página volta com suas opções. O CPF é obtido de `usuario.getUsername()` e segue para o serviço. A mensagem temporária de sucesso inclui o dia do treino. [TreinamentoController.java linhas 54–66](../src/main/java/com/api/Projeto_3/Controller/TreinamentoController.java#L54-L66)

```java
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
```

`buscarSemana` usa o ID da semana e o CPF do treinador para localizar uma semana de um atleta vinculado. `buscarOuCriarDia` reutiliza o dia da semana existente ou cria um associado. `montarTreino` transfere para a entidade os dados do formulário e o dia. Cada exercício recebe a referência ao treino e entra na coleção do pai. Quando `snJpa.save(semana)` persiste a semana, as relações em cascata alcançam os dias, o treino e os exercícios. [TrenamentosSerive.java linhas 26–45](../src/main/java/com/api/Projeto_3/service/TrenamentosSerive.java#L26-L45)

#### 2.9.3 Acrescentar um exercício a um treino já gravado

```java
@Transactional
public void criarExercicio(CriarExercicioDto form, String cpf) {
    TreinamentoModel treino = treinamentoRepository
            .buscarDoTreinadorParaAtualizar(form.getTreinamentoId(), form.getSemanaId(), cpf)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Selecione um treinamento pertencente à semana escolhida."));
    if (Boolean.TRUE.equals(treino.getCancelado())) {
        throw new IllegalArgumentException("Este treinamento foi cancelado pelo médico e não aceita novos exercícios.");
    }
    if (treino.isConcluido()) {
        throw new IllegalArgumentException("Este treinamento já foi concluído e não aceita novos exercícios.");
    }
    ExercicioModel exercicio = new ExercicioModel();
    exercicio.setNome(form.getNome().trim());
    exercicio.setDescricao(form.getDescricao());
    exercicio.setDuracacao(form.getDuracao());
    exercicio.setTreinamento_fk(treino);
    treino.getExercicios().add(exercicio);
    treinamentoRepository.save(treino);
}
```

`buscarDoTreinadorParaAtualizar` seleciona o treino pelo ID, semana e CPF do treinador e bloqueia sua linha durante a operação. Ausência do treino, cancelamento ou conclusão causa `IllegalArgumentException`. O handler de `TreinamentoController` transforma essa exceção em HTTP 400. Quando a operação é válida, `criarExercicio` associa o novo item e salva o treino. [TrenamentosSerive.java linhas 56–75](../src/main/java/com/api/Projeto_3/service/TrenamentosSerive.java#L56-L75)

#### 2.9.4 Carregar a agenda de uma semana

```java
@Transactional(readOnly = true)
public TreinosAtletaDto buscarAgenda(String cpf, Long semanaId) {
    AtletaModelo atleta = atletaService.buscarAtletaPorCpf(cpf);
    List<SemanaModel> semanas = semanaRepository.buscarSemanasDoAtleta(cpf);

    List<SemanaAgendaDto> semanasDaAgenda = new ArrayList<>();
    for (SemanaModel semana : semanas) {
        semanasDaAgenda.add(mapearSemana(semana));
    }

    Optional<Long> semanaSelecionada = semanas.stream()
            .map(SemanaModel::getId)
            .filter(id -> semanaId == null || semanaId.equals(id))
            .findFirst();
    List<DiaAgendaDto> dias = semanaSelecionada
            .map(id -> montarDias(treinoRepository.listarDaSemanaDoAtleta(id, cpf)))
            .orElseGet(List::of);
    return new TreinosAtletaDto(atleta.getName(), semanaSelecionada.orElse(null),
            List.copyOf(semanasDaAgenda), dias);
}
```

`buscarAgenda` consulta o atleta e suas semanas pelo CPF recebido do controller. É uma transação de leitura. Para o seletor da página, `mapearSemana` converte cada entidade em ID e nome. Sem `semanaId`, `findFirst()` seleciona a primeira semana retornada. Com ID informado, somente uma semana correspondente pode carregar os dias. `montarDias` agrupa as linhas da consulta em dias, treinos e exercícios. Quando nenhuma semana corresponde, `List::of` fornece a lista vazia de dias. [TreinosAtletaService.java linhas 44–63](../src/main/java/com/api/Projeto_3/service/TreinosAtletaService.java#L44-L63)

#### 2.9.5 Registrar o progresso e voltar à mesma semana

```java
@PostMapping("/{treinoId}")
public String registrar(@AuthenticationPrincipal UserDetails usuario, @PathVariable Long treinoId,
                        @ModelAttribute TreinoAtletaProgressoDto formulario, RedirectAttributes redirect) {
    var resultado = service.registrarProgresso(usuario.getUsername(), treinoId, formulario);
    redirect.addAttribute("semanaId", resultado.getSemanaId());
    redirect.addFlashAttribute("treinoComRetorno", treinoId);
    redirect.addFlashAttribute("resultado", resultado);
    return "redirect:/atleta/treinos#treino-" + treinoId;
}
```

`registrar` recebe `treinoId` do caminho, a ação do formulário e o CPF autenticado. O resultado do serviço fornece o `semanaId` do redirecionamento. Os atributos temporários `treinoComRetorno` e `resultado` permitem renderizar a mensagem no treino correto. O fragmento `#treino-` posiciona a página nesse treino no navegador. [TreinosAtletaController.java linhas 34–42](../src/main/java/com/api/Projeto_3/Controller/TreinosAtletaController.java#L34-L42)

```java
@Transactional
public ResultadoProgressoTreinoDto registrarProgresso(String cpf, Long treinoId, TreinoAtletaProgressoDto formulario) {
    TreinamentoModel treino = treinoRepository.buscarDoAtletaParaAtualizar(treinoId, cpf).orElseThrow();
    return switch (formulario.getAcao()) {
        case CONCLUIR_EXERCICIOS -> concluirExercicios(treino, formulario.getExercicioIds());
        case CONCLUIR_TREINO -> concluirTreino(treino);
    };
}

private ResultadoProgressoTreinoDto concluirExercicios(TreinamentoModel treino, List<Long> exercicioIds) {
    Set<Long> idsSelecionados = new HashSet<>(exercicioIds);
    treino.getExercicios().stream()
            .filter(exercicio -> idsSelecionados.contains(exercicio.getId()))
            .forEach(exercicio -> exercicio.setConcluido(true));
    return resultado(treino, "Exercícios selecionados concluídos.");
}

private ResultadoProgressoTreinoDto concluirTreino(TreinamentoModel treino) {
    treino.setConcluido(true);
    return resultado(treino, "Treino concluído. O dashboard desta semana foi atualizado.");
}

private ResultadoProgressoTreinoDto resultado(TreinamentoModel treino, String mensagem) {
    DiaModel dia = treino.getDia_fk();
    SemanaModel semana = dia.getSemana_fk();
    return new ResultadoProgressoTreinoDto(semana.getId(), true, mensagem);
}
```

`registrarProgresso` busca o treino pertencente ao atleta antes de despachar a ação pelo `switch`. `concluirExercicios` transforma os IDs enviados em um conjunto e altera apenas os itens correspondentes que pertencem à coleção do treino carregado. `concluirTreino` marca o próprio treino como concluído. A transação grava as mudanças nessas entidades gerenciadas. `resultado` devolve o ID da semana, um indicador de sucesso e a mensagem usada no redirecionamento. [TreinosAtletaService.java linhas 65–91](../src/main/java/com/api/Projeto_3/service/TreinosAtletaService.java#L65-L91)

```java
public long getTotalExerciciosConcluidos() {
    long total = 0;
    for (ExercicioAgendaDto exercicio : exercicios) {
        if (exercicio.isConcluido()) {
            total++;
        }
    }
    return total;
}

public boolean isPodeConcluir() {
    return !cancelado && !concluido && getTotalExerciciosConcluidos() == exercicios.size();
}
```

`getTotalExerciciosConcluidos` conta os itens marcados como concluídos. `isPodeConcluir` fornece ao template a condição para habilitar o botão. O treino deve estar ativo e a quantidade de exercícios concluídos deve coincidir com o tamanho da lista. O template também consulta as propriedades de conclusão e de cancelamento para exibir o estado. [TreinoAgendaDto.java linhas 19–31](../src/main/java/com/api/Projeto_3/dtos/DtosModelos/TreinoAgendaDto.java#L19-L31)

#### 2.9.6 Montar o painel e somar indicadores

```java
@GetMapping("/atleta")
public String mostrarDashboard(@AuthenticationPrincipal UserDetails usuario,
        @RequestParam(name = "semanaId", required = false) Long semanaId, Model model) {
    model.addAttribute("dash", dashService.buscarDashboard(usuario.getUsername(), semanaId));
    return "atletaPages/dashAtleta";
}
```

`mostrarDashboard` atende GET `/atleta`, lê `semanaId` quando presente e coloca em `dash` o resultado de `buscarDashboard`. Esse nome permite ao Thymeleaf consultar os dados na vista `atletaPages/dashAtleta`. [DashController.java linhas 19–24](../src/main/java/com/api/Projeto_3/Controller/DashController.java#L19-L24)

```java
    for (DiaModel dia : semana.getDias()) {
        List<DashTreinoDto> treinosDoDia = dia.getTreinamentos().stream().map(this::criarTreinoDoHistorico).toList();
        dashboard.getTreinos().addAll(treinosDoDia);

        DashPontoDto atividade = atividadeSemanal.get(dia.getNome().ordinal());
        List<DashTreinoDto> treinosAtivos = treinosDoDia.stream()
                .filter(treino -> !treino.isCancelado())
                .toList();

        for (DashTreinoDto treino : treinosAtivos) {
            long duracao = Objects.requireNonNullElse(treino.getDuracao(), 0L);
            tempoTotal += duracao;
            somarNoGrafico(atividade, duracao, treino.isConcluido());

            DashPontoDto categoria = categorias.computeIfAbsent(treino.getCategoria(), this::criarPonto);
            somarNoGrafico(categoria, 1, treino.isConcluido());

            Integer nota = treino.getSatisfacao();
            if (nota != null) {
                somaDasNotas += nota;
                totalDeNotas++;
            }
        }
    }

    dashboard.getCategorias().addAll(categorias.values());
    dashboard.setTempoTotal(tempoTotal);
    dashboard.setMediaDiaria((double) tempoTotal / atividadeSemanal.size());
    if (totalDeNotas > 0) {
        dashboard.setSatisfacaoMedia((double) somaDasNotas / totalDeNotas);
    }
}
```

Antes desse trecho, `buscarDashboard` cria um ponto para cada dia da semana e escolhe a semana a carregar. `preencherDadosDaSemana` inclui todos os treinos no histórico e filtra os cancelados para os indicadores. Para cada treino ativo, usa a duração em minutos no gráfico diário e uma unidade no total por categoria. `criarTreinoDoHistorico` calcula a duração pela diferença entre os horários, quando informados. A média diária divide o tempo total pelos pontos da semana. A média de satisfação usa apenas as notas disponíveis. [DashService.java linhas 63–94](../src/main/java/com/api/Projeto_3/service/DashService.java#L63-L94)

```java
private void somarNoGrafico(DashPontoDto ponto, long valor, boolean concluido) {
    long valorConcluido = concluido ? valor : 0;
    ponto.setY(ponto.getY() + valor);
    ponto.setYConcluido(ponto.getYConcluido() + valorConcluido);
}
```

`somarNoGrafico` atualiza dois valores do mesmo ponto. `y` acumula o total programado. `yConcluido` recebe a contribuição apenas quando o treino está concluído. Esses valores alimentam as proporções renderizadas na seção 1.5.2. [DashService.java linhas 113–117](../src/main/java/com/api/Projeto_3/service/DashService.java#L113-L117)

### 2.10 Código dos vínculos e da área médica

#### 2.10.1 Vincular um atleta a um profissional

```java
@Transactional
public void insertFkATreinador(Long atletaid , Long treinaid){
    jpa.vincularTreinador(atletaid, treinaid);
}
```

`insertFkATreinador` delega a atualização ao repositório. O primeiro parâmetro identifica o atleta que recebe a chave estrangeira e o segundo identifica o treinador. O método é chamado pelo controller de treinador em uma operação de vínculo. [AtletaService.java linhas 21–24](../src/main/java/com/api/Projeto_3/service/AtletaService.java#L21-L24)

```java
@Transactional
public void insertFkAMedico(Long atletaid , Long medicoId){
    jpa.vincularMedico(atletaid, medicoId);
}
```

`insertFkAMedico` recebe o ID do atleta e o ID do médico. `vincularMedico` atualiza a coluna de vínculo médico do atleta em uma transação. Os métodos de contagem da mesma classe usam cada chave estrangeira para calcular quantos atletas estão vinculados ou disponíveis. [AtletaService.java linhas 36–39](../src/main/java/com/api/Projeto_3/service/AtletaService.java#L36-L39)

#### 2.10.2 Cancelar um treino

```java
@Transactional
public boolean cancelarTreinamento(Long medicoId, Long atletaId, Long treinamentoId, String motivo) {
    TreinamentoModel treino = jpaTreinamento.buscarDoAtletaDoMedico(treinamentoId, atletaId, medicoId)
            .orElse(null);

    if (treino == null || Boolean.TRUE.equals(treino.getCancelado())) {
        return false;
    }

    String motivoLimpo = motivo == null ? "" : motivo.trim();
    if (motivoLimpo.isEmpty() || motivoLimpo.length() > MOTIVO_CANCELAMENTO_MAX) {
        return false;
    }

    treino.setCancelado(true);
    treino.setMotivoCancelamento(motivoLimpo);
    treino.setCanceladoEm(LocalDateTime.now());
    treino.setCanceladoPor(jpaMedico.getReferenceById(medicoId));
    jpaTreinamento.save(treino);
    return true;
}
```

`cancelarTreinamento` solicita um treino pelo conjunto de IDs do médico, do atleta e do treino. O repositório só devolve uma linha quando essas relações coincidem. O método devolve `false` para treino ausente, já cancelado ou motivo inválido. `trim()` remove espaços nas extremidades e o limite configurado é de 255 caracteres. Em um cancelamento aceito, a entidade guarda o motivo, a data e a referência ao médico. O controller usa o booleano retornado para escolher o retorno da ação na página. [MedicoService.java linhas 97–117](../src/main/java/com/api/Projeto_3/service/MedicoService.java#L97-L117)

#### 2.10.3 Validar e registrar uma orientação alimentar

```java
public boolean dataRevisaoInvalida(RecomendacaoAlimentarDto dados) {
    LocalDate dataInicio = dados.getDataInicio();
    LocalDate dataRevisao = dados.getDataRevisao();
    if (dataInicio == null || dataRevisao == null) {
        return false;
    }

    return dataRevisao.isBefore(dataInicio);
}
```

`dataRevisaoInvalida` compara as datas quando as duas foram fornecidas. O retorno `true` indica revisão anterior ao início. O controller usa esse resultado para registrar um erro em `dataRevisao`. [AlimentoService.java linhas 41–49](../src/main/java/com/api/Projeto_3/service/AlimentoService.java#L41-L49)

```java
if (alimentoService.dataRevisaoInvalida(recomendacao)) {
    result.rejectValue("dataRevisao", "dataRevisao.invalida", "A revisão deve ser na data inicial ou depois.");
}

AtletaModelo atleta = alimentoService.buscarAtletaVinculado(medico.getId(), atletaId);
if (atleta == null) {
    return "redirect:/medico/atletas-vinculados?atletaIndisponivel";
}

if (result.hasErrors()) {
    return mostrarFormulario(medico.getId(), atleta, false, model);
}

if (!alimentoService.recomendarAlimentacao(medico.getId(), atletaId, recomendacao)) {
    return "redirect:/medico/atletas-vinculados?atletaIndisponivel";
}

return "redirect:/medico/alimentacao/" + atletaId + "?salvo=true";
```

`salvarAlimentacao` já identificou o médico autenticado neste ponto. Depois da validação da data, o método procura o atleta entre os vinculados ao médico. Quando o atleta está indisponível, redireciona para a lista de vinculados. Quando o formulário tem erro, `mostrarFormulario` recarrega a tela para correção. Depois da gravação, a URL de retorno contém o ID do atleta e `salvo=true`. [AlimentoController.java linhas 91–108](../src/main/java/com/api/Projeto_3/Controller/AlimentoController.java#L91-L108)

```java
@Transactional
public boolean recomendarAlimentacao(Long medicoId, Long atletaId, RecomendacaoAlimentarDto dados) {

    MedicoModelo medico = medicoRepository.findById(medicoId).orElse(null);
    AtletaModelo atleta = buscarAtletaVinculado(medicoId, atletaId);

    RecomendacaoAlimentar recomendacao = new RecomendacaoAlimentar();
    recomendacao.setMedico(medico);
    recomendacao.setAtleta(atleta);
    recomendacao.setRefeicao(dados.getRefeicao().trim());
    recomendacao.setObjetivo(dados.getObjetivo().trim());
    recomendacao.setOrientacao(dados.getOrientacao().trim());
    recomendacao.setDataInicio(dados.getDataInicio());
    recomendacao.setDataRevisao(dados.getDataRevisao());

    recomendacaoRepository.save(recomendacao);
    return true;
}
```

`recomendarAlimentacao` carrega as entidades que serão associadas à recomendação, copia refeição, objetivo, orientação e datas e persiste o registro. `trim()` limpa as extremidades dos campos de texto antes do armazenamento. A leitura do histórico usa os IDs de médico e atleta e ordena por data de início, da mais recente para a mais antiga. [AlimentoService.java linhas 56–73](../src/main/java/com/api/Projeto_3/service/AlimentoService.java#L56-L73)

### 2.11 Código dos exames e da análise com Ollama

#### 2.11.1 Receber formulário e abrir o resultado

```java
@PostMapping({"/exame/urina", "/exame-urina/salvar"})
public String salvarExameUrina(@ModelAttribute("exameUrina") ExameUrinaDtos exame,
        RedirectAttributes redirectAttributes) {
    ExameUrinaDtos resultado = urinaService.realizarExameParaPagina(exame);
    redirectAttributes.addFlashAttribute("exameUrina", resultado);
    return "redirect:/medico/exame-urina/resultado";
}

@GetMapping("/exame-urina/resultado")
public String resultadoExameUrina() {
    return "medicoPages/result";
}

@PostMapping("/exame-sangue/salvar")
public String salvarExameSangue(@ModelAttribute("exameSangue") ExameSagueDtos exame,
        RedirectAttributes redirectAttributes) {
    ExameSagueDtos resultado = sangueService.realizarExameParaPagina(exame);
    redirectAttributes.addFlashAttribute("exameSangue", resultado);
    return "redirect:/medico/exame-sangue/resultado";
}

@GetMapping("/exame-sangue/resultado")
public String resultadoExameSangue() {
    return "medicoPages/resultadoSangue";
}
```

Cada POST recebe o DTO do formulário e chama `realizarExameParaPagina` no serviço do exame. `addFlashAttribute` disponibiliza o DTO processado no GET que sucede o redirecionamento. As duas rotas GET retornam os nomes dos templates de resultado. Esse encadeamento permite recarregar a página de relatório sem reenviar o formulário do POST. [ChatMedicoExame.java linhas 26–50](../src/main/java/com/api/Projeto_3/Controller/ChatMedicoExame.java#L26-L50)

#### 2.11.2 Salvar os exames e preparar a análise

```java
public ExameUrinaDtos realizarExameParaPagina(ExameUrinaDtos exame) {
      exame.setAnalise(processamentoChatService.separarResposta(realizandoExame(exame)));
      return exame;
}

public String realizandoExame(ExameUrinaDtos dtos) {

      salvarExame(dtos);
       return chatExecucao(dtos);
}
```

`realizandoExame` chama `salvarExame` antes de executar a consulta de IA. `salvarExame` localiza o atleta, converte os campos de triagem para os enums do modelo e grava `EmaxeUrina`. `realizarExameParaPagina` converte a resposta em `ProcessamentoChat` e a inclui no DTO de retorno para a tela. [UrinaExamesService.java linhas 40–49](../src/main/java/com/api/Projeto_3/service/UrinaExamesService.java#L40-L49)

```java
public ExameSagueDtos realizarExameParaPagina(ExameSagueDtos dados) {
    String resposta = realizandoExame(dados);
    dados.setAnalise(processamentoChatService.separarResposta(resposta));
    return dados;
}

public String realizandoExame(ExameSagueDtos dados) {
    ExameSangue exame = salvarExame(dados);
    String resposta = analisarExame(exame);

    exame.setAnaliseIa(resposta);
    sangueRepository.save(exame);
    dados.setAnaliseIa(resposta);
    return resposta;
}
```

`salvarExame` monta `ExameSangue` e as substâncias informadas. `analisarExame` prepara a consulta ao Ollama com os dados do exame já salvo. Depois de receber a resposta, o serviço grava o texto em `analiseIa`, inclui-o no DTO e retorna o texto para o método que organiza a análise exibida. [SangueTest.java linhas 32–46](../src/main/java/com/api/Projeto_3/service/SangueTest.java#L32-L46)

#### 2.11.3 Interpretar o retorno do modelo

```java
public ProcessamentoChat separarResposta(String resposta) {
    if (resposta == null || resposta.isBlank()) {
        return ANALISE_INDISPONIVEL;
    }

    String texto = resposta.strip();
    if (!pareceJsonOuCodigo(texto)) {
        return new ProcessamentoChat(resposta, List.of(), List.of());
    }

    try {
        String json = retirarMarcadoresDeCodigo(texto);
        JsonNode analiseJson = leitorJson.readTree(json);
        JsonNode resumo = analiseJson.path("resumo");
        JsonNode pontosDeAtencao = analiseJson.path("pontosAtencao");
        JsonNode recomendacoes = analiseJson.path("recomendacoes");

        boolean camposValidos = textoValido(resumo)
                && listaDeTextosValida(pontosDeAtencao)
                && listaDeTextosValida(recomendacoes);
        if (!camposValidos) {
            return ANALISE_INDISPONIVEL;
        }

        return leitorJson.treeToValue(analiseJson, ProcessamentoChat.class);
    } catch (JacksonException exception) {
        return ANALISE_INDISPONIVEL;
    }
}
```

`separarResposta` trata três formatos. Resposta vazia devolve a mensagem de indisponibilidade. Texto simples é usado no resumo com listas vazias. Se a resposta parece JSON ou vem em um bloco de código, `retirarMarcadoresDeCodigo` limpa os delimitadores e o Jackson analisa os campos. `textoValido` e `listaDeTextosValida` verificam o resumo e os itens das listas. Uma estrutura válida vira `ProcessamentoChat`. Estrutura inválida ou erro de leitura gera o estado de análise indisponível. [ProcessamentoChatService.java linhas 19–47](../src/main/java/com/api/Projeto_3/service/ProcessamentoChatService.java#L19-L47)

#### 2.11.4 Configurar o cliente e o processo local

```java
@Value("${app.ollama.base-url:http://localhost:11434}")
private String baseUrl;

@Value("${app.ollama.model-name:llama3.2}")
private String modelName;

@Value("${app.ollama.timeout-seconds:180}")
private long timeoutSeconds;

@Bean
@Primary
public ChatModel chatModel() {

    return OllamaChatModel.builder()
            .httpClientBuilder(JdkHttpClient.builder())
            .baseUrl(baseUrl)
            .modelName(modelName)
            .timeout(Duration.ofSeconds(timeoutSeconds))
            .maxRetries(0)
            .temperature(0.2)
            .build();
}
```

Os campos com `@Value` recebem a URL, o nome do modelo e o tempo máximo por chamada. `chatModel()` registra o cliente primário injetado nos serviços de exames. O construtor usa o cliente HTTP do JDK, configura a duração em segundos, define temperatura `0.2` e configura zero tentativas adicionais. No Docker, as variáveis do serviço `springboot` apontam a URL e o modelo para o serviço interno `ollama`. [OllamaConfig.java linhas 18–39](../src/main/java/com/api/Projeto_3/config/OllamaConfig.java#L18-L39)

```java
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
```

A condição de propriedade define quando o Spring cria `OllamaLifecycle`. `iniciar()` consulta `localhost:11434/api/tags` com limites curtos para conexão e leitura. Se recebe HTTP 200, reaproveita o processo existente. Caso contrário, executa `ollama serve` e guarda a referência em `processo`. `parar()` usa essa referência para encerrar o processo iniciado pelo Spring. Em Compose, `APP_OLLAMA_START_LOCAL=false` desativa esse componente. [OllamaLifecycle.java linhas 13–45](../src/main/java/com/api/Projeto_3/config/OllamaLifecycle.java#L13-L45)

#### 2.11.5 Iniciar a aplicação no computador de desenvolvimento

```bash
if ! ollama show "$APP_OLLAMA_MODEL_NAME" >/dev/null 2>&1; then
    echo "Modelo ausente. Execute: ollama pull $APP_OLLAMA_MODEL_NAME" >&2
    exit 1
fi

echo "Iniciando projeto com o modelo $APP_OLLAMA_MODEL_NAME. Use Ctrl+C para parar."
./mvnw spring-boot:run "$@"
```

`ollama show` verifica se o modelo selecionado está disponível. Se faltar, o script exibe a instrução de download e encerra com código 1. Com o modelo presente, `./mvnw spring-boot:run "$@"` inicia o Spring e encaminha os argumentos que o operador passou ao script. [iniciar.sh linhas 55–61](../iniciar.sh#L55-L61)

### 2.12 Código das entidades, dos DTOs e das consultas

#### 2.12.1 Herança de perfis e relacionamentos

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PerfisModelo implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
```

`@Entity` torna `PerfisModelo` persistente. A estratégia `JOINED` liga a tabela base à tabela do perfil especializado pelo mesmo ID. `@GeneratedValue` delega a geração do identificador ao banco. [PerfisModelo.java linhas 28–36](../src/main/java/com/api/Projeto_3/model/PerfisModelo.java#L28-L36)

```java
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "fal_fk" , unique = true )
private AfiliacaoModelo pais_fk;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "mord")
private MoradiaModel moradia_fk;

@ManyToOne
@JoinColumn(name = "role_id")
private RolesModel role;
```

`pais_fk` e `moradia_fk` representam associações um para um. A configuração de cascata propaga operações às entidades associadas. `role` representa a autoridade compartilhável por várias contas. `@JoinColumn` identifica a coluna usada para armazenar cada referência. [PerfisModelo.java linhas 75–85](../src/main/java/com/api/Projeto_3/model/PerfisModelo.java#L75-L85)

```java
@ManyToOne(optional = false)
@JoinColumn(name = "atleta_fk", nullable = false)
private AtletaModelo atleta_fk;

@OneToMany(mappedBy = "semana_fk", cascade = CascadeType.ALL)
private List<DiaModel> dias = new ArrayList<>();
```

`@ManyToOne` aponta da semana para o atleta. A coluna `atleta_fk` é obrigatória. `mappedBy="semana_fk"` diz que o campo em `DiaModel` contém o lado que grava a referência da relação. A cascata permite que a gravação da semana alcance os dias associados. [SemanaModel.java linhas 31–36](../src/main/java/com/api/Projeto_3/model/SemanaModel.java#L31-L36)

```java
@ManyToOne(optional = false)
@JoinColumn(name = "dia_fk", nullable = false)
@ToString.Exclude
private DiaModel dia_fk;

@OneToMany(mappedBy = "treinamento_fk", cascade = CascadeType.ALL)
@ToString.Exclude
private List<ExercicioModel> exercicios = new ArrayList<>();
```

O treino pertence a um dia, identificado por `dia_fk`. A coleção `exercicios` é o lado inverso da relação com `ExercicioModel`. O serviço ajusta tanto a referência no exercício quanto o conteúdo da coleção antes da gravação. [TreinamentoModel.java linhas 51–58](../src/main/java/com/api/Projeto_3/model/TreinamentoModel.java#L51-L58)

#### 2.12.2 Validação dos DTOs

```java
@Size(max = 50, message = "Adicione até 50 exercícios por treino.")
private java.util.List<ExercicioDoTreinoDto> exercicios = new java.util.ArrayList<>();
```

`@Size(max = 50)` aplica ao formulário o mesmo limite de itens usado pelo JavaScript. `BindingResult` recebe a violação quando a lista ultrapassa o tamanho permitido. O método `isHorarioValido()` calcula a condição de horários, com a anotação `@AssertTrue` comentada na revisão documentada. [CriarTreinoDto.java linhas 13–14](../src/main/java/com/api/Projeto_3/dtos/DtosModelos/CriarTreinoDto.java#L13-L14)

```java
@NotBlank
@Size(max = 40)
private String refeicao;

@NotBlank
@Size(max = 120)
private String objetivo;

@NotBlank
@Size(max = 2000)
private String orientacao;

@NotNull
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
private LocalDate dataInicio = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
```

`@NotBlank` exige texto nos campos de refeição, objetivo e orientação. As anotações `@Size` limitam cada texto a um tamanho próprio. `@NotNull` exige a data inicial. O controller complementa essas regras comparando as datas com `dataRevisaoInvalida`. [RecomendacaoAlimentarDto.java linhas 13–28](../src/main/java/com/api/Projeto_3/dtos/DtosModelos/RecomendacaoAlimentarDto.java#L13-L28)

#### 2.12.3 Consultar agenda e bloquear o treino para atualização

```java
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
```

Esse trecho de `listarDaSemanaDoAtleta` une treino, dia, semana e perfil do atleta. O `LEFT JOIN` inclui treinos mesmo quando a tabela de exercícios não tem uma linha correspondente. `.param(...)` vincula o ID e o CPF aos parâmetros SQL. `.query(LinhaDaAgenda.class)` mapeia as colunas em uma projeção usada por `montarDias`. [AgendaTreinamentoRepositoryImpl.java linhas 32–46](../src/main/java/com/api/Projeto_3/repository/AgendaTreinamentoRepositoryImpl.java#L32-L46)

```java
@NativeQuery("""
                SELECT t.*
                FROM treinamento t
                JOIN dia d ON d.id = t.dia_fk
                JOIN semana s ON s.id = d.semana_fk
                JOIN atletas a ON a.id = s.atleta_fk
                JOIN perfis_modelo p ON p.id = a.id
                WHERE t.id = :treinoId
                  AND p.cpf = :cpf
                FOR UPDATE OF t
                """)
Optional<TreinamentoModel> buscarDoAtletaParaAtualizar(@Param("treinoId") Long treinoId, @Param("cpf") String cpf);
```

A consulta de progresso exige que `t.id` corresponda ao ID do treino e que `p.cpf` corresponda à conta autenticada. `FOR UPDATE OF t` bloqueia a linha do treino enquanto a transação altera o progresso. As consultas usadas pelo médico e pelo treinador aplicam a mesma ideia com os filtros de vínculo do respectivo perfil. [TreinamentoRespository.java linhas 41–52](../src/main/java/com/api/Projeto_3/repository/TreinamentoRespository.java#L41-L52)

## Capítulo 3 Docker e entrega contínua na VPS

### 3.1 Imagens, serviços e volumes

O `Dockerfile` da raiz permite compilar e executar o projeto localmente com Docker. O estágio `build` usa `maven:3.9-eclipse-temurin-21` e executa `mvn clean package -DskipTests`. O estágio final usa `eclipse-temurin:21-jre`, copia o JAR para `/app/app.jar` e inicia `java -jar app.jar`. No deploy automatizado, o GitHub Actions gera um Dockerfile de execução que copia o JAR pré-compilado. [Dockerfile](../Dockerfile)

| Serviço | Função, acesso e persistência |
| --- | --- |
| `postgres` | Contêiner `tcc_postgres` com PostgreSQL 16. O Spring acessa `postgres:5432`. O volume `postgres_data` guarda o banco em `/var/lib/postgresql/data`. O Compose aguarda o teste `pg_isready` antes de iniciar o Spring. |
| `springboot` | Contêiner `tcc_springboot`. Publica a porta 8080 em `127.0.0.1:8080` na máquina hospedeira. Monta o volume `uploads_data` em `/app/uploads` e recebe os limites de heap `-Xms256m -Xmx1024m`. |
| `ollama` | Contêiner `tcc_ollama` com `ollama/ollama:latest`. O Spring conecta a `http://ollama:11434` e usa `qwen2.5:1.5b`. O volume `ollama_data` armazena modelos em `/root/.ollama`. A configuração limita a um modelo carregado e uma operação paralela. |

Os três serviços participam da rede interna `tcc_network` e usam `restart: unless-stopped`. A porta da aplicação fica no loopback do host. PostgreSQL e Ollama se comunicam pela rede do Compose e têm suas portas disponíveis dentro dessa rede. `OPENAI_API_KEY` é uma variável opcional do Compose. Os serviços de exame usam o `ChatModel` do Ollama. [docker-compose.yml](../docker-compose.yml)

### 3.2 Subir Docker no computador de desenvolvimento

Instale Docker Engine ou Docker Desktop, com o plugin `docker compose`. Na raiz do repositório, crie ou edite `.env` com os valores do seu ambiente. Use uma senha própria no lugar de `SENHA_DO_BANCO`.

```dotenv
DB_NAME=projeto_tcc
DB_USER=tcc
DB_PASSWORD='SENHA_DO_BANCO'
```

Execute os comandos pela ordem abaixo. O primeiro acesso ao Ollama em um volume vazio exige o download do modelo. [CLI do Ollama](https://docs.ollama.com/cli)

```bash
docker compose config --quiet
docker compose up -d --build
docker compose exec ollama ollama pull qwen2.5:1.5b
docker compose ps
docker compose logs --tail 100 springboot
curl -fsS -o /dev/null -w '%{http_code}\n' http://127.0.0.1:8080/home
```

Quando a abertura de `/home` devolver HTTP 200, acesse `http://127.0.0.1:8080/home` no navegador. O comando `docker compose stop` para os serviços e mantém os volumes. Na próxima execução, `docker compose up -d` usa o banco e o modelo já gravados.

### 3.3 Preparar a VPS e o usuário de deploy

O exemplo usa uma VPS Ubuntu com acesso administrativo. Substitua `tcc-deploy` pelo nome da conta de publicação escolhida e `vps.example.com` pelo IP ou nome DNS da VPS. O workflow atual conecta na porta SSH padrão 22. Permita o acesso SSH no firewall e nas regras de rede do provedor para que o runner alcance o servidor.

1. **Instale os programas do servidor.** Instale `openssh-server`, `rsync` e `curl`. Instale o Docker Engine pelo repositório oficial da distribuição, com Docker Buildx e o plugin Docker Compose. [Docker Engine no Ubuntu](https://docs.docker.com/engine/install/ubuntu/) · [OpenSSH Server no Ubuntu](https://ubuntu.com/server/docs/how-to/security/openssh-server/)

```bash
sudo apt update
sudo apt install openssh-server rsync curl
# Após configurar o repositório APT oficial do Docker
sudo apt install docker-ce docker-ce-cli containerd.io \
  docker-buildx-plugin docker-compose-plugin
sudo systemctl enable --now ssh docker
sudo docker version
docker compose version
```

2. **Prepare a conta.** Crie a conta se ela ainda não existir. O workflow usa Docker em uma sessão SSH sem `sudo`, portanto a conta precisa de acesso ao daemon e de escrita em `/opt/projeto-tcc`. No arranjo abaixo, o grupo `docker` concede privilégios equivalentes a root. Abra uma nova sessão da conta após alterar seus grupos. [Permissões pós-instalação do Docker](https://docs.docker.com/engine/install/linux-postinstall/)

```bash
sudo adduser --disabled-password --gecos '' tcc-deploy
sudo usermod -aG docker tcc-deploy
sudo install -d -o tcc-deploy -g tcc-deploy -m 0750 /opt/projeto-tcc
sudo install -d -o tcc-deploy -g tcc-deploy -m 0700 \
  /home/tcc-deploy/.ssh
```

3. **Instale a chave de acesso.** No computador administrativo, gere um par Ed25519 dedicado ao GitHub Actions. O workflow valida o uso da chave sem senha. Transfira o arquivo `.pub` pela sua conta administrativa e acrescente seu conteúdo a `/home/tcc-deploy/.ssh/authorized_keys`. Substitua `admin` pelo seu usuário administrativo. O comando `tee -a` preserva as chaves já autorizadas.

```bash
# No computador administrativo
ssh-keygen -t ed25519 -N '' -C 'github-actions-projeto-tcc' \
  -f "$HOME/.ssh/projeto-tcc-actions"
scp "$HOME/.ssh/projeto-tcc-actions.pub" \
  admin@vps.example.com:projeto-tcc-actions.pub

# Na VPS, com a conta administrativa
sudo -u tcc-deploy touch /home/tcc-deploy/.ssh/authorized_keys
sudo chmod 600 /home/tcc-deploy/.ssh/authorized_keys
sudo -u tcc-deploy tee -a /home/tcc-deploy/.ssh/authorized_keys \
  < "$HOME/projeto-tcc-actions.pub" >/dev/null
```

4. **Verifique a identidade da VPS.** Na console administrativa da VPS, obtenha a impressão digital da chave do servidor. No computador administrativo, colete a linha `known_hosts` para o mesmo endereço que será cadastrado como `VPS_HOST`. Compare as impressões digitais antes de usar a linha. O exemplo usa a chave Ed25519 padrão do OpenSSH. [Chaves e autenticação do OpenSSH](https://ubuntu.com/server/docs/how-to/security/openssh-server/)

```bash
# Na VPS
sudo ssh-keygen -lf /etc/ssh/ssh_host_ed25519_key.pub

# No computador administrativo
ssh-keyscan -t ed25519 vps.example.com > "$HOME/.ssh/projeto-tcc-host"
ssh-keygen -lf "$HOME/.ssh/projeto-tcc-host"
```

5. **Teste o mesmo caminho que o workflow usará.** A conexão deve autenticar com a chave, reconhecer o host e confirmar o acesso ao Docker e à pasta do projeto.

```bash
ssh -i "$HOME/.ssh/projeto-tcc-actions" \
  -o IdentitiesOnly=yes -o StrictHostKeyChecking=yes \
  -o UserKnownHostsFile="$HOME/.ssh/projeto-tcc-host" \
  tcc-deploy@vps.example.com \
  'docker info >/dev/null && docker compose version &&
   command -v rsync && test -w /opt/projeto-tcc'
```

### 3.4 Configurar o ambiente e os segredos

Na VPS, como usuário de deploy, crie ou edite `/opt/projeto-tcc/.env` com `DB_NAME`, `DB_USER` e `DB_PASSWORD`, conforme o exemplo da seção 3.2. Use `umask 077` antes de criar o arquivo e `chmod 600 .env` ao final. O Compose injeta esses valores no PostgreSQL e na conexão do Spring. Em uma VPS com banco existente, mantenha as credenciais compatíveis com o volume já inicializado. A imagem usa as variáveis `POSTGRES_*` para criar o usuário e o banco apenas na inicialização de um diretório vazio. [Imagem oficial do PostgreSQL](https://github.com/docker-library/docs/blob/master/postgres/README.md#environment-variables)

No repositório GitHub, abra **Settings → Secrets and variables → Actions → New repository secret**. Cadastre os valores a seguir. [Segredos no GitHub Actions](https://docs.github.com/en/actions/how-tos/write-workflows/choose-what-workflows-do/use-secrets)

| Secret | Valor |
| --- | --- |
| `VPS_HOST` | IP ou nome DNS da VPS, como `vps.example.com`. |
| `VPS_USER` | Usuário Linux que executa o deploy, como `tcc-deploy`. |
| `VPS_SSH_KEY` | Conteúdo completo da chave privada `projeto-tcc-actions`, com cabeçalho, rodapé e quebras de linha. |
| `VPS_KNOWN_HOSTS` | Conteúdo de `projeto-tcc-host` com a linha do host cuja impressão digital foi conferida. |

Guarde a chave privada no secret e as credenciais do banco no `.env` da VPS. O repositório guarda o workflow e o Compose. A sincronização preserva o `.env` e os arquivos de `uploads/` mantidos no servidor.

### 3.5 Entrega contínua com GitHub Actions

Depois de revisar e gravar as alterações em um commit da branch `producao`, envie a branch ao GitHub. Cada push nessa branch dispara o workflow **Deploy TCC**. Em **Actions → Deploy TCC**, acompanhe as etapas da execução. A opção **Run workflow** permite escolher `producao` quando o workflow com `workflow_dispatch` está presente na branch padrão. [Execução manual no GitHub Actions](https://docs.github.com/en/actions/how-tos/manage-workflow-runs/manually-run-a-workflow)

```bash
git push origin producao
```

| Etapa | O que acontece |
| --- | --- |
| Checkout e Java | O runner baixa o commit, instala ou seleciona Temurin 21 e configura o cache Maven com `actions/setup-java@v5`. |
| Empacotamento | `./mvnw -B -ntp clean package -DskipTests` produz o JAR. O workflow o copia para `.deploy/app.jar` e gera `.deploy/Dockerfile` e `.deploy/compose.yml`. |
| Acesso remoto | O runner instala a chave privada e o `known_hosts` em `~/.ssh`. O cliente exige a identidade do host e usa keepalive na conexão. |
| Sincronização | O `rsync` envia o projeto e a pasta `.deploy` para `/opt/projeto-tcc`. A sincronização remove arquivos obsoletos apenas de `src/` e exclui `.git/`, `.env`, `target/` e `uploads/`. |
| Atualização | A VPS valida a configuração, executa `docker compose up --detach --build` com os dois arquivos Compose e lista os serviços com `docker compose ps`. O grupo de concorrência mantém um deploy ativo por vez. |

```text
Push em producao → GitHub Actions → Maven → JAR em .deploy
                                               ↓ rsync por SSH
Navegador ← Spring Boot ← Docker Compose ← /opt/projeto-tcc
```

O Compose combina `docker-compose.yml` com `.deploy/compose.yml`. O segundo arquivo coloca o contexto de build do Spring em `.deploy`. Nesse contexto, o Dockerfile de execução copia `app.jar` e usa apenas a imagem JRE 21. Essa configuração deixa os downloads de dependências e a compilação no runner. A etapa `export-jar` pertence ao workflow anterior. [Workflow Deploy TCC](../.github/workflows/deploy.yml) · [Combinação de arquivos Compose](https://docs.docker.com/compose/how-tos/multiple-compose-files/merge/) · [Setup Java](https://github.com/actions/setup-java/tree/v5)

### 3.6 Validar a primeira publicação

Depois de uma execução bem-sucedida em **Actions**, conecte-se à VPS como usuário de deploy. Use os dois arquivos Compose para verificar o estado publicado. Baixe o modelo se o volume do Ollama estiver vazio.

```bash
cd /opt/projeto-tcc
docker compose -f docker-compose.yml -f .deploy/compose.yml ps
docker compose -f docker-compose.yml -f .deploy/compose.yml \
  exec ollama ollama pull qwen2.5:1.5b
docker compose -f docker-compose.yml -f .deploy/compose.yml \
  exec ollama ollama list
docker compose -f docker-compose.yml -f .deploy/compose.yml \
  logs --tail 100 springboot
curl -fsS -o /dev/null -w '%{http_code}\n' http://127.0.0.1:8080/home
```

A validação inclui os três contêineres em execução, PostgreSQL saudável, modelo instalado e HTTP 200 em `/home`. Em seguida, abra a aplicação e teste login e redirecionamento dos perfis. Para conferir as integrações, vincule um atleta de teste, cadastre um treino, registre progresso e processe um exame com dados fictícios. O workflow termina após `docker compose ps`. As verificações HTTP e por perfil são executadas à parte.

### 3.7 Acesso remoto e domínio HTTPS

Para validar pelo navegador do seu computador, abra um túnel SSH para o loopback da VPS. Mantenha a sessão aberta e acesse `http://127.0.0.1:18080/home`.

```bash
ssh -i "$HOME/.ssh/projeto-tcc-actions" \
  -o IdentitiesOnly=yes -o StrictHostKeyChecking=yes \
  -o UserKnownHostsFile="$HOME/.ssh/projeto-tcc-host" \
  -N -L 18080:127.0.0.1:8080 tcc-deploy@vps.example.com
```

Para oferecer acesso público, aponte um domínio para a VPS e configure um proxy reverso no host. O exemplo usa Caddy e mantém o Spring em `127.0.0.1:8080`. Com o domínio apontado corretamente e as portas 80 e 443 acessíveis, o Caddy pode obter e renovar o certificado HTTPS. Instale o Caddy no host e adicione este bloco a `/etc/caddy/Caddyfile`, substituindo o domínio. [Instalação do Caddy](https://caddyserver.com/docs/install#debian-ubuntu-raspbian) · [HTTPS automático](https://caddyserver.com/docs/automatic-https)

```caddyfile
tcc.example.com {
    reverse_proxy 127.0.0.1:8080
}
```

```bash
sudo caddy validate --config /etc/caddy/Caddyfile
sudo systemctl reload caddy
curl -fsS -o /dev/null -w '%{http_code}\n' https://tcc.example.com/home
```

Abra o domínio no navegador e confira a página inicial, o login e os redirecionamentos em HTTPS.

### 3.8 Operação, backup e reversão

Para operar a versão publicada, entre em `/opt/projeto-tcc` e use os dois arquivos Compose. `logs -f springboot` acompanha a aplicação. `restart springboot` reinicia a imagem existente. `up -d --build springboot` refaz a imagem com o último JAR sincronizado. `stop` para os serviços e mantém os volumes. Ao publicar uma nova versão, o workflow reutiliza os volumes do projeto `projeto-tcc`.

Antes de uma atualização que altere a persistência, salve um dump do banco e uma cópia dos uploads em uma pasta protegida fora de `/opt/projeto-tcc`. O exemplo usa o usuário do próprio contêiner do PostgreSQL para solicitar o dump.

```bash
cd /opt/projeto-tcc
umask 077
backup_dir="$HOME/backups/projeto-tcc/$(date +%Y%m%d-%H%M%S)"
mkdir -p "$backup_dir"
docker compose -f docker-compose.yml -f .deploy/compose.yml \
  exec -T postgres sh -c \
  'exec pg_dump -Fc -U "$POSTGRES_USER" "$POSTGRES_DB"' \
  > "$backup_dir/banco.dump"
docker compose -f docker-compose.yml -f .deploy/compose.yml \
  cp springboot:/app/uploads "$backup_dir/uploads"
```

Para reverter uma alteração de código, use `git revert` no commit responsável e publique o novo commit em `producao`. O GitHub Actions compila essa revisão e atualiza a VPS. A reversão do código mantém o estado salvo no volume do PostgreSQL. Uma recuperação de dados ou esquema exige tratamento próprio a partir do backup.

### 3.9 Diagnóstico dos erros de publicação

| Sintoma | Onde conferir |
| --- | --- |
| `target stage "export-jar" could not be found` | Confira o commit do job em **Actions**. O workflow atual executa `./mvnw` e gera `.deploy`. Dispare uma execução do commit que contém o workflow corrigido. |
| `Connection reset by peer` e `Broken pipe` com código 255 | Essas mensagens registram a queda da sessão SSH. Confira o horário nos logs de SSH, Docker e kernel da VPS para investigar a origem. |
| `Permission denied (publickey)` ou erro de host key | Confira `VPS_USER`, `VPS_SSH_KEY`, `authorized_keys` e `VPS_KNOWN_HOSTS`. Repita o teste SSH da seção 3.3. |
| Erro no socket Docker ou ao escrever em `/opt/projeto-tcc` | Confira `docker info` como o usuário de deploy e as permissões de escrita da própria conta. Abra uma nova sessão após atualizar o grupo `docker`. |
| PostgreSQL indisponível ou falha de autenticação | Confira a saúde do serviço, os logs de `postgres` e a correspondência do `.env` com as credenciais usadas pelo volume existente. |
| Ollama acessível e modelo ausente | Execute `ollama list` dentro de `ollama` e baixe `qwen2.5:1.5b` pelo comando da seção 3.6. |
| O navegador externo não acessa a porta 8080 | Teste `/home` na própria VPS. O acesso externo segue o túnel SSH ou o domínio com proxy da seção 3.7. |

Para investigar uma queda recente, rode na VPS os comandos abaixo. Ajuste o intervalo ao horário registrado no GitHub Actions.

```bash
sudo journalctl --since '30 minutes ago' --no-pager -u ssh -u sshd -u docker
sudo journalctl -k --since '30 minutes ago' --no-pager
```

### 3.10 Código do Dockerfile e da composição dos serviços

#### 3.10.1 Compilação e execução pelo Dockerfile da raiz

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

`FROM ... AS build` define o estágio de compilação. `WORKDIR /app` concentra os arquivos na pasta de trabalho. `COPY pom.xml .` e `COPY src ./src` enviam o necessário para gerar o pacote. `RUN mvn clean package -DskipTests` compila e empacota a aplicação com a execução dos testes desativada. O segundo `FROM` parte do JRE. `COPY --from=build` transfere o JAR produzido para a imagem final. `EXPOSE` declara a porta da aplicação e `ENTRYPOINT` executa `java -jar app.jar` quando o contêiner começa. [Dockerfile linhas 2–18](../Dockerfile#L2-L18)

#### 3.10.2 PostgreSQL e teste de disponibilidade

```yaml
postgres:
  image: postgres:16
  container_name: tcc_postgres
  restart: unless-stopped

  environment:
    POSTGRES_DB: ${DB_NAME}
    POSTGRES_USER: ${DB_USER}

    POSTGRES_PASSWORD: ${DB_PASSWORD}

  volumes:
    - postgres_data:/var/lib/postgresql/data

  healthcheck:
    test: ["CMD-SHELL", "pg_isready -U $$POSTGRES_USER -d $$POSTGRES_DB"]
    interval: 10s
    timeout: 5s
    retries: 5

  networks:
    - tcc_network
```

`environment` passa nome, usuário e senha do `.env` ao PostgreSQL. `volumes` liga a pasta de dados do banco ao volume nomeado, que é reutilizado entre recriações. No `healthcheck`, `$$` preserva o símbolo de variável para expansão dentro do contêiner. `pg_isready` verifica se o processo do banco aceita conexões. `interval`, `timeout` e `retries` controlam a frequência, o limite e a tolerância a falhas do teste. [docker-compose.yml linhas 6–27](../docker-compose.yml#L6-L27)

#### 3.10.3 Aplicação, variáveis e dependência do banco

```yaml
springboot:
  build: .
  container_name: tcc_springboot
  restart: unless-stopped

  ports:
    - "127.0.0.1:8080:8080"

  environment:
    SERVER_PORT: 8080
    JAVA_TOOL_OPTIONS: "-Xms256m -Xmx1024m"

    SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${DB_NAME}
    SPRING_DATASOURCE_USERNAME: ${DB_USER}
    SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}

    OPENAI_API_KEY: ${OPENAI_API_KEY:-false}

    APP_OLLAMA_BASE_URL: http://ollama:11434
    APP_OLLAMA_MODEL_NAME: qwen2.5:1.5b
    APP_OLLAMA_START_LOCAL: "false"

  depends_on:
    postgres:
      condition: service_healthy

  volumes:
    - uploads_data:/app/uploads

  networks:
    - tcc_network
```

`build: .` usa o Dockerfile da raiz quando o Compose é executado com o arquivo base. `ports` encaminha a porta do contêiner ao loopback da máquina. A URL JDBC usa `postgres` como nome do serviço, e o cliente de IA usa `ollama` na mesma rede. `service_healthy` faz o Compose aguardar o teste do banco antes de iniciar a aplicação. `JAVA_TOOL_OPTIONS` define os limites de heap e `uploads_data` monta a pasta de uploads. A configuração de produção da seção 3.11 troca o contexto de build. [docker-compose.yml linhas 29–59](../docker-compose.yml#L29-L59)

#### 3.10.4 Serviço de IA, rede e volumes nomeados

```yaml
  ollama:
    image: ollama/ollama:latest
    container_name: tcc_ollama
    restart: unless-stopped

    environment:
      OLLAMA_NUM_PARALLEL: "1"
      OLLAMA_MAX_LOADED_MODELS: "1"

    volumes:
      - ollama_data:/root/.ollama

    networks:
      - tcc_network

volumes:
  postgres_data:
  uploads_data:
  ollama_data:

networks:
  tcc_network:
    driver: bridge
```

`OLLAMA_NUM_PARALLEL` controla quantas solicitações o serviço pode tratar em paralelo por modelo. `OLLAMA_MAX_LOADED_MODELS` limita a quantidade de modelos carregados. `ollama_data:/root/.ollama` mantém o modelo baixado no volume. As chaves de nível raiz `volumes` e `networks` registram os recursos nomeados usados pelos serviços. A rede `bridge` permite resolver os serviços pelos nomes do Compose. [docker-compose.yml linhas 61–83](../docker-compose.yml#L61-L83)

### 3.11 Código do workflow de entrega contínua

#### 3.11.1 Gatilhos, concorrência e ferramenta de build

```yaml
name: Deploy TCC

on:
  push:
    branches:
      - producao
  workflow_dispatch:

permissions:
  contents: read

concurrency:
  group: deploy-tcc-vps
  cancel-in-progress: false

jobs:
  deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Baixar projeto
        uses: actions/checkout@v4

      - name: Configurar Java 21
        uses: actions/setup-java@v5
        with:
          distribution: temurin
          java-version: '21'
          cache: maven
```

`on.push.branches` limita o gatilho automático à branch `producao`. `workflow_dispatch` habilita a execução manual conforme a configuração do GitHub. `contents: read` dá ao job acesso de leitura ao código, e `concurrency` agrupa as execuções da VPS preservando a execução que já está em andamento. O checkout obtém a revisão do job. `setup-java` seleciona Temurin 21 e configura o cache de dependências do Maven. [deploy.yml linhas 1–29](../.github/workflows/deploy.yml#L1-L29)

#### 3.11.2 Empacotar o JAR e gerar os arquivos de produção

```yaml
- name: Compilar o JAR no GitHub Actions
  run: |
    ./mvnw -B -ntp clean package -DskipTests
    mkdir -p .deploy
    cp target/*.jar .deploy/app.jar

    cat > .deploy/Dockerfile <<'EOF'
    FROM eclipse-temurin:21-jre
    WORKDIR /app
    COPY app.jar app.jar
    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "app.jar"]
    EOF

    cat > .deploy/compose.yml <<'EOF'
    services:
      springboot:
        build:
          context: .deploy
          dockerfile: Dockerfile
    EOF
```

O bloco `run: |` executa várias linhas Bash. `./mvnw` usa a versão indicada pelo Maven Wrapper. `-B` seleciona modo em lote e `-ntp` suprime o progresso de transferências. `clean package` recria o pacote. O comando `cp` coloca o JAR em um caminho fixo para a etapa de transferência.

Cada `<<'EOF'` escreve as linhas seguintes literalmente até o marcador `EOF`. O primeiro arquivo gerado contém apenas o ambiente de execução com JRE e o JAR. O segundo preserva os serviços do Compose base e aponta o contexto de build do Spring para `.deploy`. Assim, o build feito na VPS copia o pacote que o runner já gerou. [deploy.yml linhas 31–51](../.github/workflows/deploy.yml#L31-L51)

#### 3.11.3 Configurar as credenciais SSH do runner

```yaml
- name: Configurar o acesso SSH
  env:
    VPS_SSH_KEY: ${{ secrets.VPS_SSH_KEY }}
    VPS_KNOWN_HOSTS: ${{ secrets.VPS_KNOWN_HOSTS }}
  run: |
    if [ -z "$VPS_SSH_KEY" ]; then
      echo "VPS_SSH_KEY ausente"
      exit 1
    fi

    if [ -z "$VPS_KNOWN_HOSTS" ]; then
      echo "VPS_KNOWN_HOSTS ausente"
      exit 1
    fi

    mkdir -p ~/.ssh
    chmod 700 ~/.ssh

    printf '%s\n' "$VPS_SSH_KEY" > ~/.ssh/id_ed25519
    printf '%s\n' "$VPS_KNOWN_HOSTS" > ~/.ssh/known_hosts
    chmod 600 ~/.ssh/id_ed25519 ~/.ssh/known_hosts

    # Verifica se a chave pode ser usada sem senha.
    ssh-keygen -y -P "" -f ~/.ssh/id_ed25519 > /dev/null
```

`env` entrega a chave privada e a identidade do servidor à etapa a partir dos secrets. Os testes `-z` encerram a etapa se algum valor estiver vazio. `printf` grava os valores em arquivos individuais. `chmod` restringe a pasta e os arquivos ao usuário do runner. `ssh-keygen -y -P ""` verifica se a chave privada pode ser aberta com senha vazia. A saída da chave pública segue para `/dev/null`. [deploy.yml linhas 53–76](../.github/workflows/deploy.yml#L53-L76)

#### 3.11.4 Transferir os arquivos e aplicar o Compose na VPS

```bash
servidor="$VPS_USER@$VPS_HOST"
opcoes_rsync=(
  --archive --compress
  --exclude='.git/'
  --exclude='.env'
  --exclude='target/'
  --exclude='uploads/'
  --rsh="ssh -o StrictHostKeyChecking=yes -o ServerAliveInterval=15 -o ServerAliveCountMax=3"
)

rsync "${opcoes_rsync[@]}" --exclude='/src/' ./ "$servidor:/opt/projeto-tcc/"
rsync "${opcoes_rsync[@]}" --delete ./src/ "$servidor:/opt/projeto-tcc/src/"
```

O primeiro `rsync` transfere a raiz para `/opt/projeto-tcc/`, inclusive `.deploy`, e deixa `src/` para a segunda chamada. O segundo comando sincroniza o conteúdo de `src/` e usa `--delete` para remover arquivos obsoletos nesse diretório de destino. Os filtros protegem `.env` e os demais caminhos excluídos. `StrictHostKeyChecking=yes` exige a identidade registrada da VPS. As opções `ServerAlive` controlam as mensagens de keepalive e o limite de respostas ausentes. [deploy.yml linhas 94–105](../.github/workflows/deploy.yml#L94-L105)

```bash
servidor="$VPS_USER@$VPS_HOST"
ssh -o StrictHostKeyChecking=yes -o ServerAliveInterval=15 -o ServerAliveCountMax=3 "$servidor" '
  set -e
  cd /opt/projeto-tcc

  docker compose -f docker-compose.yml -f .deploy/compose.yml config --quiet
  docker compose -f docker-compose.yml -f .deploy/compose.yml up --detach --build
  docker compose -f docker-compose.yml -f .deploy/compose.yml ps
'
```

O texto entre aspas simples é executado na VPS. `set -e` faz a sequência parar se um comando simples falhar. `cd` coloca a execução na raiz sincronizada. `config --quiet` valida a combinação dos dois arquivos. `up --detach --build` monta a imagem de execução e atualiza os serviços em segundo plano. `ps` mostra o estado final dos contêineres para o log do job. [deploy.yml linhas 112–120](../.github/workflows/deploy.yml#L112-L120)
