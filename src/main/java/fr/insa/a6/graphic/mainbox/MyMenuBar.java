package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MenuButton;
import fr.insa.a6.utilities.*;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.io.IOException;

public class MyMenuBar extends MenuBar {

    // variables permettants la lecture des fichier json  de preference
    private JSONObject jsonPreferences;

    private Options optionsData = new Options();

    private MenuButton files;
    private MenuButton calculation;
    private MenuButton dessin;
    
    public MyMenuBar() throws IOException, ParseException {
        super();

        jsonInit();

        addFilesItems();

        calculation = new MenuButton(optionsData.traduction("calculation"));
        dessin = new MenuButton(optionsData.traduction("design"));

        this.getMenus().addAll(files, calculation, dessin);
        
    }

    //initialisation des variables permettants la lecture des fichier json de langue et de preference
    private void jsonInit() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        jsonPreferences = (JSONObject) jsonParser.parse(new FileReader("src/main/java/fr/insa/a6/ressources/preference.json"));
    }

    //creation du menu File
    private void addFilesItems()
    {
        //cree une nouvelle feuille de dessin (affiche une pop up de confirmation avant)
        MenuItem newMI = new MenuItem(optionsData.traduction("new"));

        //affiche le dossier contenant les projets
        MenuItem open = new MenuItem(optionsData.traduction("open"));

        //affiche les fichiers ouvert recemment
        Menu openRecent = openRecent();

        //sauvegarde le fichier dans l'emplacement de sauvegarde par defaut
        MenuItem save = new MenuItem(optionsData.traduction("save"));

        //affiche une pop up avec les options quand on clique dessus
        MenuItem options = new MenuItem(optionsData.traduction("options"));
        options.setOnAction(e -> OptionWindow.display());

        //creation du bouton de menu "File"
        files = new MenuButton(optionsData.traduction("files"));

        files.getItems().addAll(newMI, open, openRecent, save, options);
    }

    //retourne le menu avec la liste des nom des fichiers ouverts récemment
    private Menu openRecent()
    {
        Menu openRecent = new Menu(optionsData.traduction("open recent"));
        JSONArray recentString = (JSONArray) jsonPreferences.get("open recent");
        for (Object name : recentString) {
            MenuItem item = new MenuItem((String) name);
            openRecent.getItems().add(item);
        }

        return openRecent;
    }


}
