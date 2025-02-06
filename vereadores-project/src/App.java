import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Candidato> candidatos = LeitorCSV.lerCandidatos("../consulta_cand_2024/consulta_cand_2024_ES.csv");

        // Exibir os 10 primeiros candidatos lidos para verificação
        for (int i = 0; i < Math.min(10, candidatos.size()); i++) {
            System.out.println(candidatos.get(i));
        }
    }
}
