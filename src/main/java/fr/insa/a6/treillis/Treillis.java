package fr.insa.a6.treillis;

import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.nodes.NoeudSimple;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.utilities.Numerateur;

import java.util.ArrayList;


/**
 * 
 */
public class Treillis {

    private final ArrayList<Terrain> terrains = new ArrayList<>();
    private final ArrayList<Noeud> noeuds = new ArrayList<>();
    private final ArrayList<Barres> barres = new ArrayList<>();
    private Type[] catalogueType;
    private final Numerateur numerateur;

    /**
     * Default constructor
     */
    public Treillis() {
        numerateur = new Numerateur();
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

    public ArrayList<Noeud> getNoeuds() {
        return noeuds;
    }

    public ArrayList<Barres> getBarres() {
        return barres;
    }

    public Numerateur getNumerateur() {
        return numerateur;
    }

    public NoeudSimple createNoeudSimple(double posX, double posY) {
        NoeudSimple node = new NoeudSimple(posX, posY, numerateur.getNewId());
        noeuds.add(node);
        return node;
    }

    public Barres createBarre(Noeud pA, Noeud pB){
        Barres b = new Barres(pA, pB, numerateur.getNewId());
        barres.add(b);
        return b;
    }

    public void removeElement(Forme f){
        if(f instanceof Noeud) removeNoeuds((Noeud) f);
        else if(f instanceof Barres) removeBarres((Barres) f);
    }
}