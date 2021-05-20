package fr.insa.a6.treillis;

import fr.insa.a6.graphic.mainbox.IconBox;
import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.graphic.utils.MyTextField;
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

/**
 * 
 */
public class Type {

    private final int id;
    private final double cout;
    private final double lMin;
    private final double lMax;
    private final double rTension;
    private final double rComp;
    private final String name;

    public Type(String name, double cout, double lMin, double lMax, double rTension, double rComp, int id) {
        this.name = name;
        this.id = id;
        this.cout = cout;
        this.lMin = lMin;
        this.lMax = lMax;
        this.rTension = rTension;
        this.rComp = rComp;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String saveString() {
        return "TypeBarre;" + id + ";" + cout + ";" + lMin + ";" + lMax + ";" + rTension + ";" + rComp + ";" + name;
    }

    static public void createTypePopUp(ActionCenter ac, ComboBox<Type> typeComboBox){
        Stage typeChoice = new Stage();
        Options options = new Options();

        typeChoice.initModality(Modality.APPLICATION_MODAL);
        typeChoice.setTitle(options.traduction("create type"));
        typeChoice.setResizable(false);


        //name
        MyLabel nameLbl = new MyLabel(options.traduction("name") + " :", "title");
        MyTextField nameTF = new MyTextField();

        HBox nameHB = new HBox(10);
        nameHB.getChildren().addAll(nameLbl, nameTF);
        nameHB.setAlignment(Pos.CENTER);


        //cout
        MyLabel costLbl = new MyLabel(options.traduction("cost") + " :", "title");
        MyTextField costTF = new MyTextField();

        HBox costHB = new HBox(10);
        costHB.getChildren().addAll(costLbl, costTF);
        costHB.setAlignment(Pos.CENTER);


        //lmin
        MyLabel lMinLbl = new MyLabel(options.traduction("length") + " " + options.traduction("minimum") +  " :", "title");
        MyTextField lMinTF = new MyTextField();

        HBox lMinHB = new HBox(10);
        lMinHB.getChildren().addAll(lMinLbl, lMinTF);
        lMinHB.setAlignment(Pos.CENTER);


        //lmax
        MyLabel lmaxLbl = new MyLabel(options.traduction("length") + " " + options.traduction("maximum") + " :", "title");
        MyTextField lmaxTF = new MyTextField();

        HBox lmaxHB = new HBox(10);
        lmaxHB.getChildren().addAll(lmaxLbl, lmaxTF);
        lmaxHB.setAlignment(Pos.CENTER);


        //rtension
        MyLabel rTensionLbl = new MyLabel(options.traduction("rTension") + " :", "title");
        MyTextField rTensionTF = new MyTextField();

        HBox rTensionHB = new HBox(10);
        rTensionHB.getChildren().addAll(rTensionLbl, rTensionTF);
        rTensionHB.setAlignment(Pos.CENTER);


        //rcomp
        MyLabel rCompLbl = new MyLabel(options.traduction("rComp") + " :", "title");
        MyTextField rCompTF = new MyTextField();

        HBox rCompHB = new HBox(10);
        rCompHB.getChildren().addAll(rCompLbl, rCompTF);
        rCompHB.setAlignment(Pos.CENTER);


        //buttons
        //boutons d'ajout, ne fait rien si une case n'est pas remplie
        Button addTypeBtn = new Button(options.traduction("add type"));
        addTypeBtn.setOnAction(e -> {
            try {
                Treillis treillis = ac.getTreillis();
                Type type = new Type(nameTF.getText(), Double.parseDouble(costTF.getText()), Double.parseDouble(lMinTF.getText()),
                        Double.parseDouble(lmaxTF.getText()), Double.parseDouble(rTensionTF.getText()),
                        Double.parseDouble(rCompTF.getText()), treillis.getNumerateur().getNewTypeId());
                treillis.addType(type);
                ArrayList<Type> catalogue = treillis.getCatalogue();
                typeComboBox.setItems(FXCollections.observableArrayList(catalogue));
                typeComboBox.getSelectionModel().select(catalogue.get(catalogue.indexOf(type)));
                typeChoice.close();
            }catch (NumberFormatException exception){
                exception.printStackTrace();
            }
        });

        //bouton pour annuler, ferme juste la fenetre
        Button cancelBtn = new Button(options.traduction("cancel"));
        cancelBtn.setOnAction(e -> typeChoice.close());

        HBox buttonHB = new HBox(10);
        buttonHB.getChildren().addAll(addTypeBtn, cancelBtn);
        buttonHB.setAlignment(Pos.CENTER);


        VBox mainVB = new VBox(10);
        mainVB.getChildren().addAll(nameHB, costHB, lMinHB, lmaxHB, rTensionHB, rCompHB, buttonHB);

        Scene scene1 = new Scene(mainVB, 400, 250);

        typeChoice.setScene(scene1);
        typeChoice.showAndWait();

    }
}