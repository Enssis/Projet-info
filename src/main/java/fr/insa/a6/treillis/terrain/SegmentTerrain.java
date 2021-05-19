package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class SegmentTerrain extends Segment {

    private final ArrayList<Triangle> triangles = new ArrayList<>();
    private final double angle;

    public SegmentTerrain(PointTerrain pA, PointTerrain pB) {
        super(pA, pB);
        angle = Maths.angle(pA, pB);
    }

    @Override
    public void draw(GraphicsContext gc, Point origin) {
        if(selected){
            gc.setStroke(Color.RED);
        }else {
            gc.setStroke(Color.DARKGRAY);
        }
        gc.setLineWidth(2);
        gc.strokeLine(pA.getPosX() + origin.getPosX(), pA.getPosY() + origin.getPosY(),
                pB.getPosX() + origin.getPosX(), pB.getPosY() + origin.getPosY());
    }

    @Override
    public void drawNear(GraphicsContext gc, Point origin) {
        super.drawNear(gc, origin);
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

    // test si un point est sur le segment avec une certaine tolérance
    public boolean contain(double x, double y, int tolerance){
        Point p = new Point(x, y);

        return distTo(p) < tolerance && distTo(p) != -1;
    }

    public double distTo(Point p){
        Point p2 = Maths.rotation(pA, p, angle);

        if(Maths.dist(getCenter(), p) > length() / 2){
            return -1;
        }
        return Math.sqrt(Math.pow(Maths.dist(pA, p), 2) - Math.pow(p2.getPosX() - pA.getPosX(), 2));
    }

    public boolean asOneTriangle() {
        return triangles.size() == 1;
    }

}