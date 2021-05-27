package fr.insa.a6.treillis;


import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.graphic.utils.MyTextField;
import fr.insa.a6.treillis.nodes.NoeudSimple;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Options;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Force {

    public double fX;
    public double fY;

    /**
     * Default constructor
     */
    public Force() {
        fX = 0;
        fY = 0;
    }

    public Force(double fx, double fy){
        this.fX = fx;
        this.fY = fy;
    }

    static public void createTypePopUp(ActionCenter ac, NoeudSimple noeudSimple){
        Stage addForce = new Stage();
        Options options = new Options();

        addForce.initModality(Modality.APPLICATION_MODAL);
        addForce.setTitle(options.traduction("create type"));
        addForce.setResizable(false);


        //Fx
        MyLabel fxLbl = new MyLabel("Fx :", "title");
        MyTextField fxTF = new MyTextField();

        HBox fxHB = new HBox(10);
        fxHB.getChildren().addAll(fxLbl, fxTF);
        fxHB.setAlignment(Pos.CENTER);

        //Fy
        MyLabel fyLbl = new MyLabel("Fy :", "title");
        MyTextField fyTF = new MyTextField();

        HBox fyHB = new HBox(10);
        fyHB.getChildren().addAll(fyLbl, fyTF);
        fyHB.setAlignment(Pos.CENTER);


        //buttons
        //boutons d'ajout, ne fait rien si une case n'est pas remplie
        Button addTypeBtn = new Button(options.traduction("add force"));
        addTypeBtn.setOnAction(e -> {
          /*  try {
                Treillis treillis = ac.getTreillis();
                Type type = new Type(nameTF.getText(), Double.parseDouble(costTF.getText()), Double.parseDouble(lMinTF.getText()),
                        Double.parseDouble(lmaxTF.getText()), Double.parseDouble(rTensionTF.getText()),
                        Double.parseDouble(rCompTF.getText()), treillis.getNumerateur().getNewTypeId());
                treillis.addType(type);
                ArrayList<Type> catalogue = treillis.getCatalogue();
                typeComboBox.setItems(FXCollections.observableArrayList(catalogue));
                typeComboBox.getSelectionModel().select(catalogue.get(catalogue.indexOf(type)));
                addForce.close();
            }catch (NumberFormatException exception){
                exception.printStackTrace();
            } */
        });

        //bouton pour annuler, ferme juste la fenetre
        Button cancelBtn = new Button(options.traduction("cancel"));
        cancelBtn.setOnAction(e -> addForce.close());

        HBox buttonHB = new HBox(10);
        buttonHB.getChildren().addAll(addTypeBtn, cancelBtn);
        buttonHB.setAlignment(Pos.CENTER);


        VBox mainVB = new VBox(10);
      //  mainVB.getChildren().addAll(nameHB, costHB, lMinHB, lmaxHB, rTensionHB, rCompHB, buttonHB);

        Scene scene1 = new Scene(mainVB, 400, 250);

        addForce.setScene(scene1);
        addForce.showAndWait();

    }

}