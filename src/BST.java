// Integrantes: Danilo Oliveira Santos - RA 10410905
//              Miguel Batista da Silva - RA 10736250

import java.util.*;

public class BST {
    private NoBST raiz;
    private int tamanho;

    public int getTamanho() { return tamanho; }
    public boolean estaVazia() { return raiz == null; }

    public boolean inserir(ProgramaNetFlix programa) {
        if (programa == null) return false;
        if (raiz == null) { raiz = new NoBST(programa); tamanho++; return true; }
        NoBST atual = raiz;
        while (true) {
            int cmp = programa.getId().compareToIgnoreCase(atual.getDado().getId());
            if (cmp == 0) return false;
            if (cmp < 0) {
                if (atual.getEsquerdo() == null) {
                    atual.setEsquerdo(new NoBST(programa)); tamanho++; return true;
                }
                atual = atual.getEsquerdo();
            } else {
                if (atual.getDireito() == null) {
                    atual.setDireito(new NoBST(programa)); tamanho++; return true;
                }
                atual = atual.getDireito();
            }
        }
    }

    public ResultadoBusca buscar(String id) {
        long inicio = System.nanoTime();
        int comparacoes = 0;
        NoBST atual = raiz;
        while (atual != null) {
            comparacoes++;
            int cmp = id.compareToIgnoreCase(atual.getDado().getId());
            if (cmp == 0) {
                return new ResultadoBusca(atual.getDado(), comparacoes, System.nanoTime() - inicio);
            }
            atual = cmp < 0 ? atual.getEsquerdo() : atual.getDireito();
        }
        return new ResultadoBusca(null, comparacoes, System.nanoTime() - inicio);
    }

    public boolean remover(String id) {
        if (buscar(id).getPrograma() == null) return false;
        raiz = removerRec(raiz, id);
        tamanho--;
        return true;
    }

    private NoBST removerRec(NoBST no, String id) {
        if (no == null) return null;
        int cmp = id.compareToIgnoreCase(no.getDado().getId());
        if (cmp < 0) no.setEsquerdo(removerRec(no.getEsquerdo(), id));
        else if (cmp > 0) no.setDireito(removerRec(no.getDireito(), id));
        else {
            if (no.getEsquerdo() == null) return no.getDireito();
            if (no.getDireito() == null) return no.getEsquerdo();
            NoBST sucessor = menor(no.getDireito());
            no.setDado(sucessor.getDado());
            no.setDireito(removerRec(no.getDireito(), sucessor.getDado().getId()));
        }
        return no;
    }

    private NoBST menor(NoBST no) {
        while (no.getEsquerdo() != null) no = no.getEsquerdo();
        return no;
    }

    public int altura() { return alturaRec(raiz); }
    private int alturaRec(NoBST no) {
        if (no == null) return -1;
        return 1 + Math.max(alturaRec(no.getEsquerdo()), alturaRec(no.getDireito()));
    }

    public List<ProgramaNetFlix> emOrdem() {
        List<ProgramaNetFlix> lista = new ArrayList<>(); emOrdemRec(raiz, lista); return lista;
    }
    private void emOrdemRec(NoBST no, List<ProgramaNetFlix> lista) {
        if (no != null) { emOrdemRec(no.getEsquerdo(), lista); lista.add(no.getDado()); emOrdemRec(no.getDireito(), lista); }
    }
    public List<ProgramaNetFlix> preOrdem() {
        List<ProgramaNetFlix> lista = new ArrayList<>(); preOrdemRec(raiz, lista); return lista;
    }
    private void preOrdemRec(NoBST no, List<ProgramaNetFlix> lista) {
        if (no != null) { lista.add(no.getDado()); preOrdemRec(no.getEsquerdo(), lista); preOrdemRec(no.getDireito(), lista); }
    }
    public List<ProgramaNetFlix> posOrdem() {
        List<ProgramaNetFlix> lista = new ArrayList<>(); posOrdemRec(raiz, lista); return lista;
    }
    private void posOrdemRec(NoBST no, List<ProgramaNetFlix> lista) {
        if (no != null) { posOrdemRec(no.getEsquerdo(), lista); posOrdemRec(no.getDireito(), lista); lista.add(no.getDado()); }
    }
    public List<ProgramaNetFlix> largura() {
        List<ProgramaNetFlix> lista = new ArrayList<>();
        if (raiz == null) return lista;
        Queue<NoBST> fila = new LinkedList<>(); fila.add(raiz);
        while (!fila.isEmpty()) {
            NoBST no = fila.remove(); lista.add(no.getDado());
            if (no.getEsquerdo() != null) fila.add(no.getEsquerdo());
            if (no.getDireito() != null) fila.add(no.getDireito());
        }
        return lista;
    }

