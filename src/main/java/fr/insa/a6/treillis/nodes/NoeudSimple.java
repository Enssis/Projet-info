package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.dessin.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 
 */
public class NoeudSimple extends Noeud {


    public NoeudSimple() {

    }

    public NoeudSimple(Point pos) {
        this.posX = pos.getPosX();
        this.posY = pos.getPosY();
    }

    public NoeudSimple(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void draw(GraphicsContext gc){
        if(selected){
            gc.setFill(Color.RED);
            gc.fillOval(posX - 5, posY - 5, 11, 11);
        }else {
            gc.setStroke(Color.GRAY);
            gc.setLineWidth(2);
            gc.strokeOval(posX - 5, posY - 5, 11, 11);
        }
    }

    public void drawNear(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(posX - 2, posY - 2,5, 5);
    }

}