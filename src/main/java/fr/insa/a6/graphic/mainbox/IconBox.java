package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.utilities.*;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;


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

    public IconBox(MainScene mainScene) {
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
        ActionCenter ac = mainScene.getActionCenter();
        noeudBtn = new MyRadioButton("Noeud");
        noeudBtn.setToggleGroup(group);

        noeudBtn.setOnAction(actionEvent -> {
            selectNoeud();
            ac.setSelectedButton(10);
            ac.removeSelected();
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
        scene.getStylesheets().add("stylesSheet/lightTheme/popUpLightStyle.css");

        choixNoeud.setScene(scene);
        choixNoeud.showAndWait();

    }

    private void initSelect() {
        ActionCenter ac = mainScene.getActionCenter();
        selectBtn = new MyRadioButton("Selection");
        selectBtn.setToggleGroup(group);
        selectBtn.setSelected(true);

        selectBtn.setOnAction(actionEvent -> ac.setSelectedButton(0));
    }

    private void initBarre() {
        ActionCenter ac = mainScene.getActionCenter();
        barreBtn = new MyRadioButton("Barre");
        barreBtn.setToggleGroup(group);

        barreBtn.setOnAction(actionEvent -> {
            ac.removeSelected();
            ac.setSelectedButton(20);
        });
    }

    private void initPointTrn() {
        ActionCenter ac = mainScene.getActionCenter();
        pointTerrainBtn = new MyRadioButton("Point");
        pointTerrainBtn.setToggleGroup(group);

        pointTerrainBtn.setOnAction(actionEvent -> {
            ac.removeSelected();
            ac.setSelectedButton(30);
        });
    }

    private void initSegmentTrn() {
        ActionCenter ac = mainScene.getActionCenter();
        segmentTerrainBtn = new MyRadioButton("Segment");
        segmentTerrainBtn.setToggleGroup(group);

        segmentTerrainBtn.setOnAction(actionEvent -> {
            ac.removeSelected();
            ac.setSelectedButton(40);
        });
    }

}
