package comparators;

import java.util.Comparator;
import model.Candidato;

public interface CandidatoComparator extends Comparator<Candidato> {
    int compare(Candidato c1, Candidato c2);
}