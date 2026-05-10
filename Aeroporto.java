package at01ed;
import java.util.Random;

/**
 * Classe principal de simulação do Aeroporto.
 * Gerencia a lógica das prateleiras de espera (filas), pistas de aterrissagem e decolagem,
 * distribuição de IDs (ímpares/pares), prioridade de combustível e cálculo de estatísticas.
 * Otimizada para evitar crescimento excessivo das filas compartilhando carga entre pistas.
 */
public class Aeroporto {
    private Fila filaAterrissagem1;
    private Fila filaAterrissagem2;
    private Fila filaDecolagem1;
    private Fila filaDecolagem2;

    private int proxIdAterrissagem = 1;
    private int proxIdDecolagem = 2;

    private int totalPousos = 0;
    private int totalDecolagens = 0;
    private int tempoTotalEsperaPouso = 0;
    private int tempoTotalEsperaDecolagem = 0;
    private int totalEmergencias = 0;
    private int totalQuedas = 0;

    private Random random;

    public Aeroporto() {
        filaAterrissagem1 = new Fila();
        filaAterrissagem2 = new Fila();
        filaDecolagem1 = new Fila();
        filaDecolagem2 = new Fila();
        random = new Random();
    }

    public void simular(int unidadesTempo) {
        for (int t = 1; t <= unidadesTempo; t++) {
            System.out.println("==================================================");
            System.out.println("Unidade de Tempo: " + t);
            
            // 1. Chegadas
            int chegadasPouso = random.nextInt(3); // 0 a 2
            for (int i = 0; i < chegadasPouso; i++) {
                int combustivel = random.nextInt(20) + 1; // 1 a 20
                Aviao a = new Aviao(proxIdAterrissagem, "Pouso", combustivel);
                proxIdAterrissagem += 2;
                
                // Escolher fila com menor tamanho para aterrissagem
                if (filaAterrissagem1.getTamanho() <= filaAterrissagem2.getTamanho()) {
                    filaAterrissagem1.enfileirar(a);
                    System.out.println("Avião " + a.getId() + " chegou para POUSO. Combustível: " + combustivel + ". Fila 1.");
                } else {
                    filaAterrissagem2.enfileirar(a);
                    System.out.println("Avião " + a.getId() + " chegou para POUSO. Combustível: " + combustivel + ". Fila 2.");
                }
            }

            int chegadasDecolagem = random.nextInt(3); // 0 a 2
            for (int i = 0; i < chegadasDecolagem; i++) {
                Aviao a = new Aviao(proxIdDecolagem, "Decolagem", -1);
                proxIdDecolagem += 2;
                
                // Escolher fila com menor tamanho para decolagem
                if (filaDecolagem1.getTamanho() <= filaDecolagem2.getTamanho()) {
                    filaDecolagem1.enfileirar(a);
                    System.out.println("Avião " + a.getId() + " chegou para DECOLAGEM. Fila 1.");
                } else {
                    filaDecolagem2.enfileirar(a);
                    System.out.println("Avião " + a.getId() + " chegou para DECOLAGEM. Fila 2.");
                }
            }

            // 2. Processar Pistas (Max 2 operações, prioridade para emergência)
            Aviao[] pista = new Aviao[2];
            
            // Verificar emergências (combustível == 0)
            for (int p = 0; p < 2; p++) {
                Aviao em = filaAterrissagem1.removerEmergencia();
                if (em == null) em = filaAterrissagem2.removerEmergencia();
                
                if (em != null) {
                    pista[p] = em;
                    totalEmergencias++;
                    System.out.println("EMERGÊNCIA! Avião " + em.getId() + " direcionado para a Pista " + (p+1) + "!");
                }
            }

            // Para as pistas restantes, decidir operação normal
            if (pista[0] == null) {
                // Pista 1 atende Pouso 1 ou Decolagem 1 (o que for maior, priorizando pouso se igual)
                if (filaAterrissagem1.getTamanho() >= filaDecolagem1.getTamanho() && !filaAterrissagem1.estaVazia()) {
                    pista[0] = filaAterrissagem1.desenfileirar();
                    System.out.println("Pista 1: Avião " + pista[0].getId() + " realizando POUSO.");
                } else if (!filaDecolagem1.estaVazia()) {
                    pista[0] = filaDecolagem1.desenfileirar();
                    System.out.println("Pista 1: Avião " + pista[0].getId() + " realizando DECOLAGEM.");
                } else if (!filaAterrissagem1.estaVazia()) {
                    pista[0] = filaAterrissagem1.desenfileirar();
                    System.out.println("Pista 1: Avião " + pista[0].getId() + " realizando POUSO.");
                } else {
                    System.out.println("Pista 1: Ociosa.");
                }

                // Otimização: Se a Pista 1 ficou ociosa, puxa da fila da Pista 2 para evitar acúmulo
                if (pista[0] == null) {
                    if (!filaAterrissagem2.estaVazia()) {
                        pista[0] = filaAterrissagem2.desenfileirar();
                        System.out.println("Pista 1: Avião " + pista[0].getId() + " realizando POUSO (Redirecionado da Fila 2).");
                    } else if (!filaDecolagem2.estaVazia()) {
                        pista[0] = filaDecolagem2.desenfileirar();
                        System.out.println("Pista 1: Avião " + pista[0].getId() + " realizando DECOLAGEM (Redirecionado da Fila 2).");
                    }
                }
            }
            
            if (pista[1] == null) {
                // Pista 2 atende Pouso 2 ou Decolagem 2
                if (filaAterrissagem2.getTamanho() >= filaDecolagem2.getTamanho() && !filaAterrissagem2.estaVazia()) {
                    pista[1] = filaAterrissagem2.desenfileirar();
                    System.out.println("Pista 2: Avião " + pista[1].getId() + " realizando POUSO.");
                } else if (!filaDecolagem2.estaVazia()) {
                    pista[1] = filaDecolagem2.desenfileirar();
                    System.out.println("Pista 2: Avião " + pista[1].getId() + " realizando DECOLAGEM.");
                } else if (!filaAterrissagem2.estaVazia()) {
                    pista[1] = filaAterrissagem2.desenfileirar();
                    System.out.println("Pista 2: Avião " + pista[1].getId() + " realizando POUSO.");
                } else {
                    System.out.println("Pista 2: Ociosa.");
                }

                // Otimização: Se a Pista 2 ficou ociosa, puxa da fila da Pista 1 para evitar acúmulo
                if (pista[1] == null) {
                    if (!filaAterrissagem1.estaVazia()) {
                        pista[1] = filaAterrissagem1.desenfileirar();
                        System.out.println("Pista 2: Avião " + pista[1].getId() + " realizando POUSO (Redirecionado da Fila 1).");
                    } else if (!filaDecolagem1.estaVazia()) {
                        pista[1] = filaDecolagem1.desenfileirar();
                        System.out.println("Pista 2: Avião " + pista[1].getId() + " realizando DECOLAGEM (Redirecionado da Fila 1).");
                    }
                }
            }

            // Registrar estatísticas
            for (int p = 0; p < 2; p++) {
                if (pista[p] != null) {
                    if (pista[p].getTipo().equals("Pouso")) {
                        totalPousos++;
                        tempoTotalEsperaPouso += pista[p].getTempoEspera();
                    } else {
                        totalDecolagens++;
                        tempoTotalEsperaDecolagem += pista[p].getTempoEspera();
                    }
                }
            }

            // 3. Atualizar tempos e combustíveis das filas
            filaAterrissagem1.atualizarFila(true);
            filaAterrissagem2.atualizarFila(true);
            filaDecolagem1.atualizarFila(false);
            filaDecolagem2.atualizarFila(false);

            // Verificar se algum avião caiu (combustível < 0)
            checarQuedas(filaAterrissagem1);
            checarQuedas(filaAterrissagem2);

            // 4. Exibir status periodicamente
            System.out.println("\n--- Status Atual das Filas ---");
            System.out.print("Fila Aterrissagem 1: "); filaAterrissagem1.imprimirFila();
            System.out.print("Fila Aterrissagem 2: "); filaAterrissagem2.imprimirFila();
            System.out.print("Fila Decolagem 1: "); filaDecolagem1.imprimirFila();
            System.out.print("Fila Decolagem 2: "); filaDecolagem2.imprimirFila();

            System.out.println("\n--- Estatísticas ---");
            System.out.println("Tempo médio de espera para decolagem: " + (totalDecolagens > 0 ? String.format("%.2f", (double) tempoTotalEsperaDecolagem / totalDecolagens) : "0.00"));
            System.out.println("Tempo médio de espera para aterrissagem: " + (totalPousos > 0 ? String.format("%.2f", (double) tempoTotalEsperaPouso / totalPousos) : "0.00"));
            System.out.println("Total de aterrissagens de emergência: " + totalEmergencias);
            if (totalQuedas > 0) System.out.println("ALERTA: " + totalQuedas + " aviões caíram por falta de combustível!");
            System.out.println("==================================================\n");
            
            try {
                Thread.sleep(200); // Pausa menor para fluidez da simulação
            } catch (InterruptedException e) {}
        }
    }
    
    private void checarQuedas(Fila fila) {
        Aviao caido = fila.removerCaido();
        while (caido != null) {
            System.out.println("TRAGÉDIA! Avião " + caido.getId() + " caiu por falta de combustível!");
            totalQuedas++;
            caido = fila.removerCaido();
        }
    }
}
