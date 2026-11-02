// Config única do Tailwind (CDN) para todo o sistema.
// Cada papel (médico / treinador / atleta) usa a MESMA escala de tokens
// (50/100/500/600/700/800), só troca o matiz — assim qualquer tela nova
// já nasce consistente com as demais, e um ajuste de paleta aqui vale
// para o projeto inteiro.
tailwind.config = {
    theme: {
        extend: {
            colors: {
                // Marca do sistema (telas públicas: login, seleção de cadastro) — neutra, não pertence a nenhum papel
                brand: { 50: '#f4f6f5', 100: '#e6ebe8', 500: '#334b41', 600: '#233a30', 700: '#182a22', 800: '#0f1c17' },
                // Módulo médico
                medico: { 50: '#eafaf2', 100: '#d3f3e3', 200: '#a7e7c7', 300: '#7bdbac', 500: '#1f9d5c', 600: '#178050', 700: '#0f5c3c', 800: '#0b4530' },
                // Módulo treinador
                treinador: { 50: '#fff8ec', 100: '#feecc7', 200: '#fcd98f', 300: '#f9c257', 500: '#e0a020', 600: '#b8790f', 700: '#8f5b0c', 800: '#6b4409' },
                // Módulo atleta
                atleta: { 50: '#eef4ff', 100: '#dbe7ff', 200: '#b3ccff', 300: '#8bb1ff', 500: '#3466e0', 600: '#2450bd', 700: '#1c3d94', 800: '#152e70' }
            },
            fontFamily: {
                sans: ['DM Sans', 'sans-serif'],
                display: ['Playfair Display', 'serif']
            }
        }
    }
}
