package fr.insa.a6.graphic.utils;

import javafx.scene.control.RadioButton;


public class MyRadioButton extends RadioButton {

    private String info = "";
    private final String name;

    public MyRadioButton(String name) {
        super(name);
        this.name = name;
        this.setId("myRB");

    }

    public MyRadioButton(String name, String info) {
        super(name);
        this.info = info;
        this.name = name;
        this.setId("myRB");
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }
}
