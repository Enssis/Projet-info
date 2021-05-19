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

    public void draw(GraphicsContext gc, Point origin) {
        if(selected){
            gc.setStroke(Color.RED);
        }else {
            gc.setStroke(Color.GRAY);
        }
        gc.setLineWidth(3);
        gc.strokeLine(pA.getPosX() + origin.getPosX(), pA.getPosY() + origin.getPosY(),
                pB.getPosX() + origin.getPosX(), pB.getPosY() + origin.getPosY());
    }

    @Override
    public void drawNear(GraphicsContext gc, Point origin) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        gc.strokeLine(pA.getPosX() + origin.getPosX(), pA.getPosY() + origin.getPosY(),
                pB.getPosX() + origin.getPosX(), pB.getPosY() + origin.getPosY());
    }

    public static void drawGhost(GraphicsContext gc, Point pA, Point pB, Point origin) {
        gc.setStroke(Color.GRAY);
        gc.setGlobalAlpha(0.3);
        gc.setLineWidth(3);
        gc.strokeLine(pA.getPosX() + origin.getPosX(), pA.getPosY() + origin.getPosY(),
                pB.getPosX() + origin.getPosX(), pB.getPosY() + origin.getPosY());
        gc.setGlobalAlpha(1);
    }

    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("Premiere extrémitée : ");
        infos.addAll(pA.getInfos());
        infos.add("Deuxieme extrémitée : ");
        infos.addAll(pB.getInfos());
        infos.add("Longueur : " + lengthInfo());

        return infos;

    }


    public Point getCenter(){
        double posX = (pA.getPosX() + pB.getPosX()) / 2;
        double posY = (pA.getPosY() + pB.getPosY()) / 2;
        return new Point(posX, posY);
    }

    public double lengthInfo(){
        return (double) ((int)(Maths.dist(pA, pB) * 100)) / 100;
    }

    public double length(){
        return Maths.dist(pA, pB);
    }

    public Point getpA() {
        return pA;
    }

    public Point getpB() {
        return pB;
    }
}
