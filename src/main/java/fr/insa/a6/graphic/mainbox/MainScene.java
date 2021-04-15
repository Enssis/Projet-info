package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.utilities.Options;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.json.simple.parser.*;

import java.io.IOException;

public class MainScene extends BorderPane {

    private MyMenuBar menus;
    private IconBox icons;
    private MainCanvas canvas;

    private Options optionsData = new Options();

    public MainScene() throws IOException, ParseException {
        super();

        menus = new MyMenuBar();
        this.setTop(menus);

        icons = new IconBox();
        this.setLeft(icons);

        canvas = new MainCanvas();
        this.setCenter(canvas);

    }

    public Scene getScene(int w, int h){
        return new Scene(this, w, h);
    }

}
