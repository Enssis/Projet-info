package fr.insa.a6.graphic;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MyMenuBar extends MenuBar {

    // variables permettants la lecture des fichier json de langue et de preference
    private JSONObject jsonPreferences;
    private JSONObject jsonLanguage;

    private MenuButton files;
    private MenuButton calculation;
    
    public MyMenuBar() throws IOException, ParseException {
        super();

        jsonInit();

        addFilesItems();
        calculation = new MenuButton(traduction("calculation"));
        
        this.getMenus().addAll(files, calculation);
        
    }

    //initialisation des variables permettants la lecture des fichier json de langue et de preference
    private void jsonInit() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        jsonPreferences = (JSONObject) jsonParser.parse(new FileReader("src/main/java/fr/insa/a6/ressources/preference.json"));
        JSONObject jsonLanguageFile = (JSONObject) jsonParser.parse(new FileReader("src/main/java/fr/insa/a6/ressources/language.json"));

        String language = (String) jsonPreferences.get("language");
        jsonLanguage = (JSONObject)  jsonLanguageFile.get(language);
    }

    //creation du menu File
    private void addFilesItems()
    {
        //cree une nouvelle feuille de dessin (affiche une pop up de confirmation avant)
        MenuItem newMI = new MenuItem(traduction("new"));

        //affiche le dossier contenant les projets
        MenuItem open = new MenuItem(traduction("open"));

        //affiche les fichiers ouvert recemment
        Menu openRecent = openRecent();

        //sauvegarde le fichier dans l'emplacement de sauvegarde par defaut
        MenuItem save = new MenuItem(traduction("save"));

        //affiche une pop up avec les options quand on clique dessus
        MenuItem options = new MenuItem(traduction("options"));
        options.setOnAction(e -> OptionWindow.display());

        //creation du bouton de menu "File"
        files = new MenuButton(traduction("files"));

        files.getItems().addAll(newMI, open, openRecent, save, options);
    }

    //retourne le menu avec la liste des nom des fichiers ouverts récemment
    private Menu openRecent()
    {
        Menu openRecent = new Menu(traduction("open recent"));
        JSONArray recentString = (JSONArray) jsonPreferences.get("open recent");
        for (Object name : recentString) {
            MenuItem item = new MenuItem((String) name);
            openRecent.getItems().add(item);
        }

        return openRecent;
    }

    //retourne la traduction du mot dans le language défini dans les paramètres
    private String traduction(String word) {
        return (String) jsonLanguage.get(word);
    }

}
