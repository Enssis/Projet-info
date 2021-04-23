package fr.insa.a6;

import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.utilities.ActionCenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

    private MainScene mainScene;
    private Treillis treillis;
    private ActionCenter actionCenter = new ActionCenter();

    public Main() {

    }

    public static void main(String[] args) {
        //System.out.println(Noeud.class);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        mainScene = new MainScene(700, 700, actionCenter);

        treillis = new Treillis();

        actionCenter.init(mainScene, treillis);

        Scene scene = new Scene(mainScene, 700, 700);

        scene.getStylesheets().add("lightStyle.css");
        stage.setScene(scene);
        stage.show();
    }





}
