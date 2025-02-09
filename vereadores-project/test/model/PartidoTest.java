package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PartidoTest {
    private Partido partido;
    private Candidato candidato1;
    private Candidato candidato2;

    @BeforeEach
    void setUp() {
        partido = new Partido(13, "PT");
        candidato1 = new Candidato(
            "13123",
            "João da Silva",
            partido,
            LocalDate.of(1980, 1, 1),
            Genero.MASCULINO,
            -1
        );
        candidato2 = new Candidato(
            "13124",
            "Maria Santos",
            partido,
            LocalDate.of(1985, 1, 1),
            Genero.FEMININO,
            -1
        );
    }

    @Test
    void testAddCandidato() {
        partido.addCandidato(candidato1);
        assertEquals(1, partido.getCandidatos().size());
        assertTrue(partido.getCandidatos().contains(candidato1));
    }

    @Test
    void testVotosLegenda() {
        partido.addVotosLegenda(100);
        assertEquals(100, partido.getVotosLegenda());
        assertEquals(100, partido.getVotosTotais());
    }

    @Test
    void testCandidatosEleitos() {
        partido.addCandidato(candidato1);
        partido.addCandidato(candidato2);
        
        candidato1.setEleito(true);
        
        List<Candidato> eleitos = partido.getCandidatosEleitos();
        assertEquals(1, eleitos.size());
        assertTrue(eleitos.contains(candidato1));
        assertFalse(eleitos.contains(candidato2));
    }
}
