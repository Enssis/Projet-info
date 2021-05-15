package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.utilities.Maths;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * 
 */
public class Triangle extends Forme {

    private int id;
    private final PointTerrain[] points = new PointTerrain[3];
    private final SegmentTerrain[] arretes = new SegmentTerrain[3];

    public Triangle(PointTerrain pt1, PointTerrain pt2, PointTerrain pt3, int id){
        points[0] = pt1;
        points[1] = pt2;
        points[2] = pt3;

        pt1.getSegments().forEach(s -> {
            if(s.getpA() == pt2 || s.getpB() == pt2){
                arretes[0] = s;
            }
            if(s.getpA() == pt3 || s.getpB() == pt3){
                arretes[1] = s;
            }
        });

        pt2.getSegments().forEach(s -> {
            if(s.getpA() == pt3 || s.getpB() == pt3){
                arretes[2] = s;
            }
        });

        for (PointTerrain point : points) {
            point.addTriangle(this);
        }

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
        StringBuilder save = new StringBuilder("Triangle;" + id);
        for (PointTerrain point: points) {
            save.append(";(").append(point.getPosX()).append(",").append(point.getPosY()).append(")");
        }
        return save.toString();
    }

    public PointTerrain[] getPoints() {
        return points;
    }

    public SegmentTerrain[] getArretes() {
        return arretes;
    }

    public Point getCenter(){
        double px = 0;
        double py = 0;

        for (PointTerrain point : points) {
            px += point.getPosX();
            py += point.getPosY();
        }
        return new Point(px, py);

    }
}