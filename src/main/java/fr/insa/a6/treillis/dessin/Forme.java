package fr.insa.a6.treillis.dessin;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public abstract class Forme {

    protected boolean selected = false;
    protected int id;

    public abstract void draw(GraphicsContext gc, Point origin);
    public abstract void drawNear(GraphicsContext gc, Point origin);

    public abstract ArrayList<String> getInfos();

    public abstract void drawResult(double result, GraphicsContext gc, Point origin);

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

