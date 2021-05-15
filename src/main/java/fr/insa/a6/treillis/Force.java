package fr.insa.a6.treillis;

import java.util.ArrayList;

/**
 * 
 */
public class Force {

    public double fX;
    public double fY;

    /**
     * Default constructor
     */
    public Force() {
        fX = 0;
        fY = 0;
    }

    public Force(double fx, double fy){
        this.fX = fx;
        this.fY = fy;
    }
    public class ForceNoeud {
       public ArrayList<Force> Forces;
       
       public ForceNoeud (Force force){
       this.fX = 2;
       this.fY=5;
       
       Forces.add(this.Force);
       //blabla on calcule la force

       }
           }
   public static void main (){
       
   }
    public class MatriceResultat {

    private int nbrLig;
    private int nbrCol = 2;
    private double[][] coeffs;

    public MatriceResultat (int nl, a ForceNoeud) {
        this.nbrLig = nl;
        this.coeffs = new double[nl][2];
        for (int i = 0; i < this.nbrLig; i++) {
            this.coeffs[i][2] = this.ForceNoeud;
            //faire un truc pour mettre un nom a chaque noeud a coté de sa force
            }
        }
    }
    
    public String toString() {
        String s;
        s = "";
        for (int i = 0; i < this.nbrLig; i++) {
            s = s + "[";
            for (int j = 0; j < 2; j++) {
                s = s + String.format("%+4.2E", this.coeffs[i][j]);
                if (j != 2 - 1) {
                    s = s + " ";
                }

            }
            s = s + "]\n";
        }
        return null;
          }

}
}