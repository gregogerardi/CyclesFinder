import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

class Printer {

    /**
     * Volcado en archivos los ciclos encontrados mediante [Johnson]
     *
     * @param circuits de listas, que es el formato devuelto por [findCircuits()]
     * @param path     absoluta donde se creara el archivo con los ciclos
     */

    void print(List<List<String>> circuits, String path) throws IOException {

        FileOutputStream fos = new FileOutputStream(path);
        DataOutputStream dos = new DataOutputStream(fos);

        if (circuits.size() == 0) {
            byte disernible = 0;
            dos.writeChar(disernible);
            dos.close();
        }

        int num = 1;
        circuits.forEach(strings -> printFormato1(strings, dos, num));
        dos.close();
    }

    private void printFormato1(List<String> strings, DataOutputStream dos, int num) {
        try {
            dos.writeChar(num);
            dos.writeChar(')');
            dos.writeChar(' ');
            strings.forEach(palabra -> printFormato2(strings, dos, num));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printFormato2(List<String> strings, DataOutputStream dos, int num) {
        for (int i = 0; i < strings.size(); i++) {
            try {
                printTerminal(strings.get(i), dos);
                if ((i + 1) != strings.get(i).length()) {
                    dos.writeChar(' ');
                    dos.writeChar('-');
                    dos.writeChar('>');
                    dos.writeChar(' ');
                }
                num++;
                dos.writeChar('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printTerminal(String palabra, DataOutputStream dos) throws IOException {
        for (int i = 0; i < palabra.length(); i++) {
            dos.writeChar(palabra.charAt(i));
        }
    }

}
