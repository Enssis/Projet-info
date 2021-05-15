package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.dessin.Forme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * 
 */
public class Triangle extends Forme {

    private int id;
    private PointTerrain[] points = new PointTerrain[3];
    private SegmentTerrain[] arretes;


    public Triangle() {

    }

    public Triangle(PointTerrain pt1, PointTerrain pt2, PointTerrain pt3, int id){
        points[0] = pt1;
        points[1] = pt2;
        points[2] = pt3;

        this.id = id;
    }

    public void draw(GraphicsContext gc){
        double[] px = new double[3];
        double[] py = new double[3];

        for (int i = 0; i < points.length; i ++) {
            px[i] = points[i].getPosX();
            py[i] = points[i].getPosY();
        }

        gc.setFill(Color.YELLOW);
        gc.setGlobalAlpha(0.5);
        gc.fillPolygon(px, py, 3);
        gc.setGlobalAlpha(1);

        gc.setStroke(Color.YELLOW);
        gc.strokePolygon(px, py, 3);
    }

    @Override
    public void drawNear(GraphicsContext gc) {
    }

    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("premier sommet : ");
        infos.addAll(points[0].getInfos());
        infos.add("second sommet : ");
        infos.addAll(points[1].getInfos());
        infos.add("troisieme sommet : ");
        infos.addAll(points[2].getInfos());

        return infos;
    }

    public String saveString() {
        String save = "Triangle;" + id;
        for (PointTerrain point: points) {
            save += ";(" + points[0].getPosX() + "," + points[0].getPosX() + ")";
        }
        return save;
    }

}