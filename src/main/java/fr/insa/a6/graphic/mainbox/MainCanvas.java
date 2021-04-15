package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.utilities.Options;
import javafx.scene.canvas.Canvas;

import java.io.IOException;
import org.json.simple.parser.*;

public class MainCanvas extends Canvas {

    private Options optionsData = new Options();

    public MainCanvas() throws IOException, ParseException {
        super();

        //style(optionsData.getStyle());

    }

    private void style(String styleType) {
        String style = switch (styleType) {
            case "light" -> "-fx-background-color: white;";
            case "dark" -> "-fx-background-color: #2a2b2a;";
            default -> "";
        };
        style += "";
        this.setStyle(style);
    }

}
