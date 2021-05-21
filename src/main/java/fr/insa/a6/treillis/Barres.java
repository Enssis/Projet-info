package fr.insa.a6.treillis;

import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.Noeud;

/**
 * 
 */
public class Barres extends Segment {

    private final Type type;

    public Barres(Noeud pA, Noeud pB, Type type, int id) {
        super(pA, pB);
        this.id = id;
        this.type = type;
    }

    public String saveString() {
        int typeId;
        if(type == null) typeId = -1;
        else typeId = type.getId();

        return "Barre;" + id + ";" + typeId + ";" + pA.getId() + ";" + pB.getId();
    }

    public Type getType() {
        return type;
    }
}