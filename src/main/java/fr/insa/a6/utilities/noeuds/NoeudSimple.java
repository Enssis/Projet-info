package fr.insa.a6.utilities.noeuds;

import fr.insa.a6.utilities.Point;
import fr.insa.a6.utilities.noeuds.Noeud;

/**
 * 
 */
public class NoeudSimple extends Noeud {

    private final Point position;

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