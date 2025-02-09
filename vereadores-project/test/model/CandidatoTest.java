package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CandidatoTest {
    private Partido partido;
    private Candidato candidato;
    private final LocalDate dataEleicao = LocalDate.of(2024, 10, 6);

    @BeforeEach
    void setUp() {
        partido = new Partido(13, "PT");
        candidato = new Candidato(
            "13123",
            "João da Silva",
            partido,
            LocalDate.of(1980, 1, 1),
            Genero.MASCULINO,
            -1
        );
    }

    @Test
    void testAddVotos() {
        candidato.addVotos(100);
        assertEquals(100, candidato.getVotosNominais());
        
        candidato.addVotos(50);
        assertEquals(150, candidato.getVotosNominais());
    }

    @Test
    void testParticipacaoFederacao() {
        assertFalse(candidato.participaFederacao());
        
        Candidato candidatoFederacao = new Candidato(
            "13124",
            "Maria Santos",
            partido,
            LocalDate.of(1985, 1, 1),
            Genero.FEMININO,
            1
        );
        
        assertTrue(candidatoFederacao.participaFederacao());
    }

    @Test
    void testCalculoIdade() {
        int idade = candidato.getIdade(dataEleicao);
        assertEquals(44, idade);
    }
}
