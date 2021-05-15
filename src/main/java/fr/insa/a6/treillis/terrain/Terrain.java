package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.dessin.Forme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Terrain {

    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private boolean selected = false;

    private ArrayList<Triangle> triangles;

    ArrayList<PointTerrain> pointsTerrain;
    ArrayList<SegmentTerrain> segmentsTerrain;

    public Terrain() {

    }

    //constructeur auto
    public Terrain(double xMin, double yMin, double xMax, double yMax) {
        pointsTerrain = new ArrayList<>();
        segmentsTerrain = new ArrayList<>();
        triangles = new ArrayList<>();

        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public boolean contain(double x, double y){
        return x >= xMin && x <= xMax && y >= yMin && y <= yMax;
    }

    //get et set

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public ArrayList<SegmentTerrain> getSegmentsTerrain() {
        return segmentsTerrain;
    }

    public ArrayList<PointTerrain> getPointsTerrain() {
        return pointsTerrain;
    }

    public void addTriangle(Triangle t){
        triangles.add(t);
    }

    public PointTerrain addPoint(double x, double y){
        PointTerrain pt = new PointTerrain(x, y);
        pointsTerrain.add(pt);
        return pt;
    }

    public void addPoint(PointTerrain pt){
        if(pointsTerrain.contains(pt)) return;
        pointsTerrain.add(pt);
    }

    public void addSegment(SegmentTerrain s){
        if(segmentsTerrain.contains(s)) return;
        segmentsTerrain.add(s);
    }

    //ajoute un segment et vérifie si il peut pas créer un triangle avec les combinaisons de segments déjà en place
    public void addSegment(SegmentTerrain s, Treillis treillis){
        segmentsTerrain.add(s);
        if(segmentsTerrain.size() > 2){
            PointTerrain pA = s.getpA();
            PointTerrain pB = s.getpB();

            for (SegmentTerrain segment : pA.getSegments()) {
                if(segment != s){

                    PointTerrain p1;
                    if(segment.getpA() != pA){
                        p1 = segment.getpA();
                    }else{
                        p1 = segment.getpB();
                    }
                    for (SegmentTerrain segmentB : pB.getSegments()) {
                        if(segmentB != s){

                            PointTerrain p2;
                            if(segmentB.getpA() != pB){
                                p2 = segmentB.getpA();
                            }else{
                                p2 = segmentB.getpB();
                            }
                            if(p1.equals(p2)){
                                Triangle triangle = new Triangle(pA, pB, p1, treillis.getNumerateur().getNewTriangleId());
                                triangles.add(triangle);
                            }
                        }
                    }
                }
            }
        }
    }

    public void remove(Forme f, boolean last){
        if(f instanceof Triangle){
            triangles.remove(f);
            if(last) return;
            for (SegmentTerrain arrete : ((Triangle) f).getArretes()) {
                remove(arrete, true);
            }
            for (PointTerrain point : ((Triangle) f).getPoints()) {
                remove(point, true);
            }
        }else if(f instanceof PointTerrain){
            pointsTerrain.remove((PointTerrain) f);
            if (last) return;
            ((PointTerrain) f).getSegments().forEach(s -> remove(s, true));
            ((PointTerrain) f).getTriangles().forEach(t -> remove(t, true));
        }else if(f instanceof SegmentTerrain){
            segmentsTerrain.remove(f);
            if(last) return;
            ((SegmentTerrain) f).getTriangles().forEach(t -> remove(t, true));
        }
    }

    public ArrayList<String> getInfos(){
        ArrayList<String> infos = new ArrayList<>();
        infos.add("xMin :" + xMin);
        infos.add("yMin :" + yMin);
        infos.add("xMax :" + xMax);
        infos.add("yMax :" + yMax);

        for (int i = 0; i < triangles.size(); i++) {
            infos.add("Triangle " + i);
            infos.addAll(triangles.get(i).getInfos());
        }
        return infos;
    }

    // fonction de dessin
    public void draw(GraphicsContext gc, boolean drawBorder){
        if(drawBorder) {
            gc.setGlobalAlpha(0.5);
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(xMin, yMin, xMax - xMin, yMax - yMin);
            gc.setGlobalAlpha(1);

            gc.setLineWidth(3);
            if(selected){
                gc.setStroke(Color.DARKGREEN);
            }else{
                gc.setStroke(Color.GREEN);
            }
            gc.strokeRect(xMin, yMin, xMax - xMin, yMax - yMin);
        }
        if(triangles.size() > 0) triangles.forEach(t -> t.draw(gc));
        if(pointsTerrain.size() > 0) pointsTerrain.forEach(p -> p.draw(gc));
        if(segmentsTerrain.size() > 0) segmentsTerrain.forEach(s -> s.draw(gc));
    }

    public String saveString() {
        return "ZoneConstructible;" + xMin + ";" + yMin + ";" + xMax + ";" + yMax;
    }

}