package fr.insa.a6.treillis;

import fr.insa.a6.treillis.noeuds.Noeud;
import fr.insa.a6.treillis.terrain.Terrain;
import java.util.ArrayList;


/**
 * 
 */
public class Treillis {

    private ArrayList<Terrain> terrains;
    private ArrayList <Noeud> noeuds;
    private ArrayList<Barres> barres;
    private Type[] catalogueType;

    /**
     * Default constructor
     */
    public Treillis() {
    }

    
    
 public void addBarres(Barres barre){
        barres.add(barre);    }
    
    public void removeBarres (Barres barre){
        barres.remove(barre);    }
    
     public void addNoeuds(Noeud noeud){
        noeuds.add(noeud); }
    
    public void removeNoeuds (Noeud noeud){
        noeuds.remove(noeud); }
    
    

}