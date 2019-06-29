import black_grey_white.CycleInDirectedGraph;
import graph.DirectedGraph;
import johnson.Johnson;
import utils.DependenciesHandler;
import utils.Printer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {

    private static final int MIN = 3;
    private static HashMap<String,Short> stringToShort = new HashMap<>(); //todo inicializo aca?
    private static HashMap<Short,String> shortToString =  new HashMap<>(); //todo inicializo aca?

    /**
     * @param args args[0] : el path del xml de entrada
     *             args[1] : #ciclos maximos
     *             args[2] : path del archivo a escribir con los ciclos encontrados
     *             args[3] : path del archivo a escribir con la tabla enumarando cantidad de ciclos para cada longitud encontrada
     *             args[4] : eleccion del algoritmo a utilizar para buscar los ciclos. Es opcional.
     */

    public static void main(String[] args) {
        DirectedGraph<String> classGraph = new DirectedGraph<>();
        HashMap<String, String> classToPackage = new HashMap<>();
        try {
            if (args.length < 4 || args.length > 5) {
                System.err.println("Se necesita un minimo de 4 argumentos o 5 indicando si utilizar 'dfs' o 'Johnson como algoritmo'");
                return;
            }
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            SAXParser saxParser = factory.newSAXParser();
            File inputFile = new File(args[0]);
            DependenciesHandler userhandler = new DependenciesHandler(classToPackage, classGraph);
            saxParser.parse(inputFile, userhandler);

            DirectedGraph<Short> packages = userhandler.getPackageGraph(stringToShort,shortToString);
            List<List<Short>> circuits;

            boolean johnson;
            if (args.length == 4) {
                System.out.println("Criterio de seleccion de algoritmo predefinido: para grafos de menos de 80 arcos, o mayores de 80 arcos pero con longitudes de circuito mayores a 15 se utiliza Johnson, en otro caso DFS");
                johnson = packages.getAllEdges().size() < 80 || Integer.valueOf(args[1]) > 15;
            } else if (args[4].equalsIgnoreCase("johnson")) johnson = true;
            else if (args[4].equalsIgnoreCase("dfs")) johnson = false;
            else {
                System.out.println("El algoritmo seleccionado debe ser 'dfs' o 'johnson'");
                return;
            }
            if (johnson) {
                System.out.println("Usando Johnson");
                Johnson j = new Johnson();
                circuits = Johnson.findCircuits(packages, MIN, Integer.valueOf(args[1]));
            } else {
                System.out.println("Usando DFS");
                circuits = CycleInDirectedGraph.findCycles(packages, Integer.valueOf(args[1]), MIN);
            }
            Printer.printCircuits(circuits, args[2],shortToString);
            Printer.printTable(circuits, args[3]);
        } catch (ParserConfigurationException | Johnson.JohnsonIllegalStateException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }
}