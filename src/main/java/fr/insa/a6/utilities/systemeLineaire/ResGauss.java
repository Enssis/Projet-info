package fr.insa.a6.utilities.systemeLineaire;

public class ResGauss {
    public int rang, signature;

    ResGauss(int rang, int signature)
    {
        this.rang = rang;
        this.signature = signature;
    }

    public int getsignature() {return(signature); }

    @Override
    public String toString() {
        return "ResGauss{" +
                "rang=" + rang +
                ", signature=" + signature +
                '}';
    }
}
