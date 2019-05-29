
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
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
import java.util.Stack;

public class Main {

    //args[0] : el path del xml
    //args[1] : #ciclos maximos
    //args[2] : path + nombre del file

    public static void main(String[] args) {
        Graph classGraph = new DirectedSparseGraph();
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
            Johnson johnson = new Johnson(packages);
            johnson.findCircuits(); //todo chequear por que se cuelga
            List<Stack<String>> circuits = johnson.getCircuits(); //todo pasar a list de list

            Printer.print(circuits, args[2]);

        } catch (IOException | SAXException | ParserConfigurationException | Johnson.JohnsonIllegalStateException e) {
            e.printStackTrace();
        }


    }
}