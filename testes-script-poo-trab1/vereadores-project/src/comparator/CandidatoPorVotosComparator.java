package comparator;

import model.Candidato;
import java.util.Comparator;

public class CandidatoPorVotosComparator implements Comparator<Candidato> {
    @Override
    public int compare(Candidato c1, Candidato c2) {
        // Ordem decrescente de votos
        int comparacaoVotos = Integer.compare(c2.getVotosNominais(), c1.getVotosNominais());
        if (comparacaoVotos == 0) {
            // Desempate por data de nascimento (mais velho primeiro)
            return c1.getDataNascimento().compareTo(c2.getDataNascimento());
        }
        return comparacaoVotos;
    }
}