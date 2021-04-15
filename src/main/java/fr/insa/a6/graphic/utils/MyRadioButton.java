package fr.insa.a6.graphic.utils;

import javafx.scene.control.RadioButton;

public class MyRadioButton extends RadioButton {

    private String info = "";

    public MyRadioButton(String name){
        super(name);
    }

    public MyRadioButton(String name, String info){
        super(name);
        this.info = info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
