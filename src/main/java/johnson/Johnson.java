package johnson;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import tarjan.Tarjan;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Collections.min;

public class Johnson<NodeType extends Comparable<? super NodeType>, EdgeType> {

    public final static int NO_MIN_LIMIT = -1;
    public final static int NO_MAX_LIMIT = Integer.MAX_VALUE;
    private Map<NodeType, Boolean> blocked;
    private Map<NodeType, List<NodeType>> blockedNodes;
    private List<List<NodeType>> circuits;
    private int minCircuit;
    private int maxCircuit;

    //todo comentar que no devolvemos los SCC de menos de minCircuit nodos porque nunca podran tener un ciclo de minCircuit nodos o mas
    public static <NodeType extends Comparable<? super NodeType>, EdgeType> DirectedGraph<NodeType, EdgeType> minSCC(DirectedGraph<NodeType, EdgeType> dg, int minCircuit) throws JohnsonIllegalStateException {
        Tarjan<NodeType, EdgeType> t = new Tarjan<>(dg, minCircuit);
        List<List<NodeType>> sccs = t.tarjan();
        List<NodeType> minScc = sccs.stream().filter(l -> l.size() >= minCircuit).reduce(Collections.emptyList(), (l1, l2) -> l1.isEmpty() || min(l2).compareTo(min(l1)) < 0 ? l2 : l1);

        //version sin streams para seguimiento
/*          NodeType min;
            List<NodeType> minScc = Collections.emptyList();
            if (sccs.size() != 0) {
            min = min(sccs.get(0));
            minScc = sccs.get(0);
            for (List<NodeType> scc : sccs) {
                if (scc.size() > 1) {
                    NodeType localMin = min(scc);
                    if (localMin.compareTo(min) < 0) {
                        minScc = scc;
                        min = localMin;
                    }
                }
            }*/
        return addEdges(minScc, dg);
    }

    //todo comentar que agrega los edges del grafo parametro al grafo resultado solo si sus nodos estan contenidos en list
    private static <NodeType extends Comparable<? super NodeType>, EdgeType> DirectedGraph<NodeType, EdgeType> addEdges(List<NodeType> list, DirectedGraph<NodeType, EdgeType> dg) throws JohnsonIllegalStateException {
        if (list == null || dg == null) {
            throw new JohnsonIllegalStateException();
        }
        DirectedGraph<NodeType, EdgeType> result = new DirectedSparseGraph<>();
        list.forEach(i -> dg.getOutEdges(i).forEach(e -> {
            NodeType to = dg.getOpposite(i, e);
            if (list.contains(to)) result.addEdge(e, i, to);
        }));

        //antes de usar for each y Consumer
       /* for (NodeType i : list) {
            for (EdgeType edge : dg.getOutEdges(i)) {
                NodeType to = dg.getOpposite(i, edge);
                if (list.contains(to)) {
                    result.addEdge(edge, i, to);
                }
            }
        }*/
        return result;
    }

    //todo comentar que solo se queda con nodos que tengan arcos a otros nodos que tambien sean mayores a i, si quedan nodos aislados no los incluye
    public static <NodeType extends Comparable<? super NodeType>, EdgeType> DirectedGraph<NodeType, EdgeType> subGraphFrom(NodeType i, DirectedGraph<NodeType, EdgeType> in) {
        DirectedGraph<NodeType, EdgeType> result = new DirectedSparseGraph<>();

        Predicate<NodeType> filter = node -> i == null || node.compareTo(i) > 0;
        in.getVertices().stream().filter(filter).forEach(from -> in.getSuccessors(from).stream().filter(filter).forEach(to -> result.addEdge(in.findEdge(from, to), from, to)));

        //antes de usar streams
/*        for (NodeType from : in.getVertices()) {
            if (i == null || from.compareTo(i) > 0) {
                for (NodeType to : in.getSuccessors(from)) {
                    if (i == null || to.compareTo(i) > 0) {
                        result.addEdge(in.findEdge(from, to), from, to);
                    }
                }
            }
        }*/
        return result;
    }

    private static <NodeType extends Comparable<? super NodeType>, EdgeType> NodeType minVertex(DirectedGraph<NodeType, EdgeType> in) {
        return min(in.getVertices());
/*
        List<NodeType> minScc = sccs.stream().filter(l -> l.size() > 1).reduce(Collections.emptyList(), (l1, l2) -> l1.isEmpty() || min(l2).compareTo(min(l1)) < 0 ? l2 : l1);
        List<NodeType> vertexs = new ArrayList<>(in.getVertices());
        if (vertexs.isEmpty()) return null;
        NodeType result = vertexs.get(0);
        for (NodeType i : in.getVertices()) {
            if (i.compareTo(result) < 0) {
                result = i;
            }
            return result;
        }*/
    }

    private void unblock(NodeType u) {
        blocked.put(u, false);
        while (blockedNodes.get(u).size() > 0) {
            NodeType w = blockedNodes.get(u).remove(0);
            if (blocked.get(w)) {
                unblock(w);
            }
        }
    }

    private boolean circuit(DirectedGraph<NodeType, EdgeType> dg, NodeType v, NodeType s, Stack<NodeType> stack) throws JohnsonIllegalStateException {
        if (dg == null) {
            throw new JohnsonIllegalStateException();
        }
        if (dg.getVertexCount() == 0) {
            return false;
        }
        boolean f = false;
        stack.push(v);
        blocked.put(v, true);
        for (NodeType w : dg.getSuccessors(v)) {
            if (w.equals(s)) {
                if (stack.size() >= minCircuit && stack.size() <= maxCircuit)
                    this.circuits.add((Stack<NodeType>) stack.clone());
                f = true;
            } else {
                if (!blocked.get(w)) {
                    if (circuit(dg, w, s, stack)) {
                        f = true;
                    }
                }
            }
        }

        if (f) unblock(v);
        else
            dg.getSuccessors(v).stream().filter(w -> blockedNodes.get(w).contains(v)).forEach(w -> blockedNodes.get(w).add(v));
           /* {for (NodeType w : dg.getSuccessors(v)) {
                if (!blockedNodes.get(w).contains(v)) {
                    blockedNodes.get(w).add(v);
                }
            }
        }*/
        stack.pop();
        return f;
    }

    public List<List<NodeType>> findCircuits(DirectedGraph<NodeType, EdgeType> dg, int minCircuit, int maxCircuit) throws JohnsonIllegalStateException {
        this.minCircuit = minCircuit;
        this.maxCircuit = maxCircuit;
        circuits = new ArrayList<>();
        blocked = new HashMap<>();
        blockedNodes = new HashMap<>();
        Stack<NodeType> stack = new Stack<>();
        NodeType min = null;
        DirectedGraph<NodeType, EdgeType> subGraph = subGraphFrom(min, dg);
        DirectedGraph<NodeType, EdgeType> minScc = minSCC(subGraph, minCircuit);
        while (minScc.getVertices().size() > 0) {
            min = minVertex(minScc);
            minScc.getVertices().forEach(i -> {
                blocked.put(i, false);
                blockedNodes.put(i, new ArrayList<>());
            });
          /*  for (NodeType i : minScc.getVertices()) {
                blocked.put(i, false);
                blockedNodes.put(i, new ArrayList<>());
            }*/
            circuit(minScc, min, min, stack);
            subGraph = subGraphFrom(min, dg);
            minScc = minSCC(subGraph, minCircuit);
        }
        return circuits;
    }

    public static class JohnsonIllegalStateException extends Throwable {
    }
}
