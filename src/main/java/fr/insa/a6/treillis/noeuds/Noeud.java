package fr.insa.a6.treillis.noeuds;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Force;
import fr.insa.a6.treillis.Point;

/**
 * 
 */
public abstract class Noeud extends Point {

    private int id;
    private Force forceApplique;
    private Barres linkedBarres;

    /**
     * Default constructor
     */
    public Noeud() {
        
    this.id = id;
    this.forceApplique = forceApplique;
    this.linkedBarres= linkedBarres;
    
    }







}