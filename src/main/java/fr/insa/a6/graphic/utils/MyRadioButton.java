package fr.insa.a6.graphic.utils;

import fr.insa.a6.utilities.Options;
import javafx.scene.control.RadioButton;

import java.io.IOException;
import org.json.simple.parser.*;

public class MyRadioButton extends RadioButton {

    private String info = "";

    public MyRadioButton(String name) {
        super(name);

        this.setId("myRB");

    }

    public MyRadioButton(String name, String style) {
        super(name);

        this.setId("myRB");

    }

    public MyRadioButton(String name, String info, String style) {
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
