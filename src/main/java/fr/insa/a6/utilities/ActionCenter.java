package fr.insa.a6.utilities;

import fr.insa.a6.graphic.Graphics;
import fr.insa.a6.graphic.mainbox.MainCanvas;
import fr.insa.a6.graphic.mainbox.MainScene;
import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.Type;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.dessin.Point;
import fr.insa.a6.treillis.dessin.Segment;
import fr.insa.a6.treillis.nodes.Appui;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.nodes.NoeudSimple;
import fr.insa.a6.treillis.terrain.PointTerrain;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.treillis.terrain.Triangle;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.ArrayList;


public class ActionCenter {

    private Forme nearest, currentSelect;
    private final ArrayList<Forme> multipleSelect = new ArrayList<>();
    private Terrain terrain;

    private Type barreType = null;

    private double mouseX, mouseY;
    private double dragMouseX, dragMouseY;

    //bouttons de selections : 0 -> select ; 1x -> noeud; 2x -> barre;  3x -> terrain; 4x -> triangle terrain
    //x : 0 -> Noeud Simple; 1 -> Appui double; 2 -> appui simple
    private int selectedButton = 0;

    private MainScene mainScene;
    private final Graphics graphics;
    private Stage stage;
    private final Options options = new Options();
    private String name;
    private String path;

    private Treillis treillis;

    private int currentClick = 0;
    private Point firstSegmentPoint = null, secondSegmentPoint = null;

    private double terrainX, terrainY;

    private boolean inMultSelect = false, drag = false;

    private boolean inDrawing = true;

    //1 m => 50 px
    private double echelle = 50;

    public ActionCenter(Treillis treillis) {

        graphics = new Graphics();
        this.treillis = treillis;

    }

    //initialisation de la classe
    // impossible de le faire dans son constructeur car cette classe a besoin de "mainscene" qui a besoin de
    // cet "action center" pour etre initialisé
    public void init(MainScene mainScene, Stage stage, String path){
        this.mainScene = mainScene;
        this.stage = stage;
        this.path = path;
        this.name = nameFromPath(path);
        this.terrain = treillis.getTerrain();

        GraphicsContext gc = mainScene.getCanvas().getGraphicsContext();
        graphics.init(mainScene, gc, this);
        graphics.updateFormes(treillis);
        graphics.draw(selectedButton, inDrawing);

        addMouseEvent();
        addKeyboardEvent();
    }

    public void reload(String path) {

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

    public void load(String path){
        treillis = Save.getTreillis(path);
        this.name = nameFromPath(path);
        this.terrain = treillis.getTerrain();

        graphics.resetFormes();
        reload(path);
    }

    //crée une nouvelle page avec un nouveau treillis
    public void newTreillis() {
        treillis = new Treillis();
        this.name = "";

        graphics.resetFormes();
        reload("");
    }

    //sauvegarde le treillis actuel
    public void saveAct(String path)
    {
        Save.saveTreillis(treillis, path);
    }

    //ajout des fonctions appelés durant différentes actions de la souris
    private void addMouseEvent() {
        MainCanvas canvas = mainScene.getCanvas();

        //actions quand la couris est déplacé dans le canvas
        canvas.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            if(inDrawing && (selectedButton/10) % 2 == 0){
                selection(selectedButton/10);
            }
            graphics.draw(selectedButton, inDrawing);
        });

