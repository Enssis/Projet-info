package fr.insa.a6.graphic.utils;

import javafx.scene.control.Label;

public class MyLabel extends Label {

    public MyLabel(String name, String type){
        super(name);
        switch (type){
            case "title" -> this.setId("labelTitle");
            case "normal" -> this.setId("labelNormal");
        }

    }

}
