package model;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

public class Candidato {
    private final String numero;
    private final String nomeUrna;
    private final Partido partido;
    private final LocalDate dataNascimento;
    private int votosNominais;
    private boolean eleito;
    private final int codigoGenero;
    private final int numeroFederacao;

    public Candidato(String numero, 
                    String nomeUrna, 
                    Partido partido, 
                    LocalDate dataNascimento,
                    int codigoGenero,
                    int numeroFederacao) {
        this.numero = numero;
        this.nomeUrna = nomeUrna;
        this.partido = partido;
        this.dataNascimento = dataNascimento;
        this.codigoGenero = codigoGenero;
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

    public int getCodigoGenero() {
        return codigoGenero;
    }

    public int getNumeroFederacao() {
        return numeroFederacao;
    }

    @Override
    public String toString() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        
        return String.format("%s%s (%s, %s votos)",
            participaFederacao() ? "*" : "",
            nomeUrna,
            partido.getSigla(),
            df.format(votosNominais));
    }
}