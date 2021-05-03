package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.utilities.ActionCenter;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import org.json.simple.parser.*;

public class MainScene extends BorderPane {

    private MyMenuBar menus;
    private IconBox icons;
    private MainCanvas canvas;
    private InfoWindow infos;

    private ActionCenter actionCenter;

    public MainScene(int w, int h, ActionCenter actionCenter) throws IOException, ParseException {
        super();

        this.actionCenter = actionCenter;

        menus = new MyMenuBar();
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
}
