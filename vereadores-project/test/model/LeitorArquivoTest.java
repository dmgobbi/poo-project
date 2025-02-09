package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LeitorArquivoTest {
    private LeitorArquivo leitor;
    private final String ARQUIVO_TESTE = "dados_teste.csv";

    @BeforeEach
    void setUp() {
        leitor = new LeitorArquivo();
    }

    @Test
    void testLeituraArquivoInexistente() {
        assertThrows(Exception.class, () -> {
            leitor.lerCSV("arquivo_inexistente.csv");
        });
    }

    @Test
    void testLeituraArquivoValido() throws Exception {
        // Criar arquivo de teste temporário
        Files.write(
            Paths.get(ARQUIVO_TESTE),
            "campo1;campo2;campo3\n\"valor1\";\"valor2\";\"valor3\""
                .getBytes(StandardCharsets.ISO_8859_1)
        );

        List<String[]> registros = leitor.lerCSV(ARQUIVO_TESTE);
        
        assertNotNull(registros);
        assertEquals(1, registros.size());
        assertArrayEquals(
            new String[]{"valor1", "valor2", "valor3"}, 
            registros.get(0)
        );

        // Limpar arquivo de teste
        Files.deleteIfExists(Paths.get(ARQUIVO_TESTE));
    }
}