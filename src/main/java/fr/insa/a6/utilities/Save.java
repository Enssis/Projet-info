package fr.insa.a6.utilities;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.Type;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.terrain.Triangle;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Save {

    public static Treillis getTreillis(String path) {
        try {
            BNFReader reader = new BNFReader(new FileReader(path));
            return reader.getTreillis();
        }catch (IOException ioException){
            return new Treillis();
        }
    }

    public static void saveTreillis(Treillis treillis, String savePath){
        StringBuilder fileString = new StringBuilder(treillis.getTerrain().saveString() + "\n");

        for (Triangle triangle: treillis.getTerrain().getTriangles()) {
            fileString.append(triangle.saveString()).append("\n");
        }
        fileString.append("FINTRIANGLES\n");

        for (Type type: treillis.getCatalogue()) {
            fileString.append(type.saveString()).append("\n");
        }
        fileString.append("FINCATALOGUE\n");

        for (Noeud noeud: treillis.getNoeuds()) {
            fileString.append(noeud.saveString()).append("\n");
        }
        fileString.append("FINNOEUDS\n");

        for (Barres barre: treillis.getBarres()) {
            fileString.append(barre.saveString()).append("\n");
        }
        fileString.append("FINBARRES\n");

        try {
            FileWriter writer = new FileWriter(savePath);
            writer.write(fileString.toString());
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
