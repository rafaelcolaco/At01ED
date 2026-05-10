package at01ed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FilaTest {
    private Fila fila;

    @BeforeEach
    public void setUp() {
        fila = new Fila();
    }

    @Test
    public void testFilaVaziaInicialmente() {
        assertTrue(fila.estaVazia());
        assertEquals(0, fila.getTamanho());
        assertNull(fila.desenfileirar());
    }

    @Test
    public void testEnfileirarEDesenfileirar() {
        Aviao a1 = new Aviao(1, "Pouso", 10);
        Aviao a2 = new Aviao(3, "Pouso", 15);

        fila.enfileirar(a1);
        fila.enfileirar(a2);

        assertFalse(fila.estaVazia());
        assertEquals(2, fila.getTamanho());

        // Testa comportamento FIFO (Primeiro a entrar, primeiro a sair)
        Aviao removido1 = fila.desenfileirar();
        assertEquals(a1, removido1);
        assertEquals(1, fila.getTamanho());

        Aviao removido2 = fila.desenfileirar();
        assertEquals(a2, removido2);
        assertTrue(fila.estaVazia());
    }

    @Test
    public void testRemoverEmergenciaNoMeioDaFila() {
        Aviao a1 = new Aviao(1, "Pouso", 10);
        Aviao a2 = new Aviao(3, "Pouso", 0); // Este avião está em emergência!
        Aviao a3 = new Aviao(5, "Pouso", 15);

        fila.enfileirar(a1);
        fila.enfileirar(a2);
        fila.enfileirar(a3);

        Aviao emergencia = fila.removerEmergencia();
        
        assertNotNull(emergencia);
        assertEquals(3, emergencia.getId(), "Deve remover o avião de ID 3 que está com combustível zerado");
        assertEquals(2, fila.getTamanho(), "O tamanho da fila deve diminuir para 2");

        // Garante que a ordem do restante da fila foi preservada (a ponte foi feita: a1 -> a3)
        assertEquals(1, fila.desenfileirar().getId());
        assertEquals(5, fila.desenfileirar().getId());
    }

    @Test
    public void testRemoverEmergenciaNoInicioDaFila() {
        Aviao a1 = new Aviao(1, "Pouso", 0); // Emergência no topo da fila
        Aviao a2 = new Aviao(3, "Pouso", 10);

        fila.enfileirar(a1);
        fila.enfileirar(a2);

        Aviao emergencia = fila.removerEmergencia();
        assertEquals(1, emergencia.getId());
        assertEquals(1, fila.getTamanho());
        assertEquals(3, fila.desenfileirar().getId()); // O que sobrou deve ser o ID 3
    }

    @Test
    public void testRemoverEmergenciaNoFimDaFila() {
        Aviao a1 = new Aviao(1, "Pouso", 10);
        Aviao a2 = new Aviao(3, "Pouso", 15);
        Aviao a3 = new Aviao(5, "Pouso", 0); // Emergência no fim da fila!

        fila.enfileirar(a1);
        fila.enfileirar(a2);
        fila.enfileirar(a3);

        Aviao emergencia = fila.removerEmergencia();
        assertEquals(5, emergencia.getId(), "Deve remover o avião ID 5 que está no fim da fila");
        assertEquals(2, fila.getTamanho());

        // Garante que o ponteiro 'fim' foi atualizado corretamente adicionando um novo avião
        Aviao a4 = new Aviao(7, "Pouso", 20);
        fila.enfileirar(a4); // Se 'fim' estiver quebrado, este avião se perderá
        
        assertEquals(3, fila.getTamanho());
    }

    @Test
    public void testRemoverCaido() {
        Aviao a1 = new Aviao(1, "Pouso", 5);
        Aviao a2 = new Aviao(3, "Pouso", -1); // Combustível negativo (caído)
        Aviao a3 = new Aviao(5, "Pouso", 10);

        fila.enfileirar(a1);
        fila.enfileirar(a2);
        fila.enfileirar(a3);

        Aviao caido = fila.removerCaido();
        assertNotNull(caido);
        assertEquals(3, caido.getId(), "Deve remover o avião de ID 3 que caiu");
        assertEquals(2, fila.getTamanho());
        
        // Verifica integridade da fila pós-remoção
        assertEquals(1, fila.desenfileirar().getId());
        assertEquals(5, fila.desenfileirar().getId());
    }

    @Test
    public void testAtualizarFilaAterrissagem() {
        Aviao a1 = new Aviao(1, "Pouso", 10);
        fila.enfileirar(a1);
        
        fila.atualizarFila(true); // true = é fila de aterrissagem
        
        assertEquals(9, a1.getCombustivel(), "Combustível deveria ter decrementado");
        assertEquals(1, a1.getTempoEspera(), "Tempo de espera deveria ter incrementado");
    }
    
    @Test
    public void testAtualizarFilaDecolagem() {
        Aviao a1 = new Aviao(2, "Decolagem", -1);
        fila.enfileirar(a1);
        
        fila.atualizarFila(false); // false = é fila de decolagem
        
        assertEquals(-1, a1.getCombustivel(), "Combustível não deveria alterar para decolagens");
        assertEquals(1, a1.getTempoEspera(), "Tempo de espera deveria ter incrementado");
    }
}