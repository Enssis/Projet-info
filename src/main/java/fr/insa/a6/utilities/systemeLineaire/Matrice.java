package fr.insa.a6.utilities.systemeLineaire;

import java.sql.SQLOutput;

public class Matrice {

    private int nbrLig, nbrCol;
    private double[][] coeffs;

    public Matrice(int nl, int nc, double[][] coeffs){
        this.nbrLig = nl;
        this.nbrCol = nc;
        this.coeffs = coeffs;
    }

    public Matrice(int nl, int nc)
    {
        double[][] coeffs = new double[nl][nc];
        for (int i = 0; i < nl; i++) {
            for (int j = 0; j < nc; j++) {
                coeffs[i][j] = 0;
            }
        }
        this.nbrLig = nl;
        this.nbrCol = nc;
        this.coeffs = coeffs;
    }

    public Matrice(int size){
        this(size, size);
    }

    @Override
    public String toString() {

        String present = "";

        for (int i = 0; i < nbrLig; i++) {
            present += "[ ";
            for (int j = 0; j < nbrCol; j++) {
                present += String.format("%+4.2E",coeffs[i][j])+ " ";
            }
            present += "]\n";
        }

        return present;
    }

    public Matrice identite(int size){
        Matrice id = new Matrice(size);
        for (int i = 0; i < size; i++) {
            id.coeffs[i][i] = 1;
        }
        return id;
    }

