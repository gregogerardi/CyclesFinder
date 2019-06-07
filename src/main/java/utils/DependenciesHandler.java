package utils;

import graph.DirectedGraph;
import graph.Vertex;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Collection;
import java.util.HashMap;

public class DependenciesHandler extends DefaultHandler {

    private HashMap<String, String> classToPackage;
    private String packageName;
    private String className;
    private DirectedGraph<String> graph;

    public DependenciesHandler(HashMap<String, String> classToPackage, DirectedGraph<String> graph) {
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
            graph.addEdge(className, dependencieClass);
        }
    }

    /**
     * Busqueda de vertices y arcos entre los distintos paquetes, a partir del grafo de clases
     *
     * @return grafo dirigido con las dependencias entre los paquetes, obviando los que no conocemos desde el xml
     */

    public DirectedGraph<String> getPackageGraph() {
        DirectedGraph<String> packageGraph = new DirectedGraph<>();
        Collection<Vertex<String>> nodos = graph.getAllVertex();
        nodos.forEach(vertice -> vertice.getAdjacentVertexes().forEach(verticeHijo -> {
            String source = classToPackage.get(vertice.getData());
            String target = classToPackage.get(verticeHijo.getData());
            if (target != null && source != null && !target.equals(source))
                packageGraph.addEdge(source, target);
        }));
        return packageGraph;
    }
}