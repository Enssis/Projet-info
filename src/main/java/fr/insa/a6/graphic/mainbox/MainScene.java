package fr.insa.a6.graphic.mainbox;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.json.simple.parser.*;

import java.io.IOException;

public class MainScene extends BorderPane {

    private MyMenuBar menus;
    private IconBox icons;
    private MainCanvas canvas;
    private Scene scene;
    private InfoWindow infos;

    public MainScene(int w, int h) throws IOException, ParseException {
        super();

        menus = new MyMenuBar();
        this.setTop(menus);

        canvas = new MainCanvas(w, h, this);
        this.setCenter(canvas);

        infos = new InfoWindow(canvas);
        this.setRight(infos);

        icons = new IconBox(this);
        this.setLeft(icons);

        this.setCenter(canvas);

        //scene = new Scene(this, w, h);



    }

    public MainCanvas getCanvas() {
        return canvas;
    }

    public InfoWindow getInfos() {
        return infos;
    }

    public double getBorderWidth() {
        return infos.getWidth() + icons.getWidth();
    }
}
