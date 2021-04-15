package fr.insa.a6;

import fr.insa.a6.graphic.mainbox.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.parser.*;

import java.io.IOException;

public class Main extends Application{

    private final MainScene mainScene = new MainScene();

    public Main() throws IOException, ParseException {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(mainScene.getScene(500, 500));
        stage.show();
    }

}
