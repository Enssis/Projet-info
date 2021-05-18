package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.terrain.PointTerrain;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Triangle;

/**
 * 
 */
public abstract class Appui extends Noeud {

    private final Triangle associatedTriangle;
    private final PointTerrain segmentPoint;
    private SegmentTerrain segmentTerrain;
    private final double posSegment;

    public Appui(Triangle associatedTriangle, SegmentTerrain segmentTerrain, double posSegment, int id){
        super();
        this.associatedTriangle = associatedTriangle;
        this.id = id;
        this.segmentTerrain = segmentTerrain;
        this.segmentPoint = segmentTerrain.getpA();
        this.posSegment = posSegment;

        this.posX = segmentPoint.getPosX() + (segmentTerrain.getpB().getPosX() - segmentPoint.getPosX()) * posSegment;
        this.posY = segmentPoint.getPosY() + (segmentTerrain.getpB().getPosY() - segmentPoint.getPosY()) * posSegment;
    }

    public double getPosSegment() {
        return posSegment;
    }

    public PointTerrain getSegmentPoint() {
        return segmentPoint;
    }

    public Triangle getAssociatedTriangle() {
        return associatedTriangle;
    }

}