import model.LeitorArquivo;
import model.ProcessadorEleicao;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class App {
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
            List<String[]> registrosCandidatos = leitor.lerCSV(arquivoCandidatos);
            processador.processarArquivoCandidatos(registrosCandidatos);

            // Ler e processar arquivo de votação
            List<String[]> registrosVotacao = leitor.lerCSV(arquivoVotacao);
            processador.processarArquivoVotacao(registrosVotacao);

            // Imprimir resultados iniciais para teste
            System.out.println("\nRelatório inicial de teste:");
            System.out.println("---------------------------");
            System.out.println("Número de vagas: " + processador.getCandidatosEleitos().size());
            System.out.println("Total de votos válidos: " + processador.getTotalVotosValidos());
            
            System.out.println("\nPrimeiros candidatos eleitos:");
            processador.getCandidatosEleitos().stream()
                .limit(5)
                .forEach(c -> System.out.println("- " + c.toString()));

            System.out.println("\nPrimeiros partidos por número de votos:");
            processador.getPartidosOrdenadosPorVotos().stream()
                .limit(5)
                .forEach(p -> System.out.println(String.format("- %s: %d votos totais (%d votos de legenda)",
                    p.toString(), p.getVotosTotais(), p.getVotosLegenda())));

        } catch (Exception e) {
            System.err.println("Erro durante a execução: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}