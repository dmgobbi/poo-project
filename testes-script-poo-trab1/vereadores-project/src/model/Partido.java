package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Partido {
    private final int numero;
    private final String sigla;
    private final List<Candidato> candidatos;
    private int votosLegenda;
    private int votosTotais;

    public Partido(int numero, String sigla) {
        this.numero = numero;
        this.sigla = sigla;
        this.candidatos = new ArrayList<>();
        this.votosLegenda = 0;
        this.votosTotais = 0;
    }

    public void addCandidato(Candidato candidato) {
        candidatos.add(candidato);
    }

    public void addVotosLegenda(int votos) {
        this.votosLegenda += votos;
        this.votosTotais += votos;
    }

    public void addVotosNominais(int votos) {
        this.votosTotais += votos;
    }

    public int getVotosTotais() {
        return votosTotais;
    }

    public List<Candidato> getCandidatosEleitos() {
        return candidatos.stream()
                .filter(Candidato::isEleito)
                .toList();
    }

    public List<Candidato> getCandidatos() {
        return Collections.unmodifiableList(candidatos);
    }

    public int getNumero() {
        return numero;
    }

    public String getSigla() {
        return sigla;
    }

    public int getVotosLegenda() {
        return votosLegenda;
    }

    @Override
    public String toString() {
        return String.format("%s - %d", sigla, numero);
    }
}