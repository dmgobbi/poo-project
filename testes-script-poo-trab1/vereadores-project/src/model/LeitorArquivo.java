package model;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LeitorArquivo {
    private static final String ENCODING = "ISO-8859-1";
    private static final String SEPARATOR = ";";

    public List<String[]> lerCSV(String filepath) throws Exception {
        List<String[]> registros = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(filepath), ENCODING))) {
            
            // Pula o cabeçalho
            String linha = br.readLine();
            
            while ((linha = br.readLine()) != null) {
                // Remove aspas duplas e divide pelo separador
                linha = linha.replace("\"", "");
                String[] campos = linha.split(SEPARATOR);
                
                // Faz trim em todos os campos
                for (int i = 0; i < campos.length; i++) {
                    campos[i] = campos[i].trim();
                }
                
                registros.add(campos);
            }
        }
        
        return registros;
    }

    public void validarArquivo(String filepath) throws Exception {
        try (FileInputStream fis = new FileInputStream(filepath)) {
            // Apenas verifica se o arquivo existe e pode ser aberto
        } catch (Exception e) {
            throw new Exception("Erro ao acessar arquivo: " + filepath, e);
        }
    }
}