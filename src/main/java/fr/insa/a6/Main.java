package fr.insa.a6;


import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Treillis;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.parser.*;

import java.io.IOException;

public class Main extends Application{

    private MainScene mainScene;
    private Treillis treillis;


    public Main() throws IOException, ParseException {

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        mainScene = new MainScene(700, 700, treillis);

        Scene scene = new Scene(mainScene, 700, 700);

        scene.getStylesheets().add("lightStyle.css");
        stage.setScene(scene);
        stage.show();
    }





}
