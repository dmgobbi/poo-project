package model;

import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Comparator;

public class GeradorRelatorio {
    private final ProcessadorEleicao processador;
    private final DecimalFormat df;

    public GeradorRelatorio(ProcessadorEleicao processador) {
        this.processador = processador;
        this.df = new DecimalFormat("#,##0");
    }

    public void gerarRelatorioCompleto() {
        gerarNumeroVagas();
        gerarVereadoresEleitos();
        gerarMaisVotados();
        gerarNaoEleitosMajoritario();
        gerarEleitosBeneficiados();
        gerarVotacaoPartidos();
        gerarPrimeiroUltimoColocados();
        gerarDistribuicaoIdade();
        gerarDistribuicaoSexo();
        gerarTotalVotos();
    }

    private void gerarNumeroVagas() {
        int vagas = processador.getCandidatosEleitos().size();
        System.out.println("Número de vagas: " + vagas);
        System.out.println();
    }

    private void gerarVereadoresEleitos() {
        System.out.println("Vereadores eleitos:");
        List<Candidato> eleitos = processador.getCandidatosEleitos().stream()
            .sorted(Comparator.comparing(Candidato::getVotosNominais).reversed()
                .thenComparing(c -> c.getDataNascimento()))
            .toList();

        for (int i = 0; i < eleitos.size(); i++) {
            Candidato c = eleitos.get(i);
            System.out.printf("%d - %s%n", i + 1, c.toString());
        }
        System.out.println();
    }

    private void gerarMaisVotados() {
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        int numVagas = processador.getCandidatosEleitos().size();
        
        List<Candidato> maisVotados = processador.getCandidatos().values().stream()
            .sorted(Comparator.comparing(Candidato::getVotosNominais).reversed()
                .thenComparing(c -> c.getDataNascimento()))
            .limit(numVagas)
            .toList();

        for (int i = 0; i < maisVotados.size(); i++) {
            Candidato c = maisVotados.get(i);
            System.out.printf("%d - %s%n", i + 1, c.toString());
        }
        System.out.println();
    }

    private void gerarNaoEleitosMajoritario() {
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");
        
        int numVagas = processador.getCandidatosEleitos().size();
        List<Candidato> todosOrdenados = processador.getCandidatos().values().stream()
            .sorted(Comparator.comparing(Candidato::getVotosNominais).reversed()
                .thenComparing(c -> c.getDataNascimento()))
            .toList();

        for (int i = 0; i < numVagas; i++) {
            Candidato c = todosOrdenados.get(i);
            if (!c.isEleito()) {
                System.out.printf("%d - %s%n", i + 1, c.toString());
            }
        }
        System.out.println();
    }

    private void gerarEleitosBeneficiados() {
        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");
        
        List<Candidato> todosOrdenados = processador.getCandidatos().values().stream()
            .sorted(Comparator.comparing(Candidato::getVotosNominais).reversed()
                .thenComparing(c -> c.getDataNascimento()))
            .toList();

        int numVagas = processador.getCandidatosEleitos().size();
        for (int i = 0; i < todosOrdenados.size(); i++) {
            Candidato c = todosOrdenados.get(i);
            if (c.isEleito() && i >= numVagas) {
                System.out.printf("%d - %s%n", i + 1, c.toString());
            }
        }
        System.out.println();
    }

    private void gerarVotacaoPartidos() {
        System.out.println("Votação dos partidos e número de candidatos eleitos:");
        
        List<Partido> partidosOrdenados = processador.getPartidosOrdenadosPorVotos();
        
        for (int i = 0; i < partidosOrdenados.size(); i++) {
            Partido p = partidosOrdenados.get(i);
            int votosNominais = p.getVotosTotais() - p.getVotosLegenda();
            System.out.printf("%d - %s - %s, %s votos (%s nominais e %s de legenda), %d candidato%s eleito%s%n",
                i + 1,
                p.getSigla(),
                p.getNumero(),
                df.format(p.getVotosTotais()),
                df.format(votosNominais),
                df.format(p.getVotosLegenda()),
                p.getCandidatosEleitos().size(),
                p.getCandidatosEleitos().size() == 1 ? "" : "s",
                p.getCandidatosEleitos().size() == 1 ? "" : "s");
        }
        System.out.println();
    }

