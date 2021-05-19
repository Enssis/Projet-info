package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MenuButton;
import fr.insa.a6.utilities.*;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.stage.FileChooser;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.File;
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

        MenuButton mode = new MenuButton(optionsData.traduction("mode"));

        MenuItem calculation = new MenuItem(optionsData.traduction("calculation"));
        calculation.setOnAction(e -> {
            actionCenter.setInDrawing(false);
            actionCenter.redraw();
        });

        MenuItem drawing = new MenuItem(optionsData.traduction("design"));
        drawing.setOnAction(e -> {
            actionCenter.setInDrawing(true);
            actionCenter.redraw();
        });

        mode.getItems().addAll(calculation, drawing);

        this.getMenus().addAll(files, mode);
        
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(optionsData.traduction("open project"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.initialDirectoryProperty().setValue(new File(optionsData.getSavePath()));

        MenuItem open = new MenuItem(optionsData.traduction("open"));
        open.setOnAction(e -> {
            try {
                File file = fileChooser.showOpenDialog(actionCenter.getStage());
                String path = file.getPath();
                actionCenter.load(path);
                optionsData.jsonInit(true);
                optionsData.addOpenRecent(path);
                optionsData.setLastOpen(path);
                optionsData.saveFile();
            } catch (IOException | ParseException exception) {
                exception.printStackTrace();
            }
        });


        //affiche les fichiers ouvert recemment
        Menu openRecent = openRecent();

        //sauvegarde le fichier la ou on veux

        FileChooser fileChooser2 = new FileChooser();
        fileChooser2.setTitle(optionsData.traduction("save as"));
        fileChooser2.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser2.initialDirectoryProperty().setValue(new File(optionsData.getSavePath()));

        MenuItem saveAs = new MenuItem(optionsData.traduction("save as"));

        saveAs.setOnAction(e -> {

            File file = fileChooser2.showSaveDialog(actionCenter.getStage());
            String path = file.getPath();
            actionCenter.saveAct(path);
            try {
                optionsData.jsonInit(true);
            } catch (IOException | ParseException ioException) {
                ioException.printStackTrace();
            }
            optionsData.setLastOpen(path);
            optionsData.addOpenRecent(path);
            optionsData.setSavePath(path);
            optionsData.saveFile();
        });

        //sauvegarde le fichier dans l'emplacement de sauvegarde par defaut
        MenuItem save = new MenuItem(optionsData.traduction("save"));
        save.setOnAction(e -> {
            String path = actionCenter.getPath();
            if(path.equals("")){
                String name = "untiled" + (int) (Math.random() * 50);
                path = optionsData.getSavePath() + name +".txt";
            }
            actionCenter.saveAct(path);
            try {
                optionsData.jsonInit(true);
            } catch (IOException | ParseException ioException) {
                ioException.printStackTrace();
            }
            try {
                optionsData.jsonInit(true);
            } catch (IOException | ParseException ioException) {
                ioException.printStackTrace();
            }
            optionsData.setLastOpen(path);
            optionsData.addOpenRecent(path);
            optionsData.saveFile();
        });

        //affiche une pop up avec les options quand on clique dessus
        MenuItem options = new MenuItem(optionsData.traduction("options"));
        options.setOnAction(e -> OptionWindow.display(actionCenter));

        //creation du bouton de menu "File"
        files = new MenuButton(optionsData.traduction("files"));

        files.getItems().addAll(newMI, open, openRecent, saveAs, save, options);
    }

    //retourne le menu avec la liste des nom des fichiers ouverts récemment
    private Menu openRecent()
    {
        Menu openRecent = new Menu(optionsData.traduction("open recent"));
        JSONArray recentString = (JSONArray) jsonPreferences.get("open recent");
        for (Object path : recentString) {
            File file = new File((String) path);
            if(!file.exists()){
                optionsData.removeOpenRecent((String) path);
                continue;
            }
            MenuItem item = new MenuItem( ActionCenter.nameFromPath((String) path));
            item.setOnAction(e -> {
                try {
                    actionCenter.load((String) path);
                    optionsData.jsonInit(true);
                    optionsData.addOpenRecent((String) path);
                    optionsData.setLastOpen((String) path);
                    optionsData.saveFile();
                } catch (IOException | ParseException ioException) {
                    System.err.println("ERREUR NOM DU FICHIER");
                }
            });
            openRecent.getItems().add(item);
        }

        return openRecent;
    }


}
