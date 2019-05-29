import javafx.util.Pair;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.HashMap;

public class SaxParserXMLToGraph {

    public static final String PATH = "apache-camel-1.6.0.odem";

    public static void main(String[] args) {
        Graph classGraph = new Graph();
        HashMap<String, String> classToPackage = new HashMap<>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            SAXParser saxParser = factory.newSAXParser();
            File inputFile = new File(PATH);
            DependenciesHandler userhandler = new DependenciesHandler(classToPackage, classGraph);
            saxParser.parse(inputFile, userhandler);
            //todo revisar trycatch generico
        } catch (Exception e) {
            e.printStackTrace();
        }
        Graph packagesGraph = new Graph();
        for (Pair<String, String> arc : classGraph.getArcs()) {
            String originPackage = classToPackage.get(arc.getValue());
            if (!packagesGraph.contains(originPackage)) {
                packagesGraph.addNode(originPackage);
            }
            String targetPackage = classToPackage.get(arc.getKey());
            if (!packagesGraph.contains(targetPackage)) {
                packagesGraph.addNode(targetPackage);
            }
            if (!packagesGraph.containsArc(originPackage, targetPackage))
                packagesGraph.addArc(originPackage, targetPackage);
        }
    }
}