package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * 
 */
public class SegmentTerrain extends Segment {

    private final ArrayList<Triangle> triangles = new ArrayList<>();

    public SegmentTerrain(PointTerrain pA, PointTerrain pB) {
        super(pA, pB);
    }

    @Override
    public void draw(GraphicsContext gc) {
        if(selected){
            gc.setStroke(Color.RED);
        }else {
            gc.setStroke(Color.DARKGRAY);
        }
        gc.setLineWidth(2);
        gc.strokeLine(pA.getPosX(), pA.getPosY(), pB.getPosX(), pB.getPosY());
    }

    @Override
    public void drawNear(GraphicsContext gc) {
        super.drawNear(gc);
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public PointTerrain getpA() {
        return (PointTerrain) super.getpA();
    }

    @Override
    public PointTerrain getpB() {
        return (PointTerrain) super.getpB();
    }

    public void addTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    public boolean isSegment(SegmentTerrain s){
        return s.pA == pA && s.pB == pB || s.pA == pB && s.pB == pB;
    }
}