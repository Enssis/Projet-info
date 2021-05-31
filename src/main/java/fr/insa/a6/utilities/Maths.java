package fr.insa.a6.utilities;

import fr.insa.a6.treillis.dessin.Point;
import javafx.scene.paint.Color;

public class Maths {

    public static double dist(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getPosX() - b.getPosX(), 2) + Math.pow(a.getPosY() - b.getPosY(), 2));
    }

    public static double dist(Point a, double x, double y) {
        return Math.sqrt(Math.pow(a.getPosX() -x, 2) + Math.pow(a.getPosY() - y, 2));
    }

    public static double max(double a, double b, double c){
        if(a > b && a > c){
            return a;
        }else if(b > a && b > c){
            return b;
        }else{
            return c;
        }
    }

    public static double min(double a, double b, double c){
        if(a < b && a < c){
            return a;
        }else if(b < a && b < c){
            return b;
        }else{
            return c;
        }
    }

    //formule pour trouver un angle orienté entre un vectuer et le vecteur horizontal (résultat entre -pi et pi)
    public static double angle(Point o, Point p){
        double op = dist(o, p);

        return Math.acos((p.getPosX() - o.getPosX()) / op) * (p.getPosY() - o.getPosY()) / Math.abs((p.getPosY() - o.getPosY()));
    }

    //formule pour trouver un angle orienté entre deux vecteurs de même origine(résultat entre -pi et pi)
    public static double angle(Point o, Point p1, Point p2){
        double angle1 = angle(o, p1);
        double angle2 = angle(o, p2);

        return Math.PI - ((angle1 - angle2 + 2 * Math.PI) % (2 * Math.PI));
    }

    //retourne si un point est compris entre deux points ou non
    public static boolean between(Point p, Point p1, Point p2){
        return (p.getPosX() <= Math.max(p1.getPosX(), p2.getPosX())
                && p.getPosX() >= Math.min(p1.getPosX(), p2.getPosX())
                && p.getPosY() <= Math.max(p1.getPosY(), p2.getPosY())
                && p.getPosY() >= Math.min(p1.getPosY(), p2.getPosY()));
    }

    //applique une rotation a un point par rapport a un point d'origine
    public static Point rotation(Point o, Point p, double angle){
        double px0 = p.getPosX() - o.getPosX();
        double py0 = p.getPosY() - o.getPosY();

        Point p2 = rotation(new Point(px0, py0), angle);

        return new Point(p2.getPosX() + o.getPosX(), p2.getPosY() + o.getPosY());
    }

    //applique une rotation a un point par rapport au point d'origine (0, 0)
    public static Point rotation(Point p, double angle){

        double px = p.getPosX() * Math.cos(angle) + p.getPosY() * Math.sin(angle);
        double py = p.getPosX() * Math.sin(angle) + p.getPosY() * Math.cos(angle);

        return new Point(px, py);
    }

    //transform a color to an hexadecimal string
    public static String colorToHexa(Color color){
        return "#" + Integer.toHexString(color.hashCode()).substring(0, 6);
    }
}
