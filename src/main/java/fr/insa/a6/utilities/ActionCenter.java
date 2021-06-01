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
import fr.insa.a6.treillis.nodes.*;
import fr.insa.a6.treillis.terrain.PointTerrain;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.treillis.terrain.Triangle;
import fr.insa.a6.utilities.systemeLineaire.Matrice;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.ArrayList;
import javafx.scene.control.Alert;
import java.util.HashMap;



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
    private String name;
    private String path;

    private Treillis treillis;

    private final HashMap<Integer, double[]> formesRes = new HashMap<>();
    private final HashMap<Forme, Integer> formeId = new HashMap<>();

    private int currentClick = 0;
    private Point firstSegmentPoint = null, secondSegmentPoint = null;

    private double terrainX, terrainY;

    private boolean inMultSelect = false, drag = false;

    private boolean inDrawing = true;

    private final HostServices hostServices;

    //1 m => 50 px
    private final double echelle = 50;



    public ActionCenter(Treillis treillis, HostServices hostServices) {
        graphics = new Graphics();
        this.treillis = treillis;
        this.hostServices = hostServices;
    }

    //initialisation de la classe
    // impossible de le faire dans son constructeur car cette classe a besoin de "mainscene" qui a besoin de
    // cet "action center" pour etre initialisÃ©
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

    //recharge la fenetre sans changer le treillis mais en changeant le style / la langue ...
    public void reload(String path) {

        Options options = new Options();

        mainScene = new MainScene((int) options.getWidth(), (int) options.getHeight(), this);
        Scene scene = new Scene(mainScene, options.getWidth(), options.getHeight());

        inDrawing = true;
        graphics.setMainScene(mainScene);

        graphics.resetOrigin();

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
        this.path = path;

        addMouseEvent();
        addKeyboardEvent();
    }

    //charge un treillis
    public void load(String path){
        treillis = Save.getTreillis(path);
        this.name = nameFromPath(path);
        this.terrain = treillis.getTerrain();

        graphics.resetFormes();
        reload(path);
    }

    //crÃ©e une nouvelle page avec un nouveau treillis
    public void newTreillis() {
        treillis = new Treillis();
        this.terrain = treillis.getTerrain();
        this.name = "";

        graphics.resetFormes();
        reload("");
    }

    //sauvegarde le treillis actuel
    public void saveAct(String path)
    {
        Save.saveTreillis(treillis, path);
    }

    //ajout des fonctions appelÃ©s durant diffÃ©rentes actions de la souris
    private void addMouseEvent() {
        MainCanvas canvas = mainScene.getCanvas();

        //actions quand la couris est dÃ©placÃ© dans le canvas
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
                    case 3 -> addZoneConstructible();
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

    //ajout des fonctions appelÃ©s durant diffÃ©rentes actions du clavier
    private void addKeyboardEvent() {
        mainScene.setOnKeyPressed(key -> {
            switch (key.getCode()) {
                case ESCAPE -> cancelButton();
                case DELETE -> {
                    deleteForme(currentSelect);
                    deleteAllFormes();
                }
                case N -> mainScene.getIcons().setSelected(10);
                case B -> mainScene.getIcons().setSelected(20);
                case A -> mainScene.getIcons().setSelected(12);
                case D -> mainScene.getIcons().setSelected(11);
                case S -> mainScene.getIcons().setSelected(0);
                case Z -> mainScene.getIcons().setSelected(30);
                case T -> mainScene.getIcons().setSelected(40);
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

    //appele la bonne fonction d'ajout du noeud selon le type choisi
    private void addNoeud(){
        if(terrain.contain(mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY())) {
            switch (selectedButton) {
                case 10 -> addNoeudSimple();
                case 11 -> addAppui(false);
                case 12 -> addAppui(true);
                default -> System.out.println(selectedButton);
            }

        }else{

            Alert alerteZoneConstructible = new Alert(Alert.AlertType.ERROR);
            alerteZoneConstructible.setTitle("Erreur");
            alerteZoneConstructible.setContentText("Noeud hors zone constructible!");
            alerteZoneConstructible.showAndWait();

        }

    }

    //fonction de creation de noeud pour les barres
    //construit par defaut un noeud simple ou sinon un appui simple s'il ne peut pas
    public Noeud createNoeudBarre(boolean distSup, Point firstSegmentPoint){
        Noeud noeudRes = null;
        double posX, posY;
        if(distSup){
            double angle = Maths.angle(firstSegmentPoint, (new Point(mouseX, mouseY)).substract(graphics.getOrigin()));
            posX = firstSegmentPoint.getPosX() + barreType.getlMax() * echelle * Math.cos(angle);
            posY = firstSegmentPoint.getPosY() + barreType.getlMax() * echelle * Math.sin(angle);
        }else {
            posX = mouseX - graphics.getOrigin().getPosX();
            posY = mouseY - graphics.getOrigin().getPosY();
        }
        if (!terrain.contain(posX, posY)){
            Alert alerteZoneConstructible = new Alert(Alert.AlertType.WARNING);
            alerteZoneConstructible.setTitle("Erreur");
            alerteZoneConstructible.setHeaderText("CREATION BARRE IMPOSSIBLE");
            alerteZoneConstructible.setContentText("Point hors zone constructible!");
            alerteZoneConstructible.showAndWait(); 
        }
        if(terrain.contain(posX, posY)) {
            noeudRes = addNoeudSimple(posX, posY);
            if(noeudRes == null){
                noeudRes = testAppui(true, posX, posY);
            }

        }else{
            Alert alerteZoneConstructible = new Alert(Alert.AlertType.WARNING);
            alerteZoneConstructible.setTitle("Erreur");
            alerteZoneConstructible.setHeaderText("CREATION BARRE IMPOSSIBLE");
            alerteZoneConstructible.setContentText("Point hors zone constructible!");
            alerteZoneConstructible.showAndWait();
        }
        return noeudRes;

    }

    //fonctions d'ajout de noeuds simple
    private void addNoeudSimple() {
        addNoeudSimple(mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY());
    }

    private NoeudSimple addNoeudSimple(double posX, double posY) {

        boolean distCreable = NoeudSimple.isDistCreable(treillis, posX, posY);
        boolean triangleCreable = NoeudSimple.isTriangleCreable(treillis, posX, posY);
        if(distCreable && triangleCreable) {

            NoeudSimple ns = treillis.createNoeudSimple(posX, posY);
            graphics.updateFormes(treillis);
            graphics.draw(selectedButton, inDrawing);
            return ns;
        }


        String textError = "";

        if(!distCreable){
            textError = "Noeuds trop proches!";
        }
        if(!triangleCreable){
            if(textError.length() > 0) textError += " et ";
            textError += "Noeud simple compris dans un triangle terrain!";
        }

        Alert alerteTriangleTerrain = new Alert(Alert.AlertType.WARNING);
        alerteTriangleTerrain.setTitle("Erreur création noeud");
        alerteTriangleTerrain.setContentText(textError);
        alerteTriangleTerrain.showAndWait();


        return null;
    }

    //fonctions d'ajout d'un appui
    public void addAppui(boolean simple) {
        testAppui(simple, mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY());
    }

    //test si il est possible de creer un appui, et si oui alors il le crÃ©e
    public Appui testAppui(boolean simple, double posX, double posY){
        SegmentTerrain segment = Appui.isCreable(terrain, posX, posY);
        if(segment != null) {
            return createAppui(simple, posX, posY, segment);
        }else {
            Alert alerteNoeudAppui = new Alert(Alert.AlertType.WARNING);
            alerteNoeudAppui.setTitle("Erreur création noeud");
            alerteNoeudAppui.setContentText("Noeud non positionné sur un segment de terrain!");
            alerteNoeudAppui.showAndWait();
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
        }
    }

    //fonction de crÃ©ation d'une barre
    private void addBarre(){
        currentClick++;
        Noeud p = null;
        //test si on clique a cotÃ© d'un point ou pas
        //Besoin d'ajouter la vÃ©rification que le point est crÃ©able, et quel type de point
        if(barreType == null){
            System.err.println("TYPE NULL");
            Alert alerteTypeNull = new Alert(Alert.AlertType.INFORMATION);
            alerteTypeNull.setTitle("");
            alerteTypeNull.setHeaderText("CREATION BARRE IMPOSSIBLE");
            alerteTypeNull.setContentText("Type nul!");
            alerteTypeNull.showAndWait();
            currentClick --;
            return;
        }

        double lMin = barreType.getlMin() * echelle;
        double lMax = barreType.getlMax() * echelle;

        if(nearest != null && nearest instanceof Noeud){
            boolean creable = true;
            if(currentClick > 1 && firstSegmentPoint != null) {
                double dist = Maths.dist((Point) nearest, firstSegmentPoint);
                if(dist < lMin || dist > lMax){
                    creable = false;
                }
            }
            if(creable) p = (Noeud) nearest;
        }
        if(p == null){
            double dist = 0;
            if(currentClick > 1 && firstSegmentPoint != null) {
                dist = Maths.dist(firstSegmentPoint, (new Point(mouseX, mouseY)).substract(graphics.getOrigin()));
            }
            if(dist >= lMin || currentClick == 1){
                p = createNoeudBarre(dist > lMax, firstSegmentPoint);
            }
            if(p == null){
                currentClick --;
                return;
            }
        }
          if(currentClick == 1){
            p.setSegmentSelected(true);
            firstSegmentPoint = p;
        }else{
            assert firstSegmentPoint != null;
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

    //met en place la zone constructible
    private void addZoneConstructible() {
        currentClick ++;

        if(currentClick == 1){
            terrainX = mouseX - graphics.getOrigin().getPosX();
            terrainY = mouseY - graphics.getOrigin().getPosY();
        }else{
            currentClick = 0;
            treillis.updateTerrain(Math.min(terrainX, mouseX - graphics.getOrigin().getPosX()), Math.min(terrainY, mouseY- graphics.getOrigin().getPosY()),
                    Math.max(terrainX,mouseX - graphics.getOrigin().getPosX()), Math.max(terrainY, mouseY- graphics.getOrigin().getPosY()));

            treillis.updateNoeuds(graphics);
            graphics.updateFormes(treillis);
            redraw();
        }
    }

    //ecrit les infos liÃ© au calcul
    public void writeCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        mainScene.getInfos().drawCalculInfo(formeId, idValues);
    }

    //fonction de creation des points composant un triangle
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

    //fonction de creation de triangles
    public void addTriangleTrn(){
        currentClick++;
        PointTerrain p;
        //test si on clique a cotÃ© d'un point ou pas
        //Besoin d'ajouter la vÃ©rification que le point est crÃ©able, et quel type de point
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
            terrain.addTriangle(triangle);

            treillis.updateNoeuds(graphics);
            firstSegmentPoint.setSegmentSelected(false);
            secondSegmentPoint.setSegmentSelected(false);
        }
        redraw();
    }


    public void calculs(){
        int id = 0;
        int ns = treillis.getNoeuds().size(), nb = treillis.getBarres().size(), nas = 0, nad = 0;
        //liste les formes et les associes Ã  un identifiant
        formeId.clear();
        HashMap<Integer, Forme> idForme = new HashMap<>();
        for (Barres barre : treillis.getBarres()) {
            formeId.put(barre, id);
            idForme.put(id, barre);
            id ++;
        }
        for (Noeud noeud : treillis.getNoeuds()) {
            if(noeud instanceof AppuiSimple){
                formeId.put(noeud, id);
                idForme.put(id, noeud);
                id ++;
                nas ++;
            }else if(noeud instanceof AppuiDouble){
                formeId.put(noeud, id);
                idForme.put(id, noeud);
                id += 2;
                nad ++;
            }
            
        }

        System.out.println(2 * ns + " " + nb + nas + 2 * nad);
        if(2 * ns != nb + nas + 2 * nad) {
            Alert alerteHyperstatique = new Alert(Alert.AlertType.ERROR);
            alerteHyperstatique.setTitle("Erreur calcul");
            alerteHyperstatique.setContentText("Le treillis n'est pas isostatique!");
            alerteHyperstatique.showAndWait();
            inDrawing = true;
            throw new Error("treillis hyperstatique");
        }
        Matrice systeme = fillMatrice(formeId, id);
        System.out.println(systeme);

        Matrice res = systeme.resolution();
        System.out.println(systeme.resolution());
        if(res == null) {
            inDrawing = true;
            throw new Error("Matrice non inversible");
        }
        res = res.subCol(systeme.getNbrCol() - 1, systeme.getNbrCol() - 1);
        System.out.println(res);

        formesRes.clear();
        for (int value : formeId.values()) {
            if(idForme.get(value) instanceof AppuiDouble){
                formesRes.put(value, new double[]{res.get(value, 0), res.get(value + 1, 0)});
            }else{
                formesRes.put(value, new double[]{res.get(value, 0)});
            }
        }
        System.out.println(formesRes);

        writeCalculInfo(formeId, formesRes);
    }

    public Matrice fillMatrice(HashMap<Forme, Integer> formeToId, int lastId){

        /*
        double[][] coeffs = {
                {0.707, 0, 0, 1, 0, 0},
                {-0.707, 0, -1, 0, 1, 0},
                {0, 0.707, 0, 0, 0, 1},
                {0, 0.707, 1, 0, 0, 0},
                {-0.707, -0.707, 0, 0, 0, 0},
                {0.707, -0.707, 0, 0, 0, 0}
        };

        Matrice systeme = new Matrice(6, 6, coeffs);
        double[] vecResult = {0, 0, 0, 0, 0, 1000};
        */
        int size = treillis.getNoeuds().size() * 2;
        Matrice systeme = new Matrice(size, lastId);
        double[] vecResult = new double[size];

        //rempli la matrice
        for (int i = 0; i < treillis.getNoeuds().size() * 2; i += 2) {
            Noeud noeud = treillis.getNoeuds().get(i/2);
            System.out.println(noeud);
            //ajout des valeurs liÃ©es aux barres
            for (Barres barre : noeud.getLinkedBarres()) {
                int col = formeToId.get(barre);
                double angle;
                if(noeud == barre.getpA()){
                    angle = Maths.angle(noeud, barre.getpB());
                }else if(noeud == barre.getpB()){
                    angle = Maths.angle(noeud, barre.getpA());
                }else{
                    angle = 0;
                    System.err.println("euuuuh");
                }
                systeme.set(i, col, Math.cos(angle));
                systeme.set(i + 1, col, Math.sin(angle));
            }
            //ajout des forces
            vecResult[i] = - noeud.getForceApplique().getfX();
            vecResult[i + 1] = - noeud.getForceApplique().getfY();


            if(noeud instanceof AppuiSimple){
                int col = formeToId.get(noeud);
                double angle = Maths.angle(((AppuiSimple) noeud).getSegmentTerrain().getpA(), ((AppuiSimple) noeud).getSegmentTerrain().getpB());
                systeme.set(i, col, Math.cos(Math.PI /2 + angle));
                systeme.set(i + 1, col, Math.sin(angle));

            }else if(noeud instanceof AppuiDouble){
                int col = formeToId.get(noeud);
                systeme.set(i, col, 1);
                systeme.set(i + 1, col + 1, 1);
            }

        }

        System.out.println(formeToId);
        return Matrice.concatCol(systeme, Matrice.creeVecteur(vecResult));
    }

    //retire le point selectionnÃ©
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

    //retire tout les points selectionnÃ©s
    public void removeSelectedAll() {
        multipleSelect.forEach(p -> p.setSelected(false));
        multipleSelect.clear();
        graphics.removeInfos();
    }

    //fonctions de suppressions :

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

    //supprime les Ã©lements selectionÃ©s
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
                Point origin = graphics.getOrigin();
                if (p.getPosX() + origin.getPosX() < Math.max(mouseX, dragMouseX) && p.getPosX() + origin.getPosX()> Math.min(mouseX, dragMouseX) &&
                        p.getPosY() + origin.getPosY() < Math.max(mouseY, dragMouseY) && p.getPosY() + origin.getPosY() > Math.min(mouseY, dragMouseY)) {
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

    public Type getBarreType() {
        return barreType;
    }

    public HashMap<Integer, double[]> getResultCalcul() {
        return formesRes;
    }

    public HashMap<Forme, Integer> getFormeId() {
        return formeId;
    }

    public void setSelectedButton(int selectedButton) {
        this.selectedButton = selectedButton;
    }

    public void setInDrawing(boolean inDrawing){
        this.inDrawing = inDrawing;
    }

    public void setBarreType(Type barreType) {
        this.barreType = barreType;
    }

    public HostServices getHostServices() {
        return hostServices;
    }
}
