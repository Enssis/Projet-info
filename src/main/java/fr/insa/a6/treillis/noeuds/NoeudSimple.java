package fr.insa.a6.treillis.noeuds;

import fr.insa.a6.treillis.Point;

/**
 * 
 */
public class NoeudSimple extends Noeud {

    private Point position;

    /**
     * Default constructor
     */
    public NoeudSimple() {
        position = new Point();
    }

    public NoeudSimple(Point pos) {
        position = pos;
    }

    public NoeudSimple(double posX, double posY) {
        position = new Point(posX, posY);
    }


}