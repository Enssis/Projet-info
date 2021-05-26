package fr.insa.a6.utilities;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.Type;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.nodes.*;
import fr.insa.a6.treillis.terrain.PointTerrain;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.treillis.terrain.Triangle;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BNFReader extends BufferedReader {

    private Terrain terrain;
    private final HashMap<Integer, Type> catalogue = new HashMap<>();
    private final HashMap<Integer, Noeud> noeuds = new HashMap<>();
    private final ArrayList<Barres> barres = new ArrayList<>();
    private Numerateur numerateur;

    private int catalogueId = 0, noeudId = 0, barreId = 0, triangleId = 0;

    private ArrayList<HashMap<String, String>> file = new ArrayList<>();

    public BNFReader(Reader in) {
        super(in);
        try {
            initFile();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void initFile() throws IOException {
        String line;
        while ((line = this.readLine()) != null){
            String[] lineArr = line.split(";");
            switch (lineArr[0]){
                case "ZoneConstructible" -> terrain = new Terrain(Double.parseDouble(lineArr[1]),
                       Double.parseDouble(lineArr[2]), Double.parseDouble(lineArr[3]), Double.parseDouble(lineArr[4]));
                case "Triangle" -> addTriangle(lineArr);
                case "TypeBarre" -> addTypeBarre(lineArr);
                case "AppuiDouble", "AppuiSimple", "NoeudSimple", "AppuiEncastre" -> addNoeud(lineArr);
                case "Barre" -> addBarre(lineArr);
            }
        }
        numerateur = new Numerateur(noeudId, barreId, catalogueId, triangleId);
    }

    private void addTriangle(String[] triangles){
        triangleId = Integer.parseInt(triangles[1]);
        PointTerrain pt1 = new PointTerrain(toPoint(triangles[2]));
        PointTerrain pt2 = new PointTerrain(toPoint(triangles[3]));
        PointTerrain pt3 = new PointTerrain(toPoint(triangles[4]));

        for (PointTerrain p : terrain.getPoints()) {
            if(p.isPoint(pt1)) pt1 = p;
            if(p.isPoint(pt2)) pt2 = p;
            if(p.isPoint(pt3)) pt3 = p;
        }
        terrain.addPoint(pt1);
        terrain.addPoint(pt2);
        terrain.addPoint(pt3);

        Triangle triangle = new Triangle(pt1, pt2, pt3, triangleId, terrain);

        terrain.addTriangle(triangle);
    }

    private Point toPoint(String point){
        point = point.replace("(", "");
        point = point.replace(")", "");
        String[] values = point.split(",");
        return new Point(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
    }

    private void addTypeBarre(String[] type){
        int id = Integer.parseInt(type[1]);
        catalogue.put(id, new Type(type[7], Double.parseDouble(type[2]), Double.parseDouble(type[3]),
                Double.parseDouble(type[4]), Double.parseDouble(type[5]),
                Double.parseDouble(type[6]), id, Color.web(type[8])));
        catalogueId ++;
    }

    private void addNoeud(String[] strNoeud){
        Noeud noeud;
        noeudId = Integer.parseInt(strNoeud[1]);
        switch (strNoeud[0]){
            case "AppuiDouble" -> {
                Triangle triangle = terrain.getTriangle(Integer.parseInt(strNoeud[2]));
                if(triangle != null) noeud = new AppuiDouble(triangle, triangle.getSegments()[Integer.parseInt(strNoeud[3])], Double.parseDouble(strNoeud[4]), noeudId);
                else noeud = null;
            }
            case "AppuiSimple" -> {
                Triangle triangle = terrain.getTriangle(Integer.parseInt(strNoeud[2]));
                if(triangle != null) noeud = new AppuiSimple(triangle, triangle.getSegments()[Integer.parseInt(strNoeud[3])], Double.parseDouble(strNoeud[4]), noeudId);
                else noeud = null;
            }
            case "NoeudSimple" -> noeud = new NoeudSimple(toPoint(strNoeud[2]), noeudId);
            case "AppuiEncastre" -> {
                Triangle triangle = terrain.getTriangle(Integer.parseInt(strNoeud[2]));
                if(triangle != null) noeud = new AppuiEncastre(triangle, triangle.getSegments()[Integer.parseInt(strNoeud[3])], Double.parseDouble(strNoeud[4]), noeudId);
                else noeud = null;
            }
            default -> noeud = new NoeudSimple(0,0,0);
        }
        if(noeud != null) noeuds.put(noeud.getId(), noeud);
    }

    public void addBarre(String[] strBarre){
        barreId = Integer.parseInt(strBarre[1]);

        barres.add(new Barres(noeuds.get(Integer.parseInt(strBarre[3])), noeuds.get(Integer.parseInt(strBarre[4])),
                catalogue.get(Integer.parseInt(strBarre[2])), barreId));

    }

    public Treillis getTreillis(){
        return new Treillis(terrain, new ArrayList<>(noeuds.values()), barres, new ArrayList<>(catalogue.values()), numerateur);
    }

}
