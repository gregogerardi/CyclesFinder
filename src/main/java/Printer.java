import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

class Printer {

    static void print(List<Stack<String>> circuits, String path) throws IOException { // todo quiza hacerlo generico?

        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        int num = 1;
        for (Stack<String> ss : circuits) {
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
