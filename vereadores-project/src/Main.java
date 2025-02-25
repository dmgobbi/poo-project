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
            
            // Converter a data da eleição para LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataEleicao = LocalDate.parse(args[3], formatter);

            // Criar instância do leitor de arquivos
            LeitorArquivo leitor = new LeitorArquivo();

            // Validar arquivos
            leitor.validarArquivo(arquivoCandidatos);
            leitor.validarArquivo(arquivoVotacao);

            // Criar processador de eleição
            ProcessadorEleicao processador = new ProcessadorEleicao(codigoMunicipio, dataEleicao);

            // Ler e processar arquivo de candidatos
            processador.processarArquivoCandidatos(leitor.lerCSV(arquivoCandidatos));

            // Ler e processar arquivo de votação
            processador.processarArquivoVotacao(leitor.lerCSV(arquivoVotacao));

            // Gerar relatórios
            GeradorRelatorio gerador = new GeradorRelatorio(processador);
            gerador.gerarRelatorioCompleto();

        } catch (Exception e) {
            System.err.println("Erro durante a execução: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}