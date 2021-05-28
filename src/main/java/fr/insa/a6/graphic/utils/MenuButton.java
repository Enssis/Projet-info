package fr.insa.a6.graphic.utils;

import javafx.scene.control.Menu;

public class MenuButton extends Menu {

    public MenuButton() {
        this("TEST");
    }

    public MenuButton(String title) {
        super(title);
        this.setId("menuButton");
    }

}
