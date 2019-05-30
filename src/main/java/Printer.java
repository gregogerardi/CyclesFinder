import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

        elements.forEach(l -> pw.println(String.join(";", l.iterator())));

        elements.forEach(list -> {
            list.stream().map(w -> new StringBuilder(w.toString()).append(';')).forEach(w -> w.chars().forEach(c -> {
                try {
                    dos.writeChar(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            try {
                dos.writeChar('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        dos.close();
    }
}
