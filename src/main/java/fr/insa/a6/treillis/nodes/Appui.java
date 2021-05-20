package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.terrain.PointTerrain;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.treillis.terrain.Triangle;
import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

/**
 * 
 */
public abstract class Appui extends Noeud {

    private final Triangle associatedTriangle;
    private final PointTerrain segmentPoint;
    private SegmentTerrain segmentTerrain;
    private final double posSegment;
    protected Image image;

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

    public SegmentTerrain getSegmentTerrain() {
        return segmentTerrain;
    }

    public static SegmentTerrain isCreable(Terrain terrain, double posX, double posY){
        for (SegmentTerrain s : terrain.getSegments()) {
            if(s.contain(posX, posY, 10) && s.asOneTriangle()){
                return s;
            }
        }
        return null;
    }

    @Override
    public String saveString() {
        int segmentNbr = 0;
        for (int i = 0; i < associatedTriangle.getSegments().length; i++) {
            if(associatedTriangle.getSegments()[i].equals(segmentTerrain)){
                segmentNbr = i;
                break;
            }
        }
        return id + ";" + associatedTriangle.getId() + ";" + segmentNbr + ";" + posSegment;
    }

    //dessine l'image corrrespondante à l'appui avec rotation
    @Override
    public void draw(GraphicsContext gc, Point origin) {
        gc.save();
        // angle de rotation et point pivot
        Rotate r = new Rotate(Maths.angle(segmentTerrain.getpB(), segmentTerrain.getpA()) * 360 / (2 * Math.PI), posX + origin.getPosX(), posY + origin.getPosY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.drawImage(image, posX - image.getWidth()/2 + origin.getPosX(), posY + origin.getPosY());
        gc.restore();
    }
}