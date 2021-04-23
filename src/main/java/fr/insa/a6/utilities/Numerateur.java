package fr.insa.a6.utilities;

public class Numerateur {

    int currentId = 0;

    public Numerateur(){

    }

    public Numerateur(int currentId){
        this.currentId = currentId;
    }

    public int getNewId() {
        currentId ++;
        return currentId;
    }
}
