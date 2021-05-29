package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Options;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.IOException;

import javafx.scene.layout.VBox;
import org.json.simple.parser.*;

public class MainScene extends BorderPane {

    private final MainCanvas canvas;
    private final InfoWindow infos;
    private final ActionCenter actionCenter;
    private final IconBox icons;

    public MainScene(int w, int h, ActionCenter actionCenter) {
        super();
        this.actionCenter = actionCenter;

        MyMenuBar menus = new MyMenuBar(actionCenter);

        this.setTop(menus);

        canvas = new MainCanvas(w, h, this);
        this.setCenter(canvas);

        infos = new InfoWindow(this);
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

    public ActionCenter getActionCenter() {
        return actionCenter;
    }

    public IconBox getIcons() {
        return icons;
    }
}
