import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.time.LocalDate;

public class LeitorCSV {

    public static List<Candidato> lerCandidatos(String caminhoArquivo) {
        List<Candidato> candidatos = new ArrayList<>();
        String linha;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(caminhoArquivo), "ISO-8859-1"))) {
            br.readLine(); // Ignorar cabeçalho

            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";");

                // Remover aspas duplas de cada campo
                for (int i = 0; i < campos.length; i++) {
                    campos[i] = campos[i].replaceAll("\"", "").trim();
                }

                int codigoMunicipio = Integer.parseInt(campos[0]);
                int cargo = Integer.parseInt(campos[1]);
                
                // Ignorar candidatos que não são vereadores
                if (cargo != 13) continue;

                int numero = Integer.parseInt(campos[2]);
                String nome = campos[3];
                int numeroPartido = Integer.parseInt(campos[4]);
                String siglaPartido = campos[5];
                int numeroFederacao = Integer.parseInt(campos[6]);
                String dataNascimento = campos[7];
                int situacaoTurno = Integer.parseInt(campos[8]);
                int genero = Integer.parseInt(campos[9]);

                // Ignorar candidatos inválidos
                if (situacaoTurno == -1) continue;

                Candidato candidato = new Candidato(
                    codigoMunicipio, numero, nome, siglaPartido, numeroPartido,
                    numeroFederacao, dataNascimento, situacaoTurno, genero
                );

                candidatos.add(candidato);
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return candidatos;
    }
}
