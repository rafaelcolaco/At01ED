package at01ed;
/**
 * Estrutura de dados Fila implementada manualmente baseada em encadeamento de Nodos.
 * Atende ao requisito do trabalho de não utilizar coleções embutidas da linguagem Java.
 * Gerencia as operações de enfileirar, desenfileirar, controle de emergências e quedas.
 */
public class Fila {
    private Nodo inicio;
    private Nodo fim;
    private int tamanho;

    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public void enfileirar(Aviao aviao) {
        Nodo novo = new Nodo(aviao);
        if (estaVazia()) {
            inicio = novo;
        } else {
            fim.prox = novo;
        }
        fim = novo;
        tamanho++;
    }

    public Aviao desenfileirar() {
        if (estaVazia()) return null;
        Aviao a = inicio.aviao;
        inicio = inicio.prox;
        if (inicio == null) fim = null;
        tamanho--;
        return a;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int getTamanho() {
        return tamanho;
    }

    public Aviao removerEmergencia() {
        Nodo atual = inicio;
        Nodo anterior = null;

        while (atual != null) {
            if (atual.aviao.getCombustivel() == 0) {
                if (anterior == null) {
                    inicio = atual.prox;
                    if (inicio == null) fim = null;
                } else {
                    anterior.prox = atual.prox;
                    if (atual.prox == null) fim = anterior;
                }
                tamanho--;
                return atual.aviao;
            }
            anterior = atual;
            atual = atual.prox;
        }
        return null;
    }

    public Aviao removerCaido() {
        Nodo atual = inicio;
        Nodo anterior = null;

        while (atual != null) {
            if (atual.aviao.getCombustivel() < 0) {
                if (anterior == null) {
                    inicio = atual.prox;
                    if (inicio == null) fim = null;
                } else {
                    anterior.prox = atual.prox;
                    if (atual.prox == null) fim = anterior;
                }
                tamanho--;
                return atual.aviao;
            }
            anterior = atual;
            atual = atual.prox;
        }
        return null;
    }

    public void atualizarFila(boolean isAterrissagem) {
        Nodo atual = inicio;
        while (atual != null) {
            atual.aviao.incrementarTempoEspera();
            if (isAterrissagem) {
                atual.aviao.decrementarCombustivel();
            }
            atual = atual.prox;
        }
    }

    public void imprimirFila() {
        Nodo atual = inicio;
        System.out.print("[");
        while (atual != null) {
            System.out.print("ID:" + atual.aviao.getId());
            if (atual.aviao.getTipo().equals("Pouso")) {
                 System.out.print("(C:" + atual.aviao.getCombustivel() + ")");
            }
            if (atual.prox != null) System.out.print(", ");
            atual = atual.prox;
        }
        System.out.println("]");
    }
}