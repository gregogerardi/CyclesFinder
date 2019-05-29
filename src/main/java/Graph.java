import javafx.util.Pair;

import java.util.ArrayList;

//todo grafo de porqueria revisar eficiencia
//todo borrar al final use el grafo de una libreria de jung de internet
public class Graph {

    private ArrayList<String> nodes = new ArrayList<>();
    private ArrayList<Pair<String, String>> arcs = new ArrayList<>();

    public void addNode(String node) {
        nodes.add(node);
    }

    public boolean contains(String node) {
        return nodes.contains(node);
    }

    public void addArc(String className, String dependencieClass) {
        arcs.add(new Pair(className, dependencieClass));
    }

    public ArrayList<Pair<String, String>> getArcs() {
        return arcs;
    }

    public boolean containsArc(String originPackage, String targetPackage) {
        return arcs.contains(new Pair<>(originPackage, targetPackage));
    }

/*
    public int getAdjacentsCount() {
        return nodes.size();
    }
*/
}
