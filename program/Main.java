import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static char[][] labirinto;
    private static Coordenada atual;

    public static void lerArquivo(String nomeArquivo) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo));

            int linhas = Integer.parseInt(reader.readLine());
            int colunas = Integer.parseInt(reader.readLine());

            labirinto = new char[linhas][colunas];

            String linhaTexto;
            int linhaMatriz = 0;

            while ((linhaTexto = reader.readLine()) != null && linhaMatriz < labirinto.length) {
                for (int colunaMatriz = 0; colunaMatriz < labirinto[linhaMatriz].length; colunaMatriz++) {
                    labirinto[linhaMatriz][colunaMatriz] = linhaTexto.charAt(colunaMatriz);
                }
                linhaMatriz++;
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public static void procurarEntrada() throws Exception {
        boolean achou = false;

        for (int coluna = 0; coluna < labirinto[0].length; coluna++) {
            if (labirinto[0][coluna] == 'E') {
                atual = new Coordenada(0, coluna); // linha 0
                achou = true; break;
            }
            if (labirinto[labirinto.length-1][coluna] == 'E') {
                atual = new Coordenada(labirinto.length-1, coluna); // última linha
                achou = true; break;
            }
        }
        if (!achou) {
            for (int linha = 0; linha < labirinto.length; linha++) {
                if (labirinto[linha][0] == 'E') {
                    atual = new Coordenada(linha, 0); // coluna 0
                    achou = true; break;
                }
                if (labirinto[linha][labirinto[0].length-1] == 'E') {
                    atual = new Coordenada(linha, labirinto[0].length-1); // última coluna
                    achou = true; break;
                }
            }
        }
        if (!achou) throw new Exception("Nao Existe!");
    }

    public static void verificarEspacosLivres(Fila<Coordenada> fila, Coordenada pos) {
       
        //cima msm linha+1,msm coluna
        //baixo msm linha-1, msm coluna
        //esquerda msm linha,msm coluna-1
        //direita msm linha,msm coluna+1
       
       
        int linha = pos.getLinha();
        int coluna = pos.getColuna();

        // direita
        if (coluna + 1 < labirinto[0].length
                && (labirinto[linha][coluna + 1] == ' ' || labirinto[linha][coluna + 1] == 'S')) {
            fila.guardeUmItem(new Coordenada(linha, coluna + 1));
        }
        // esquerda
        if (coluna - 1 >= 0
                && (labirinto[linha][coluna - 1] == ' ' || labirinto[linha][coluna - 1] == 'S')) {
            fila.guardeUmItem(new Coordenada(linha, coluna - 1));
        }
        // baixo
        if (linha + 1 < labirinto.length
                && (labirinto[linha + 1][coluna] == ' ' || labirinto[linha + 1][coluna] == 'S')) {
            fila.guardeUmItem(new Coordenada(linha + 1, coluna));
        }
        // cima
        if (linha - 1 >= 0
                && (labirinto[linha - 1][coluna] == ' ' || labirinto[linha - 1][coluna] == 'S')) {
            fila.guardeUmItem(new Coordenada(linha - 1, coluna));
        }
    }

    public static void imprimirLabirinto() {
        for (char[] linha : labirinto) {
            System.out.println(new String(linha));
        }
    }

    public static void mostrarCoordenadas(Pilha<Coordenada> caminho, int cap) throws Exception {
        Pilha<Coordenada> pilhaInvertida = new Pilha<>(cap);
        
        while (!caminho.isVazia()) {
            pilhaInvertida.guardeUmItem(caminho.removaUmItem());
        }
        
        System.out.println("\nCoordenadas do caminho percorrido:");
        while (!pilhaInvertida.isVazia()) {
            Coordenada coord = pilhaInvertida.removaUmItem();
            System.out.println("[" + coord.getLinha() + ", " + coord.getColuna() + "]");
            caminho.guardeUmItem(coord);
        }
    }

    public static void main(String[] args) throws Exception {
            Scanner teclado = new Scanner(System.in);
            System.out.print("Digite o nome do arquivo (ex: labirinto.txt): ");
            String nomeArquivo = teclado.nextLine();

        lerArquivo(nomeArquivo);
        procurarEntrada();

        //capacidade baseada na área total pra evitar erro em labirintos grandes
        int cap = labirinto.length * labirinto[0].length;
        Pilha<Coordenada> caminho = new Pilha<>(cap);
        Pilha<Fila<Coordenada>> possibilidades = new Pilha<>(cap);
       
        Fila<Coordenada> filaAtual = new Fila<>(4);
        verificarEspacosLivres(filaAtual, atual);

        boolean achouSaida = false;

        while (!achouSaida) {
            //se não tem opções de movimento na posição atual, volta (backtracking)
            while (filaAtual.isVazia()) {
                if (possibilidades.isVazia()) break;

                filaAtual = possibilidades.removaUmItem();
                Coordenada errado = caminho.removaUmItem();
                //remove '*' do mapa se o caminho não levou a saída
                if (labirinto[errado.getLinha()][errado.getColuna()] == '*') {
                    labirinto[errado.getLinha()][errado.getColuna()] = ' ';
                }
            }

            if (filaAtual.isVazia()) break; //esgotou todas as possibilidades do labirinto

            atual = filaAtual.removaUmItem();

            if (labirinto[atual.getLinha()][atual.getColuna()] == 'S') {
                achouSaida = true;
            } else {
                //marca caminho e empilha o estado pra caso precise voltar
                labirinto[atual.getLinha()][atual.getColuna()] = '*';
                caminho.guardeUmItem(atual);
                possibilidades.guardeUmItem(filaAtual);

                //prepara a fila pra nova posição
                filaAtual = new Fila<>(4);
                verificarEspacosLivres(filaAtual, atual);
            }
        }

        if (achouSaida) {
            System.out.println("Saida encontrada! Caminho marcado com '*':");
            imprimirLabirinto();
            mostrarCoordenadas(caminho, cap);
        } else {
            System.out.println("Nao existe caminho ate a saida.");
            imprimirLabirinto();
        }
    }
}