    private void gerarPrimeiroUltimoColocados() {
        System.out.println("Primeiro e último colocados de cada partido:");
        
        List<Partido> partidosComVotos = processador.getPartidos().values().stream()
            .filter(p -> p.getVotosTotais() > 0)
            .sorted((p1, p2) -> {
                int maxVotosP1 = p1.getCandidatos().stream()
                    .mapToInt(Candidato::getVotosNominais)
                    .max()
                    .orElse(0);
                int maxVotosP2 = p2.getCandidatos().stream()
                    .mapToInt(Candidato::getVotosNominais)
                    .max()
                    .orElse(0);
                int compare = Integer.compare(maxVotosP2, maxVotosP1);
                if (compare == 0) {
                    return Integer.compare(p1.getNumero(), p2.getNumero());
                }
                return compare;
            })
            .toList();

        for (int i = 0; i < partidosComVotos.size(); i++) {
            Partido p = partidosComVotos.get(i);
            List<Candidato> candidatosOrdenados = p.getCandidatos().stream()
                .filter(c -> c.getVotosNominais() > 0)
                .sorted(Comparator.comparing(Candidato::getVotosNominais).reversed()
                    .thenComparing(c -> c.getDataNascimento()))
                .toList();

            if (!candidatosOrdenados.isEmpty()) {
                Candidato primeiro = candidatosOrdenados.get(0);
                Candidato ultimo = candidatosOrdenados.get(candidatosOrdenados.size() - 1);
                
                System.out.printf("%d - %s - %d, %s (%s, %s votos) / %s (%s, %s votos)%n",
                    i + 1,
                    p.getSigla(),
                    p.getNumero(),
                    primeiro.getNomeUrna(),
                    primeiro.getNumero(),
                    df.format(primeiro.getVotosNominais()),
                    ultimo.getNomeUrna(),
                    ultimo.getNumero(),
                    df.format(ultimo.getVotosNominais()));
            }
        }
        System.out.println();
    }

    private void gerarDistribuicaoIdade() {
        System.out.println("Eleitos, por faixa etária (na data da eleição):");
        
        List<Candidato> eleitos = processador.getCandidatosEleitos();
        LocalDate dataEleicao = processador.getDataEleicao();
        
        int menos30 = 0, entre30e40 = 0, entre40e50 = 0, entre50e60 = 0, mais60 = 0;
        
        for (Candidato c : eleitos) {
            int idade = c.getIdade(dataEleicao);
            if (idade < 30) menos30++;
            else if (idade < 40) entre30e40++;
            else if (idade < 50) entre40e50++;
            else if (idade < 60) entre50e60++;
            else mais60++;
        }
        
        double total = eleitos.size();
        System.out.printf("Idade < 30: %d (%.2f%%)%n", menos30, (menos30/total)*100);
        System.out.printf("30 <= Idade < 40: %d (%.2f%%)%n", entre30e40, (entre30e40/total)*100);
        System.out.printf("40 <= Idade < 50: %d (%.2f%%)%n", entre40e50, (entre40e50/total)*100);
        System.out.printf("50 <= Idade < 60: %d (%.2f%%)%n", entre50e60, (entre50e60/total)*100);
        System.out.printf("60 <= Idade: %d (%.2f%%)%n", mais60, (mais60/total)*100);
        System.out.println();
    }

    private void gerarDistribuicaoSexo() {
        System.out.println("Eleitos, por gênero:");
        
        List<Candidato> eleitos = processador.getCandidatosEleitos();
        long mulheres = eleitos.stream().filter(c -> c.getGenero() == Genero.FEMININO).count();
        long homens = eleitos.stream().filter(c -> c.getGenero() == Genero.MASCULINO).count();
        double total = eleitos.size();
        
        System.out.printf("Feminino: %d (%.2f%%)%n", mulheres, (mulheres/total)*100);
        System.out.printf("Masculino: %d (%.2f%%)%n", homens, (homens/total)*100);
        System.out.println();
    }

    private void gerarTotalVotos() {
        int totalValidos = processador.getTotalVotosValidos();
        int nominais = processador.getTotalVotosNominais();
        int legenda = totalValidos - nominais;
        
        System.out.printf("Total de votos válidos: %s%n", df.format(totalValidos));
        System.out.printf("Total de votos nominais: %s (%.2f%%)%n", 
            df.format(nominais), 
            (double)nominais/totalValidos * 100);
        System.out.printf("Total de votos de legenda: %s (%.2f%%)%n",
            df.format(legenda),
            (double)legenda/totalValidos * 100);
    }
}