package fr.insa.a6;

import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Options;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

    private MainScene mainScene;
    private Treillis treillis;
    private final ActionCenter actionCenter = new ActionCenter();

    public Main() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Options options = new Options();

        mainScene = new MainScene(700, 700, actionCenter);

        treillis = new Treillis();

        Scene scene = new Scene(mainScene, options.getWidth(), options.getHeight());

        actionCenter.init(mainScene, treillis, scene);

        if(options.getTheme().equals("light")){
            scene.getStylesheets().add("stylesSheet/lightTheme/lightStyle.css");
        }else{
            scene.getStylesheets().add("stylesSheet/darkTheme/darkStyle.css");
        }

        stage.setScene(scene);
        stage.show();
    }





}
