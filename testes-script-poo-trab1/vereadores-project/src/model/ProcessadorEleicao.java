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

    // Valores especificos para validacao
    private static final String CARGO_VEREADOR = "13";
    private static final String CANDIDATO_INVALIDO = "-1";
    private static final String CANDIDATO_ELEITO = "2";
    private static final String CANDIDATO_ELEITO_MEDIA = "3";

    // Constantes para colunas do arquivo de votacao
    private static final int CD_MUNICIPIO = 13;
    private static final int CD_CARGO_VOTACAO = 17;
    private static final int NR_VOTAVEL = 19;
    private static final int QT_VOTOS = 21;

    // Constantes para tipos de votos
    private static final String VOTO_BRANCO = "95";
    private static final String VOTO_NULO = "96";
    private static final String VOTO_ANULADO_97 = "97";
    private static final String VOTO_ANULADO_98 = "98";

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

    private String normalizarCodigoMunicipio(String code) {
        try {
            return Integer.toString(Integer.parseInt(code));
        } catch (NumberFormatException e) {
            return code;
        }
    }

    public void processarLinhaCandidato(String[] campos) {
        // Sempre carrega as informacoes do partido, independente do municipio/cargo
        if (campos.length > NR_PARTIDO && campos.length > SG_PARTIDO) {
            try {
                int numeroPartido = Integer.parseInt(campos[NR_PARTIDO]);
                String siglaPartido = campos[SG_PARTIDO];
                
                partidos.computeIfAbsent(
                    String.valueOf(numeroPartido),
                    k -> new Partido(numeroPartido, siglaPartido)
                );
            } catch (NumberFormatException e) {
                System.err.println("Erro ao converter numero do partido: " + campos[NR_PARTIDO]);
            }
        }

        String codigoMunicipioNormalizado = normalizarCodigoMunicipio(codigoMunicipio);
        String codigoArquivoNormalizado = campos.length > SG_UE ? normalizarCodigoMunicipio(campos[SG_UE]) : "";

        if (!codigoArquivoNormalizado.equals(codigoMunicipioNormalizado) || !campos[CD_CARGO].equals(CARGO_VEREADOR)) {
            return;
        }
        
        if (campos[CD_SIT_TOT_TURNO].equals(CANDIDATO_INVALIDO)) {
            return;
        }
    
        // Processa o partido ja carregado anteriormente
        String numeroPartidoStr = campos[NR_PARTIDO];
        Partido partido = partidos.get(numeroPartidoStr);
             
        // Processa candidato
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataNascimento = LocalDate.parse(campos[DT_NASCIMENTO], formatter);
        int codigoGenero = Integer.parseInt(campos[CD_GENERO]);
        int numeroFederacao = Integer.parseInt(campos[NR_FEDERACAO]);
    
        Candidato candidato = new Candidato(
            campos[NR_CANDIDATO],
            campos[NM_URNA_CANDIDATO],
            partido,
            dataNascimento, 
            codigoGenero,
            numeroFederacao
        );
    
        partido.addCandidato(candidato);
        candidatos.put(candidato.getNumero(), candidato);
        
        boolean eleito = campos[CD_SIT_TOT_TURNO].equals(CANDIDATO_ELEITO) || campos[CD_SIT_TOT_TURNO].equals(CANDIDATO_ELEITO_MEDIA);
        candidato.setEleito(eleito);
    }
    
    public void processarLinhaVotacao(String[] campos) {
        String codigoMunicipioNormalizado = normalizarCodigoMunicipio(codigoMunicipio);
        String codigoArquivoNormalizado = campos.length > CD_MUNICIPIO ? normalizarCodigoMunicipio(campos[CD_MUNICIPIO]) : "";

        if (!codigoArquivoNormalizado.equals(codigoMunicipioNormalizado) || !campos[CD_CARGO_VOTACAO].equals(CARGO_VEREADOR)) {
            return;
        }
        
        String numeroVotavel = campos[NR_VOTAVEL];
        int qtdVotos = Integer.parseInt(campos[QT_VOTOS]);
        
        if (numeroVotavel.equals(VOTO_BRANCO) || numeroVotavel.equals(VOTO_NULO) || 
            numeroVotavel.equals(VOTO_ANULADO_97) || numeroVotavel.equals(VOTO_ANULADO_98)) {
            return;
        }
        
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