package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.utils.*;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Options;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class OptionWindow {

    public static void display(ActionCenter actionCenter) {
        Stage optionWindow = new Stage();

        optionWindow.initModality(Modality.APPLICATION_MODAL);
        optionWindow.setTitle("Options");
        optionWindow.setResizable(false);

        Options options = new Options();
        Options tempOptions = new Options(true);

        //theme
        MyLabel themeLbl = new MyLabel(options.traduction("theme"), "title");

        ToggleGroup themeTG = new ToggleGroup();

        MyRadioButton lightRB = new MyRadioButton(options.traduction("light"));
        lightRB.setOnAction(e -> tempOptions.setTheme("light"));
        lightRB.setToggleGroup(themeTG);

        MyRadioButton darkRB = new MyRadioButton(options.traduction("dark"));
        darkRB.setOnAction(e -> tempOptions.setTheme("dark"));
        darkRB.setToggleGroup(themeTG);

        if(options.getTheme().equals("light")) {
            lightRB.setSelected(true);
        }else{
            darkRB.setSelected(true);
        }
        VBox themeVB = new VBox(10);
        themeVB.getChildren().addAll(themeLbl, lightRB, darkRB);
        themeVB.setAlignment(Pos.CENTER);

        //taille par defaut
        MyLabel sizeLbl = new MyLabel(options.traduction("default size"), "title");

        //hauteur
        MyLabel heightLbl = new MyLabel(options.traduction("height") + " :", "title");
        MyTextField heightTF = new MyTextField(Long.toString(options.getHeight()));

        HBox heightHB = new HBox(10);
        heightHB.getChildren().addAll(heightLbl, heightTF);
        heightHB.setAlignment(Pos.CENTER);

        //largeur
        MyLabel widthLbl = new MyLabel(options.traduction("width") + " :", "title");
        MyTextField widthTF = new MyTextField(Long.toString(options.getWidth()));

        HBox widthHB = new HBox(10);
        widthHB.getChildren().addAll(widthLbl, widthTF);
        widthHB.setAlignment(Pos.CENTER);

        VBox sizeVB = new VBox(10);
        sizeVB.getChildren().addAll(sizeLbl, heightHB, widthHB);
        sizeVB.setAlignment(Pos.CENTER);

        //Langue
        MyLabel languageLbl = new MyLabel(options.traduction("language") + " :", "title");

        ComboBox<Language> languageCB = new ComboBox<>(FXCollections.observableArrayList(new Language("English", "en"), new Language("Français", "fr")));
        int i = -1;
        switch (options.getLanguage()){
            case "en" -> i = 0;
            case "fr" -> i = 1;
        }

        languageCB.getSelectionModel().select(i);

        HBox languageHB = new HBox(10);
        languageHB.getChildren().addAll(languageLbl, languageCB);
        languageHB.setAlignment(Pos.CENTER);



        //boutons
        Button applyBtn = new Button(options.traduction("apply"));
        applyBtn.setOnAction(e -> {
            tempOptions.setDefaultH(Long.parseLong(heightTF.getText()));
            tempOptions.setDefaultW(Long.parseLong(widthTF.getText()));

            tempOptions.setLanguage(languageCB.getSelectionModel().getSelectedItem().getValue());

            tempOptions.saveFile();

            try {
                actionCenter.reload(actionCenter.getPath());
            } catch (IOException | ParseException ioException) {
                ioException.printStackTrace();
            }
        });

        Button cancelBtn = new Button(options.traduction("cancel"));
        cancelBtn.setOnAction(e -> optionWindow.close());

        HBox buttonHB = new HBox(20);
        buttonHB.getChildren().addAll(applyBtn, cancelBtn);
        buttonHB.setAlignment(Pos.CENTER);


        VBox layout = new VBox(20);
        layout.getChildren().setAll(themeVB, sizeVB, languageHB, buttonHB);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 300, 300);

        optionWindow.setScene(scene1);
        optionWindow.showAndWait();

    }
}
