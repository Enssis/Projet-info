package fr.insa.a6;

import fr.insa.a6.graphic.mainbox.MainCanvas;
import javafx.scene.canvas.Canvas;
import fr.insa.a6.graphic.mainbox.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.simple.parser.*;

import java.awt.*;
import java.io.IOException;

public class Main extends Application{

    private MainScene mainScene;

    public Main() throws IOException, ParseException {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        mainScene = new MainScene(500, 500);

        Scene scene = mainScene.getScene();
        scene.getStylesheets().add("lightStyle.css");
        stage.setScene(scene);
        stage.show();
    }

}
