package fr.insa.a6.treillis.terrain;

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

    public Terrain(double xMin, double yMin, double xMax, double yMax) {    //constructeur auto
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

    public void addTriangle(Triangle t){
        triangles.add(t);
    }

    public void addPoint(double x, double y){
        pointsTerrain.add(new PointTerrain(x, y));
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

            if(selected){
                gc.setStroke(Color.DARKGREEN);
            }else{
                gc.setStroke(Color.GREEN);
            }
            gc.strokeRect(xMin, yMin, xMax - xMin, yMax - yMin);
        }
        if(triangles.size() > 0) triangles.forEach(t -> t.draw(gc));
    }

    public String saveString() {
        return "ZoneConstructible;" + xMin + ";" + yMin + ";" + xMax + ";" + yMax;
    }

}