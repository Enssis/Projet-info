package fr.insa.a6.utilities;

import java.util.*;

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