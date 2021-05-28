package fr.insa.a6.treillis;

import fr.insa.a6.graphic.mainbox.IconBox;
import fr.insa.a6.graphic.utils.MyButton;
import fr.insa.a6.graphic.utils.MyLabel;
import fr.insa.a6.graphic.utils.MyTextField;
import fr.insa.a6.utilities.ActionCenter;
import fr.insa.a6.utilities.Maths;
import fr.insa.a6.utilities.Options;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private final Color color;

    public Type(String name, double cout, double lMin, double lMax, double rTension, double rComp, int id, Color color) {
        this.name = name;
        this.id = id;
        this.cout = cout;
        this.lMin = lMin;
        this.lMax = lMax;
        this.rTension = rTension;
        this.rComp = rComp;
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String saveString() {
        return "TypeBarre;" + id + ";" + cout + ";" + lMin + ";" + lMax + ";" + rTension + ";" + rComp + ";" + name + ";" + Maths.colorToHexa(color);
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


        //couleurs
        MyLabel colorLBL = new MyLabel(options.traduction("color") + " :", "title");
        ColorPicker colorPicker = new ColorPicker(Color.GRAY);
        colorPicker.setId("colorPicker");

        HBox colorHB = new HBox(10);
        colorHB.getChildren().addAll(colorLBL, colorPicker);
        colorHB.setAlignment(Pos.CENTER);

        //buttons
        //boutons d'ajout, ne fait rien si une case n'est pas remplie
        MyButton addTypeBtn = new MyButton(options.traduction("add type"));
        addTypeBtn.setOnAction(e -> {
            try {
                Treillis treillis = ac.getTreillis();
                Type type = new Type(nameTF.getText(), Double.parseDouble(costTF.getText()), Double.parseDouble(lMinTF.getText()),
                        Double.parseDouble(lmaxTF.getText()), Double.parseDouble(rTensionTF.getText()),
                        Double.parseDouble(rCompTF.getText()), treillis.getNumerateur().getNewTypeId(), colorPicker.getValue());
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
        MyButton cancelBtn = new MyButton(options.traduction("cancel"));
        cancelBtn.setOnAction(e -> typeChoice.close());

        HBox buttonHB = new HBox(10);
        buttonHB.getChildren().addAll(addTypeBtn, cancelBtn);
        buttonHB.setAlignment(Pos.CENTER);


        VBox mainVB = new VBox(10);
        mainVB.getChildren().addAll(nameHB, costHB, lMinHB, lmaxHB, rTensionHB, rCompHB, colorHB, buttonHB);
        mainVB.setId("vBox");

        Scene scene1 = new Scene(mainVB, 560, 300);

        if(options.getTheme().equals("light")){
            scene1.getStylesheets().add("stylesSheet/lightTheme/popUpLightStyle.css");
        }else{
            scene1.getStylesheets().add("stylesSheet/darkTheme/popUpDarkStyle.css");
        }

        typeChoice.setScene(scene1);
        typeChoice.showAndWait();

    }

    public double getCout() {
        return cout;
    }

    public Color getColor() {
        return color;
    }

    public double getlMin() {
        return lMin;
    }

    public double getlMax() {
        return lMax;
    }

    public ArrayList<String> getInfos() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("Nom : " + name);
        infos.add("Cout : " + cout);
        infos.add("Longueur min : " + lMin);
        infos.add("Longueur max : " + lMax);
        infos.add("Resistance maximale à la compression : " + rComp);
        infos.add("Resistance maximale à la tension : " + rTension);
        infos.add("Couleur : " + Maths.colorToHexa(color));

        return infos;
    }
}