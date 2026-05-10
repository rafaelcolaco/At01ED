package at01ed;
/**
 * Representa um elemento (nó) da estrutura de dados Fila encadeada.
 * Contém a referência para o avião e o ponteiro para o próximo elemento da fila.
 */
public class Nodo {
    public Aviao aviao;
    public Nodo prox;

    public Nodo(Aviao aviao) {
        this.aviao = aviao;
        this.prox = null;
    }
}
