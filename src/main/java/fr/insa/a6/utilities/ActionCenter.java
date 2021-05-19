package fr.insa.a6.utilities;

import fr.insa.a6.graphic.Graphics;
import fr.insa.a6.graphic.mainbox.MainCanvas;
import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.Type;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.nodes.NoeudSimple;
import fr.insa.a6.treillis.terrain.PointTerrain;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.treillis.terrain.Triangle;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;


public class ActionCenter {

    private Forme nearest, curentSelect;
    private ArrayList<Forme> multipleSelect = new ArrayList<>();
    private Terrain selectedTerrain;

    private Type barreType = null;

    private double mouseX, mouseY;
    private double dragMouseX, dragMouseY;

    //bouttons de selections : 0 -> select ; 1xx -> noeud; 2xx -> barre;  3xx -> terrain; 4xx -> pointTerrain; 5xx -> segmentTerrain
    //x : 0 -> Noeud Simple; 1 -> Appui double; 2 -> appui simple
    private int selectedButton = 0;

    private MainScene mainScene;
    private Graphics graphics;
    private Stage stage;
    private Options options = new Options();
    private String name;
    private String path;

    private Treillis treillis;

    private int currentClick = 0;
    private Point firstSegmentPoint = null;

    private double terrainX, terrainY;

    private boolean inMultSelect = false, drag = false;

    private boolean inDrawing = true;

    public ActionCenter() {

        graphics = new Graphics();

    }

    //initialisation de la classe
    // impossible de le faire dans son constructeur car cette classe a besoin de "mainscene" qui a besoin de
    // cet "action center" pour etre initialisé
    public void init(MainScene mainScene, Treillis treillis, Stage stage, String path){
        this.mainScene = mainScene;
        this.treillis = treillis;
        this.stage = stage;
        this.path = path;
        this.name = nameFromPath(path);

        GraphicsContext gc = mainScene.getCanvas().getGraphicsContext();
        graphics.init(mainScene, gc, this);
        graphics.updateFormes(treillis);
        graphics.draw(selectedButton, inDrawing);

        addMouseEvent();
        addKeyboardEvent();
    }

    public void reload(String path) throws IOException, ParseException {


        mainScene = new MainScene((int) options.getWidth(), (int) options.getHeight(), this);
        Scene scene = new Scene(mainScene, options.getWidth(), options.getHeight());

        graphics.setMainScene(mainScene);

        if(options.getTheme().equals("light")){
            scene.getStylesheets().add("stylesSheet/lightTheme/lightStyle.css");
        }else{
            scene.getStylesheets().add("stylesSheet/darkTheme/darkStyle.css");
        }

        stage.setScene(scene);
        stage.show();
        if(path.equals("")) name = "~Nouveau~";
        stage.setTitle(name);
        graphics.updateFormes(treillis);
        graphics.draw(selectedButton, inDrawing);

        addMouseEvent();
        addKeyboardEvent();
    }

    public void load(String path) throws IOException, ParseException {
        treillis = Sauvegarde.getTreillis(path);
        this.name = nameFromPath(path);

        graphics.resetFormes();
        reload(path);
    }

    public void newTreillis() throws IOException, ParseException {
        treillis = new Treillis();
        this.name = "";

        graphics.resetFormes();
        reload("");
    }

    public void saveAct(String path)
    {
        Sauvegarde.saveTreillis(treillis, path);
    }

