package fr.insa.a6.treillis;

import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.Noeud;

/**
 * 
 */
public class Barres extends Segment {

    /**
     * Default constructor
     */
    public Barres(Noeud pA, Noeud pB, int id) {
        super(pA, pB);
        this.id = id;
    }


}