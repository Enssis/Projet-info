package fr.insa.a6.utilities.systemeLineaire;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.nodes.AppuiDouble;
import fr.insa.a6.treillis.nodes.AppuiSimple;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.nodes.NoeudSimple;

import java.util.ArrayList;

public class SystemeLineaire {
    //faire tout ce que j'ai mis sur la feuille

    private ArrayList<Noeud> noeuds;
    private ArrayList<Barres> barres;
    private Treillis treillis;

    public SystemeLineaire (Treillis treillis,  Noeud clneud) {
        this.noeuds = treillis.getNoeuds();
        this.barres = treillis.getBarres();
        this.treillis = treillis;

        int nbbarres = barres.size();
        int nbnoeuds = noeuds.size();
        int nbnoeudssimple = 0;
        int nbasimple = 0;
        int nbadouble = 0;

        for (int f = 0; f < nbnoeuds; f++) {
            if (noeuds.get(f) instanceof NoeudSimple) {
                nbnoeudssimple = nbnoeudssimple + 1;
            }
            else if (noeuds.get(f) instanceof AppuiSimple) {
                nbasimple = nbasimple + 1;
            }
            else if (noeuds.get(f) instanceof AppuiDouble) {
                nbadouble = nbadouble + 1;
            }
            else {/* faire l'erreur -> appui encastré, on sait pas gérer */ }
        }

        if (2 * nbnoeudssimple != nbbarres + nbasimple + 2 * nbadouble) { /* faire l'erreur de : EH C'EST PAS ISOSTATIQUE */ }

    }

}
