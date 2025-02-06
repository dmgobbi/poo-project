import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Candidato {
    private int codigoMunicipio;
    private int numero;
    private String nome;
    private String partido;
    private int numeroPartido;
    private int numeroFederacao;
    private LocalDate dataNascimento;
    private int situacaoTurno;
    private char genero;
    private int votosNominais;

    // Construtor
    public Candidato(int codigoMunicipio, int numero, String nome, String partido, int numeroPartido,
                     int numeroFederacao, String dataNascimento, int situacaoTurno, int genero) {
        this.codigoMunicipio = codigoMunicipio;
        this.numero = numero;
        this.nome = nome;
        this.partido = partido;
        this.numeroPartido = numeroPartido;
        this.numeroFederacao = numeroFederacao;
        this.dataNascimento = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.situacaoTurno = situacaoTurno;
        this.genero = (genero == 2) ? 'M' : 'F'; // Converte código numérico para 'M' ou 'F'
        this.votosNominais = 0; // Inicialmente 0, será atualizado com a leitura da votação
    }

    // Métodos Getters
    public int getCodigoMunicipio() { return codigoMunicipio; }
    public int getNumero() { return numero; }
    public String getNome() { return nome; }
    public String getPartido() { return partido; }
    public int getNumeroPartido() { return numeroPartido; }
    public int getNumeroFederacao() { return numeroFederacao; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public int getSituacaoTurno() { return situacaoTurno; }
    public char getGenero() { return genero; }
    public int getVotosNominais() { return votosNominais; }

    // Método para adicionar votos ao candidato
    public void adicionarVotos(int votos) {
        this.votosNominais += votos;
    }

    // Método para calcular idade no dia da eleição
    public int calcularIdade(LocalDate dataEleicao) {
        return dataEleicao.getYear() - dataNascimento.getYear();
    }

    // Representação em String para facilitar depuração
    @Override
    public String toString() {
        return nome + " (" + partido + ", " + votosNominais + " votos)";
    }
}
