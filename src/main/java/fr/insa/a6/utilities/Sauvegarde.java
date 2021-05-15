package fr.insa.a6.utilities;

import fr.insa.a6.treillis.Barres;
import fr.insa.a6.treillis.Treillis;
import fr.insa.a6.treillis.Type;
import fr.insa.a6.treillis.nodes.Noeud;
import fr.insa.a6.treillis.terrain.Triangle;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unchecked")
public class Sauvegarde {

    public static Treillis getTreillis(String path) throws IOException {

        BNFReader reader = new BNFReader(new FileReader(path));
        return reader.getTreillis();
    }

    public static void saveTreillis(Treillis treillis, String savePath){
        String fileString = treillis.getTerrain().saveString() + "\n";

        for (Triangle triangle: treillis.getTerrain().getTriangles()) {
            fileString += triangle.saveString() + "\n";
        }
        fileString += "FINTRIANGLES\n";

        for (Type type: treillis.getCatalogue()) {
            fileString += type.saveString() + "\n";
        }
        fileString += "FINCATALOGUE\n";

        for (Noeud noeud: treillis.getNoeuds()) {
            fileString += noeud.saveString() + "\n";
        }
        fileString += "FINNOEUDS\n";

        for (Barres barre: treillis.getBarres()) {
            fileString += barre.saveString() + "\n";
        }
        fileString += "FINBARRES\n";

        try {
            FileWriter writer = new FileWriter(savePath);
            writer.write(fileString);
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
