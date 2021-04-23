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
import fr.insa.a6.utilities.ActionCenter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;

public class Graphics {

    private GraphicsContext gc;
    private MainScene mainScene;
    private HashMap<Integer, Forme> formes = new HashMap<>();
    private ActionCenter ac;

    InfoWindow infoWindow;

    public Graphics() {

    }

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

    public void drawInfosMultiplePoint(int nbPoint, int nbSegment){
        infoWindow.drawInfosMultiplePoint(nbPoint,nbSegment);
    }

    public void drawInfos(Forme nearest){
        infoWindow.drawInfos(nearest);
    }

    public void updateFormes(Treillis treillis){
        ArrayList<Noeud> noeuds = treillis.getNoeuds();

        for (Noeud n: noeuds) {
            if(!formes.containsValue(n)){
                formes.put(n.getId(), n);
            }
        }

        ArrayList<Barres> barres = treillis.getBarres();

        for (Barres b: barres) {
            if(!formes.containsValue(b)){
                formes.put(b.getId(), b);
            }
        }
    }

    private void drawNear(){
        Forme nearest = ac.getNearest();
        if(nearest != null){
            nearest.drawNear(gc);
        }
    }

    public void redraw(int selectedButton){
        MainCanvas canvas = mainScene.getCanvas();

        gc.setFill(Color.LIGHTCYAN);
        gc.fillRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight());

        formes.forEach((k, f) -> f.draw(gc));


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
        }else if(selectedButton == 0 || selectedButton == 20){
            drawNear();
        }
        gc.setGlobalAlpha(1);

    }

    public void remove(int id){
        formes.remove(id);
    }

    public Forme[] getFormes() {
        return formes.values().toArray(new Forme[]{});
    }
}
