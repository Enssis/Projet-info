package fr.insa.a6.utilities.systemeLineaire;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.Treillis;

import java.util.ArrayList;

public class SystemeLineaire {
    //faire tout ce que j'ai mis sur la feuille

    private ArrayList<Noeud> noeuds;
    private ArrayList<Barres> barres;
    private Treillis treillis;

    public SystemeLineaire (Treillis treillis){
        this.noeuds = treillis.getNoeuds();
        this.barres = treillis.getBarres();
        this.treillis = treillis;

        int nbbarres = barres.size();
        int nbnoeuds = noeuds.size();

     //   for (int f = 0; f < nbnoeuds; f++){



    //    }


    }

}
