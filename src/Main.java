// Integrantes: Danilo Oliveira Santos - RA 10410905
//              Miguel Batista da Silva - RA 10736250

import java.io.*;
import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final BST arvore = new BST();

    public static void main(String[] args) {
        int opcao;
        do {
            menu();
            opcao = lerInt("Escolha uma opção: ");
            try {
                switch (opcao) {
                    case 1 -> carregarArquivo();
                    case 2 -> menuAnalises();
                    case 3 -> inserirPrograma();
                    case 4 -> buscarPrograma();
                    case 5 -> removerPrograma();
                    case 6 -> System.out.println("Altura da BST: " + arvore.altura() + " (árvore vazia = -1)");
                    case 7 -> salvarArquivo();
                    case 8 -> { arvore.liberar(); System.out.println("Dados liberados. Aplicação encerrada."); }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Não foi possível concluir a operação: " + e.getMessage());
            }
        } while (opcao != 8);
    }

    private static void menu() {
        System.out.println("\n========== NETFLIX - ÁRVORE BST ==========");
        System.out.println("1 - Ler dados de arquivo");
        System.out.println("2 - Análises de dados");
        System.out.println("3 - Inserir programa");
        System.out.println("4 - Buscar programa");
        System.out.println("5 - Remover programa");
        System.out.println("6 - Exibir altura da árvore");
        System.out.println("7 - Salvar dados em arquivo");
        System.out.println("8 - Encerrar aplicação");
    }


    private static void carregarArquivo() throws IOException {
        System.out.print("Nome/caminho do arquivo CSV (ex.: titles.csv): ");
        String caminho = sc.nextLine().trim();
        int inseridos = 0, descartados = 0, duplicados = 0;
        String conteudo = java.nio.file.Files.readString(java.nio.file.Path.of(caminho));
        List<List<String>> registros = parseCsvCompleto(conteudo);
        if (registros.isEmpty()) { System.out.println("Arquivo vazio."); return; }
        for (int i = 1; i < registros.size(); i++) {
            try {
                List<String> c = registros.get(i);
                if (c.size() != 15 || c.stream().anyMatch(v -> v == null || v.trim().isEmpty())) {
                    descartados++; continue;
                }
                ProgramaNetFlix p = criarPrograma(c);
                if (arvore.inserir(p)) inseridos++; else duplicados++;
            } catch (Exception ex) { descartados++; }
        }
        System.out.println("Leitura concluída. Inseridos: " + inseridos +
            " | Descartados por dados incompletos/inválidos: " + descartados +
            " | IDs duplicados: " + duplicados +
            " | Total na BST: " + arvore.getTamanho());
    }

    // Leitor CSV que também reconhece vírgulas e quebras de linha dentro de campos entre aspas.
    private static List<List<String>> parseCsvCompleto(String texto) {
        List<List<String>> registros = new ArrayList<>();
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean aspas = false;
        for (int i = 0; i < texto.length(); i++) {
            char ch = texto.charAt(i);
            if (ch == '"') {
                if (aspas && i + 1 < texto.length() && texto.charAt(i + 1) == '"') {
                    campo.append('"'); i++;
                } else aspas = !aspas;
            } else if (ch == ',' && !aspas) {
                campos.add(campo.toString()); campo.setLength(0);
            } else if ((ch == '\n' || ch == '\r') && !aspas) {
                if (ch == '\r' && i + 1 < texto.length() && texto.charAt(i + 1) == '\n') i++;
                campos.add(campo.toString()); campo.setLength(0);
                if (!(campos.size() == 1 && campos.get(0).isEmpty())) registros.add(campos);
                campos = new ArrayList<>();
            } else campo.append(ch);
        }
        if (campo.length() > 0 || !campos.isEmpty()) {
            campos.add(campo.toString()); registros.add(campos);
        }
        return registros;
    }

    private static ProgramaNetFlix criarPrograma(List<String> c) {
        return new ProgramaNetFlix(c.get(0), c.get(1), c.get(2), c.get(3),
            Integer.parseInt(c.get(4)), c.get(5), Integer.parseInt(c.get(6)), c.get(7),
            c.get(8), (int) Double.parseDouble(c.get(9)), c.get(10), Double.parseDouble(c.get(11)),
            (int) Double.parseDouble(c.get(12)), Double.parseDouble(c.get(13)), Double.parseDouble(c.get(14)));
    }

    private static void menuAnalises() {
        if (arvore.estaVazia()) { System.out.println("Carregue o dataset primeiro."); return; }
        System.out.println("\n--- ANÁLISES ---");
        System.out.println("1 - Top 10 títulos TV-14 que possuem gênero crime (percurso em ordem)");
        System.out.println("2 - Mais bem avaliados no TMDB com mínimo de votos IMDb (largura)");
        System.out.println("3 - Mais populares por tipo, com nota TMDB >= 6 (pré-ordem)");
        System.out.println("4 - Filmes com 120+ minutos e IMDb >= 7 (pós-ordem)");
        System.out.println("5 - Séries com 2+ temporadas e nota TMDB acima da média (largura)");
        int op = lerInt("Análise: ");
        List<ProgramaNetFlix> r;
        switch (op) {
            case 1 -> r = arvore.analiseCrimeTV14();
            case 2 -> r = arvore.analiseMaisBemAvaliados(lerInt("Mínimo de votos no IMDb: "), lerQuantidade());
            case 3 -> {
                System.out.print("Tipo (SHOW ou MOVIE): ");
                String tipo = sc.nextLine().trim().toUpperCase();
                if (!tipo.equals("SHOW") && !tipo.equals("MOVIE")) { System.out.println("Tipo inválido."); return; }
                r = arvore.analisePopularesPorTipo(tipo, lerQuantidade());
            }
            case 4 -> r = arvore.analiseFilmesLongos(lerQuantidade());
            case 5 -> r = arvore.analiseSeriesAcimaDaMedia(lerQuantidade());
            default -> { System.out.println("Análise inválida."); return; }
        }
        exibirLista(r);
    }

    private static void inserirPrograma() {
        System.out.print("Tipo (SHOW ou MOVIE): ");
        String tipo = sc.nextLine().trim().toUpperCase();
        if (!tipo.equals("SHOW") && !tipo.equals("MOVIE")) { System.out.println("Tipo inválido."); return; }
        String prefixo = tipo.equals("SHOW") ? "ts" : "tm";
        int numero = 1;
        for (ProgramaNetFlix p : arvore.emOrdem()) {
            if (p.getId().matches(prefixo + "\\d+")) {
                try { numero = Math.max(numero, Integer.parseInt(p.getId().substring(2)) + 1); }
                catch (NumberFormatException ignored) {}
            }
        }
        String id = prefixo + numero;
        System.out.println("ID gerado: " + id);
        System.out.print("Título: "); String titulo = obrigatorio();
        System.out.print("Descrição: "); String desc = obrigatorio();
        int ano = lerInt("Ano de lançamento: ");
        System.out.print("Classificação etária: "); String cert = obrigatorio();
        int duracao = lerInt("Duração em minutos: ");
        System.out.print("Gêneros (ex.: ['drama', 'crime']): "); String generos = obrigatorio();
        System.out.print("Países (ex.: ['US']): "); String paises = obrigatorio();
        int temporadas = tipo.equals("SHOW") ? lerInt("Temporadas: ") : 0;
        System.out.print("IMDb ID: "); String imdb = obrigatorio();
        double imdbScore = lerDouble("Nota IMDb: ");
        int votos = lerInt("Votos IMDb: ");
        double pop = lerDouble("Popularidade TMDB: ");
        double score = lerDouble("Nota TMDB: ");
        ProgramaNetFlix p = new ProgramaNetFlix(id, titulo, tipo, desc, ano, cert, duracao,
            generos, paises, temporadas, imdb, imdbScore, votos, pop, score);
        if (arvore.inserir(p)) System.out.println("Programa inserido com sucesso.");
        else System.out.println("Não foi possível inserir: ID já existente.");
    }

    private static void buscarPrograma() {
        System.out.print("Informe o ID: "); String id = sc.nextLine().trim();
        BST.ResultadoBusca r = arvore.buscar(id);
        if (r.getPrograma() == null) System.out.println("Programa não encontrado.");
        else System.out.println(r.getPrograma());
        System.out.println("Comparações realizadas: " + r.getComparacoes());
        System.out.printf(Locale.US, "Tempo de busca: %.3f microssegundos%n", r.getNanos() / 1000.0);
    }

    private static void removerPrograma() {
        System.out.print("ID do programa a remover: "); String id = sc.nextLine().trim();
        System.out.println(arvore.remover(id) ? "Programa removido." : "ID não encontrado.");
    }

    private static void salvarArquivo() throws IOException {
        System.out.print("Nome do arquivo de saída (ex.: netflix_atualizado.csv): ");
        String caminho = sc.nextLine().trim();
        if (caminho.isEmpty()) { System.out.println("Nome inválido."); return; }
        arvore.salvar(caminho);
        System.out.println("Arquivo salvo com " + arvore.getTamanho() + " registros.");
    }

    private static void exibirLista(List<ProgramaNetFlix> lista) {
        if (lista.isEmpty()) { System.out.println("Nenhum resultado encontrado."); return; }
        System.out.println("\nResultados: " + lista.size());
        int i = 1;
        for (ProgramaNetFlix p : lista) {
            System.out.printf("%n%d) %s | %s | %s | Ano: %d | TMDB: %.2f | IMDb: %.1f | Popularidade: %.2f%n",
                i++, p.getId(), p.getTitle(), p.getType(), p.getReleaseYear(),
                p.getTmdbScore(), p.getImdbScore(), p.getTmdbPopularity());
        }
    }

    private static int lerQuantidade() {
        int n = lerInt("Quantidade de resultados (mínimo 6): ");
        while (n < 6) n = lerInt("Informe uma quantidade maior ou igual a 6: ");
        return n;
    }
    private static int lerInt(String msg) {
        while (true) {
            try { System.out.print(msg); return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Digite um número inteiro válido."); }
        }
    }
    private static double lerDouble(String msg) {
        while (true) {
            try { System.out.print(msg); return Double.parseDouble(sc.nextLine().trim().replace(',', '.')); }
            catch (NumberFormatException e) { System.out.println("Digite um número válido."); }
        }
    }
    private static String obrigatorio() {
        String s = sc.nextLine().trim();
        while (s.isEmpty()) { System.out.print("Campo obrigatório. Digite novamente: "); s = sc.nextLine().trim(); }
        return s;
    }
}
