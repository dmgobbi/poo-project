package comparator;

import model.Partido;
import model.Candidato;
import java.util.Comparator;

public class PartidoPorVotosComparator implements Comparator<Partido> {
    @Override
    public int compare(Partido p1, Partido p2) {
        // Ordem decrescente pelo candidato com mais votos
        int maxVotosP1 = p1.getCandidatos().stream()
            .mapToInt(Candidato::getVotosNominais)
            .max()
            .orElse(0);
        int maxVotosP2 = p2.getCandidatos().stream()
            .mapToInt(Candidato::getVotosNominais)
            .max()
            .orElse(0);
        
        int comparacaoVotos = Integer.compare(maxVotosP2, maxVotosP1);
        if (comparacaoVotos == 0) {
            // Desempate por número do partido
            return Integer.compare(p1.getNumero(), p2.getNumero());
        }
        return comparacaoVotos;
    }
}