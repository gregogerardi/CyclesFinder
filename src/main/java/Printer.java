import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class Printer {

    static void print(List<List<String>> circuits, String path) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        int num = 1;
        for (List<String> ss : circuits) {
            StringBuilder output = new StringBuilder();
            output.append(num).append(") ");
            int cont = 1;
            for (String s : ss) {
                output.append(s);
                if (cont != ss.size())
                    output.append(" -> ");
                cont++;
            }
            writer.write(output.toString());
            writer.newLine();
            writer.newLine();
            num++;
        }
        writer.close();
    }

}
