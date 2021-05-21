package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.utilities.*;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


//VBox avec les informations sur la selection en cours
public class InfoWindow extends VBox {

    private final MainScene mainScene;
    private final ActionCenter actionCenter;
    private final Options optionsData = new Options();

    public InfoWindow(MainScene mainScene) {
        super();
        this.mainScene = mainScene;
        this.actionCenter = mainScene.getActionCenter();
        this.setAlignment(Pos.CENTER);
        this.setId("infoBox");
    }

    //dessine les informations de la forme sélectionné
    public void drawInfos(Forme f) {
        removeInfos();
        ArrayList<String> infos = f.getInfos();
        for (String line : infos) {
            MyLabel mL = new MyLabel(line, "normal");
            this.getChildren().add(mL);
        }

        MyButton delete = new MyButton(optionsData.traduction("delete"));
        delete.setOnAction(actionEvent -> {
            actionCenter.deleteForme(f);
            removeInfos();
        });
        this.getChildren().add(delete);
    }

    //dessine les informations de l'élément sélectionné
    public void drawInfos(Terrain t) {
        removeInfos();
        ArrayList<String> infos = t.getInfos();
        for (String line : infos) {
            MyLabel mL = new MyLabel(line, "normal");
            this.getChildren().add(mL);
        }

        MyButton delete = new MyButton(optionsData.traduction("delete"));
        delete.setOnAction(actionEvent -> {
            actionCenter.deleteZoneConstru(t);
            removeInfos();
        });
        this.getChildren().add(delete);
    }

    public void removeInfos() {
        this.getChildren().clear();
    }

    //dessine des informations général des élements selectionné (nombre et possibilité de tout supprimer)
    public void drawInfosMultiplePoint(int nbPoint, int nbSegment) {
        removeInfos();

        MyLabel mLP = new MyLabel("nombre de points : " + nbPoint, "normal");
        MyLabel mLS = new MyLabel("nombre de segments : " + nbSegment, "normal");

        Options optionsData = new Options();
        MyButton delete = new MyButton(optionsData.traduction("deleteAll"));
        delete.setOnAction(actionEvent -> {
            actionCenter.deleteAllFormes();
            removeInfos();
        });
        this.getChildren().addAll(mLP, mLS, delete);

    }


    public void drawCalculInfo(){
        removeInfos();

        MyLabel priceLbl = new MyLabel(optionsData.traduction("treillis price") + " : " + actionCenter.getCost() + " €", "normal");

        this.getChildren().addAll(priceLbl);
    }


}
