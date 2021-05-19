package fr.insa.a6.treillis.nodes;

import fr.insa.a6.treillis.terrain.SegmentTerrain;
import fr.insa.a6.treillis.terrain.Triangle;

/**
 * 
 */
public class AppuiEncastre extends Appui {

    /**
     * Default constructor
     */
    public AppuiEncastre(Triangle associatedTriangle, SegmentTerrain segmentTerrain, double posSegment, int id) {
        super(associatedTriangle, segmentTerrain, posSegment, id);
    }

    @Override
    public String saveString() {
        return null;
    }

}