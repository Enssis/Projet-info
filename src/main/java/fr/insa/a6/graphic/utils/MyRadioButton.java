package fr.insa.a6.graphic.utils;

import javafx.scene.control.RadioButton;


public class MyRadioButton extends RadioButton {

    private String info = "";

    public MyRadioButton(String name) {
        super(name);

        this.setId("myRB");

    }

    public MyRadioButton(String name, String info) {
        super(name);
        this.info = info;

        this.setId("myRB");
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

}
