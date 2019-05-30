import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import johnson.Johnson;
import org.xml.sax.SAXException;
import utils.DummyEdge;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {

    //args[0] : el path del xml
    //args[1] : #ciclos maximos
    //args[2] : path + nombre del file

    public static void main(String[] args) {
        DirectedGraph<String, DummyEdge> classGraph = new DirectedSparseGraph<>();
        HashMap<String, String> classToPackage = new HashMap<>();
        try {
            if (args.length < 2) {
                System.err.println("Se necesitan 3 argumentos!");
                return;
            }
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            SAXParser saxParser = factory.newSAXParser();
            File inputFile = new File(args[0]);
            DependenciesHandler userhandler = new DependenciesHandler(classToPackage, classGraph);
            saxParser.parse(inputFile, userhandler);

            DirectedGraph<String, DummyEdge> packages = userhandler.getPackageGraph();
            Johnson<String, DummyEdge> johnson = new Johnson<>();
            long startTime = System.currentTimeMillis();
            List<List<String>> circuits = johnson.findCircuits(packages, 3, 7);
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println(elapsedTime / 1000f);
            startTime = System.currentTimeMillis();
            Printer.print(circuits, args[2]);
            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println(elapsedTime / 1000f);
        } catch (IOException | SAXException | ParserConfigurationException | Johnson.JohnsonIllegalStateException e) {
            e.printStackTrace();
        }


    }
}