    // Análise 1: títulos de crime com classificação TV-14, ordenados pela nota TMDB.
    public List<ProgramaNetFlix> analiseCrimeTV14() {
        List<ProgramaNetFlix> r = new ArrayList<>();
        for (ProgramaNetFlix p : emOrdem())
            if (p.getAgeCertification().equalsIgnoreCase("TV-14") &&
                p.getGenres().toLowerCase().contains("crime")) r.add(p);
        r.sort(Comparator.comparingDouble(ProgramaNetFlix::getTmdbScore).reversed());
        return limitar(r, 10);
    }

    // Análise 2: títulos com maior nota TMDB, considerando um mínimo de votos no IMDb.
    public List<ProgramaNetFlix> analiseMaisBemAvaliados(int minimoVotos, int quantidade) {
        List<ProgramaNetFlix> r = new ArrayList<>();
        for (ProgramaNetFlix p : largura())
            if (p.getImdbVotes() >= minimoVotos) r.add(p);
        r.sort(Comparator.comparingDouble(ProgramaNetFlix::getTmdbScore).reversed());
        return limitar(r, quantidade);
    }

    // Análise 3: títulos mais populares de cada tipo, evitando resultados pouco avaliados.
    public List<ProgramaNetFlix> analisePopularesPorTipo(String tipo, int quantidade) {
        List<ProgramaNetFlix> r = new ArrayList<>();
        for (ProgramaNetFlix p : preOrdem())
            if (p.getType().equalsIgnoreCase(tipo) && p.getTmdbScore() >= 6.0) r.add(p);
        r.sort(Comparator.comparingDouble(ProgramaNetFlix::getTmdbPopularity).reversed());
        return limitar(r, quantidade);
    }

    // Análise 4: filmes longos (>= 120 min) bem avaliados no IMDb.
    public List<ProgramaNetFlix> analiseFilmesLongos(int quantidade) {
        List<ProgramaNetFlix> r = new ArrayList<>();
        for (ProgramaNetFlix p : posOrdem())
            if (p.getType().equalsIgnoreCase("MOVIE") && p.getRuntime() >= 120 && p.getImdbScore() >= 7.0)
                r.add(p);
        r.sort(Comparator.comparingDouble(ProgramaNetFlix::getImdbScore).reversed()
            .thenComparing(Comparator.comparingInt(ProgramaNetFlix::getImdbVotes).reversed()));
        return limitar(r, quantidade);
    }

    // Análise 5: séries com pelo menos duas temporadas e nota TMDB acima da média geral.
    public List<ProgramaNetFlix> analiseSeriesAcimaDaMedia(int quantidade) {
        List<ProgramaNetFlix> todos = emOrdem();
        if (todos.isEmpty()) return new ArrayList<>();
        double media = todos.stream().mapToDouble(ProgramaNetFlix::getTmdbScore).average().orElse(0);
        List<ProgramaNetFlix> r = new ArrayList<>();
        for (ProgramaNetFlix p : largura())
            if (p.getType().equalsIgnoreCase("SHOW") && p.getSeasons() >= 2 && p.getTmdbScore() > media) r.add(p);
        r.sort(Comparator.comparingDouble(ProgramaNetFlix::getTmdbScore).reversed());
        return limitar(r, quantidade);
    }

    private List<ProgramaNetFlix> limitar(List<ProgramaNetFlix> lista, int n) {
        return new ArrayList<>(lista.subList(0, Math.min(Math.max(n, 0), lista.size())));
    }

    public boolean salvar(String caminho) throws java.io.IOException {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(caminho))) {
            bw.write("id,title,type,description,release_year,age_certification,runtime,genres,production_countries,seasons,imdb_id,imdb_score,imdb_votes,tmdb_popularity,tmdb_score");
            bw.newLine();
            for (ProgramaNetFlix p : emOrdem()) { bw.write(p.toCsv()); bw.newLine(); }
        }
        return true;
    }

    public void liberar() { raiz = null; tamanho = 0; }
    public static class ResultadoBusca {
        private final ProgramaNetFlix programa; private final int comparacoes; private final long nanos;
        public ResultadoBusca(ProgramaNetFlix p, int c, long n) { programa=p; comparacoes=c; nanos=n; }
        public ProgramaNetFlix getPrograma() { return programa; }
        public int getComparacoes() { return comparacoes; }
        public long getNanos() { return nanos; }
    }
}
