package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Triangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 
 */
public class AppuiSimple extends Appui {

    /**
     * Default constructor
     */
    public AppuiSimple(Triangle associatedTriangle, SegmentTerrain segmentTerrain, double posSegment, int id) {
        super(associatedTriangle, segmentTerrain, posSegment, id);
    }

    @Override
    public String saveString() {
        return null;
    }

    @Override
    public void draw(GraphicsContext gc, Point origin) {
        if(selected){
            gc.setFill(Color.RED);
            gc.fillOval(posX - 5 + origin.getPosX(), posY - 5 + origin.getPosY(), 11, 11);
        }else if(segmentSelected){
            gc.setStroke(Color.DARKBLUE);
            gc.setLineWidth(2);
            gc.strokeOval(posX - 5 + origin.getPosX(), posY - 5 + origin.getPosY(), 11, 11);
        }else{
            gc.setStroke(Color.DARKGRAY);
            gc.setLineWidth(2);
            gc.strokeOval(posX - 5 + origin.getPosX(), posY - 5 + origin.getPosY(), 11, 11);
        }
    }
}