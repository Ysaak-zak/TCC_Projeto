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
