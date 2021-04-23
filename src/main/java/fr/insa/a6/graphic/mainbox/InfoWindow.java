package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.utilities.*;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class InfoWindow extends VBox {

    public MainScene mainScene;

    public InfoWindow(MainScene mainScene) {
        super();
        this.mainScene = mainScene;
        this.setAlignment(Pos.CENTER);
        this.setId("infoBox");
    }

    public void drawInfos(Forme f) {
        ActionCenter ac = mainScene.getActionCenter();
        removeInfos();
        ArrayList<String> infos = f.getInfos();
        for (String line : infos) {
            MyLabel mL = new MyLabel(line, "normal");
            this.getChildren().add(mL);
        }

        Options optionsData = new Options();
        MyButton delete = new MyButton(optionsData.traduction("delete"));
        delete.setOnAction(actionEvent -> {
            ac.deleteForme(f);
            removeInfos();
        });
        this.getChildren().add(delete);

    }

    public void removeInfos() {
        this.getChildren().clear();
    }

    public void drawInfosMultiplePoint(int nbPoint, int nbSegment){
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
