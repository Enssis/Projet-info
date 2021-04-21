package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.treillis.Point;
import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;

public class MainCanvas extends Canvas {

    private HashMap<Integer, Point> points = new HashMap<>();
    private GraphicsContext gc;
    private double mouseX = 0, mouseY = 0;
    private int actuKey = 2;
    private Point nearest, curentSelect;
    private MainScene mainScene;

    //bouttons de selections : 0 -> select ; 1x -> noeud; 2 -> barre; 3 -> pointTerrain; 4 -> segmentTerrain
    private int selectedButton = 0;

    public MainCanvas(double width, double height, MainScene mainScene) {
        super(width, height);

        this.mainScene = mainScene;

        gc = this.getGraphicsContext2D();

        points.put(1,new Point(200,200));
        drawpoint();

        gc.setFill(Color.LIGHTCYAN);
        gc.fillRect(
                0,
                0,
                this.getWidth(),
                this.getHeight()
        );


        //ajout des fonctions appelés durant différentes actions de la souris
        this.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            redraw();
        });

        this.setOnMousePressed(mouseEvent -> {
            //treillis.addNoeud/barre()
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            switch (selectedButton) {
                case 0 -> {
                    try {
                        setSelected();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case 10 -> addNoeud();
            }

        });

    }

    private void addNoeud(){
        Point p = new Point(mouseX, mouseY, actuKey);
        points.put(actuKey, p);
        actuKey ++;
        redraw();
    }

    private void setSelected() throws IOException {
        if (nearest != null) {
            if (curentSelect != null) {
                curentSelect.setSelected(false);
            }
            if(nearest.equals(curentSelect)){
                nearest.setSelected(false);
                curentSelect = null;
                mainScene.setRight(null);
            }else {
                nearest.setSelected(true);
                curentSelect = nearest;
                InfoWindow iWindow = new InfoWindow(nearest, this);
                mainScene.setRight(iWindow);
            }
        }
    }

    private void drawpoint() {

        nearest = null;
        double bestDist = 15;

        for (Point p : points.values()) {
            p.draw(gc);
            double dist = Maths.dist(p, new Point(mouseX, mouseY));
            if(dist < bestDist){
                nearest = p;
                bestDist = dist;
            }
        }

        if(nearest != null){
            nearest.drawNear(gc);
        }

    }


    public void redraw(){
        gc.setFill(Color.LIGHTCYAN);
        gc.fillRect(
                0,
                0,
                this.getWidth(),
                this.getHeight());

        drawpoint();
    }

    public void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;

    }

    public void removeSelected() {
        if (curentSelect != null) {
            curentSelect.setSelected(false);
            mainScene.setRight(null);
        }
        curentSelect = null;
    }

    public void deletePoint(Point p){
        if(p != null){
            points.remove(p.getId());
            curentSelect = null;
        }
    }
}