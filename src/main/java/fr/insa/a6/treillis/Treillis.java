package fr.insa.a6.treillis;

import fr.insa.a6.treillis.dessin.Forme;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.nodes.NoeudSimple;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.utilities.Numerateur;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 
 */
public class Treillis {

    private final ArrayList<Terrain> terrains;
    private final HashMap<Integer, Noeud> noeuds;
    private final ArrayList<Barres> barres;
    private Type[] catalogueType;
    private final Numerateur numerateur;

    /**
     * Default constructor
     */
    public Treillis() {
        numerateur = new Numerateur();
        terrains = new ArrayList<>();
        noeuds = new HashMap<>();
        barres = new ArrayList<>();
    }

    public Treillis(ArrayList<Terrain> terrains, HashMap<Integer, Noeud> noeuds, ArrayList<Barres> barres, Numerateur numerateur) {
        this.terrains = terrains;
        this.noeuds = noeuds;
        this.barres = barres;
        this.numerateur = numerateur;
    }
    
    
    public void addBarres(Barres barre){
        barres.add(barre);
    }
    
    public void removeBarres (Barres barre){
        barres.remove(barre);
    }
    
    public void addNoeuds(Noeud noeud){
        noeuds.put(noeud.getId(), noeud);
    }
    
    public void removeNoeuds (Noeud noeud){
        noeuds.remove(noeud.getId());
    }

    public HashMap<Integer, Noeud> getNoeuds() {
        return noeuds;
    }

    public ArrayList<Barres> getBarres() {
        return barres;
    }

    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    public Numerateur getNumerateur() {
        return numerateur;
    }

    public NoeudSimple createNoeudSimple(double posX, double posY) {
        NoeudSimple node = new NoeudSimple(posX, posY, numerateur.getNewId());
        noeuds.put(node.getId(), node);
        return node;
    }

    public void createBarre(Noeud pA, Noeud pB){
        Barres b = new Barres(pA, pB, numerateur.getNewId());
        barres.add(b);
    }

    public void removeElement(Forme f){
        if(f instanceof Noeud) removeNoeuds((Noeud) f);
        else if(f instanceof Barres) removeBarres((Barres) f);
    }
}