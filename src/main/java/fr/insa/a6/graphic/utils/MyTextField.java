package fr.insa.a6.graphic.utils;

import javafx.scene.control.TextField;

public class MyTextField extends TextField {

    public MyTextField() {
        super();
        this.setId("myTF");
    }

    public MyTextField(String defaut) {
        super();
        this.setText(defaut);
        this.setId("myTF");
    }
}
