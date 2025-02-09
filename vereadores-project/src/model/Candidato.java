package model;
import java.time.LocalDate;
import java.time.Period;

public class Candidato {
    private final String numero;
    private final String nomeUrna;
    private final Partido partido;
    private final LocalDate dataNascimento;
    private int votosNominais;
    private boolean eleito;
    private final Genero genero;
    private final int numeroFederacao;

    public Candidato(String numero, 
                    String nomeUrna, 
                    Partido partido, 
                    LocalDate dataNascimento,
                    Genero genero, 
                    int numeroFederacao) {
        this.numero = numero;
        this.nomeUrna = nomeUrna;
        this.partido = partido;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.numeroFederacao = numeroFederacao;
        this.votosNominais = 0;
        this.eleito = false;
    }

    public int getIdade(LocalDate dataRef) {
        return Period.between(dataNascimento, dataRef).getYears();
    }

    public void addVotos(int votos) {
        this.votosNominais += votos;
    }

    public boolean participaFederacao() {
        return this.numeroFederacao != -1;
    }

    // Getters
    public String getNumero() {
        return numero;
    }

    public String getNomeUrna() {
        return nomeUrna;
    }

    public Partido getPartido() {
        return partido;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public int getVotosNominais() {
        return votosNominais;
    }

    public boolean isEleito() {
        return eleito;
    }

    public void setEleito(boolean eleito) {
        this.eleito = eleito;
    }

    public Genero getGenero() {
        return genero;
    }

    public int getNumeroFederacao() {
        return numeroFederacao;
    }

    @Override
    public String toString() {
        String prefixo = participaFederacao() ? "*" : "";
        return String.format("%s%s (%s, %d votos)", 
            prefixo, nomeUrna, partido.getSigla(), votosNominais);
    }
}