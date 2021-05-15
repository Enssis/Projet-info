package fr.insa.a6;

import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Options;
import fr.insa.a6.utilities.Sauvegarde;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

    private final ActionCenter actionCenter = new ActionCenter();

    public Main() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Options options = new Options();

        MainScene mainScene = new MainScene((int) options.getWidth(), (int) options.getHeight(), actionCenter);


        String lastOpenPath = options.getLastOpen();
        String[] nameS = lastOpenPath.split("/");
        String name = nameS[nameS.length - 1].split("\\.")[0];
        Treillis treillis;
        if(lastOpenPath.equals("")){
            treillis = new Treillis();
        }else{
           treillis = Sauvegarde.getTreillis(lastOpenPath);
        }

        Scene scene = new Scene(mainScene, options.getWidth(), options.getHeight());

        actionCenter.init(mainScene, treillis, stage, name);

        if(options.getTheme().equals("light")){
            scene.getStylesheets().add("stylesSheet/lightTheme/lightStyle.css");
        }else{
            scene.getStylesheets().add("stylesSheet/darkTheme/darkStyle.css");
        }

        stage.setScene(scene);
        stage.show();
    }


}
