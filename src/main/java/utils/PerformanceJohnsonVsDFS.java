package utils;

import black_grey_white.CycleInDirectedGraph;
import graph.DirectedGraph;
import johnson.Johnson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Benchmark para decidir que algortimo conviene utilizar dadas las configuraciones de los grafos. Modificar los valores
 * de numberOfNodes, numberOfEdges and limits to change the circuits configuration
 */
public class PerformanceJohnsonVsDFS {

    /**
     * @param args contiene en args[0] la ruta donde guardar el archivo de salida del benchmark
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    public static void main(String[] args) {
        int[] numberOfNodes = {3, 5, 10, 20, 30};
        int[] numberOfEdges = {1, 3, 5, 10, 20, 30, 40, 50, 60, 70, 80};
        int[] limits = {1, 2, 3, 5, 10, 15, 20, 30};
        if (args.length != 1) return;
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[0])))) {
            for (int nNodes : numberOfNodes) {
                pw.println(new StringBuilder().append("\t For ").append(nNodes).append(" packages:").toString());
                for (int nEdges : numberOfEdges) {
                    pw.println(new StringBuilder().append("\t \t For ").append(nEdges).append(" dependencies:").toString());
                    for (int limit : limits) {
                        long mediaJohnsonTime = 0;
                        long mediaDfsTime = 0;
                        for (int i = 0; i < 10; i++) {
                            DirectedGraph<String> dg = new DirectedGraph<>();
                            Random r = new Random();
                            int edges = nEdges;
                            while (edges > 0) {
                                String from = String.valueOf(r.nextInt(nNodes) + 1);
                                String to = String.valueOf(r.nextInt(nNodes) + 1);
                                if (!from.equals(to)) {
                                    dg.addEdge(from, to);
                                    edges--;
                                }
                            }
                            Johnson j = new Johnson();
                            long johnsonTime = 0;
                            try {
                                johnsonTime = System.currentTimeMillis();
                                Johnson.findCircuits(dg, Johnson.NO_MIN_LIMIT, limit);
                                johnsonTime = (System.currentTimeMillis() - johnsonTime);
                            } catch (Johnson.JohnsonIllegalStateException e) {
                                e.printStackTrace();
                            }
                            long dfsTime = System.currentTimeMillis();
                            CycleInDirectedGraph.findCycles(dg, limit, CycleInDirectedGraph.NO_MIN);
                            dfsTime = (System.currentTimeMillis() - dfsTime);
                            mediaJohnsonTime += johnsonTime;
                            mediaDfsTime += dfsTime;
                        }
                        mediaDfsTime = mediaDfsTime / 10;
                        mediaJohnsonTime = mediaJohnsonTime / 10;
                        System.out.println(new StringBuilder().append("amount of packages: ").append(nNodes).append("; amount of dependencies: ").append(nEdges).append("; max length: ").append(limit).append("; Johnson time is: ").append(mediaJohnsonTime).append("; DFStime is: ").append(mediaDfsTime).toString());
                        String better = "iguales";
                        if (mediaDfsTime > 0 || mediaJohnsonTime > 0)
                            better = (mediaDfsTime > mediaJohnsonTime) ? "Johnson" : "Dfs";
                        pw.println(new StringBuilder().append("\t \t For ").append("max length: ").append(limit).append(" the best choice is ").append(better).toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
