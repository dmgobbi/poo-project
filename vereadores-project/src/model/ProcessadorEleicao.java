package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProcessadorEleicao {
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
            if (!registro[0].equals(codigoMunicipio) || !registro[1].equals("13")) {
                continue; // Ignora registros de outros municípios ou cargos
            }
            
            // Ignora candidatos com situação inválida
            if (registro[8].equals("-1")) {
                continue;
            }
            
            // Processa partido
            int numeroPartido = Integer.parseInt(registro[4]);
            String siglaPartido = registro[5];
            Partido partido = partidos.computeIfAbsent(
                String.valueOf(numeroPartido),
                k -> new Partido(numeroPartido, siglaPartido)
            );
            
            // Processa candidato
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataNascimento = LocalDate.parse(registro[7], formatter);
            Genero genero = Genero.fromCodigo(Integer.parseInt(registro[9]));
            int numeroFederacao = Integer.parseInt(registro[6]);
            
            Candidato candidato = new Candidato(
                registro[2],      // número candidato
                registro[3],      // nome urna
                partido,          // partido
                dataNascimento,   // data nascimento
                genero,          // gênero
                numeroFederacao  // número federação
            );
            
            partido.addCandidato(candidato);
            candidatos.put(candidato.getNumero(), candidato);
            
            // Define se foi eleito
            boolean eleito = registro[8].equals("2") || registro[8].equals("3");
            candidato.setEleito(eleito);
        }
    }
    
    public void processarArquivoVotacao(List<String[]> registros) {
        for (String[] registro : registros) {
            if (!registro[1].equals(codigoMunicipio) || !registro[0].equals("13")) {
                continue; // Ignora registros de outros municípios ou cargos
            }
            
            String numeroVotavel = registro[2];
            int qtdVotos = Integer.parseInt(registro[3]);
            
            // Ignora votos brancos/nulos
            if (numeroVotavel.equals("95") || numeroVotavel.equals("96") || 
                numeroVotavel.equals("97") || numeroVotavel.equals("98")) {
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
}