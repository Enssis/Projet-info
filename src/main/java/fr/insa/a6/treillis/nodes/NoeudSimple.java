package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.Force;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.treillis.terrain.Triangle;
import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 */
public class NoeudSimple extends Noeud {



    public NoeudSimple(double posX, double posY, int id) {
        super(posX, posY, id);
    }

    @Override
    public String saveString() {
        return "NoeudSimple;"+ id +";(" + posX + "," + posY + ")";
    }

    public NoeudSimple(Point pos, int id) {
        super();
        this.posX = pos.getPosX();
        this.posY = pos.getPosY();
        this.id = id;
    }

    public NoeudSimple(double posX, double posY) {
        super();
        this.posX = posX;
        this.posY = posY;
    }

    public void draw(GraphicsContext gc){
        if(selected){
            gc.setFill(Color.RED);
            gc.fillOval(posX - 5, posY - 5, 11, 11);
        }else if(segmentSelected){
            gc.setStroke(Color.DARKBLUE);
            gc.setLineWidth(2);
            gc.strokeOval(posX - 5, posY - 5, 11, 11);
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

    public static boolean isCreable1(Treillis treillis, double posX, double posY){
        boolean creable = true;
        for (Noeud p : treillis.getNoeuds()) {
            if(Maths.dist(p, new Point(posX, posY)) < 15) creable = false;
        }
        
        return creable;
    }
    
    public static boolean isCreable2(Treillis treillis, double posX, double posY){
        boolean creable = true;
        
        for (Triangle triangle : treillis.getTerrain().getTriangles()) {
            if (triangle.contain(posX, posY)) creable = false;
        }
        return creable;
    }
     
    @Override
    public ArrayList<String> getInfos(){
        String[] str = new String[]{"posX : " + posX ,
                "posY : " + posY,
                "selected : " + selected
        };
        ArrayList<String> output = new ArrayList<>(Arrays.asList(str));

        if(forceApplique != null){
            output.add("Forces :");
            output.addAll(forceApplique.getInfos());
        }

        return output;
    }


}