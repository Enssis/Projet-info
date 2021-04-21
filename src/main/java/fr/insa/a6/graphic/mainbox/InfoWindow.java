package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.treillis.Point;
import fr.insa.a6.utilities.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import org.json.simple.parser.*;

public class InfoWindow extends VBox {

    public InfoWindow(Point p, MainCanvas canvas) {
        super();
        String[] infos = p.getInfos();
        for (String line : infos) {
            MyLabel mL = new MyLabel(line);
            this.getChildren().add(mL);
        }
        try {
            Options optionsData = new Options();
            MyButton delete = new MyButton(optionsData.traduction("delete"));
            delete.setOnAction(actionEvent -> {
                canvas.deletePoint(p);
            });
            this.getChildren().add(delete);
        }catch (IOException | ParseException ignored){

        }
    }

}
