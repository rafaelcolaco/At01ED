package at01ed;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== SIMULADOR DE AEROPORTO ===");
        System.out.print("Digite o número de unidades de tempo para a simulação (padrão 20): ");
        int tempo = 20; // default
        String input = "";
        if (scanner.hasNextLine()) {
            input = scanner.nextLine();
        }
        try {
            if (!input.trim().isEmpty()) {
                tempo = Integer.parseInt(input);
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Usando 20 unidades de tempo.");
        }
        
        Aeroporto aeroporto = new Aeroporto();
        aeroporto.simular(tempo);
        
        System.out.println("Simulação finalizada.");
        scanner.close();
    }
}
