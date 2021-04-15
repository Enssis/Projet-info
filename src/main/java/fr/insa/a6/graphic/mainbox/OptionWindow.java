package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.MyRadioButton;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OptionWindow {


    public static void display() {
        Stage optionWindow = new Stage();

        optionWindow.initModality(Modality.APPLICATION_MODAL);
        optionWindow.setTitle("Options");

        BorderPane mainBP = new BorderPane();



        ToggleGroup tgLangue = new ToggleGroup();

        MyRadioButton rdAnglais = new MyRadioButton("Anglais");
        MyRadioButton rdFrancais = new MyRadioButton("Français");
        rdFrancais.setToggleGroup(tgLangue);
        rdAnglais.setToggleGroup(tgLangue);

        HBox hbLangue = new HBox(rdFrancais, rdAnglais);


        Button button1 = new Button("Close this pop up window");


        button1.setOnAction(e -> optionWindow.close());

        VBox layout = new VBox(10);

        layout.getChildren().addAll(hbLangue, button1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 300, 250);

        optionWindow.setScene(scene1);

        optionWindow.showAndWait();

    }
}
