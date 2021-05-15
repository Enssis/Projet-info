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

    private final Options optionsData = new Options();
    private final ActionCenter actionCenter;

    private MenuButton files;

    public MyMenuBar(ActionCenter actionCenter) throws IOException, ParseException {
        super();

        this.actionCenter = actionCenter;

        jsonInit();

        addFilesItems();

        MenuButton calculation = new MenuButton(optionsData.traduction("calculation"));
        calculation.setOnAction(e -> actionCenter.setInDrawing(false));

        MenuButton drawing = new MenuButton(optionsData.traduction("design"));
        drawing.setOnAction(e -> actionCenter.setInDrawing(true));

        this.getMenus().addAll(files, calculation, drawing);
        
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
        newMI.setOnAction(e -> {
            try {
                actionCenter.newTreillis();
            } catch (IOException | ParseException ioException) {
                ioException.printStackTrace();
            }
        });

        //affiche le dossier contenant les projets
        MenuItem open = new MenuItem(optionsData.traduction("open"));

        //affiche les fichiers ouvert recemment
        Menu openRecent = openRecent();

        //sauvegarde le fichier dans l'emplacement de sauvegarde par defaut
        MenuItem save = new MenuItem(optionsData.traduction("save"));
        save.setOnAction(e -> {
            String name = "test" + (int) (Math.random() * 500); //TODO ajouter un moyen de nommer les fichiers
            String path = optionsData.getSavePath() + name +".txt";
            actionCenter.saveAct(path);
            optionsData.setLastOpen(path);
            optionsData.addOpenRecent(name);
        });

        //affiche une pop up avec les options quand on clique dessus
        MenuItem options = new MenuItem(optionsData.traduction("options"));
        options.setOnAction(e -> OptionWindow.display(actionCenter));

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
            item.setOnAction(e -> {
                try {
                    actionCenter.load((String) name);
                } catch (IOException | ParseException ioException) {
                    System.err.println("ERREUR NOM DU FICHIER");
                }
            });
            openRecent.getItems().add(item);
        }

        return openRecent;
    }


}
