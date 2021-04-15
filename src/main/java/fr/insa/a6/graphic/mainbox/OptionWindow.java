package fr.insa.a6.graphic.mainbox;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OptionWindow {

public static void display()
{
    Stage optionWindow =new Stage();

    optionWindow.initModality(Modality.APPLICATION_MODAL);
    optionWindow.setTitle("Options");

    BorderPane mainBP = new BorderPane();



    Label label1= new Label("Pop up window now displayed");


    Button button1= new Button("Close this pop up window");


    button1.setOnAction(e -> optionWindow.close());

    VBox layout= new VBox(10);

    layout.getChildren().addAll(label1, button1);

    layout.setAlignment(Pos.CENTER);

    Scene scene1= new Scene(layout, 300, 250);

    optionWindow.setScene(scene1);

    optionWindow.showAndWait();

}
}
