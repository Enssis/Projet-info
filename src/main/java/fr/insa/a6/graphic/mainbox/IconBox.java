package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.treillis.Type;
import fr.insa.a6.utilities.*;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private MyButton choixNoeud;
    private MyRadioButton noeudBtn;

    private MyLabel terrainLbl;
    private MyRadioButton terrainBtn;
    private MyRadioButton pointTerrainBtn;
    private MyRadioButton segmentTerrainBtn;
    private MainScene mainScene;

    private MainCanvas mainCanvas;
    private ActionCenter actionCenter;

    //types : 0 -> simple, 1 -> appuiDouble, 2 -> appuiSimple, 3 -> appuiEncastre
    private String typeNoeud = "0";
    private String name = optionsData.traduction("simple node");
    private int choosedNoeud = 10;

    public IconBox(MainScene mainScene) {
        super(10);
        this.setAlignment(Pos.CENTER);
        this.setId("iconBox");

        this.mainScene = mainScene;
        this.mainCanvas = mainScene.getCanvas();
        this.actionCenter = mainScene.getActionCenter();

        initSelect();

        treillisLbl = new MyLabel(optionsData.traduction("treillis"), "title");

        choixNoeud = new MyButton(optionsData.traduction("simple node"));
        choixNoeud.setOnAction(a -> {
            selectNoeud();
        });

        initNoeud();
        initBarre();

        terrainLbl = new MyLabel(optionsData.traduction("ground"), "title");

        initTrn();
        initPointTrn();
        initSegmentTrn();


        this.getChildren().addAll(selectBtn, treillisLbl, choixNoeud, noeudBtn, barreBtn, terrainLbl, terrainBtn, pointTerrainBtn, segmentTerrainBtn);

    }

    private void initNoeud() {
        noeudBtn = new MyRadioButton("Noeud");
        noeudBtn.setToggleGroup(group);

        noeudBtn.setOnAction(actionEvent -> {
            System.out.println(choosedNoeud);
            actionCenter.setSelectedButton(choosedNoeud);
            actionCenter.removeSelected();
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
            choosedNoeud = 10 + Integer.parseInt(typeNoeud);
            if(actionCenter.getSelectedButton() / 10 == 1) actionCenter.setSelectedButton(choosedNoeud);
            this.choixNoeud.setText(name);
            choixNoeud.close();
        });

        //radiobutton pour le choix du type de noeud
        ToggleGroup tGroup = new ToggleGroup();

        tGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (tGroup.getSelectedToggle() != null) {
                MyRadioButton button = (MyRadioButton) tGroup.getSelectedToggle();
                typeNoeud = button.getInfo();
                name = button.getName();
            }
        });

        MyRadioButton noeudSimple = new MyRadioButton(optionsData.traduction("simple node"), "0");
        noeudSimple.setToggleGroup(tGroup);
        noeudSimple.setSelected(true);

        MyRadioButton appuiDouble = new MyRadioButton(optionsData.traduction("double support"), "1");
        appuiDouble.setToggleGroup(tGroup);

        MyRadioButton appuiSimple = new MyRadioButton(optionsData.traduction("simple support"), "2");
        appuiSimple.setToggleGroup(tGroup);

        MyRadioButton appuiEncastre = new MyRadioButton(optionsData.traduction("embedded support"), "3");
        appuiEncastre.setDisable(true);
        appuiEncastre.setToggleGroup(tGroup);

        //hbox contenant les boutons
        HBox radioLayout = new HBox(5);
        radioLayout.getChildren().addAll(noeudSimple, appuiDouble, appuiSimple, appuiEncastre);

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
        selectBtn = new MyRadioButton("Selection");
        selectBtn.setToggleGroup(group);
        selectBtn.setSelected(true);

        selectBtn.setOnAction(actionEvent -> actionCenter.setSelectedButton(0));
    }

    private void initBarre() {
        barreBtn = new MyRadioButton("Barre");
        barreBtn.setToggleGroup(group);

        barreBtn.setOnAction(actionEvent -> {
            actionCenter.removeSelected();
            typeChoicePopUp(null);
            actionCenter.setSelectedButton(20);
        });
    }

    //pop up de selection du type de la barre
    public void typeChoicePopUp(Stage creator){
        Stage typeChoice = new Stage();

        typeChoice.initModality(Modality.APPLICATION_MODAL);
        typeChoice.setTitle(optionsData.traduction("type choice"));
        typeChoice.setResizable(false);

        //type list
        MyLabel typeLabel = new MyLabel(optionsData.traduction("type") + " :", "title");

        ComboBox<Type> typeComboBox = new ComboBox<>(FXCollections.observableArrayList(actionCenter.getTreillis().getCatalogue()));

        HBox typeHB = new HBox(10);
        typeHB.getChildren().addAll(typeLabel, typeComboBox);
        typeHB.setAlignment(Pos.CENTER);

        //boutons
        Button addTypeBtn = new Button(optionsData.traduction("add type"));
        addTypeBtn.setOnAction(e -> Type.createTypePopUp(actionCenter, typeComboBox));

        Button chooseBtn = new Button(optionsData.traduction("choose"));
        chooseBtn.setOnAction(e -> {
            actionCenter.setBarreType(typeComboBox.getValue());
            typeChoice.close();
        });

        HBox buttonHB = new HBox(10);
        buttonHB.getChildren().addAll(addTypeBtn, chooseBtn);
        buttonHB.setAlignment(Pos.CENTER);


        VBox mainVB = new VBox(5);
        mainVB.getChildren().addAll(typeHB, buttonHB);


        Scene scene1 = new Scene(mainVB, 300, 80);

        if(creator != null) creator.close();

        typeChoice.setScene(scene1);
        typeChoice.showAndWait();

    }

    public void initTrn(){
        terrainBtn = new MyRadioButton(optionsData.traduction("ground"));
        terrainBtn.setToggleGroup(group);

        terrainBtn.setOnAction( actionEvent -> {
            actionCenter.removeSelected();
            actionCenter.setSelectedButton(30);
        });
    }

    private void initPointTrn() {
        pointTerrainBtn = new MyRadioButton("Point");
        pointTerrainBtn.setToggleGroup(group);

        pointTerrainBtn.setOnAction(actionEvent -> {
            actionCenter.removeSelected();
            actionCenter.setSelectedButton(40);
        });
    }

    private void initSegmentTrn() {
        segmentTerrainBtn = new MyRadioButton("Segment");
        segmentTerrainBtn.setToggleGroup(group);

        segmentTerrainBtn.setOnAction(actionEvent -> {
            actionCenter.removeSelected();
            actionCenter.setSelectedButton(50);
        });
    }

}
