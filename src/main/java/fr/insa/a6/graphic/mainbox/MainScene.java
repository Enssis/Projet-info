package fr.insa.a6.graphic.mainbox;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.json.simple.parser.*;

import java.io.IOException;

public class MainScene extends BorderPane {

    private MyMenuBar menus;
    private IconBox icons;
    private MainCanvas canvas;
    private Scene scene;

    public MainScene(int w, int h) throws IOException, ParseException {
        super();

        menus = new MyMenuBar();
        this.setTop(menus);

        canvas = new MainCanvas(w, h, this);
        this.setCenter(canvas);

        icons = new IconBox(this);
        this.setLeft(icons);

        scene = new Scene(this, w, h);

        //ajout des fonctions permettant l'ajustement de la taille du canvas
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            canvas.setWidth(newSceneWidth.doubleValue());
            canvas.redraw();
        });

        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            canvas.setHeight(newSceneHeight.doubleValue());
            canvas.redraw();
        });

    }

    public MainCanvas getCanvas() {
        return canvas;
    }
}
