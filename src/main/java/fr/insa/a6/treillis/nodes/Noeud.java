package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Force;
import fr.insa.a6.treillis.dessin.Point;
import java.util.ArrayList;


public abstract class Noeud extends Point {

    protected Force forceApplique;

    private final ArrayList<Barres> linkedBarres = new ArrayList<>();

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

    public Force getForceApplique() {
        if(forceApplique == null){
            return new Force();
        }
        return forceApplique;
    }

    public ArrayList<Barres> getLinkedBarres() {
        return linkedBarres;
    }
}