package fr.insa.a6.utilities;

import java.util.*;

/**
 * 
 */
public class PointTerrrain extends Point {

    private Segment[] segments;
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