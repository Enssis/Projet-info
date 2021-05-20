package fr.insa.a6.treillis;

import fr.insa.a6.graphic.Graphics;
import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.nodes.*;
import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.treillis.terrain.Triangle;
import fr.insa.a6.utilities.Numerateur;

import java.util.ArrayList;


public class Treillis {

    private Terrain terrain;
    private final ArrayList<Noeud> noeuds;
    private final ArrayList<Barres> barres;
    private final ArrayList<Type> catalogue;
    private final Numerateur numerateur;


    public Treillis() {
        numerateur = new Numerateur();
        noeuds = new ArrayList<>();
        barres = new ArrayList<>();
        catalogue = new ArrayList<>();
        terrain = new Terrain();
    }

    public Treillis(Terrain terrain, ArrayList<Noeud> noeuds, ArrayList<Barres> barres, ArrayList<Type> catalogue, Numerateur numerateur) {
        this.terrain = terrain;
        this.noeuds = noeuds;
        this.barres = barres;
        this.numerateur = numerateur;
        this.catalogue = catalogue;
    }

    public void addBarres(Barres barre){
        barres.add(barre);
    }
    
    public void removeBarres (Barres barre){
        barres.remove(barre);
    }
    
    public void addNoeuds(Noeud noeud){
        noeuds.add(noeud);
    }
    
    public void removeNoeuds (Noeud noeud){
        noeuds.remove(noeud);
    }

    public void setTerrain(Terrain terrain){
        this.terrain = terrain;
    }

    public ArrayList<Noeud> getNoeuds() {
        return noeuds;
    }

    public ArrayList<Barres> getBarres() {
        return barres;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Numerateur getNumerateur() {
        return numerateur;
    }

    public ArrayList<Type> getCatalogue() {
        return catalogue;
    }

    public NoeudSimple createNoeudSimple(double posX, double posY) {
        NoeudSimple node = new NoeudSimple(posX, posY, numerateur.getNewNoeudId());
        noeuds.add(node);
        return node;
    }

    public Appui createAppui(boolean simple, Triangle associatedTriangle, SegmentTerrain segmentTerrain, double posSegment){
        Appui appui;
        if(simple){
            appui =  new AppuiSimple(associatedTriangle, segmentTerrain, posSegment, numerateur.getNewNoeudId());
        }else{
            appui = new AppuiDouble(associatedTriangle, segmentTerrain, posSegment, numerateur.getNewNoeudId());
        }
        noeuds.add(appui);
        return appui;
    }

    public void addType(Type type){
        catalogue.add(type);
    }

    public void updateTerrain(double x1, double y1, double x2, double y2){
        this.terrain.setBorder(x1,y1, x2, y2);
    }

    public void createBarre(Noeud pA, Noeud pB, Type type){
        if(type == null) {
            System.err.println("TYPE NULL");
            return;
        }
        Barres b = new Barres(pA, pB, type, numerateur.getNewBarreId());
        barres.add(b);
    }

    public void removeElement(Forme f){
        if(f instanceof Noeud) removeNoeuds((Noeud) f);
        else if(f instanceof Barres) removeBarres((Barres) f);
    }

    public void updateNoeuds(Graphics graphics){
        ArrayList<Noeud> toRemove = new ArrayList<>();
        for (Noeud noeud : noeuds) {
            if(!terrain.contain(noeud.getPosX(), noeud.getPosY())){
                toRemove.add(noeud);
                continue;
            }
            if(noeud instanceof NoeudSimple) {
                for (Triangle triangle : terrain.getTriangles()) {
                    if (triangle.contain(noeud.getPosX(), noeud.getPosY())) {
                        toRemove.add(noeud);
                        break;
                    }
                }
            }else{
                if(!((Appui) noeud).getSegmentTerrain().asOneTriangle()){
                    toRemove.add(noeud);
                }
            }
        }
        for (Noeud noeud : toRemove) {
            noeuds.remove(noeud);
            graphics.remove(noeud);
        }
    }
}