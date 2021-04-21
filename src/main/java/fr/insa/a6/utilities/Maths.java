package fr.insa.a6.utilities;

import fr.insa.a6.treillis.Point;

public class Maths {

    public static double dist(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getPosX() - b.getPosX(), 2) + Math.pow(a.getPosY() - b.getPosY(), 2));
    }
}
