package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.Point;

/**
 * 
 */
public class PointTerrrain extends Point {

    private SegmentTerrain[] segments;
    private Triangle[] triangles;

    /**
     * Default constructor
     */
    public PointTerrrain() {
        super();
    }

    public PointTerrrain(double posX, double posY){
        super(posX, posY);
    }


}