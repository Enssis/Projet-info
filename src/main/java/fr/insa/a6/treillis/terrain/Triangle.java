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

    private final int id;
    private final PointTerrain[] points = new PointTerrain[3];
    private final SegmentTerrain[] segment = new SegmentTerrain[3];

    public Triangle(PointTerrain pt1, PointTerrain pt2, PointTerrain pt3, int id){

        double max = Maths.max(pt1.getPosX(), pt2.getPosX(), pt3.getPosX());
        if(max == pt1.getPosX()){
            points[0] = pt1;
        }else if(max == pt2.getPosX()){
            points[0] = pt2;
        }else{
            points[0] = pt3;
        }

        double min = Maths.min(pt1.getPosX(), pt2.getPosX(), pt3.getPosX());
        if(min == pt1.getPosX()){
            points[2] = pt1;
        }else if(min == pt2.getPosX()){
            points[2] = pt2;
        }else{
            points[2] = pt3;
        }

        if(pt1 != points[0] && pt1 != points[2]){
            points[1] = pt1;
        }else if(pt2 != points[0] && pt2 != points[2]){
            points[1] = pt2;
        }else{
            points[1] = pt3;
        }


        pt1.getSegments().forEach(s -> {
            if(s.getpA() == pt2 || s.getpB() == pt2){
                segment[0] = s;
            }
            if(s.getpA() == pt3 || s.getpB() == pt3){
                segment[1] = s;
            }
        });

        pt2.getSegments().forEach(s -> {
            if(s.getpA() == pt3 || s.getpB() == pt3){
                segment[2] = s;
            }
        });

        for (PointTerrain point : points) {
            point.addTriangle(this);
        }

        this.id = id;
    }

    public void draw(GraphicsContext gc, Point origin){
        double[] px = new double[3];
        double[] py = new double[3];

        for (int i = 0; i < points.length; i ++) {
            px[i] = points[i].getPosX() + origin.getPosX();
            py[i] = points[i].getPosY() + origin.getPosY();
        }

        gc.setFill(Color.YELLOW);
        gc.setGlobalAlpha(0.5);
        gc.fillPolygon(px, py, 3);
        gc.setGlobalAlpha(1);

        gc.setStroke(Color.YELLOW);
        gc.strokePolygon(px, py, 3);
    }

    @Override
    public void drawNear(GraphicsContext gc, Point origin) {
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

    public SegmentTerrain[] getSegment() {
        return segment;
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

    public boolean contain(double posX, double posY){
        char[] pos = new char[3];
        Point point = new Point(posX, posY);
        boolean near = false;
        for (int i = 0; i < pos.length; i++) {
            double angle = (double) ((int) (Maths.angle(points[i], points[(i + 1) % 3], point) * 100)) / 100;
            if(angle % Math.PI == 0.0){
                pos[i] = 'c';
            }else if(angle > 0){
                pos[i] = 'p';
            }else{
                pos[i] = 'n';
            }


            double distS = segment[i].distTo(point);
            if(distS != -1) near |= distS < 10;
        }
        boolean colineaire = false;
        for (int i = 0; i < pos.length; i++) {
            colineaire = colineaire || pos[0] == 'c' && Maths.between(point, points[i], points[(i + 1) % 3]);
        }

        return pos[0] == pos[1] && pos[1] == pos[2] || colineaire || near;
    }
}