package fr.insa.a6.graphic;

import fr.insa.a6.graphic.mainbox.InfoWindow;
import fr.insa.a6.graphic.mainbox.MainCanvas;
import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.*;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//classe s'occupant principalement de tout ce qui se réfère au dessin sur le canvas
public class Graphics {

    private GraphicsContext gc;
    private MainScene mainScene;
    private ArrayList<Forme> formes = new ArrayList<>();
    private ActionCenter ac;
    private final Point origin = new Point(0,0);
    private final Point lastOrigin = new Point(origin.getPosX(), origin.getPosY());
    private double scale = 1;

    private InfoWindow infoWindow;

    public Graphics() {

    }

    //méthode d'initialisation, nécessaire de ne pas la mettre dans le constructeur car sinon bug
    //(besoin d'une instance de canvas pour créer cette instance et de cette instace pour créer l'instance de canvas)
    public void init(MainScene mainScene, GraphicsContext graphicsContext, ActionCenter actionCenter){
        this.mainScene = mainScene;
        this.gc = graphicsContext;
        this.ac = actionCenter;

        infoWindow = mainScene.getInfos();
    }

    public void removeInfos()
    {
        infoWindow.removeInfos();
    }

    public void drawInfosMultiplePoint(int nbNoeud,int nbAppuiDouble, int nbAppuiSimple,int nbBarre) {
        infoWindow.drawInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
    }

    public void drawInfos(Forme nearest){
        infoWindow.drawInfos(nearest);
    }

    public void drawInfos(Terrain terrain){
        infoWindow.drawInfos(terrain);
    }

    public void resetFormes(){
        formes = new ArrayList<>();
    }

    public void updateFormes(Treillis treillis){
        ArrayList<Noeud> noeuds = treillis.getNoeuds();

        for (Noeud n: noeuds) {
            if(!formes.contains(n)){
                formes.add(n);
            }
        }

        ArrayList<Barres> barres = treillis.getBarres();

        for (Barres b: barres) {
            if(!formes.contains(b)){
                formes.add(b);
            }
        }
    }

    private void drawNear(){
        Forme nearest = ac.getNearest();
        if(nearest != null){
            nearest.drawNear(gc, origin);
        }
    }

    //fonction de dessin principale
    public void draw(int selectedButton, boolean inDrawing) {
        Point mousePoint = new Point(ac.getMouseX(), ac.getMouseY());

        MainCanvas canvas = mainScene.getCanvas();

        //fond
        gc.setFill(Color.LIGHTCYAN);
        gc.fillRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight());

        //dessin de l'echelle
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.setFill(Color.BLACK);
        gc.fillText("1 m", canvas.getWidth() - 30 - ac.getEchelle() / 2, canvas.getHeight() - 5);
        gc.strokeLine(canvas.getWidth() - 20 - ac.getEchelle(), canvas.getHeight() - 20, canvas.getWidth() - 20, canvas.getHeight() - 20);



        //dessin du terrain
        Terrain terrain = ac.getTreillis().getTerrain();
        if(terrain != null) terrain.draw(gc, inDrawing, origin);

        //dessin des noeud et des barres
        for (Forme f: formes) {
            if(selectedButton != 0) {
                f.setSelected(false);
            }
            if(selectedButton != 20 && selectedButton != 40 && f instanceof Point){
                ((Point) f).setSegmentSelected(false);
            }
            f.draw(gc, origin);
        }

        //dessin des noeuds et barres selectionné + des infos associées
        if(ac.isInMultSelect()){
            ArrayList<Forme> multipleSelect = ac.getMultipleSelect();
            int nbNoeud = 0;
            int nbAppuiSimple = 0;
            int nbAppuiDouble = 0;
            int nbBarre = 0;
            for (Forme f: multipleSelect) {
                if(f instanceof NoeudSimple) nbNoeud ++;
                else if(f instanceof Barres) nbBarre ++;
                else if(f instanceof AppuiDouble) nbAppuiDouble ++;
                else if(f instanceof AppuiSimple) nbAppuiSimple ++;
            }
            drawInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
        }

        //dessin de la zone de selection
        if (ac.isDrag()) {
            double mouseX = ac.getMouseX(), mouseY = ac.getMouseY();
            double dragMouseX = ac.getDragMouseX(), dragMouseY = ac.getDragMouseY();
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
        }else if(selectedButton == 0 || selectedButton == 20 || selectedButton == 40){
            drawNear();
        }
        gc.setGlobalAlpha(1);

