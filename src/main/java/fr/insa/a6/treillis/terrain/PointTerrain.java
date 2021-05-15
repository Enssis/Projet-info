package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.dessin.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 
 */
public class PointTerrain extends Point {

    private SegmentTerrain[] segments;
    private Triangle[] triangles;

    public PointTerrain() {
        super();
    }

    public PointTerrain(Point p) {
        super(p.getPosX(), p.getPosY());
    }

    public PointTerrain(double posX, double posY){
        super(posX, posY);
    }

    public PointTerrain(double posX, double posY, int id){
        super(posX, posY, id);
    }

    public void draw(GraphicsContext gc){
        if(selected){
            gc.setFill(Color.RED);
            gc.fillOval(posX - 5, posY - 5, 11, 11);
        }else if(segmentSelected){
            gc.setStroke(Color.DARKBLUE);
            gc.setLineWidth(2);
            gc.strokeOval(posX - 5, posY - 5, 11, 11);
        }else{
            gc.setStroke(Color.GRAY);
            gc.setLineWidth(2);
            gc.strokeOval(posX - 5, posY - 5, 11, 11);
        }
    }

}