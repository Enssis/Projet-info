package fr.insa.a6.utilities;

public class Numerateur {

    int currentNoeudId = 0;
    int currentBarreId = 0;
    int currentTypeId = 0;
    int currentTriangleId = 0;

    public Numerateur(){

    }

    public Numerateur(int currentNoeudId, int currentBarreId, int currentTypeId, int currentTriangleId){
        this.currentNoeudId = currentNoeudId;
        this.currentBarreId = currentBarreId;
        this.currentTypeId = currentTypeId;
        this.currentTriangleId = currentTriangleId;
    }

    public int getNewNoeudId() {
        currentNoeudId ++;
        return currentNoeudId;
    }

    public int getCurrentNoeudId() {
        return currentNoeudId;
    }


    public int getNewBarreId() {
        currentBarreId ++;
        return currentBarreId;
    }

    public int getCurrentBarreId() {
        return currentBarreId;
    }


    public int getNewTypeId() {
        currentTypeId ++;
        return currentTypeId;
    }

    public int getCurrentTypeId() {
        return currentTypeId;
    }


    public int getNewTriangleId() {
        currentTriangleId ++;
        return currentTriangleId;
    }

    public int getCurrentTriangleId() {
        return currentTriangleId;
    }


}
