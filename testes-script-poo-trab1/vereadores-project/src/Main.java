import model.LeitorArquivo;
import model.ProcessadorEleicao;
import model.GeradorRelatorio;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Uso: java -jar vereadores.jar <codigo_municipio> <arquivo_candidatos> <arquivo_votacao> <data_eleicao>");
            System.exit(1);
        }

        try {
            String codigoMunicipio = args[0];
            String arquivoCandidatos = args[1];
            String arquivoVotacao = args[2];
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataEleicao = LocalDate.parse(args[3], formatter);

            LeitorArquivo leitor = new LeitorArquivo();

            leitor.validarArquivo(arquivoCandidatos);
            leitor.validarArquivo(arquivoVotacao);

            ProcessadorEleicao processador = new ProcessadorEleicao(codigoMunicipio, dataEleicao);

            leitor.processarCSV(arquivoCandidatos, campos -> {
                processador.processarLinhaCandidato(campos);
            });

            leitor.processarCSV(arquivoVotacao, campos -> {
                processador.processarLinhaVotacao(campos);
            });

            GeradorRelatorio gerador = new GeradorRelatorio(processador);
            gerador.gerarRelatorioCompleto();

        } catch (Exception e) {
            System.err.println("Erro durante a execucao: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}