package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Force;
import fr.insa.a6.treillis.dessin.Point;
import java.util.ArrayList;

/**
 *
 */

public abstract class Noeud extends Point {

    private Force forceApplique;
    private ArrayList<Barres> linkedBarres;

    /**
     * Default constructor
     */
    public Noeud(int id, Force forceApplique, ArrayList<Barres> linkedBarres) {

        this.id = id;
        this.forceApplique = forceApplique;
        this.linkedBarres= linkedBarres;

    }

    public void addBarres(Barres barre){
        linkedBarres.add(barre);
    }

    public void removeBarres (Barres barre){
        linkedBarres.remove(barre);
    }

    public Noeud (){
        //rien du tout
    }
}