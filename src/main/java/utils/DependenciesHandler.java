package utils;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Collection;
import java.util.HashMap;

//todo eliminar string hardcodeados
public class DependenciesHandler extends DefaultHandler {


    //todo chequear estructura de hashmap y si va de clase a paquete o viceversa
    private HashMap<String, String> classToPackage;
    private String packageName;
    private String className;
    private Graph<String, DummyEdge> graph;

    public DependenciesHandler(HashMap<String, String> classToPackage, Graph<String, DummyEdge> graph) {
        this.classToPackage = classToPackage;
        this.graph = graph;
    }

    /**
     * Recorrido por el xml, capturando las clases necesarias para armar un grafo entre estas
     * Se almacena en un HashMap el paquete del que proviene cada clase
     */

    @Override
    public void startElement(String uri,
                             String localName, String qName, Attributes attributes) {

        if (qName.equalsIgnoreCase("namespace")) {
            packageName = attributes.getValue("name");
        } else if (qName.equalsIgnoreCase("type")) {
            className = attributes.getValue("name");
            classToPackage.put(className, packageName);
        } else if (qName.equalsIgnoreCase("depends-on")) {
            String dependencieClass = attributes.getValue("name");
            graph.addEdge(new DummyEdge(), className, dependencieClass);
        }
    }

    /**
     * Busqueda de vertices y arcos entre los distintos paquetes, a partir del grafo de clases
     *
     * @return grafo dirigido con las dependencias entre los paquetes, obviando los que no conocemos desde el xml
     */

    public DirectedSparseGraph<String, DummyEdge> getPackageGraph() {
        DirectedSparseGraph<String, DummyEdge> packageGraph = new DirectedSparseGraph<>();
        Collection<String> nodos = graph.getVertices();

        for (String vertice : nodos) {
            Collection<String> hijos = graph.getSuccessors(vertice);
            for (String verticeHijo : hijos) {
                String source = classToPackage.get(vertice);
                String target = classToPackage.get(verticeHijo);
                if (!(target == null)) // todo comentar que ignoramos todas las clases que no conocemos el paquete + las dependencias propias de java
                    packageGraph.addEdge(new DummyEdge(), source, target);
            }
        }

        return packageGraph;
    }

}