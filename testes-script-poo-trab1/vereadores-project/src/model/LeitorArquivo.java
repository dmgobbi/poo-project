package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LeitorArquivo {
    private static final String ENCODING = "ISO-8859-1";
    private static final String SEPARATOR = ";";

    public interface LinhaCallback {
        void processarLinha(String[] campos);
    }
    
    public void processarCSV(String filepath, LinhaCallback callback) throws Exception {
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
                
                callback.processarLinha(campos);
                linha = null; // ajuda o GC
            }
        }
    }

    public void validarArquivo(String filepath) throws Exception {
        if (!Files.exists(Paths.get(filepath))) {
            throw new Exception("Arquivo não encontrado: " + filepath);
        }
    }
}