        if(ac.isInDrawing()) {
            //dessin le fantome de la zone constructible
            if (selectedButton == 30 && ac.getCurrentClick() == 1) {
                drawTerrainZone(ac.getMouseX(), ac.getMouseY(), ac.getTerrainX() + origin.getPosX(), ac.getTerrainY() + origin.getPosY());
            }

            //dessin le fantome des triangles
            if (selectedButton == 40) {
                if (ac.getCurrentClick() >= 1 && ac.getFirstSegmentPoint() != null) {
                    SegmentTerrain.drawGhost(gc, origin, ac.getFirstSegmentPoint(), mousePoint);
                }
                if (ac.getCurrentClick() == 2 && ac.getSecondSegmentPoint() != null && ac.getFirstSegmentPoint() != null) {
                    SegmentTerrain.drawGhost(gc, origin, ac.getFirstSegmentPoint(), new Point(
                            ac.getSecondSegmentPoint().getPosX() + origin.getPosX(),
                            ac.getSecondSegmentPoint().getPosY() + origin.getPosY()));
                    SegmentTerrain.drawGhost(gc, origin, ac.getSecondSegmentPoint(), mousePoint);
                }
            }

            //dessin le fantome des barres
            if (selectedButton == 20) {
                Point firstPoint = ac.getFirstSegmentPoint();
                if (ac.getCurrentClick() >= 1 && firstPoint != null) {
                    if (Maths.dist(firstPoint, mousePoint.substract(origin)) > ac.getBarreType().getlMin() * ac.getEchelle()) {
                        Point point = mousePoint;

                        double lMax = ac.getBarreType().getlMax() * ac.getEchelle();
                        if (Maths.dist(firstPoint, mousePoint.substract(origin)) > lMax) {
                            double angle = Maths.angle(firstPoint, mousePoint.substract(origin));
                            point = new Point(firstPoint.getPosX() + lMax * Math.cos(angle) + origin.getPosX(), firstPoint.getPosY() + lMax * Math.sin(angle) + origin.getPosY());
                        }


                        assert terrain != null;
                        SegmentTerrain segment = Appui.isCreable(terrain, mousePoint.getPosX() - origin.getPosX(), mousePoint.getPosY() - origin.getPosY());
                        if (segment != null) {
                            Point point1 = Appui.drawGhost(gc, origin, segment, Maths.dist(segment.getpA(), mousePoint.getPosX() - origin.getPosX(), mousePoint.getPosY() - origin.getPosY()) / segment.length());
                            Barres.drawGhost(gc, ac.getFirstSegmentPoint(), point1, origin);
                        } else if (terrain.containOutTriangle(point.getPosX() - origin.getPosX(), point.getPosY() - origin.getPosY())) {
                            Barres.drawGhost(gc, ac.getFirstSegmentPoint(), point, origin);
                            point.drawGhost(gc, new Point(0, 0));
                        }
                    }

                }
            }

            //dessin du fantome des noeuds
            if (!(selectedButton == 0 || selectedButton == 30) && (selectedButton != 20 || ac.getCurrentClick() == 0)) {
                assert terrain != null;

                SegmentTerrain segment = Appui.isCreable(terrain, mousePoint.getPosX() - origin.getPosX(), mousePoint.getPosY() - origin.getPosY());
                if (selectedButton == 10 || (segment == null && (selectedButton != 11 && selectedButton != 12))) {
                    if (terrain.containOutTriangle(mousePoint.getPosX() - origin.getPosX(), mousePoint.getPosY() - origin.getPosY())) {
                        mousePoint.drawGhost(gc, new Point(0, 0));
                    }
                } else if (segment != null) {
                   Appui.drawGhost(gc, origin, segment, Maths.dist(segment.getpA(), mousePoint.getPosX() - origin.getPosX(), mousePoint.getPosY() - origin.getPosY()) / segment.length());
                }

            }
        }else{
            HashMap<Integer, double[]> results = ac.getResultCalcul();
            HashMap<Forme, Integer> formeId = ac.getFormeId();

            if(results.size() > 0) {
                for (Forme value : formeId.keySet()) {
                    int id = formeId.get(value);
                    if (value instanceof AppuiDouble) {
                        value.drawResult(results.get(id)[0], gc, origin);
                        value.drawResult(results.get(id)[1], gc, new Point(origin.getPosX() + 5, origin.getPosY() + 10));

                    } else if (value instanceof Barres) {
                        value.drawResult(results.get(id)[0], gc, origin);
                    }else {
                        value.drawResult(results.get(id)[0], gc, origin);
                    }
                }
            }
        }

    }

    public void drawTerrainZone(double x1, double y1, double x2, double y2){

        double x = Math.min(x1, x2), y = Math.min(y1, y2);
        double w = Math.abs(x2 - x1), h = Math.abs(y2 - y1);

        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(x, y, w, h);
        gc.setGlobalAlpha(1);

        gc.setStroke(Color.GREEN);
        gc.strokeRect(x, y, w, h);
    }

    public void remove(Forme f){
        formes.remove(f);
    }

    public ArrayList<Forme> getFormes() {
        return formes;
    }

    public void setMainScene(MainScene mainScene) {
        this.mainScene = mainScene;
        this.infoWindow = mainScene.getInfos();
        this.gc = mainScene.getCanvas().getGraphicsContext();
    }

    public void moveOrigin(double x, double y, double newX, double newY){
        origin.setPosX(lastOrigin.getPosX() + (newX - x));
        origin.setPosY(lastOrigin.getPosY() + (newY - y));
    }

    public void updateLastOrigin() {
        this.lastOrigin.setPosX(origin.getPosX());
        this.lastOrigin.setPosY(origin.getPosY());
    }

    public Point getOrigin() {
        return origin;
    }

    public void resetOrigin() {
        origin.setPosX(0);
        origin.setPosY(0);
        lastOrigin.setPosX(0);
        lastOrigin.setPosY(0);
    }
}
