package fr.insa.a6.treillis.dessin;

import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Segment extends Forme{

    protected Point pA, pB;

    public Segment(){

    }

    public Segment(Point a, Point b, int id){
        this(a, b);
        this.id = id;
    }

    public Segment(Point a, Point b){
        pA = a;
        pB = b;
    }

    public void draw(GraphicsContext gc) {
        if(selected){
            gc.setStroke(Color.RED);
        }else {
            gc.setStroke(Color.GRAY);
        }
        gc.setLineWidth(3);
        gc.strokeLine(pA.getPosX(), pA.getPosY(), pB.getPosX(), pB.getPosY());
    }

    @Override
    public void drawNear(GraphicsContext gc) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        gc.strokeLine(pA.getPosX(), pA.getPosY(), pB.getPosX(), pB.getPosY());
    }

    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("Premiere extrémitée : ");
        infos.addAll(pA.getInfos());
        infos.add("Deuxieme extrémitée : ");
        infos.addAll(pB.getInfos());
        infos.add("Longueur : " + length());

        return infos;

    }

    public Point getCenter(){
        double posX = (pA.getPosX() + pB.getPosX()) / 2;
        double posY = (pA.getPosY() + pB.getPosY()) / 2;
        return new Point(posX, posY);
    }

    public double length(){
        return (double) ((int)(Maths.dist(pA, pB) * 100)) / 100;
    }

    public Point getpA() {
        return pA;
    }

    public Point getpB() {
        return pB;
    }
}