        //action quand on clic sur la souris
        canvas.setOnMousePressed(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();

            if(mouseEvent.getButton() == MouseButton.PRIMARY && inDrawing) {
                inMultSelect = false;
                removeSelectedAll();

                switch (selectedButton/10) {
                    case 0 -> setSelected();
                    case 1 -> addNoeud();
                    case 2 -> addBarre();
                    case 3 -> addTerrain();
                    case 4 -> addTriangleTrn();
                }
            }
        });

        //action quand on arrete de cliquer sur la souris
        canvas.setOnMouseReleased(mouseEvent -> {
            dragMouseX = -1;
            dragMouseY = -1;
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                drag = false;
                graphics.draw(selectedButton, inDrawing);
            }else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                graphics.updateLastOrigin();
            }
        });

        canvas.setOnMouseDragged(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY && inDrawing) {
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

    }

    private void addKeyboardEvent() {
        mainScene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ESCAPE){
                cancelButton();
            }else if(key.getCode() == KeyCode.DELETE){
                deleteForme(currentSelect);
                deleteAllFormes();
            }
        });
    }

    //fonction s'activant quand on appui sur la touche 'echap'
    public void cancelButton(){
        currentClick = 0;
        if(firstSegmentPoint != null)
        firstSegmentPoint.setSegmentSelected(false);
        if(secondSegmentPoint != null)
        secondSegmentPoint.setSegmentSelected(false);
        removeSelected();
        removeSelectedAll();
        terrain.update();

        graphics.draw(selectedButton, inDrawing);
    }

    private void addNoeud(){
        if(terrain.contain(mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY())) {
            switch (selectedButton) {
                case 10 -> addNoeudSimple();
                case 11 -> addAppui(false);
                case 12 -> addAppui(true);
                default -> System.out.println(selectedButton);
            }
        }
    }

    //fonction de creation de noeud pour les barres
    //construit par defaut un noeud simple ou sinon un appui simple s'ilne ne peut
    public Noeud createNoeudBarre(){
        Noeud noeudRes = null;
        double posX = mouseX - graphics.getOrigin().getPosX(), posY = mouseY - graphics.getOrigin().getPosY();
        if(terrain.contain(posX, posY)) {
            noeudRes = addNoeudSimple();
            if(noeudRes == null){
                noeudRes = testAppui(true, posX, posY);
            }
        }
        return noeudRes;
    }

    private NoeudSimple addNoeudSimple() {
        return addNoeudSimple(mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY());
    }

    private NoeudSimple addNoeudSimple(double posX, double posY) {
        if(NoeudSimple.isCreable(treillis, posX, posY)) {
            NoeudSimple ns = treillis.createNoeudSimple(posX, posY);
            graphics.updateFormes(treillis);
            graphics.draw(selectedButton, inDrawing);
            return ns;
        }
        return null;
    }

    public void addAppui(boolean simple) {
        testAppui(simple, mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY());
    }

    //test si il est possible de creer un appui, et si oui alors il le crée
    public Appui testAppui(boolean simple, double posX, double posY){
        SegmentTerrain segment = Appui.isCreable(terrain, posX, posY);
        if(segment != null) {
            return createAppui(simple, posX, posY, segment);
        }
        return null;
    }

    public Appui createAppui(boolean simple, double posX, double posY, SegmentTerrain segment) {
            Appui appui = treillis.createAppui(simple, segment.getTriangles().get(0), segment, Maths.dist(segment.getpA(), posX, posY) / segment.length());
            graphics.updateFormes(treillis);
            graphics.draw(selectedButton, inDrawing);
            return appui;
    }

    //fonction permettant la selection d'un point
    private void setSelected(){
        if(terrain.isSelected()){
            terrain.setSelected(false);
            graphics.removeInfos();
        }

        if (currentSelect != null) {
            currentSelect.setSelected(false);
        }
        if (nearest != null) {
            if(nearest.equals(currentSelect)){
                nearest.setSelected(false);
                currentSelect = null;
                graphics.removeInfos();
            }else {
                nearest.setSelected(true);
                currentSelect = nearest;
                graphics.drawInfos(nearest);
            }
        }else if(terrain != null){
            currentSelect = null;

            if(terrain.contain(mouseX, mouseY)){
                terrain.setSelected(true);
                graphics.drawInfos(terrain);
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
            p = createNoeudBarre();
            if(p == null){
                currentClick --;
                return;
            }
        }
        if(currentClick == 1){
            p.setSegmentSelected(true);
            firstSegmentPoint = p;
        }else{
            if(firstSegmentPoint.equals(p)){
                currentClick--;
                return;
            }
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

    public void drawCalculInfo(){
        mainScene.getInfos().drawCalculInfo();
    }

    public PointTerrain addPointTrn() {
        double px = mouseX - graphics.getOrigin().getPosX(), py = mouseY - graphics.getOrigin().getPosY();
        PointTerrain pt = null;

        boolean creable = true;
        for (PointTerrain p : terrain.getPoints()) {
            if(Maths.dist(p, new Point(px, py)) < 15) creable = false;
        }
        if(creable) pt = terrain.addPoint(px, py);

        graphics.draw(selectedButton, inDrawing);
        return pt;
    }

    public void addTriangleTrn(){
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
        }else if(currentClick == 2){
            if(firstSegmentPoint.equals(p)){
                currentClick--;
                return;
            }
            p.setSegmentSelected(true);
            secondSegmentPoint = p;
        }else{
            if(secondSegmentPoint.equals(p) || firstSegmentPoint.equals(p)){
                currentClick--;
                return;
            }
            currentClick = 0;
            Triangle triangle = new Triangle((PointTerrain) firstSegmentPoint, (PointTerrain) secondSegmentPoint, p, treillis.getNumerateur().getNewTriangleId(), terrain);
            System.out.println(triangle.getId());
            terrain.addTriangle(triangle);

            treillis.updateNoeuds(graphics);
            firstSegmentPoint.setSegmentSelected(false);
            secondSegmentPoint.setSegmentSelected(false);
        }
        redraw();
    }

    public void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;
    }

    public void setInDrawing(boolean inDrawing){
        this.inDrawing = inDrawing;
    }

    //retire le point selectionné
    public void removeSelected() {
        if (currentSelect != null) {
            currentSelect.setSelected(false);
        }
        if(terrain != null) {
            terrain.setSelected(false);
        }
        mainScene.getInfos().removeInfos();
        currentSelect = null;
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
                if(terrain != null) {
                    terrain.remove(f, false);
                    graphics.draw(selectedButton, inDrawing);
                }
            } else {
                graphics.remove(f);
                treillis.removeElement(f);
                currentSelect = null;
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

    //calcul le prix final du treillis
    public double getCost(){
        double price = 0;
        for (Barres barres : treillis.getBarres()) {
            if(barres.getType() == null) continue;
            price += barres.getType().getCout() * barres.length() / echelle;
        }
        return (double) ((int) price * 100) / 100;
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
            formes.addAll(terrain.getPoints());
            formes.addAll(terrain.getSegments());
        }

        double bestDist = 15;

        for (Forme f: formes) {
            Point p;
            if(f instanceof Noeud && (selectedButton == 2 || selectedButton == 0)) {
                p = (Point) f;
            }else if(f instanceof PointTerrain && (selectedButton == 4 || selectedButton == 0)){
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

    public Point getFirstSegmentPoint() {
        return firstSegmentPoint;
    }

    public Point getSecondSegmentPoint() {
        return secondSegmentPoint;
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

    public double getEchelle() {
        return echelle;
    }
}
