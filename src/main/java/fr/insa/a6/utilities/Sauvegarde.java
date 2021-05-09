package fr.insa.a6.utilities;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.nodes.*;
import fr.insa.a6.treillis.terrain.Terrain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class Sauvegarde {

    public static Treillis getTreillis(String path) throws IOException, ParseException {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonTreillis = (JSONObject) jsonParser.parse(new FileReader(path));

        ArrayList<Terrain> terrains = new ArrayList<>();
        ((JSONArray) jsonTreillis.get("terrains")).forEach(it -> terrains.add(new Terrain()));

        HashMap<Integer, Noeud> noeuds = new HashMap<>();
        ((JSONArray) jsonTreillis.get("noeuds")).forEach(it -> {
            Noeud noeud = noeudFromJson((JSONObject) it);
            noeuds.put(noeud.getId(), noeud);
        });

        ArrayList<Barres> barres = new ArrayList<>();
        ((JSONArray) jsonTreillis.get("barres")).forEach(it -> barres.add(barreFromJson((JSONObject) it, noeuds)));

        Numerateur numerateur = new Numerateur((int)((long) jsonTreillis.get("numerateurId")));

        return new Treillis(terrains, noeuds, barres, numerateur);
    }

    public static Noeud noeudFromJson(JSONObject object){

        int posX = (int) ((double) object.get("posX"));
        int posY = (int) ((double) object.get("posY"));
        int id = (int) ((long) object.get("id"));
        Noeud output;

        switch ((String) object.get("type")) {
            case "noeudSimple" -> output = new NoeudSimple(posX, posY, id);
            case "appuiSimple" -> output = new AppuiSimple();
            case "appuiEncastre" -> output = new AppuiEncastre();
            case "appuiDouble" -> output = new AppuiDouble();
            default -> output = new NoeudSimple(0, 0, 0);
        }
        return output;
    }

    public static Barres barreFromJson(JSONObject object, HashMap<Integer, Noeud> noeuds){
        Noeud pointA = noeuds.get((int) ((long) object.get("pointA")));
        Noeud pointB = noeuds.get((int) ((long)object.get("pointB")));
        int id = (int) ((long)object.get("id"));

        return new Barres(pointA, pointB, id);
    }

    public static JSONObject noeudToJson(Noeud noeud){
        JSONObject noeudJson = new JSONObject();

        noeudJson.put("posX", noeud.getPosX());
        noeudJson.put("posY", noeud.getPosY());
        noeudJson.put("id", noeud.getId());

        if (noeud instanceof NoeudSimple) {
            noeudJson.put("type", "noeudSimple");

        }else if (noeud instanceof AppuiSimple) {
            noeudJson.put("type", "appuiSimple");

        }else if (noeud instanceof AppuiEncastre) {
            noeudJson.put("type", "appuiEncastre");

        }else if (noeud instanceof AppuiDouble) {
            noeudJson.put("type", "appuiDouble");
        }

        return noeudJson;
    }

    public static JSONObject barreToJson(Barres barre){
        JSONObject barreJson = new JSONObject();

        int pointA = barre.getpA().getId();
        barreJson.put("pointA", pointA);

        int pointB = barre.getpB().getId();
        barreJson.put("pointB", pointB);

        barreJson.put("id", barre.getId());

        return barreJson;
    }

    public static void saveTreillis(Treillis treillis, String savePath){
        JSONObject treillisJson = new JSONObject();

        ArrayList<Terrain> terrains = treillis.getTerrains();
        JSONArray terrainArray = new JSONArray();
        terrains.forEach(t -> terrainArray.add(""));
        //TODO mettre a jour quand les terrains seront fait
        treillisJson.put("terrains", terrainArray);


        HashMap<Integer, Noeud> noeuds = treillis.getNoeuds();
        JSONArray noeudsArray = new JSONArray();
        noeuds.forEach((k, n) -> noeudsArray.add(noeudToJson(n)));
        treillisJson.put("noeuds", noeudsArray);


        ArrayList<Barres> barres = treillis.getBarres();
        JSONArray barresArray = new JSONArray();
        barres.forEach(b -> barresArray.add(barreToJson(b)));
        treillisJson.put("barres", barresArray);

        treillisJson.put("numerateurId", treillis.getNumerateur().getCurrentId());

        try {
            FileWriter file = new FileWriter(savePath);
            file.write(treillisJson.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
