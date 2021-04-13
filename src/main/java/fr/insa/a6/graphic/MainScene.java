package fr.insa.a6.graphic;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class MainScene extends BorderPane {

    private MyMenuBar menus;

    public MainScene() throws IOException, ParseException {
        super();

        menus = new MyMenuBar();
        this.setTop(menus);

    }

    public Scene getScene(int w, int h){
        return new Scene(this, w, h);
    }

}
