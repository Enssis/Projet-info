package fr.insa.a6.utilities;

import org.json.simple.*;
import org.json.simple.parser.*;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


@SuppressWarnings("unchecked")
public class Options {

    // variables permettants la lecture des fichier json de langue et de preference
    private JSONObject jsonPreferences;
    private JSONObject jsonLanguage;

    private String language;
    private String theme;
    private boolean daltonien;
    private long defaultW, defaultH;
    private String savePath;
    private ArrayList<String> openRecent;
    private HashMap<String, String> keys;
    private String lastOpen;

    private final boolean onlyPreferences;

    public Options(){
        this(false);
    }

    public Options(boolean onlyPref) {
        this.onlyPreferences = onlyPref;
        try {
            jsonInit(onlyPref);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }



    //initialisation des variables permettants la lecture des fichier json de langue et de preference
    public void jsonInit(boolean onlyPref) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        jsonPreferences = (JSONObject) jsonParser.parse(new FileReader("src/main/java/fr/insa/a6/ressources/preference.json"));

        //recupere les préfernece enregistrées
        language = (String) jsonPreferences.get("language");
        theme = (String) jsonPreferences.get("theme");
        daltonien = (boolean) jsonPreferences.get("daltonien");
        defaultH = (long) jsonPreferences.get("defaultH");
        defaultW = (long) jsonPreferences.get("defaultW");
        savePath = (String) jsonPreferences.get("save path");
        openRecent = (ArrayList<String>) jsonPreferences.get("open recent");
        keys = (HashMap<String, String>) jsonPreferences.get("keys");
        lastOpen = (String) jsonPreferences.get("lastOpen");

        if(!onlyPref) {
            JSONObject jsonLanguageFile = (JSONObject) jsonParser.parse(new FileReader("src/main/java/fr/insa/a6/ressources/language.json"));
            //recupere la liste de mot traduit dans la bonne langue
            jsonLanguage = (JSONObject) jsonLanguageFile.get(language);
        }

    }

    //retourne la traduction du mot dans le language défini dans les paramètres
    public String traduction(String word) {
        if(onlyPreferences) throw new Error("options en mode fichier preference seulement");
        return (String) jsonLanguage.get(word);
    }

    public String getLanguage() {
        return language;
    }

    public String getTheme() {
        return theme;
    }

    public long getHeight(){
        return defaultH;
    }

    public long getWidth() {
        return defaultW;
    }

    public ArrayList<String> getOpenRecent() {
        return openRecent;
    }

    public HashMap<String, String> getKeys() {
        return keys;
    }

    public String getSavePath() {
        return savePath;
    }

    public String getLastOpen() {
        return lastOpen;
    }

    public void setTheme(String newTheme){
        this.theme = newTheme;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDaltonien(boolean daltonien) {
        this.daltonien = daltonien;
    }

    public void setDefaultH(long defaultH) {
        this.defaultH = defaultH;
    }

    public void setDefaultW(long defaultW) {
        this.defaultW = defaultW;
    }

    public void setKeys(HashMap<String, String> keys) {
        this.keys = keys;
    }

    public void setOpenRecent(ArrayList<String> openRecent) {
        this.openRecent = openRecent;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setLastOpen(String lastOpen) {
        this.lastOpen = lastOpen;
    }

    public void removeOpenRecent(String path){
        this.openRecent.remove(path);
    }

    public void addOpenRecent(String path){
        for (int i = 0; i < openRecent.size(); i++) {
            if(openRecent.get(i).equals(path)){
                openRecent.remove(i);
                openRecent.add(0, path);
                return;
            }
        }

        if(this.openRecent.size() == 5 ){
            this.openRecent.remove(4);
        }
        this.openRecent.add(0, path);
    }

    public void addKey(String effect, String key){
        keys.put(effect, key);
    }

    public void saveFile(){
        jsonPreferences.put("language", language);
        jsonPreferences.put("theme", theme);
        jsonPreferences.put("daltonien", daltonien);
        jsonPreferences.put("defaultH", defaultH);
        jsonPreferences.put("defaultW", defaultW);
        jsonPreferences.put("save path", savePath);
        jsonPreferences.put("open recent", openRecent);
        jsonPreferences.put("keys", keys);
        jsonPreferences.put("lastOpen", lastOpen);

        try {
            FileWriter file = new FileWriter("src/main/java/fr/insa/a6/ressources/preference.json");
            file.write(jsonPreferences.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
