package fr.insa.a6.treillis;

import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.Noeud;

/**
 * 
 */
public class Barres extends Segment {

    private Type type;

    public Barres(Noeud pA, Noeud pB, Type type, int id) {
        super(pA, pB);
        this.id = id;
        this.type = type;
    }

    public String saveString() {
        return "Barre;" + id + ";" + type.getId() + ";" + pA.getId() + ";" + pB.getId();
    }


}