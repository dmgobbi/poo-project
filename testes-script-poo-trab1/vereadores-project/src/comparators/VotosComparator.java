package comparators;

import java.time.LocalDate;
import model.Candidato;

public class VotosComparator implements CandidatoComparator {
    private final LocalDate dataEleicao;

    public VotosComparator(LocalDate dataEleicao) {
        this.dataEleicao = dataEleicao;
    }

    @Override
    public int compare(Candidato c1, Candidato c2) {
        int compareVotos = Integer.compare(c2.getVotosNominais(), c1.getVotosNominais());
        if (compareVotos == 0) {
            return Integer.compare(c2.getIdade(dataEleicao), c1.getIdade(dataEleicao));
        }
        return compareVotos;
    }
}