package fr.insa.a6.treillis;

import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * 
 */
public class Barres extends Segment {

    private final Type type;

    public Barres(Noeud pA, Noeud pB, Type type, int id) {
        super(pA, pB);
        this.id = id;
        this.type = type;
    }

    public String saveString() {
        int typeId;
        if(type == null) typeId = -1;
        else typeId = type.getId();

        return "Barre;" + id + ";" + typeId + ";" + pA.getId() + ";" + pB.getId();
    }

    public Type getType() {
        return type;
    }

    public void draw(GraphicsContext gc, Point origin) {
        if(selected){
            gc.setStroke(Color.RED);
        }else {
            gc.setStroke(type.getColor());
        }
        gc.setLineWidth(3);
        gc.strokeLine(pA.getPosX() + origin.getPosX(), pA.getPosY() + origin.getPosY(),
                pB.getPosX() + origin.getPosX(), pB.getPosY() + origin.getPosY());
    }

    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = super.getInfos();
        infos.add("Type : ");
        infos.addAll(type.getInfos());

        return infos;

    }

    @Override
    public void drawResult(double result, GraphicsContext gc, Point origin) {
        gc.save();

        double angle = Maths.angle(pA, pB) * 360 / (2 * Math.PI);
        Rotate r = new Rotate(angle, getCenter().getPosX() + origin.getPosX(), getCenter().getPosY() + origin.getPosY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

        //dessine le resultat
        double resultat = ((double)((int) result * 100) / 100);
        String stringRes = String.valueOf(resultat);
        gc.setFill(Color.BLACK);
        gc.fillText(stringRes, getCenter().getPosX() + origin.getPosX() - (int)(stringRes.length() * 3 / 2), getCenter().getPosY() + origin.getPosY() - 10);
        gc.restore();

        boolean compression = true;
        if(result >= 0) compression = false;
        if((compression && result < type.getrComp()) || (!compression && result > type.getrTension())) {
            gc.setStroke(Color.RED);
        }else{
            gc.setStroke(Color.GREEN);
        }

        gc.setLineWidth(3);
        gc.strokeLine(pA.getPosX() + origin.getPosX(), pA.getPosY() + origin.getPosY(),
                pB.getPosX() + origin.getPosX(), pB.getPosY() + origin.getPosY());
    }

}