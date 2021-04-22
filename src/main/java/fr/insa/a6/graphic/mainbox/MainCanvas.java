package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class MainCanvas extends Pane {

    private HashMap<Integer, Forme> formes = new HashMap<>();
    private GraphicsContext gc;

    private MainScene mainScene;
    private Canvas canvas;

    private int actuKey = 1;
    private double mouseX = 0, mouseY = 0;
    private Forme nearest, curentSelect;

    private double dragMouseX = -1, dragMouseY = -1;
    private boolean drag = false, inMultSelect = false;
    private ArrayList<Forme> multipleSelect = new ArrayList<>();

    private int segmentClick = 0;
    private Point firstSegmentPoint = null;

    //bouttons de selections : 0 -> select ; 1x -> noeud; 2 -> barre; 3 -> pointTerrain; 4 -> segmentTerrain
    private int selectedButton = 0;

    public MainCanvas(double width, double height, MainScene mainScene) {
        super();
        this.setPrefSize(width, height);

        this.canvas = new Canvas();
        this.mainScene = mainScene;

        gc = canvas.getGraphicsContext2D();

        canvas.setManaged(false);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(canvas);

        this.canvas.heightProperty().addListener((o) -> redraw());
        this.canvas.widthProperty().addListener((o) -> redraw());

       redraw();


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
            inMultSelect = false;
            removeSelectedAll();

            switch (selectedButton) {
                case 0 -> setSelected();
                case 10, 3 -> addPoint();
                case 2, 4 -> addSegment();
            }

        });

        this.setOnMouseReleased(mouseEvent -> {
            dragMouseX = -1;
            dragMouseY = -1;
            drag = false;
            redraw();
        });

        this.setOnMouseDragged(mouseEvent -> {
            if (selectedButton == 0 || selectedButton == 2){
                drag = true;
                inMultSelect = true;
                dragMouseX = mouseEvent.getX();
                dragMouseY = mouseEvent.getY();
                redraw();
            }
        });

    }

    //fonction d'ajout de noeud
    private void addPoint(Point p){
        p.setId(actuKey);
        formes.put(actuKey, p);
        actuKey ++;
        redraw();
    }

    private void addPoint(double x, double y){
        addPoint(new Point(x, y));

    }

    private void addPoint(){
        addPoint(mouseX, mouseY);
    }

    //fonction permettant la selection d'un point
    private void setSelected(){
        if (nearest != null) {
            if (curentSelect != null) {
                curentSelect.setSelected(false);
            }
            if(nearest.equals(curentSelect)){
                nearest.setSelected(false);
                curentSelect = null;
                mainScene.getInfos().removeInfos();
            }else {
                nearest.setSelected(true);
                curentSelect = nearest;
                mainScene.getInfos().drawInfos(nearest);
            }
        }
    }

    private void addSegment() {
        segmentClick ++;
        Point p;
        if(nearest == null || nearest.getClass() != Point.class){
            p = new Point(mouseX, mouseY);
            addPoint(p);
        }else{
            p = (Point) nearest;
        }
        if(segmentClick == 1){
            firstSegmentPoint = p;
        }else{
            segmentClick = 0;
            Segment seg = new Segment(firstSegmentPoint, p, actuKey);
            formes.put(actuKey, seg);
            actuKey ++;
            redraw();
        }
    }

    //fonction de dessin des points et d'opérations sur la selection des points
    private void drawForme() {

        nearest = null;
        double bestDist = 15;

        for (Forme f : formes.values()) {

            Point p;
            if(f.getClass() == Point.class) {
                p = (Point) f;
            }else{
                p = ((Segment) f).getCenter();
            }
            //ajoute les formes dans la selection
            if (drag) {
                if(p.getPosX() < Math.max(mouseX, dragMouseX) && p.getPosX() > Math.min(mouseX, dragMouseX) &&
                        p.getPosY() < Math.max(mouseY, dragMouseY) && p.getPosY() > Math.min(mouseY, dragMouseY)){
                    if(!multipleSelect.contains(f)){
                        multipleSelect.add(f);
                        f.setSelected(true);
                    }
                }else{
                    multipleSelect.remove(f);
                    f.setSelected(false);
                }
            }
            //trouve la forme le plus proche de la souris
            double dist = Maths.dist(p, new Point(mouseX, mouseY));
            if(dist < bestDist){
                nearest = f;
                bestDist = dist;
            }
            //dessine la forme
            f.draw(gc);
        }

        if(nearest != null && (selectedButton == 0 || selectedButton == 2) && !drag){
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

        drawForme();

        if (drag) {
            gc.setFill(Color.BLUE);
            gc.setStroke(Color.DARKBLUE);
            gc.setGlobalAlpha(0.5);
            gc.fillRect(
                    Math.min(mouseX, dragMouseX),
                    Math.min(mouseY, dragMouseY),
                    Math.abs(dragMouseX - mouseX),
                    Math.abs(dragMouseY - mouseY));
            gc.strokeRect(
                    Math.min(mouseX, dragMouseX),
                    Math.min(mouseY, dragMouseY),
                    Math.abs(dragMouseX - mouseX),
                    Math.abs(dragMouseY - mouseY));
        }
        gc.setGlobalAlpha(1);

        if(inMultSelect){
            int nbPoint = 0;
            int nbSeg = 0;
            for (Forme f: multipleSelect) {
                if(f.getClass() == Point.class) nbPoint ++;
                else if(f.getClass() == Segment.class) nbSeg ++;
            }

            mainScene.getInfos().drawInfosMultiplePoint(nbPoint, nbSeg);
        }
    }

    public void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;
    }

    //retire le point selectionner
    public void removeSelected() {
        if (curentSelect != null) {
            curentSelect.setSelected(false);
            mainScene.getInfos().removeInfos();
        }
        curentSelect = null;
    }

    public void removeSelectedAll() {
        multipleSelect.forEach(p -> p.setSelected(false));
        multipleSelect.clear();
        mainScene.getInfos().removeInfos();
    }

    //supprime un point
    public void deleteForme(Forme f){
        if(f != null){
            formes.remove(f.getId());
            curentSelect = null;
        }
    }

    public void deleteAllFormes() {
        multipleSelect.forEach(f -> formes.remove(f.getId()));
        multipleSelect.clear();
        mainScene.getInfos().removeInfos();
        inMultSelect = false;
        redraw();
    }
}