package fr.insa.a6;

import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Options;
import fr.insa.a6.utilities.Save;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

    private ActionCenter actionCenter;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Options options = new Options();

        String lastOpenPath = options.getLastOpen();
        String name = ActionCenter.nameFromPath(lastOpenPath);
        Treillis treillis;
        if(lastOpenPath.equals("")){
            treillis = new Treillis();
        }else{
            treillis = Save.getTreillis(lastOpenPath);
            if(treillis == null){
                name = "";
                treillis = new Treillis();
            }
        }

        actionCenter  = new ActionCenter(treillis);

        MainScene mainScene = new MainScene((int) options.getWidth(), (int) options.getHeight(), actionCenter);


        Scene scene = new Scene(mainScene, options.getWidth(), options.getHeight());

        actionCenter.init(mainScene, stage, lastOpenPath);

        if(options.getTheme().equals("light")){
            scene.getStylesheets().add("stylesSheet/lightTheme/lightStyle.css");
        }else{
            scene.getStylesheets().add("stylesSheet/darkTheme/darkStyle.css");
        }

        if(name.equals("")) name = "~Nouveau~";
        stage.setTitle(name);
        stage.setScene(scene);
        stage.show();
    }


}
