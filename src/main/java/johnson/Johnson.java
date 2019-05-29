package johnson;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import tarjan.Tarjan;
import utils.DummyEdge;

import java.util.*;

//todo pasar a generics
public class Johnson {

    private Map<String, Boolean> blocked;
    private Map<String, List<String>> blockedNodes;
    private List<Stack<String>> circuits;
    private DirectedGraph<String, DummyEdge> dg;

    public Johnson(DirectedGraph<String, DummyEdge> dg) {
        blocked = new HashMap<>();
        blockedNodes = new HashMap<>();
        circuits = new ArrayList<>();
        this.dg = dg;
    }

    public static DirectedGraph<String, DummyEdge> leastSCC(DirectedGraph<String, DummyEdge> dg) throws JohnsonIllegalStateException {
        Tarjan<String, DummyEdge> t = new Tarjan<>(dg);
        List<List<String>> sccs = t.tarjan();
        String min = null;
        List<String> minScc = new ArrayList<>();
        for (List<String> scc : sccs) {
            if (scc.size() == 1) {
                continue;
            }
            for (String i : scc) {
                if (min == null) {
                    min = i;
                    minScc = scc;
                } else if (i.compareTo(min) < 0) {
                    minScc = scc;
                    min = i;
                }
            }
        }
        return addEdges(minScc, dg);
    }

    private static DirectedGraph<String, DummyEdge> addEdges(List<String> list, DirectedGraph<String, DummyEdge> dg) throws JohnsonIllegalStateException {
        if (list == null) {
            throw new JohnsonIllegalStateException();
        }
        if (dg == null) {
            throw new JohnsonIllegalStateException();
        }
        DirectedGraph<String, DummyEdge> result = new DirectedSparseGraph<>();
        for (String i : list) {
            for (DummyEdge edge : dg.getOutEdges(i)) {
                String to = dg.getOpposite(i, edge);
                if (list.contains(to)) {
                    result.addEdge(edge, i, to);
                }
            }
        }
        return result;
    }

    //todo comentar que solo se queda con nodos que tengan arcos a otros nodos que tambien sean mayores a i, si quedan nodos aislados no los incluye
    public static DirectedGraph<String, DummyEdge> subGraphFrom(String i, DirectedGraph<String, DummyEdge> in) {
        DirectedGraph<String, DummyEdge> result = new DirectedSparseGraph<>();
        for (String from : in.getVertices()) {
            if (from.compareTo(i) > 0) {
                for (String to : in.getSuccessors(from)) {
                    if (to.compareTo(i) > 0) {
                        result.addEdge(in.findEdge(from, to), from, to);
                    }
                }
            }
        }
        return result;
    }

    public void unblock(String u) {
        blocked.put(u, false);
        while (blockedNodes.get(u).size() > 0) {
            String w = blockedNodes.get(u).remove(0);
            if (blocked.get(w)) {
                unblock(w);
            }
        }
    }

    public boolean circuit(DirectedGraph<String, DummyEdge> dg, String v, String s, Stack<String> stack) throws JohnsonIllegalStateException {
        if (dg == null) {
            throw new JohnsonIllegalStateException();
        }
        if (dg.getVertexCount() == 0) {
            return false;
        }
        boolean f = false;
        stack.push(v);
        blocked.put(v, true);
        for (String w : dg.getSuccessors(v)) {
            if (w.equals(s)) {
                this.circuits.add((Stack<String>) stack.clone());
                f = true;
            } else {
                if (!blocked.get(w)) {
                    if (circuit(dg, w, s, stack)) {
                        f = true;
                    }
                }
            }
        }
        if (f) {
            unblock(v);
        } else {
            for (String w : dg.getSuccessors(v)) {
                if (!blockedNodes.get(w).contains(v)) {
                    blockedNodes.get(w).add(v);
                }
            }
        }
        stack.pop();
        return f;
    }

    public String leastVertex(DirectedGraph<String, DummyEdge> in) {
        //todo use streams
        List<String> vertexs = new ArrayList<>(in.getVertices());
        if (vertexs.isEmpty()) return null;
        String result = vertexs.get(0);
        for (String i : in.getVertices()) {
            if (i.compareTo(result) < 0) {
                result = i;
            }
        }
        return result;
    }

    public void findCircuits() throws JohnsonIllegalStateException {
        blocked = new HashMap<>();
        blockedNodes = new HashMap<>();
        Stack<String> stack = new Stack<>();
        String min = "";
        DirectedGraph<String, DummyEdge> subGraph = subGraphFrom(min, dg);
        DirectedGraph<String, DummyEdge> leastScc = leastSCC(subGraph);
        while (leastScc.getVertices().size() > 0) {
            min = leastVertex(leastScc);
            for (String i : leastScc.getVertices()) {
                blocked.put(i, false);
                blockedNodes.put(i, new ArrayList<>());
            }
            circuit(leastScc, min, min, stack);
            subGraph = subGraphFrom(min, dg);
            leastScc = leastSCC(subGraph);
        }
    }

    public List<Stack<String>> getCircuits() {
        return circuits;
    }

    public static class JohnsonIllegalStateException extends Throwable {
    }
}
