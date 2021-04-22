package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.utilities.*;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class InfoWindow extends VBox {

    public MainCanvas canvas;

    public InfoWindow(MainCanvas canvas) {
        super();
        this.canvas = canvas;
        this.setAlignment(Pos.CENTER);
        this.setId("infoBox");
    }

    public void drawInfos(Forme f) {
        removeInfos();
        ArrayList<String> infos = f.getInfos();
        for (String line : infos) {
            MyLabel mL = new MyLabel(line, "normal");
            this.getChildren().add(mL);
        }

        Options optionsData = new Options();
        MyButton delete = new MyButton(optionsData.traduction("delete"));
        delete.setOnAction(actionEvent -> {
            canvas.deleteForme(f);
            removeInfos();
        });
        this.getChildren().add(delete);

    }

    public void removeInfos() {
        this.getChildren().clear();
    }

    public void drawInfosMultiplePoint(int nbPoint, int nbSegment){
        removeInfos();
        if(nbPoint > 0) {
            MyLabel mLP = new MyLabel("nombre de points : " + nbPoint, "normal");
            MyLabel mLS = new MyLabel("nombre de segments : " + nbSegment, "normal");

            Options optionsData = new Options();
            MyButton delete = new MyButton(optionsData.traduction("deleteAll"));
            delete.setOnAction(actionEvent -> {
                canvas.deleteAllFormes();
                removeInfos();
            });
            this.getChildren().addAll(mLP, mLS, delete);
        }
    }



}
