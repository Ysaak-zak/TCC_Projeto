const campoBusca = document.getElementById('search');
const avisoSemResultados = document.getElementById('empty');

campoBusca?.addEventListener('input', () => {
    const termo = campoBusca.value.trim().toLowerCase();
    const atletas = document.querySelectorAll('.athlete-row');
    let encontrouAtleta = false;

    for (const atleta of atletas) {
        const nomeECpf = `${atleta.dataset.name} ${atleta.dataset.cpf}`.toLowerCase();
        const corresponde = nomeECpf.includes(termo);

        atleta.classList.toggle('hidden', !corresponde);
        encontrouAtleta = encontrouAtleta || corresponde;
    }

    avisoSemResultados.classList.toggle('hidden', atletas.length === 0 || encontrouAtleta);
});
