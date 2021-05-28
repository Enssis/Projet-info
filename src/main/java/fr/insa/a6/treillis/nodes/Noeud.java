package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Force;
import fr.insa.a6.treillis.dessin.Point;
import java.util.ArrayList;


public abstract class Noeud extends Point {

    protected Force forceApplique;
    private ArrayList<Barres> linkedBarres;

    public Noeud(int id, Force forceApplique, ArrayList<Barres> linkedBarres) {
        this.id = id;
        this.forceApplique = forceApplique;
        this.linkedBarres= linkedBarres;
    }

    public Noeud (){

    }

    public Noeud(double posX, double posY, int id){
        this.posX = posX;
        this.posY = posY;
        this.id = id;
    }

    public void addBarres(Barres barre){
        linkedBarres.add(barre);
    }

    public void removeBarres (Barres barre){
        linkedBarres.remove(barre);
    }

    public abstract String saveString();

    public void setForceApplique(Force forceApplique) {
        this.forceApplique = forceApplique;
    }
}