    //ajout des fonctions appelés durant différentes actions de la souris
    private void addMouseEvent() {
        MainCanvas canvas = mainScene.getCanvas();

        canvas.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            switch (selectedButton/10) {
                case 0, 2, 5 -> selection(selectedButton/10);
            }
            graphics.draw(selectedButton, inDrawing);
        });

        canvas.setOnMousePressed(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                inMultSelect = false;
                removeSelectedAll();

                switch (selectedButton/10) {
                    case 0 -> setSelected();
                    case 1 -> addNoeud();
                    case 2 -> addBarre();
                    case 3 -> addTerrain();
                    case 4 -> addPointTrn();
                    case 5 -> addSegmentTrn();
                }
            }
        });

        canvas.setOnMouseReleased(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                dragMouseX = -1;
                dragMouseY = -1;
                drag = false;
                graphics.draw(selectedButton, inDrawing);
            }else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                dragMouseX = -1;
                dragMouseY = -1;

                graphics.updateLastOrigin();
            }
        });

        canvas.setOnMouseDragged(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (selectedButton == 0) {
                    drag = true;
                    inMultSelect = true;
                    dragMouseX = mouseEvent.getX();
                    dragMouseY = mouseEvent.getY();
                    dragSelection();
                    graphics.draw(selectedButton, inDrawing);
                }
            } else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                dragMouseX = mouseEvent.getX();
                dragMouseY = mouseEvent.getY();

                graphics.moveOrigin(mouseX, mouseY, dragMouseX, dragMouseY);
                graphics.draw(selectedButton, inDrawing);
            }
        });

        canvas.setOnScroll(scrollEvent -> {
            System.out.println(scrollEvent.getDeltaY());
            graphics.setScale(graphics.getScale() + scrollEvent.getDeltaY() * scrollEvent.getMultiplierY() / 4000);
            graphics.draw(selectedButton, inDrawing);
        });
    }

    private void addKeyboardEvent() {
        mainScene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ESCAPE){
                currentClick = 0;
                firstSegmentPoint.setSegmentSelected(false);
                removeSelected();
                removeSelectedAll();

                graphics.draw(selectedButton, inDrawing);
            }
        });
    }

    private void addNoeud(){
        if(treillis.getTerrain().contain(mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY())) {
            switch (selectedButton) {
                case 10 -> addNoeudSimple();
                case 11 -> addAppui(false);
                case 12 -> addAppui(true);
                default -> System.out.println(selectedButton);
            }
        }
    }

    private NoeudSimple addNoeudSimple() {
        return addNoeudSimple(mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY());
    }

    private NoeudSimple addNoeudSimple(double posX, double posY) {
        boolean creable = true;
        for (Noeud p : treillis.getNoeuds()) {
            if(Maths.dist(p, new Point(posX, posY)) < 15) creable = false;
        }
        Terrain terrain = treillis.getTerrain();
        if(terrain != null) {
            for (Triangle triangle : terrain.getTriangles()) {
                if (triangle.contain(posX, posY)) creable = false;
            }
        }
        if(creable) {
            NoeudSimple ns = treillis.createNoeudSimple(posX, posY);
            graphics.updateFormes(treillis);
            graphics.draw(selectedButton, inDrawing);
            return ns;
        }
        return null;
    }

    public void addAppui(boolean simple) {
        addAppui(simple, mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY());
    }

    public void addAppui(boolean simple, double posX, double posY) {
        for (SegmentTerrain s : treillis.getTerrain().getSegmentsTerrain()) {
            System.out.println(s.asOneTriangle());
            if(s.contain(posX, posY, 10) && s.asOneTriangle()){
                treillis.createAppui(simple, s.getTriangles().get(0), s, Maths.dist(s.getpA(), posX, posY) / s.length());
                graphics.updateFormes(treillis);
                graphics.draw(selectedButton, inDrawing);
            }
        }
    }

    //fonction permettant la selection d'un point
    private void setSelected(){
        if(selectedTerrain != null){
            selectedTerrain.setSelected(false);
            graphics.removeInfos();
            selectedTerrain = null;
        }

        if (curentSelect != null) {
            curentSelect.setSelected(false);
        }
        if (nearest != null) {
            if(nearest.equals(curentSelect)){
                nearest.setSelected(false);
                curentSelect = null;
                graphics.removeInfos();
            }else {
                nearest.setSelected(true);
                curentSelect = nearest;
                graphics.drawInfos(nearest);
            }
        }else if(treillis.getTerrain() != null){
            curentSelect = null;

            if(treillis.getTerrain().contain(mouseX, mouseY)){
                selectedTerrain = treillis.getTerrain();
                treillis.getTerrain().setSelected(true);
                graphics.drawInfos(selectedTerrain);

            }
        }
    }

    //fonction de création d'une barre
    private void addBarre(){
        currentClick++;
        Noeud p;
        //test si on clique a coté d'un point ou pas
        //Besoin d'ajouter la vérification que le point est créable, et quel type de point
        if(nearest != null && nearest instanceof Noeud){
            p = (Noeud) nearest;
        }else{
            p = addNoeudSimple();
            if(p == null){
                currentClick --;
                return;
            }
        }
        if(currentClick == 1){
            p.setSegmentSelected(true);
            firstSegmentPoint = p;
        }else{
            currentClick = 0;
            treillis.createBarre((Noeud) firstSegmentPoint, p, barreType);
            firstSegmentPoint.setSegmentSelected(false);
            graphics.updateFormes(treillis);
        }
        graphics.draw(selectedButton, inDrawing);
    }

    private void addTerrain() {
        currentClick ++;

        if(currentClick == 1){
            terrainX = mouseX;
            terrainY = mouseY;
        }else{
            currentClick = 0;
            treillis.updateTerrain(Math.min(terrainX, mouseX), Math.min(terrainY, mouseY),
                    Math.max(terrainX,mouseX), Math.max(terrainY, mouseY));

            treillis.updateNoeuds(graphics);
            graphics.updateFormes(treillis);
            redraw();
        }
    }

    public PointTerrain addPointTrn() {
        double px = mouseX - graphics.getOrigin().getPosX(), py = mouseY - graphics.getOrigin().getPosY();
        Terrain terrain = treillis.getTerrain();
        PointTerrain pt = null;

        boolean creable = true;
        for (PointTerrain p : terrain.getPointsTerrain()) {
            if(Maths.dist(p, new Point(px, py)) < 15) creable = false;
        }
        if(creable) pt = terrain.addPoint(px, py);

        graphics.draw(selectedButton, inDrawing);
        return pt;
    }

    public void addSegmentTrn(){
        currentClick++;
        PointTerrain p;
        //test si on clique a coté d'un point ou pas
        //Besoin d'ajouter la vérification que le point est créable, et quel type de point
        if(nearest != null && nearest instanceof PointTerrain){
            p = (PointTerrain) nearest;
        }else{
            p = addPointTrn();
            if(p == null) {
                currentClick --;
                return;
            }
        }
        if(currentClick == 1){
            p.setSegmentSelected(true);
            firstSegmentPoint = p;
        }else{
            currentClick = 0;
            SegmentTerrain segmentTerrain = new SegmentTerrain((PointTerrain) firstSegmentPoint, p);
            ((PointTerrain) firstSegmentPoint).addSegments(segmentTerrain);
            p.addSegments(segmentTerrain);
            treillis.getTerrain().addSegment(segmentTerrain, treillis, this);
            firstSegmentPoint.setSegmentSelected(false);
        }
        graphics.draw(selectedButton, inDrawing);
    }

    public void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;
    }

    public void setInDrawing(boolean inDrawing){
        this.inDrawing = inDrawing;
    }

    //retire le point selectionné
    public void removeSelected() {
        if (curentSelect != null) {
            curentSelect.setSelected(false);
        }
        if(selectedTerrain != null) {
            selectedTerrain.setSelected(false);
        }
        mainScene.getInfos().removeInfos();
        curentSelect = null;
    }

    //retire tout les points selectionnés
    public void removeSelectedAll() {
        multipleSelect.forEach(p -> p.setSelected(false));
        multipleSelect.clear();
        graphics.removeInfos();
    }

    //supprime un element
    public void deleteForme(Forme f){
        if(f != null) {
            if (f instanceof PointTerrain || f instanceof Triangle || f instanceof SegmentTerrain) {
                Terrain terrain = treillis.getTerrain();
                if(terrain != null) {
                    terrain.remove(f, false);
                    graphics.draw(selectedButton, inDrawing);
                }
            } else {
                graphics.remove(f);
                treillis.removeElement(f);
                curentSelect = null;
            }
        }
    }

    //supprime les élements selectionés
    public void deleteAllFormes() {
        multipleSelect.forEach(f -> {
            graphics.remove(f);
            treillis.removeElement(f);
        });

        multipleSelect.clear();
        graphics.removeInfos();
        inMultSelect = false;
        graphics.draw(selectedButton, inDrawing);
    }

    public void deleteZoneConstru(Terrain t){
        if(t != null){
            t.setBorderNull();
        }
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Forme getNearest(){
        return nearest;
    }

    public void selection(int selectedButton){
        //ajoute les formes dans la selection

        ArrayList<Forme> formes = new ArrayList<>();

        formes.addAll(graphics.getFormes());
        Terrain terrain = treillis.getTerrain();
        if(terrain != null){
            formes.addAll(terrain.getTriangles());
            formes.addAll(terrain.getPointsTerrain());
            formes.addAll(terrain.getSegmentsTerrain());
        }

        double bestDist = 15;

        for (Forme f: formes) {
            Point p;
            if(f instanceof Noeud && (selectedButton == 2 || selectedButton == 0)) {
                p = (Point) f;
            }else if(f instanceof PointTerrain && (selectedButton == 5 || selectedButton == 0)){
                p = (Point) f;
            }else if(selectedButton == 0){
                if(f instanceof Segment){
                    p = ((Segment) f).getCenter();
                }else if(f instanceof Triangle){
                    p = ((Triangle) f).getCenter();
                }else {
                    return;
                }
            }else{
                continue;
            }

            //trouve la forme le plus proche de la souris
            double dist = Maths.dist(p, new Point(mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY()));
            if(dist < bestDist){
                nearest = f;
                bestDist = dist;
            }
            if(bestDist == 15) {
                nearest = null;
            }
        }

    }

    private void dragSelection() {
        ArrayList<Forme> formes = graphics.getFormes();

        for (Forme f: formes) {
            Point p;
            if(f instanceof Point) {
                p = (Point) f;
            }else{
                p = ((Segment) f).getCenter();
            }

            if (drag) {
                if (p.getPosX() < Math.max(mouseX, dragMouseX) && p.getPosX() > Math.min(mouseX, dragMouseX) &&
                        p.getPosY() < Math.max(mouseY, dragMouseY) && p.getPosY() > Math.min(mouseY, dragMouseY)) {
                    if (!multipleSelect.contains(f)) {
                        multipleSelect.add(f);
                        f.setSelected(true);
                    }
                } else {
                    multipleSelect.remove(f);
                    f.setSelected(false);
                }
            }
        }
    }

    public void redraw() {
        graphics.draw(selectedButton, inDrawing);
    }

    public boolean isDrag() {
        return drag;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getDragMouseX() {
        return dragMouseX;
    }

    public double getDragMouseY() {
        return dragMouseY;
    }

    public int getSelectedButton() {
        return selectedButton;
    }

    public String getName() {
        return name;
    }

    public double getTerrainX() {
        return terrainX;
    }

    public double getTerrainY() {
        return terrainY;
    }

    public int getCurrentClick() {
        return currentClick;
    }

    public Treillis getTreillis() {
        return treillis;
    }

    public Stage getStage() {
        return stage;
    }

    public String getPath() {
        return path;
    }

    public void setBarreType(Type barreType) {
        this.barreType = barreType;
    }

    public boolean isInDrawing() {
        return inDrawing;
    }

    public boolean isInMultSelect() {
        return inMultSelect;
    }

    public ArrayList<Forme> getMultipleSelect() {
        return multipleSelect;
    }

    public static String nameFromPath(String path){
        String[] nameS = path.split("\\\\");
        return nameS[nameS.length - 1].split("\\.")[0];
    }
}
