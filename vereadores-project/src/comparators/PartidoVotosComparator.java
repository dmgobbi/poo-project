package comparators;

import model.Candidato;

public class PartidoVotosComparator implements CandidatoComparator {
    @Override
    public int compare(Candidato c1, Candidato c2) {
        int comparePartido = Integer.compare(
            c1.getPartido().getNumero(), 
            c2.getPartido().getNumero()
        );
        
        if (comparePartido == 0) {
            return Integer.compare(c2.getVotosNominais(), c1.getVotosNominais());
        }
        return comparePartido;
    }
}