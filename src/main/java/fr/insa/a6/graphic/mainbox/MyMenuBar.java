package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MenuButton;
import fr.insa.a6.utilities.*;

import javafx.application.HostServices;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyMenuBar extends MenuBar {


    private final Options optionsData = new Options();
    private final ActionCenter actionCenter;

    private MenuButton files;

    public MyMenuBar(ActionCenter actionCenter){
        super();

        this.setId("myMenuBar");

        this.actionCenter = actionCenter;

        addFilesItems();

        MenuButton mode = new MenuButton(optionsData.traduction("mode"));

        MenuItem calculation = new MenuItem(optionsData.traduction("calculation"));
        calculation.setOnAction(e -> {
            actionCenter.setInDrawing(false);
            actionCenter.removeSelectedAll();
            actionCenter.removeSelected();
            actionCenter.redraw();
            actionCenter.calculs();
        });

        MenuItem drawing = new MenuItem(optionsData.traduction("design"));
        drawing.setOnAction(e -> {
            actionCenter.setInDrawing(true);
            actionCenter.getGraphics().removeInfos();
            actionCenter.redraw();
        });

        mode.getItems().addAll(calculation, drawing);

        //creation du bouton menu aide
        MenuButton aide = new MenuButton(optionsData.traduction("help"));

        MenuItem help = new MenuItem(optionsData.traduction("manual"));
        help.setOnAction(e -> {
            try {
                openPdf();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        MenuItem report = new MenuItem(optionsData.traduction("report"));

        aide.getItems().addAll(help, report);

        this.getMenus().addAll(files, mode, aide);
        
    }

    private void openPdf() throws IOException {
        File file = new File("src/main/resources/pdfs/Manuel Info v1.pdf");
        HostServices hostServices = actionCenter.getHostServices();
        hostServices.showDocument(file.getAbsolutePath());

    }

    //creation du menu File
    private void addFilesItems()
    {
        //cree une nouvelle feuille de dessin
        MenuItem newMI = new MenuItem(optionsData.traduction("new"));
        newMI.setOnAction(e -> actionCenter.newTreillis());


        //affiche le dossier contenant les projets
        FileChooser fileChooser = optionsData.getFileChooser(optionsData.traduction("open project"));

        MenuItem open = new MenuItem(optionsData.traduction("open"));
        open.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(actionCenter.getStage());
            String path = file.getPath();
            actionCenter.load(path);
            optionsData.updatePath(path, true);
        });


        //affiche les fichiers ouvert recemment
        Menu openRecent = openRecent();

        //sauvegarde le fichier la ou on veut

        FileChooser fileChooser2 = optionsData.getFileChooser(optionsData.traduction("save as"));

        MenuItem saveAs = new MenuItem(optionsData.traduction("save as"));

        saveAs.setOnAction(e -> {

            File file = fileChooser2.showSaveDialog(actionCenter.getStage());
            String path = file.getPath();
            actionCenter.saveAct(path);
            optionsData.updatePath(path, true);
            actionCenter.reload(path);
        });

        //sauvegarde le fichier dans l'emplacement de sauvegarde par defaut
        MenuItem save = new MenuItem(optionsData.traduction("save"));
        save.setOnAction(e -> {
            String path = actionCenter.getPath();
            if(path.equals("")){
                String name = "untiled" + (int) (Math.random() * 50);
                path = optionsData.getSavePath() + name +".treillis";
            }
            System.out.println(path);
            actionCenter.saveAct(path);
            optionsData.updatePath(path, true);
        });

        //affiche une pop up avec les options quand on clique dessus
        MenuItem options = new MenuItem(optionsData.traduction("options"));
        options.setOnAction(e -> OptionWindow.display(actionCenter));

        //creation du bouton de menu "File"
        files = new MenuButton(optionsData.traduction("files"));

        files.getItems().addAll(newMI, open, openRecent, saveAs, save, options);
    }

    //retourne le menu avec la liste des nom des fichiers ouverts r??cemment
    private Menu openRecent()
    {
        Menu openRecent = new Menu(optionsData.traduction("open recent"));
        ArrayList<String> recentString = optionsData.getOpenRecent();

        ArrayList<String> recentToRemove = new ArrayList<>();

        for (String path : recentString) {
            //test si le fichier existe toujours, si non, on le rajoute a la liste des chemin de fichier ?? enlever
            File file = new File(path);
            if(!file.exists()){
                recentToRemove.add(path);
                continue;
            }

            MenuItem item = new MenuItem(ActionCenter.nameFromPath(path));
            item.setOnAction(e -> {
                actionCenter.load(path);
                optionsData.updatePath(path, false);
            });
            openRecent.getItems().add(item);
        }

        for (String path : recentToRemove) {
            optionsData.removeOpenRecent(path);
        }

        return openRecent;
    }
}
