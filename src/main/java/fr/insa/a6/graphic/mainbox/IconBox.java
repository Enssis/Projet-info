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
    private VBox typeBox;
    private MyButton choixNoeud;
    private MyRadioButton noeudBtn;

    private MyLabel terrainLbl;
    private MyRadioButton terrainBtn;
    private MyRadioButton triangleTerrainBtn;
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

        treillisLbl = new MyLabel("      " + optionsData.traduction("treillis") + "      ", "title");

        choixNoeud = new MyButton(optionsData.traduction("simple node"));
        choixNoeud.setOnAction(a -> {
            selectNoeud();
        });

        initNoeud();
        initBarre();
        initTypeBarre();

        terrainLbl = new MyLabel("      " + optionsData.traduction("ground") + "      ", "title");

        initTrn();
        initTriangleTrn();


        this.getChildren().addAll(selectBtn, treillisLbl, noeudBtn, choixNoeud, barreBtn, typeBox, terrainLbl, terrainBtn, triangleTerrainBtn);

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
        Stage choixNoeud = new Stage();

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
        layout.setId("vBox");

        int width = switch (optionsData.getLanguage()) {
            case "en" -> 500;
            case "fr" -> 420;
            default -> 600;
        };
        Scene scene = new Scene(layout, width, 100);

        if(optionsData.getTheme().equals("light")){
            scene.getStylesheets().add("stylesSheet/lightTheme/popUpLightStyle.css");
        }else{
            scene.getStylesheets().add("stylesSheet/darkTheme/popUpDarkStyle.css");
        }

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
            actionCenter.setSelectedButton(20);
        });
    }

    public void initTypeBarre() {
        //type list
        MyLabel typeLabel = new MyLabel(optionsData.traduction("type") + " :", "normal");

        ComboBox<Type> typeComboBox = new ComboBox<>(FXCollections.observableArrayList(actionCenter.getTreillis().getCatalogue()));
        typeComboBox.setId("typeCB");

        HBox typeHB = new HBox(10);
        typeHB.getChildren().addAll(typeLabel, typeComboBox);
        typeHB.setAlignment(Pos.CENTER);

        //boutons
        MyButton addTypeBtn = new MyButton(optionsData.traduction("add type"));
        addTypeBtn.setOnAction(e -> Type.createTypePopUp(actionCenter, typeComboBox));

        MyButton chooseBtn = new MyButton(optionsData.traduction("choose"));
        chooseBtn.setOnAction(e -> actionCenter.setBarreType(typeComboBox.getValue()));


        typeBox = new VBox(5);
        typeBox.getChildren().addAll(typeHB, addTypeBtn, chooseBtn);
        typeBox.setAlignment(Pos.CENTER);
        typeBox.setId("typeBox");

    }

    public void initTrn(){
        terrainBtn = new MyRadioButton(optionsData.traduction("constructible area"));
        terrainBtn.setToggleGroup(group);

        terrainBtn.setOnAction( actionEvent -> {
            actionCenter.removeSelected();
            actionCenter.setSelectedButton(30);
        });
    }

    private void initTriangleTrn() {
        triangleTerrainBtn = new MyRadioButton(optionsData.traduction("triangle"));
        triangleTerrainBtn.setToggleGroup(group);

        triangleTerrainBtn.setOnAction(actionEvent -> {
            actionCenter.removeSelected();
            actionCenter.setSelectedButton(40);
        });
    }

}
