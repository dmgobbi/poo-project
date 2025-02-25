package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProcessadorEleicao {
    // Constantes para colunas do arquivo de candidatos
    private static final int SG_UE = 11;
    private static final int CD_CARGO = 13;
    private static final int NR_CANDIDATO = 16;
    private static final int NM_URNA_CANDIDATO = 18;
    private static final int NR_PARTIDO = 25;
    private static final int SG_PARTIDO = 26;
    private static final int NR_FEDERACAO = 28;
    private static final int DT_NASCIMENTO = 36;
    private static final int CD_GENERO = 38;
    private static final int CD_SIT_TOT_TURNO = 48;

    // Valores específicos para validação
    private static final String CARGO_VEREADOR = "13";
    private static final String CANDIDATO_INVALIDO = "-1";
    private static final String CANDIDATO_ELEITO = "2";
    private static final String CANDIDATO_ELEITO_MEDIA = "3";

    // Constantes para colunas do arquivo de votação
    private static final int CD_MUNICIPIO = 13;
    private static final int CD_CARGO_VOTACAO = 17;
    private static final int NR_VOTAVEL = 19;
    private static final int QT_VOTOS = 21;

    // Constantes para tipos de votos
    private static final String VOTO_BRANCO = "95";
    private static final String VOTO_NULO = "96";
    private static final String VOTO_ANULADO_97 = "97";
    private static final String VOTO_ANULADO_98 = "98"; // verificar na documentacao

    private final String codigoMunicipio;
    private final LocalDate dataEleicao;
    private final Map<String, Partido> partidos;
    private final Map<String, Candidato> candidatos;
    private int totalVotosNominais;
    private int totalVotosLegenda;

    public ProcessadorEleicao(String codigoMunicipio, LocalDate dataEleicao) {
        this.codigoMunicipio = codigoMunicipio;
        this.dataEleicao = dataEleicao;
        this.partidos = new HashMap<>();
        this.candidatos = new HashMap<>();
        this.totalVotosNominais = 0;
        this.totalVotosLegenda = 0;
    }
    
    public void processarArquivoCandidatos(List<String[]> registros) {
        for (String[] registro : registros) {
            if (!registro[SG_UE].equals(codigoMunicipio) || !registro[CD_CARGO].equals(CARGO_VEREADOR)) {
                continue; // Ignora registros de outros municípios ou cargos
            }
            
            // Ignora candidatos com situação inválida
            if (registro[CD_SIT_TOT_TURNO].equals(CANDIDATO_INVALIDO)) {
                continue;
            }
            
            // Processa partido
            int numeroPartido = Integer.parseInt(registro[NR_PARTIDO]);
            String siglaPartido = registro[SG_PARTIDO];
            Partido partido = partidos.computeIfAbsent(
                String.valueOf(numeroPartido),
                k -> new Partido(numeroPartido, siglaPartido)
            );
            
            // Processa candidato
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataNascimento = LocalDate.parse(registro[DT_NASCIMENTO], formatter);
            Genero genero = Genero.fromCodigo(Integer.parseInt(registro[CD_GENERO]));
            int numeroFederacao = Integer.parseInt(registro[NR_FEDERACAO]);

            Candidato candidato = new Candidato(
                registro[NR_CANDIDATO],      // número candidato
                registro[NM_URNA_CANDIDATO],      // nome urna
                partido,          // partido
                dataNascimento,   // data nascimento
                genero,          // gênero
                numeroFederacao  // número federação
            );

            partido.addCandidato(candidato);
            candidatos.put(candidato.getNumero(), candidato);
            
            // Define se foi eleito
            boolean eleito = registro[CD_SIT_TOT_TURNO].equals(CANDIDATO_ELEITO) || registro[CD_SIT_TOT_TURNO].equals(CANDIDATO_ELEITO_MEDIA);
            candidato.setEleito(eleito);
        }
    }
    
    public void processarArquivoVotacao(List<String[]> registros) {
        for (String[] registro : registros) {
            if (!registro[CD_MUNICIPIO].equals(codigoMunicipio) || !registro[CD_CARGO_VOTACAO].equals(CARGO_VEREADOR)) {
                continue; // Ignora registros de outros municípios ou cargos
            }
            
            String numeroVotavel = registro[NR_VOTAVEL];
            int qtdVotos = Integer.parseInt(registro[QT_VOTOS]);
            
            // Ignora votos brancos/nulos
            if (numeroVotavel.equals(VOTO_BRANCO) || numeroVotavel.equals(VOTO_NULO) || 
                numeroVotavel.equals(VOTO_ANULADO_97) || numeroVotavel.equals(VOTO_ANULADO_98)) {
                continue;
            }
            
            // Verifica se é voto de legenda (2 dígitos) ou nominal (5 dígitos)
            if (numeroVotavel.length() == 2) {
                Partido partido = partidos.get(numeroVotavel);
                if (partido != null) {
                    partido.addVotosLegenda(qtdVotos);
                    totalVotosLegenda += qtdVotos;
                }
            } else {
                Candidato candidato = candidatos.get(numeroVotavel);
                if (candidato != null) {
                    candidato.addVotos(qtdVotos);
                    candidato.getPartido().addVotosNominais(qtdVotos);
                    totalVotosNominais += qtdVotos;
                }
            }
        }
    }
    
    // Getters úteis para relatórios
    public int getTotalVotosValidos() {
        return totalVotosNominais + totalVotosLegenda;
    }
    
    public List<Candidato> getCandidatosEleitos() {
        return candidatos.values().stream()
                .filter(Candidato::isEleito)
                .toList();
    }
    
    public List<Partido> getPartidosOrdenadosPorVotos() {
        return partidos.values().stream()
                .sorted((p1, p2) -> {
                    int compareVotos = Integer.compare(p2.getVotosTotais(), p1.getVotosTotais());
                    if (compareVotos == 0) {
                        return Integer.compare(p1.getNumero(), p2.getNumero());
                    }
                    return compareVotos;
                })
                .toList();
    }
    
    public LocalDate getDataEleicao() {
        return dataEleicao;
    }
    
    public Map<String, Partido> getPartidos() {
        return Collections.unmodifiableMap(partidos);
    }
    
    public Map<String, Candidato> getCandidatos() {
        return Collections.unmodifiableMap(candidatos);
    }

    public int getTotalVotosNominais() {
        return totalVotosNominais;
    }
}