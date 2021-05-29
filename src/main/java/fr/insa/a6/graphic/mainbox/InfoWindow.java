package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Force;
import fr.insa.a6.treillis.Type;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.nodes.AppuiSimple;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.nodes.NoeudSimple;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.utilities.*;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;


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

        Options optionsData = new Options();

        if(f instanceof Noeud) {
            MyButton addForceBtn = new MyButton(optionsData.traduction("add force"));
            addForceBtn.setOnAction(actionEvent -> Force.createTypePopUp(actionCenter, (Noeud) f));
            this.getChildren().add(addForceBtn);
        }
        MyButton deleteBtn = new MyButton(optionsData.traduction("delete"));
        deleteBtn.setOnAction(actionEvent -> {
            actionCenter.deleteForme(f);
            removeInfos();
        });

        this.getChildren().add(deleteBtn);
    }

    //dessine les informations de l'élément sélectionné
    public void drawInfos(Terrain t) {
        removeInfos();
        ArrayList<String> infos = t.getInfos();
        for (String line : infos) {
            MyLabel mL = new MyLabel(line, "normal");
            this.getChildren().add(mL);
        }

        Options optionsData = new Options();
        MyButton deleteBtn = new MyButton(optionsData.traduction("delete"));
        deleteBtn.setOnAction(actionEvent -> {
            actionCenter.deleteZoneConstru(t);
            removeInfos();
        });

        this.getChildren().add(deleteBtn);
    }

    public void removeInfos() {
        this.getChildren().clear();
    }

    //dessine des informations général des élements selectionné (nombre et possibilité de tout supprimer)
    public void drawInfosMultiplePoint(int nbNoeud,int nbAppuiDouble, int nbAppuiSimple,int nbBarre) {
        removeInfos();

        MyLabel mLN = new MyLabel("nombre de noeuds simple : " + nbNoeud, "normal");
        MyLabel mLAD = new MyLabel("nombre d'appuis double : " + nbAppuiDouble, "normal");
        MyLabel mLAS = new MyLabel("nombre d'appuis simple : " + nbAppuiSimple, "normal");
        MyLabel mLB = new MyLabel("nombre de barres : " + nbBarre, "normal");

        Options optionsData = new Options();
        MyButton delete = new MyButton(optionsData.traduction("deleteAll"));
        delete.setOnAction(actionEvent -> {
            actionCenter.deleteAllFormes();
            removeInfos();
        });
        this.getChildren().addAll(mLN, mLAD, mLAS, mLB, delete);

    }


    public void drawCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        removeInfos();

        MyLabel priceLbl = new MyLabel(optionsData.traduction("treillis price") + " : " + actionCenter.getCost() + " €", "normal");
        this.getChildren().addAll(priceLbl);


        for(Forme f : formeId.keySet()){
            int id = formeId.get(f);
            if(f instanceof Barres){
                MyLabel t = new MyLabel("Traction de la barre : " + idValues.get(id)[0], "normal");
                this.getChildren().add(t);
            }else if(f instanceof AppuiSimple){
                MyLabel r = new MyLabel("Reaction de l'appui : " + idValues.get(id)[0], "normal");
                this.getChildren().add(r);
            }else{
                MyLabel rx = new MyLabel("Reaction de l'appui en x : " + idValues.get(id)[0], "normal");
                this.getChildren().add(rx);
                MyLabel ry = new MyLabel("Reaction de l'appui en y : " + idValues.get(id)[1], "normal");
                this.getChildren().add(ry);
            }
        }
    }


}
