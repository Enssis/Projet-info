package fr.insa.a6.graphic.mainbox;

import fr.insa.a6.graphic.Graphics;
import fr.insa.a6.utilities.ActionCenter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;


public class MainCanvas extends Pane {

    private final GraphicsContext gc;

    private final ActionCenter actionCenter;
    private final Graphics graphics;

    public MainCanvas(double width, double height, MainScene mainScene) {
        super();
        this.setPrefSize(width, height);

        Canvas canvas = new Canvas();
        this.actionCenter = mainScene.getActionCenter();
        this.graphics = actionCenter.getGraphics();

        gc = canvas.getGraphicsContext2D();

        canvas.setManaged(false);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(canvas);

        canvas.heightProperty().addListener((o) -> graphics.draw(actionCenter.getSelectedButton(), actionCenter.isInDrawing()));
        canvas.widthProperty().addListener((o) -> graphics.draw(actionCenter.getSelectedButton(), actionCenter.isInDrawing()));

    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

}