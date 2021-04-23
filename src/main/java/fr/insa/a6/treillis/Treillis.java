package fr.insa.a6.treillis;

import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.terrain.Terrain;
import fr.insa.a6.utilities.Numerateur;

import java.util.ArrayList;


/**
 * 
 */
public class Treillis {

    private ArrayList<Terrain> terrains;
    private ArrayList<Noeud> noeuds;
    private ArrayList<Barres> barres;
    private Type[] catalogueType;
    private Numerateur numerateur;

    /**
     * Default constructor
     */
    public Treillis() {
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

    public Numerateur getNumerateur() {
        return numerateur;
    }
}