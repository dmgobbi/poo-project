# Class Diagram

```mermaid
classDiagram
    class Main {
        +main(args: String[])
    }
    class LeitorArquivo {
        +processarCSV(filepath: String, callback: LinhaCallback)
        +validarArquivo(filepath: String)
    }
    class LinhaCallback {
        <<interface>>
        +processarLinha(campos: String[])
    }
    class ProcessadorEleicao {
        -codigoMunicipio: String
        -dataEleicao: LocalDate
        -partidos: Map<String, Partido>
        -candidatos: Map<String, Candidato>
        -totalVotosNominais: int
        -totalVotosLegenda: int
        +processarLinhaCandidato(campos: String[])
        +processarLinhaVotacao(campos: String[])
        +getTotalVotosValidos(): int
        +getCandidatosEleitos(): List<Candidato>
        +getPartidosOrdenadosPorVotos(): List<Partido>
    }
    class Candidato {
        -numero: String
        -nomeUrna: String
        -partido: Partido
        -dataNascimento: LocalDate
        -votosNominais: int
        -eleito: boolean
        -codigoGenero: int
        -numeroFederacao: int
        +addVotos(votos: int)
        +getIdade(dataRef: LocalDate): int
        +participaFederacao(): boolean
        +toString(): String
    }
    class Partido {
        -numero: int
        -sigla: String
        -candidatos: List<Candidato>
        -votosLegenda: int
        -votosTotais: int
        +addCandidato(candidato: Candidato)
        +addVotosLegenda(votos: int)
        +addVotosNominais(votos: int)
        +getVotosTotais(): int
        +getCandidatosEleitos(): List<Candidato>
    }
    class GeradorRelatorio {
        -processador: ProcessadorEleicao
        -df: DecimalFormat
        +gerarRelatorioCompleto()
    }
    class CandidatoPorVotosComparator {
        +compare(c1: Candidato, c2: Candidato): int
    }
    class PartidoPorVotosComparator {
        +compare(p1: Partido, p2: Partido): int
    }

    Main --> LeitorArquivo : usa
    Main --> ProcessadorEleicao : usa
    Main --> GeradorRelatorio : usa
    LeitorArquivo --> LinhaCallback : usa
    ProcessadorEleicao "0..*" --> "0..*" Partido : partidos
    ProcessadorEleicao "0..*" --> "0..*" Candidato : candidatos
    Partido "1" *--> "0..*" Candidato : candidatos
    Candidato "1" --> "1" Partido : partido
    GeradorRelatorio --> ProcessadorEleicao : depende
    CandidatoPorVotosComparator ..|> Comparator
    PartidoPorVotosComparator ..|> Comparator
    ```