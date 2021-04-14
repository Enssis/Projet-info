package fr.insa.a6.graphic;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class IconBox extends VBox {

    private MyToggleButton barre;
    private MyToggleButton select;
    private MyToggleButton noeud;


    public IconBox() {
        super();

        noeud = new MyToggleButton("Noeud");
        barre = new MyToggleButton("Barre");
        select = new MyToggleButton("Selection");


        this.getChildren().addAll(barre, select);

    }
}
