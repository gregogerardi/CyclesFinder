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

    @Override
    public <T> void newCycle(List<T> newCycle) {
        printWriter.println(newCycle.stream().map(Object::toString).collect(Collectors.joining(";")));
        largeToCount.put(newCycle.size(), largeToCount.containsKey(newCycle.size()) ? (largeToCount.get(newCycle.size()) + 1) : 1);
    }

    public Printer(String circuitsPath, String circuitsTable) {
        this.circuitsTable = circuitsTable;
        try {
            this.printWriter = new PrintWriter(new BufferedWriter(new FileWriter(circuitsPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printTable() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(circuitsTable)))
        ) {
            largeToCount.entrySet().forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
