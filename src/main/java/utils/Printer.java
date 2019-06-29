package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Printer implements Reciver {

    private HashMap<Integer, Integer> largeToCount = new HashMap<>();
    private String circuitsTable;
    private PrintWriter printWriter;
    private HashMap<Short, String> shortToString;

    @Override
    public <T> void newCycle(List<T> newCycle) {
        printWriter.println(newCycle.stream().map(shortToString::get).collect(Collectors.joining(";")));
        largeToCount.put(newCycle.size(), largeToCount.containsKey(newCycle.size()) ? (largeToCount.get(newCycle.size()) + 1) : 1);
    }

    public Printer(String circuitsPath, String circuitsTable, HashMap<Short, String> shortToString) {
        this.circuitsTable = circuitsTable;
        this.shortToString = shortToString;
        try {
            this.printWriter = new PrintWriter(new BufferedWriter(new FileWriter(circuitsPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printTable() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(circuitsTable)))
        ) {
            pw.println("Cantidad de paquetes en el ciclo -> cantidad de ciclos");
            pw.println();
            largeToCount.forEach((key, value) -> pw.println(key + " -> " + value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        printWriter.close();
    }
}
