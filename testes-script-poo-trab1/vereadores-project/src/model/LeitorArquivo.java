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
            
            String linha = br.readLine();
            
            while ((linha = br.readLine()) != null) {
                linha = linha.replace("\"", "");
                String[] campos = linha.split(SEPARATOR);
                
                for (int i = 0; i < campos.length; i++) {
                    campos[i] = campos[i].trim();
                }
                
                callback.processarLinha(campos);
                linha = null;
            }
        }
    }

    public void validarArquivo(String filepath) throws Exception {
        if (!Files.exists(Paths.get(filepath))) {
            throw new Exception("Arquivo nao encontrado: " + filepath);
        }
    }
}