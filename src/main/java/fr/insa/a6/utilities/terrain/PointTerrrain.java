package fr.insa.a6.utilities.terrain;

import fr.insa.a6.utilities.Point;

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