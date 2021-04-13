package fr.insa.a6.graphic;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MyMenuBar extends MenuBar {

    // variables permettants la lecture des fichier json de langue et de preference
    private JSONObject jsonPreferences;
    private JSONObject jsonLanguage;

    private MenuButton files;
    
    public MyMenuBar() throws IOException, ParseException {
        super();

        jsonInit();

        addFilesItems();
        
        this.getMenus().addAll(files);
        
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
        MenuItem newMI = new MenuItem(traduction("new"));
        MenuItem open = new MenuItem(traduction("open"));
        MenuItem save = new MenuItem(traduction("save"));
        MenuItem options = new MenuItem(traduction("options"));
        options.setOnAction(e -> OptionWindow.display());

        files = new MenuButton(traduction("files"));

        files.getItems().addAll(newMI, open, save, options);
    }

    private String traduction(String word) {
        return (String) jsonLanguage.get(word);
    }

}
