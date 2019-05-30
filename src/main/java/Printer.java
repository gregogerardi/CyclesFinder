import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

class Printer {

    /**
     * Volcado en archivo de una {@link List} de {@link List}s de elementos
     * En caso de que no haya elementos se genera un archivo vacio
     *
     * @param elements es la listas de listas conteniendo los elementos a imprimir
     * @param path     ruta absoluta donde se creara el archivo con la informacion impresa
     */

    static void print(List<? extends List<?>> elements, String path) throws IOException {

        FileWriter fw = new FileWriter(path);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        elements.forEach(l -> pw.println(l.stream().map(Object::toString).collect(Collectors.joining(";"))));
        bw.close();
    }
}
