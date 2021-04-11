package fr.insa.a6.utilities;

public class Segment {

    private final Point[] extremites = new Point[2];

    public Segment(){

    }

    public Segment(Point a, Point b){
        extremites[0] = a;
        extremites[1] = b;
    }


}
