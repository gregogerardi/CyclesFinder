import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;

//todo eliminar string hardcodeados
public class DependenciesHandler extends DefaultHandler {


    /*
        boolean dNameSpace = false; // el paquete origen
        boolean dType = false; // la clase que necesita la dependencia
        boolean dDependsOn = false; // las clases de que dependo
        */
    //todo chequear estructura de hashmap y si va de clase a paquete o viceversa
    private HashMap<String, String> classToPackage;
    private String packageName;
    private String className;
    private Graph graph;

    public DependenciesHandler(HashMap<String, String> classToPackage, Graph graph) {
        this.classToPackage = classToPackage;
        this.graph = graph;
    }

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
            if (!graph.contains(dependencieClass)) {
                graph.addNode(dependencieClass);
            }
            if (!graph.contains(className)) {
                graph.addNode(className);
            }
            graph.addArc(className, dependencieClass);
        }
    }

}