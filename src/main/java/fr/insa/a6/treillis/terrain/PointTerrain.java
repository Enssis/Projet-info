package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.dessin.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * 
 */
public class PointTerrain extends Point {

    private final ArrayList<SegmentTerrain> segments = new ArrayList<>();
    private final ArrayList<Triangle> triangles = new ArrayList<>();

    public PointTerrain(Point p) {
        super(p.getPosX(), p.getPosY());
    }

    public PointTerrain(double posX, double posY){
        super(posX, posY);
    }

    public PointTerrain(double posX, double posY, int id){
        super(posX, posY, id);
    }

    @Override
    public void draw(GraphicsContext gc, Point origin){
        int size = 7;
        int offset = (size - 1) / 2;
        if(selected){
            gc.setFill(Color.DARKRED);
            gc.fillOval(posX - offset + origin.getPosX(), posY - offset + origin.getPosY(), size, size);
        }else if(segmentSelected){
            gc.setStroke(Color.DARKBLUE);
            gc.setLineWidth(2);
            gc.strokeOval(posX - offset + origin.getPosX(), posY - offset + origin.getPosY(), size, size);
        }else{
            gc.setFill(Color.BLACK);
            gc.fillOval(posX - offset + origin.getPosX(), posY - offset + origin.getPosY(), size, size);
        }
    }

    public void addTriangle(Triangle triangle){
        triangles.add(triangle);
    }

    public void addSegments(SegmentTerrain segmentTerrain){
        segments.add(segmentTerrain);
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public ArrayList<SegmentTerrain> getSegments() {
        return segments;
    }

    @Override
    public void drawNear(GraphicsContext gc, Point origin) {
        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(posX - 3 + origin.getPosX(), posY - 3 + origin.getPosY(),7, 7);
    }

    public boolean isPoint(PointTerrain pt){
        return this.posX == pt.posX && this.posY == posY;
    }
}