const seletorSemana = document.getElementById('semanaId');
const camposExercicio = document.getElementById('exercicio-campos');
const seletorTreinamento = document.getElementById('treinamentoId');

seletorSemana.addEventListener('change', () => {
    camposExercicio.disabled = true;
    seletorTreinamento.value = '';
});
