package fr.insa.a6.graphic;

import fr.insa.a6.graphic.mainbox.InfoWindow;
import fr.insa.a6.graphic.mainbox.MainCanvas;
import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.utilities.ActionCenter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;


// class s'occupant principalement de tout ce qui se réfère au dessin sur le canvas
public class Graphics {

    private GraphicsContext gc;
    private MainScene mainScene;
    private ArrayList<Forme> formes = new ArrayList<>();
    private ActionCenter ac;
    private Point origin = new Point(0,0);
    private Point lastOrigin = new Point(origin.getPosX(), origin.getPosY());
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

    public void drawInfosMultiplePoint(int nbPoint, int nbSegment) {
        infoWindow.drawInfosMultiplePoint(nbPoint,nbSegment);
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

        MainCanvas canvas = mainScene.getCanvas();

        //fond
        gc.setFill(Color.LIGHTCYAN);
        gc.fillRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight());

        //dessin du terrain
        Terrain terrain = ac.getTreillis().getTerrain();
        if(terrain != null) terrain.draw(gc, inDrawing, origin);

        //dessin des noeud et des barres
        for (Forme f: formes) {
            if(selectedButton != 0) {
                f.setSelected(false);
            }
            if(selectedButton != 20 && selectedButton != 50 && f instanceof Point){
                ((Point) f).setSegmentSelected(false);
            }
            f.draw(gc, origin);
        }

        //dessin des noeuds et barres selectionné + des infos associées
        if(ac.isInMultSelect()){
            ArrayList<Forme> multipleSelect = ac.getMultipleSelect();
            int nbPoint = 0;
            int nbSeg = 0;
            for (Forme f: multipleSelect) {
                if(f instanceof Point) nbPoint ++;
                else if(f instanceof Segment) nbSeg ++;
            }
            drawInfosMultiplePoint(nbPoint, nbSeg);
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
        }else if(selectedButton == 0 || selectedButton == 20 || selectedButton == 50){
            drawNear();
        }
        gc.setGlobalAlpha(1);
        if(selectedButton == 30 && ac.getCurrentClick() == 1){
            drawTerrainZone(ac.getMouseX() - origin.getPosX(), ac.getMouseY() - origin.getPosY(), ac.getTerrainX(), ac.getTerrainY());
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

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public Point getOrigin() {
        return origin;
    }
}
