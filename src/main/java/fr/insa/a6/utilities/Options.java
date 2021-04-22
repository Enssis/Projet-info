package fr.insa.a6.utilities;

import org.json.simple.*;
import org.json.simple.parser.*;


import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.*;

public class Options {

    // variables permettants la lecture des fichier json de langue et de preference
    private JSONObject jsonPreferences;
    private JSONObject jsonLanguage;

    private String language;
    private String style;

    public Options() {
        try {
            jsonInit();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //initialisation des variables permettants la lecture des fichier json de langue et de preference
    private void jsonInit() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        jsonPreferences = (JSONObject) jsonParser.parse(new FileReader("src/main/java/fr/insa/a6/ressources/preference.json"));
        JSONObject jsonLanguageFile = (JSONObject) jsonParser.parse(new FileReader("src/main/java/fr/insa/a6/ressources/language.json"));

        //recupere le style voulu
        style = (String) jsonPreferences.get("style");

        //recupere la langue et la liste de mot traduit dans la bonne langue
        language = (String) jsonPreferences.get("language");
        jsonLanguage = (JSONObject)  jsonLanguageFile.get(language);
    }

    //retourne la traduction du mot dans le language défini dans les paramètres
    public String traduction(String word) {
        return (String) jsonLanguage.get(word);
    }

    public String getLanguage() {
        return language;
    }

    public String getStyle() {
        return style;
    }
}
