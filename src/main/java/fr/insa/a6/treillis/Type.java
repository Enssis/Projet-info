package fr.insa.a6.treillis;

import fr.insa.a6.graphic.mainbox.IconBox;
import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.graphic.utils.MyTextField;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Options;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 */
public class Type {

    private int id;
    private double cout;
    private double lMin;
    private double lMax;
    private double rTension;
    private double rComp;

    public Type(double cout, double lMin, double lMax, double rTension, double rComp, int id) {
        this.id = id;
        this.cout = cout;
        this.lMin = lMin;
        this.lMax = lMax;
        this.rTension = rTension;
        this.rComp = rComp;
    }

    public int getId() {
        return id;
    }

    public String saveString() {
        return "TypeBarre;" + id + ";" + cout + ";" + lMin + ";" + lMax + ";" + rTension + ";" + rComp;
    }

    static public void createTypePopUp(ActionCenter ac, IconBox iconBox, Stage chooseStage){
        Stage typeChoice = new Stage();
        Options options = new Options();

        typeChoice.initModality(Modality.APPLICATION_MODAL);
        typeChoice.setTitle(options.traduction("create type"));
        typeChoice.setResizable(false);


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
        Button addTypeBtn = new Button(options.traduction("add type"));
        addTypeBtn.setOnAction(e -> {
            try {
                Treillis treillis = ac.getTreillis();
                Type type = new Type(Double.parseDouble(costTF.getText()), Double.parseDouble(lMinTF.getText()),
                        Double.parseDouble(lmaxTF.getText()), Double.parseDouble(rTensionTF.getText()),
                        Double.parseDouble(rCompTF.getText()), treillis.getNumerateur().getNewTypeId());
                treillis.addType(type);
                chooseStage.close();
                iconBox.typeChoicePopUp(typeChoice);
            }catch (NumberFormatException exception){
                exception.printStackTrace();
            }
        });

        Button cancelBtn = new Button(options.traduction("cancel"));
        cancelBtn.setOnAction(e -> typeChoice.close());

        HBox buttonHB = new HBox(10);
        buttonHB.getChildren().addAll(addTypeBtn, cancelBtn);
        buttonHB.setAlignment(Pos.CENTER);


        VBox mainVB = new VBox(10);
        mainVB.getChildren().addAll(costHB, lMinHB, lmaxHB, rTensionHB, rCompHB, buttonHB);

        Scene scene1 = new Scene(mainVB, 300, 300);

        typeChoice.setScene(scene1);
        typeChoice.showAndWait();

    }
}