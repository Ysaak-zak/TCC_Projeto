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
