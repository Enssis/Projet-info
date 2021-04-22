package fr.insa.a6.treillis.noeuds;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Force;
import fr.insa.a6.treillis.dessin.Point;

/**
 * 
 */
public abstract class Noeud extends Point {

    private Force forceApplique;
    private Barres[] linkedBarres;

    /**
     * Default constructor
     */
    public Noeud(int id, Force forceApplique, Barres[] linkedBarres) {
        
    this.id = id;
    this.forceApplique = forceApplique;
    this.linkedBarres= linkedBarres;
    
    }
}