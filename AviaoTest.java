package at01ed;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AviaoTest {

    @Test
    public void testCriacaoAviao() {
        Aviao aviao = new Aviao(1, "Pouso", 15);
        
        assertEquals(1, aviao.getId());
        assertEquals("Pouso", aviao.getTipo());
        assertEquals(15, aviao.getCombustivel());
        assertEquals(0, aviao.getTempoEspera(), "Tempo de espera inicial deve ser 0");
    }

    @Test
    public void testDecrementarCombustivel() {
        Aviao aviao = new Aviao(3, "Pouso", 5);
        aviao.decrementarCombustivel();
        assertEquals(4, aviao.getCombustivel(), "Combustível deve reduzir em 1 unidade");
    }

    @Test
    public void testIncrementarTempoEspera() {
        Aviao aviao = new Aviao(2, "Decolagem", -1);
        aviao.incrementarTempoEspera();
        aviao.incrementarTempoEspera();
        assertEquals(2, aviao.getTempoEspera(), "Tempo de espera deve refletir os incrementos");
    }
}