    public static Matrice matTest1() {
        Matrice matTest = new Matrice(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matTest.coeffs[i][j] = i * 3 + j;
            }
        }
        return matTest;
    }

    public static int aleaUnOuDeux() {return Math.random() < 0.5 ? 1: 2;}

    public static Matrice matAleaZeroUnDeux(int nl, int nc, double pz){
        Matrice mat = new Matrice(nl, nc);
        for (int i = 0; i < nl; i++) {
            for (int j = 0; j < nc; j++) {
                mat.coeffs[i][j] = Math.random() < pz ? 0: aleaUnOuDeux();
            }
        }
        return mat;
    }

    public static Matrice creeVecteur(double[] values) {
        Matrice vect = new Matrice(values.length, 1);
        for (int i = 0; i < values.length; i++) {
            vect.coeffs[i][0] = values[i];
        }
        return vect;
    }

    public Double get(int i,int j) {
        return coeffs[i][j];
    }

    public int getNbrCol() {
        return nbrCol;
    }

    public int getNbrLig() {
        return nbrLig;
    }

    public void set(int i, int j, double value) {
        coeffs[i][j] = value;
    }



    public Matrice concatLig(Matrice mat2) {
        if (this.nbrCol != mat2.nbrCol) {
            throw new Error("nombre de colonnes non égal : "+ this.nbrCol + " et " + mat2.nbrCol);
        }
        Matrice mat = new Matrice(this.nbrLig + mat2.nbrLig, this.nbrCol);
        for (int i = 0; i < mat.nbrLig; i++) {
            for (int j = 0; j < mat.nbrCol; j++) {
                mat.coeffs[i][j] = (i < this.nbrLig) ? this.coeffs[i][j] : mat2.coeffs[i - this.nbrLig][j];
            }
        }
        return mat;

    }

    public static Matrice concatCol(Matrice mat1, Matrice mat2) {
        if (mat1.nbrLig != mat2.nbrLig) {
            throw new Error("nombre de colonnes non égal : " + mat1.nbrLig + " et " + mat2.nbrLig);
        } else {

            Matrice mat = new Matrice(mat1.nbrLig, mat1.nbrCol + mat2.nbrCol);
            for (int i = 0; i < mat.nbrLig; i++) {
                for (int j = 0; j < mat.nbrCol; j++) {
                    mat.coeffs[i][j] = (j < mat1.nbrCol) ? mat1.coeffs[i][j] : mat2.coeffs[i][j - mat1.nbrCol];
                }
            }
            return mat;
        }
    }

    public Matrice subLignes(int nMin, int nMax){
        if(nMin < 0 || nMax >= nbrLig){
            throw new Error("erreur sur nMin ou nMax");
        } else {
            Matrice mat = new Matrice(nMax - nMin + 1, nbrCol);
            for (int i = 0; i < mat.nbrLig; i++) {
                for (int j = 0; j < mat.nbrCol; j++) {
                    System.out.println(i + " et " +j);
                    mat.coeffs[i][j] = coeffs[nMin + i][j];
                }
            }
            return mat;
        }
    }

    public Matrice subCol(int nMin, int nMax){
        if(nMin < 0 || nMax >= nbrCol){
            throw new Error("erreur sur nMin ou nMax");
        } else {
            Matrice mat = new Matrice(nbrLig, nMax - nMin + 1);
            for (int i = 0; i < mat.nbrLig; i++) {
                for (int j = 0; j < mat.nbrCol; j++) {
                    mat.coeffs[i][j] = coeffs[i][nMin + j];
                }
            }
            return mat;
        }
    }

    public Matrice transposee() {
        Matrice mat = new Matrice(nbrCol, nbrLig);
        for (int i = 0; i < mat.nbrCol; i++) {
            for (int j = 0; j < nbrLig; j++) {
                mat.coeffs[i][j] = coeffs[j][i];
            }
        }
        return mat;
    }

    public Matrice metAuCarre()  {
        return concatCol(this.concatLig(identite(nbrCol)), identite(nbrLig).concatLig(this.transposee()));
    }

    public static int intAlea(int bmin, int bmax){
        return (int)(bmin + Math.random()*(bmax-bmin));
    }


    public Matrice add(Matrice mat2){
        if(this.nbrLig != mat2.nbrLig || this.nbrCol != mat2.nbrCol){
            throw new Error("matrices de tailles différentes");
        }
        else {
            double[][] coeffs = new double[this.nbrLig][this.nbrCol];
            for (int i = 0; i < coeffs.length; i++) {
                for (int j = 0; j < coeffs[i].length; j++) {
                    coeffs[i][j] = this.coeffs[i][j] + mat2.coeffs[i][j];
                }
            }
            return new Matrice(this.nbrLig, this.nbrCol, coeffs);
        }

    }

    public Matrice opp(){
        double[][] coeffs = new double[this.nbrCol][this.nbrLig];
        for (int i = 0; i < coeffs.length; i++) {
            for (int j = 0; j < coeffs[i].length; j++) {
                coeffs[i][j] = -this.coeffs[i][j];
            }
        }
        return new Matrice(this.nbrLig, this.nbrCol, coeffs);
    }

    public Matrice moins(Matrice mat2){
        return this.add(mat2.opp());
    }

    public Matrice mult(Matrice mat2){
        if (this.nbrCol != mat2.nbrLig) throw new Error("erreur de dimension");
        Matrice out = new Matrice(this.nbrLig, mat2.nbrCol);
        for (int i = 0; i < out.nbrLig; i++) {
            for (int j = 0; j < out.nbrCol; j++) {
                double coeff = 0;
                for (int k = 0; k < this.nbrCol; k++) {
                    coeff += this.coeffs[i][k] * mat2.coeffs[k][j];
                }
                out.coeffs[i][j] = coeff;
            }
        }
        return out;
    }

    public static Matrice test3() {
        return matTest1().add(matTest1().mult(matTest1()));
    }

    public int permuteLigne(int l1, int l2) {
        double[][] newCoeffs = new double[nbrLig][nbrCol];
        for (int i = 0; i < coeffs.length; i++) {
            for (int j = 0; j < coeffs[i].length; j++) {
                newCoeffs[i][j] = coeffs[i][j];
            }
        }
        boolean egaux = true;

        for (int i = 0; i < nbrCol; i++) {

            if(coeffs[l1][i] != coeffs[l2][i]){
                egaux = false;
            }
            newCoeffs[l2][i] = coeffs[l1][i];
            newCoeffs[l1][i] = coeffs[l2][i];
        }

        if (egaux) return 1;
        else {
            coeffs = newCoeffs;
            return -1;
        }
    }

    public void transvection(int l1, int l2) {
        if (l1 > nbrCol) throw new Error("l1 > nbrCol");
        if (coeffs[l1][l1] == 0.0) throw new Error("Ml1l1 = 0");

        double p = coeffs[l2][l1] / coeffs[l1][l1];

        for (int i = 0; i < nbrCol; i++) {
            if (i == l1) coeffs[l2][l1] = 0.0;
            else coeffs[l2][i] = coeffs[l2][i] - p * coeffs[l1][i];
        }
    }

    public int lignePlusGrandPivot(int e) {
        if(e >= nbrCol || e >= nbrLig ) throw new Error("e trop grand");
        double max = 0.0;
        int maxI = 0;
        for (int i = e; i < nbrLig; i++) {
            if( max < Math.abs(coeffs[i][e])) {
                max = Math.abs(coeffs[i][e]);
                maxI = i;
            }
        }
        if(max > Math.pow(10, -8.0)) return maxI;
        else return -1;
    }

    public ResGauss descenteGauss() {
        int signature = 1;
        int rang = Math.min(this.nbrCol,this.nbrLig);

        for (int i = 0; i < this.nbrLig; i++) {
            for (int j = 0; j < this.nbrLig; j++) {
                int lppp = lignePlusGrandPivot(j);
                if (lppp!= -1){
                    permuteLigne(j, lppp);
                }
                else signature= -signature;
            }
        }

        for (int i = 0; i < this.nbrLig; i++) {
            for (int j =0; j< i; j++) {
                if (coeffs[i][j] != 0) transvection(j, i);
            }
        }
        return new ResGauss(rang,signature);
    }

    public double determinant (int signature){
        double determinant = signature;
        for(int i=0; i<this.nbrLig; i++){
            determinant = determinant * this.coeffs[i][i];
        }
        return(determinant);
    }

    public Matrice pivotsaun (){
        for(int i=0; i<this.nbrCol; i++){
            for(int j=i; j<this.nbrLig;j++){
                double pivot = this.coeffs[i][i];
                this.coeffs[i][j] = this.coeffs[i][j] / pivot;
            }
        }
        return this;
    }

    public Matrice remonteeGauss (){
        for (int j = 0; j< this.nbrLig; j++) {
            for (int i = 0; i < j; i++) {
                if (coeffs[i][j] != 0) transvection(j, i);
            }
        }
        return this;
    }

    public Matrice resolution() {
        ResGauss resGauss  = descenteGauss();
        if(determinant(resGauss.signature) == 0) return null;
        pivotsaun();
        return remonteeGauss();
    }

    public static void main(String[] args) {

        double[][] coeffs = {{0,1,2,1},{3,-4,5,2}, {6,7,-8,3}};
        Matrice lamatrice = new Matrice(3,4, coeffs);


        System.out.println(lamatrice.descenteGauss());

        System.out.println("matrice avec la descente");
        System.out.println(lamatrice);


        System.out.println("pivots a un :");
        System.out.println(lamatrice.pivotsaun());
        System.out.println(lamatrice);

        System.out.println("matrice inversée :");
        System.out.println(lamatrice.remonteeGauss());
        System.out.println(lamatrice);

    }
}
