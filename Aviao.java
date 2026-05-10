package at01ed;
/**
 * Representa uma aeronave no sistema do aeroporto.
 * Armazena informações essenciais como ID (ímpar para pouso, par para decolagem),
 * tipo de operação, reserva de combustível e o tempo total de espera nas filas.
 */
public class Aviao {
    private int id;
    private String tipo; // "Pouso" ou "Decolagem"
    private int combustivel;
    private int tempoEspera;

    public Aviao(int id, String tipo, int combustivel) {
        this.id = id;
        this.tipo = tipo;
        this.combustivel = combustivel;
        this.tempoEspera = 0;
    }

    // Getters
    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public int getCombustivel() { return combustivel; }
    public int getTempoEspera() { return tempoEspera; }

    //Decrementa a unidade de combustível do avião a cada ciclo de tempo.
    public void decrementarCombustivel() {
        this.combustivel--;
    }

    // Incrementa o tempo de espera do avião a cada ciclo que ele passa na fila.
    public void incrementarTempoEspera() {
        this.tempoEspera++;
    }
}
