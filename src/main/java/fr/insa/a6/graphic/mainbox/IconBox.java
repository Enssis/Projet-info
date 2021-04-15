package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.utilities.Options;

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

    //types : 0 -> simple, 1 -> appuiDouble, 2 -> appuiEncastre, 3 -> appuiSimple
    private String typeNoeud = "0";

    private String style = "";

    public IconBox() throws IOException, ParseException {
        super();
        this.setAlignment(Pos.CENTER);
        this.setId("iconBox");

        style = optionsData.getStyle();

        initSelect();

        treillisLbl = new MyLabel(optionsData.traduction("treillis"));

        initNoeud();
        initBarre();

        terrainLbl = new MyLabel(optionsData.traduction("ground"));

        initPointTrn();
        initSegmentTrn();

        this.getChildren().addAll(selectBtn, treillisLbl, noeudBtn, barreBtn, terrainLbl, pointTerrainBtn, segmentTerrainBtn);

    }

    private void initNoeud() throws IOException, ParseException {
        noeudBtn = new MyRadioButton("Noeud", style);
        noeudBtn.setToggleGroup(group);

        noeudBtn.setOnAction(actionEvent -> {
            selectNoeud();
        });
    }

    private void selectNoeud() {

        //pop up window de choix
        Stage choixNoeud =new Stage();

        choixNoeud.initModality(Modality.APPLICATION_MODAL);
        choixNoeud.setTitle(optionsData.traduction("node choice"));
        choixNoeud.setResizable(false);

        //text devant les boutons
        MyLabel label = new MyLabel(optionsData.traduction("choose node"));

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

        MyRadioButton noeudSimple = new MyRadioButton(optionsData.traduction("simple node"), "0", style);
        noeudSimple.setToggleGroup(tGroup);
        noeudSimple.setSelected(true);

        MyRadioButton appuiDouble = new MyRadioButton(optionsData.traduction("double support"), "1", style);
        appuiDouble.setToggleGroup(tGroup);

        MyRadioButton appuiEncastre = new MyRadioButton(optionsData.traduction("embedded support"), "2", style);
        appuiEncastre.setToggleGroup(tGroup);

        MyRadioButton appuiSimple = new MyRadioButton(optionsData.traduction("simple support"), "3", style);
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

    private void initSelect() throws IOException, ParseException {
        selectBtn = new MyRadioButton("Selection", style);
        selectBtn.setToggleGroup(group);
        selectBtn.setSelected(true);

        selectBtn.setOnAction(actionEvent -> {

        });
    }

    private void initBarre() {
        barreBtn = new MyRadioButton("Barre", style);
        barreBtn.setToggleGroup(group);

        barreBtn.setOnAction(actionEvent -> {

        });
    }

    private void initPointTrn() {
        pointTerrainBtn = new MyRadioButton("Point", style);
        pointTerrainBtn.setToggleGroup(group);

        pointTerrainBtn.setOnAction(actionEvent -> {

        });
    }

    private void initSegmentTrn() {
        segmentTerrainBtn = new MyRadioButton("Segment", style);
        segmentTerrainBtn.setToggleGroup(group);

        segmentTerrainBtn.setOnAction(actionEvent -> {

        });
    }

}
