package fr.insa.a6.treillis;

import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.Noeud;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
}