package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.utilities.*;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.parser.*;


//VBox avec les informations sur la selection en cours
public class InfoWindow extends VBox {

    public MainScene mainScene;

    public InfoWindow(MainScene mainScene) {
        super();
        this.mainScene = mainScene;
        this.setAlignment(Pos.CENTER);
        this.setId("infoBox");
    }

    //dessine les informations de la forme sélectionné
    public void drawInfos(Forme f) {
        ActionCenter ac = mainScene.getActionCenter();
        removeInfos();
        ArrayList<String> infos = f.getInfos();
        for (String line : infos) {
            MyLabel mL = new MyLabel(line, "normal");
            this.getChildren().add(mL);
        }

        Options optionsData = new Options();

        MyButton addForceBtn = new MyButton(optionsData.traduction("add force"));
        addForceBtn.setOnAction(actionEvent -> {

        });



        MyButton deleteBtn = new MyButton(optionsData.traduction("delete"));
        deleteBtn.setOnAction(actionEvent -> {
            ac.deleteForme(f);
            removeInfos();
        });

        HBox buttonHB = new HBox(20);
        buttonHB.getChildren().addAll(deleteBtn, addForceBtn);

        this.getChildren().add(deleteBtn);
    }

    //dessine les informations de l'élément sélectionné
    public void drawInfos(Terrain t) {
        ActionCenter ac = mainScene.getActionCenter();
        removeInfos();
        ArrayList<String> infos = t.getInfos();
        for (String line : infos) {
            MyLabel mL = new MyLabel(line, "normal");
            this.getChildren().add(mL);
        }

        Options optionsData = new Options();
        MyButton deleteBtn = new MyButton(optionsData.traduction("delete"));
        deleteBtn.setOnAction(actionEvent -> {
            ac.deleteZoneConstru(t);
            removeInfos();
        });

        this.getChildren().add(deleteBtn);
    }

    public void removeInfos() {
        this.getChildren().clear();
    }

    //dessine des informations général des élements selectionné (nombre et possibilité de tout supprimer)
    public void drawInfosMultiplePoint(int nbPoint, int nbSegment) {
        ActionCenter ac = mainScene.getActionCenter();
        removeInfos();

        MyLabel mLP = new MyLabel("nombre de points : " + nbPoint, "normal");
        MyLabel mLS = new MyLabel("nombre de segments : " + nbSegment, "normal");

        Options optionsData = new Options();
        MyButton delete = new MyButton(optionsData.traduction("deleteAll"));
        delete.setOnAction(actionEvent -> {
            ac.deleteAllFormes();
            removeInfos();
        });
        this.getChildren().addAll(mLP, mLS, delete);

    }



}
