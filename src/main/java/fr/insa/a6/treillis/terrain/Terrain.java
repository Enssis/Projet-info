package fr.insa.a6.treillis.terrain;

import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.utilities.ActionCenter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Terrain {

    public double xMin;
    public double xMax;
    public double yMin;
    public double yMax;
    private boolean selected = false;

    private HashMap<Integer, Triangle> triangles = new HashMap<>();

    private final ArrayList<PointTerrain> points = new ArrayList<>();
    private final ArrayList<SegmentTerrain> segments = new ArrayList<>();

    public Terrain() {
    }

    //constructeur auto
    public Terrain(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public boolean contain(double x, double y){
        return (x >= xMin && x <= xMax && y >= yMin && y <= yMax);
    }

    //verifie si un point est dans la zone constructible mais pas dans les triangles
    public boolean containOutTriangle(double x, double y){
        for (Triangle triangle : triangles.values()) {
            if(triangle.contain(x, y)) return false;
        }
        return contain(x, y);
    }

    //get et set
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Collection<Triangle> getTriangles() {
        return triangles.values();
    }

    public ArrayList<PointTerrain> getPoints() {
        return points;
    }

    public ArrayList<SegmentTerrain> getSegments() {
        return segments;
    }

    public Triangle getTriangle(int id){
        return triangles.get(id);
    }

    public void addTriangle(Triangle t){
        for (Triangle triangle: triangles.values()) {
            if (triangle.isTriangle(t)) {
                return;
            }
        }
        triangles.put(t.getId(), t);
    }

    public PointTerrain addPoint(double x, double y){
        PointTerrain pt = new PointTerrain(x, y);
        points.add(pt);
        return pt;
    }

    public void addPoint(PointTerrain pt){
        if(points.contains(pt)) return;
        points.add(pt);
    }

    public void addSegment(SegmentTerrain s){
        if(segments.contains(s)) return;
        segments.add(s);
    }

    public boolean isSelected() {
        return selected;
    }

    public SegmentTerrain asSegment(SegmentTerrain s) {
        for (SegmentTerrain segment : segments) {
            if(s.isSegment(segment)) return segment;
        }
        return null;
    }

    public void update(){
        ArrayList<PointTerrain> pointTerrain = new ArrayList<>();
        for (PointTerrain point : points) {
            if (!point.asTriangle()) pointTerrain.add(point);
        }
        for (PointTerrain pt : pointTerrain) {
            remove(pt, false);
        }
    }

    public void remove(Forme f, boolean last){
        if(f instanceof Triangle){
            triangles.remove(f.getId());
            if(last) return;
            for (SegmentTerrain segment : ((Triangle) f).getSegments()) {
                remove(segment, true);
            }
            for (PointTerrain point : ((Triangle) f).getPoints()) {
                remove(point, true);
            }
        }else if(f instanceof PointTerrain){
            points.remove((PointTerrain) f);
            if (last) return;
            ((PointTerrain) f).getSegments().forEach(s -> remove(s, true));
            ((PointTerrain) f).getTriangles().forEach(t -> remove(t, true));
        }else if(f instanceof SegmentTerrain){
            segments.remove(f);
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

        return infos;
    }

    // fonction de dessin
    public void draw(GraphicsContext gc, boolean drawBorder, Point origin){
        if(drawBorder) {
            gc.setGlobalAlpha(0.5);
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(xMin + origin.getPosX(), yMin + origin.getPosY(), xMax - xMin, yMax - yMin);
            gc.setGlobalAlpha(1);

            gc.setLineWidth(3);
            if(selected){
                gc.setStroke(Color.DARKGREEN);
            }else{
                gc.setStroke(Color.GREEN);
            }
            gc.strokeRect(xMin + origin.getPosX(), yMin + origin.getPosY(), xMax - xMin, yMax - yMin);
        }
        if(triangles.size() > 0) triangles.values().forEach(t -> t.draw(gc, origin));
        if(points.size() > 0) points.forEach(p -> p.draw(gc, origin));
        if(segments.size() > 0) segments.forEach(s -> s.draw(gc, origin));
    }

    public String saveString() {
        return "ZoneConstructible;" + xMin + ";" + yMin + ";" + xMax + ";" + yMax;
    }

    public void setBorderNull(){
        xMax = -1;
        xMin = -1;
        yMax = -1;
        yMin = -1;
    }

    public void setBorder(double x1, double y1, double x2, double y2){
        this.xMax = Math.max(x1, x2);
        this.xMin = Math.min(x1, x2);
        this.yMax = Math.max(y1, y2);
        this.yMin = Math.min(y1, y2);
    }

}