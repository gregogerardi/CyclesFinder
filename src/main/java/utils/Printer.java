package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Printer {

    /**
     * Volcado en archivo de una {@link List} de {@link List}s de elementos
     * En caso de que no haya elementos se genera un archivo vacio
     *
     * @param elements     es la listas de listas conteniendo los elementos a imprimir
     * @param circuitsPath ruta absoluta donde se creara el archivo con la informacion impresa de las listas
     */

    public static void printCircuits(List<? extends List<?>> elements, String circuitsPath,HashMap<Short,String> shortToString) throws IOException {
        new Thread(() -> {
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(circuitsPath)))
            ) {
                elements.forEach(l -> pw.println(l.stream().map(shortToString::get).collect(Collectors.joining(";"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Volcado en archivo de las cantidades de elementos contenidos en cada lista
     * En caso de que no haya elementos se genera un archivo vacio
     *
     * @param elements  {@link List} de {@link List}s de elementos a contar ocurrencias
     * @param tablePath ruta absoluta donde se creara el archivo con la tabla impresa de las listas
     */
    public static void printTable(List<? extends List<?>> elements, String tablePath) throws IOException {
        new Thread(() -> {
            HashMap<Integer, Integer> largeToCount = new HashMap<>();
            elements.forEach(l -> largeToCount.put(l.size(), largeToCount.containsKey(l.size()) ? (largeToCount.get(l.size()) + 1) : 1));
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(tablePath)))
            ) {
                largeToCount.entrySet().forEach(pw::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
