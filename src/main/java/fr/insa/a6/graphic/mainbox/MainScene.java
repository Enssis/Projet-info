package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.treillis.Treillis;
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
    private InfoWindow infos;
    private Treillis treillis;

    public MainScene(int w, int h, Treillis treillis) throws IOException, ParseException {
        super();

        this.treillis = treillis;

        menus = new MyMenuBar();
        this.setTop(menus);

        canvas = new MainCanvas(w, h, this);
        this.setCenter(canvas);

        infos = new InfoWindow(canvas);
        this.setRight(infos);

        icons = new IconBox(this);
        this.setLeft(icons);

        this.setCenter(canvas);

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

    public Treillis getTreillis() {
        return treillis;
    }
}
