package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.utilities.*;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.parser.*;

public class IconBox extends VBox {

    private Options optionsData = new Options();

    private ToggleGroup group= new ToggleGroup();

    private MyRadioButton selectBtn;

    private MyLabel treillisLbl;
    private MyRadioButton barreBtn;
    private MyRadioButton noeudBtn;

    private MyLabel terrainLbl;
    private MyRadioButton pointTerrainBtn;
    private MyRadioButton segmentTerrainBtn;
    private MainScene mainScene;

    private MainCanvas mainCanvas;

    //types : 0 -> simple, 1 -> appuiDouble, 2 -> appuiEncastre, 3 -> appuiSimple
    private String typeNoeud = "0";

    public IconBox(MainScene mainScene) throws IOException, ParseException {
        super();
        this.setAlignment(Pos.CENTER);
        this.setId("iconBox");

        this.mainScene = mainScene;
        this.mainCanvas = mainScene.getCanvas();

        initSelect();

        treillisLbl = new MyLabel(optionsData.traduction("treillis"), "title");

        initNoeud();
        initBarre();

        terrainLbl = new MyLabel(optionsData.traduction("ground"), "title");

        initPointTrn();
        initSegmentTrn();

        this.getChildren().addAll(selectBtn, treillisLbl, noeudBtn, barreBtn, terrainLbl, pointTerrainBtn, segmentTerrainBtn);

    }

    private void initNoeud() {
        noeudBtn = new MyRadioButton("Noeud");
        noeudBtn.setToggleGroup(group);

        noeudBtn.setOnAction(actionEvent -> {
            selectNoeud();
            mainCanvas.setSelectedButton(10);
            mainCanvas.removeSelected();
        });
    }

    //pop up de selection du type de noeud
    private void selectNoeud() {

        Stage choixNoeud =new Stage();

        choixNoeud.initModality(Modality.APPLICATION_MODAL);
        choixNoeud.setTitle(optionsData.traduction("node choice"));
        choixNoeud.setResizable(false);

        //text devant les boutons
        MyLabel label = new MyLabel(optionsData.traduction("choose node"), "title");

        //bouton de fermeture et confirmation du choix
        MyButton fin = new MyButton(optionsData.traduction("choose"));
        fin.setOnAction(e -> {
            System.out.println("type noeud : " + typeNoeud);
            choixNoeud.close();
        });

        //radiobutton pour le choix du type de noeud
        ToggleGroup tGroup = new ToggleGroup();

        tGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (tGroup.getSelectedToggle() != null) {
                MyRadioButton button = (MyRadioButton) tGroup.getSelectedToggle();
                typeNoeud = button.getInfo();
            }
        });

        MyRadioButton noeudSimple = new MyRadioButton(optionsData.traduction("simple node"), "0");
        noeudSimple.setToggleGroup(tGroup);
        noeudSimple.setSelected(true);

        MyRadioButton appuiDouble = new MyRadioButton(optionsData.traduction("double support"), "1");
        appuiDouble.setToggleGroup(tGroup);

        MyRadioButton appuiEncastre = new MyRadioButton(optionsData.traduction("embedded support"), "2");
        appuiEncastre.setToggleGroup(tGroup);

        MyRadioButton appuiSimple = new MyRadioButton(optionsData.traduction("simple support"), "3");
        appuiSimple.setToggleGroup(tGroup);

        //hbox contenant les boutons
        HBox radioLayout = new HBox(5);
        radioLayout.getChildren().addAll(noeudSimple, appuiDouble, appuiEncastre, appuiSimple);

        //Vbox contenant tout les items
        VBox layout= new VBox(5);
        layout.getChildren().addAll(label, radioLayout, fin);
        layout.setAlignment(Pos.CENTER);

        int width = switch (optionsData.getLanguage()) {
            case "en" -> 500;
            case "fr" -> 420;
            default -> 600;
        };
        Scene scene = new Scene(layout, width, 100);
        scene.getStylesheets().add("popUpLightStyle.css");

        choixNoeud.setScene(scene);
        choixNoeud.showAndWait();

    }

    private void initSelect() {
        selectBtn = new MyRadioButton("Selection");
        selectBtn.setToggleGroup(group);
        selectBtn.setSelected(true);

        selectBtn.setOnAction(actionEvent -> {
            mainCanvas.setSelectedButton(0);
        });
    }

    private void initBarre() {
        barreBtn = new MyRadioButton("Barre");
        barreBtn.setToggleGroup(group);

        barreBtn.setOnAction(actionEvent -> {
            mainCanvas.removeSelected();
            mainCanvas.setSelectedButton(2);
        });
    }

    private void initPointTrn() {
        pointTerrainBtn = new MyRadioButton("Point");
        pointTerrainBtn.setToggleGroup(group);

        pointTerrainBtn.setOnAction(actionEvent -> {
            mainCanvas.removeSelected();
            mainCanvas.setSelectedButton(3);
        });
    }

    private void initSegmentTrn() {
        segmentTerrainBtn = new MyRadioButton("Segment");
        segmentTerrainBtn.setToggleGroup(group);

        segmentTerrainBtn.setOnAction(actionEvent -> {
            mainCanvas.removeSelected();
            mainCanvas.setSelectedButton(4);
        });
    }

}
