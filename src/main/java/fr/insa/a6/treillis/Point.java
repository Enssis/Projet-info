package fr.insa.a6.treillis;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 
 */
public class Point {

    protected final double posX;
    protected final double posY;
    protected final int id;
    private boolean selected = false;

    /**
     * Default constructor
     */
    public Point() {
        this(0,0, -1);
    }

    public Point(double x, double y) {
        this(x, y,-1);
    }

    public Point(double x, double y, int id) {
        posY = y;
        posX = x;
        this.id = id;
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

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getId() {
        return id;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String[] getInfos(){
        return new String[]
                {"posX : " + posX ,
                "posY : " + posY,
                "selected : " + selected
                };

    }
}