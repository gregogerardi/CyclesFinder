package johnson;

import graph.DirectedGraph;
import graph.Vertex;
import tarjan.Tarjan;
import utils.Reciver;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.min;
import static java.util.stream.Collectors.toList;

/**
 * Implementation of Johnson Algorithm. @See <a href="https://epubs.siam.org/doi/abs/10.1137/0204007?journalCode=smjcat">Finding All the Elementary Circuits of a Directed Grap</a>
 * for a description and the pseudocode followed here of the algorithm (also available on <a href="https://www.cs.tufts.edu/comp/150GA/homeworks/hw1/Johnson%2075.PDF">Johnson Algorithm</a>)
 **/

/*
    la complejidad temporal del algoritmo expresada en la pubnlicación es de O((n+e)c). En la publicacion no concideran el tiempo
    de guardar cada ciclo encontrado, que es de O(largo del ciclo) = en el peor caso O(n) y ocurre una vez por cada ciclo encontrado
    dando una complejidad resultante de O((n+e)cn) o O(cn^2+cne). En el peor caso (grafo totalmente conexo) e=n^2 dando
    O(cn^2+cn^3)= O(cn^3)
 */
public class Johnson {

    public final static int NO_MIN_LIMIT = -1;
    public final static int NO_MAX_LIMIT = Integer.MAX_VALUE;

    /**
     * Method for returning the minimun Strongly Connected Component of a given {@link DirectedGraph}.
     * The minimun SCC is the one who has the smallest {@link NodeType}.
     *
     * @param dg         the {@link DirectedGraph} to search in for
     * @param minCircuit the minimum length expected for the SCC. We use this to avoid SCCs of least than this amount of vertex.
     *                   This kind of SCC can´t contain circuits of more than minCircuit length a so tey are useless to the algorithm
     * @param <NodeType> the type used as vertex. Should implement {@link Comparable}
     * @return a {@link DirectedGraph} to representing the SCC
     */

    public static <NodeType extends Comparable<? super NodeType>> DirectedGraph<NodeType> minSCC(DirectedGraph<NodeType> dg, int minCircuit) throws JohnsonIllegalStateException {
        List<List<Vertex<NodeType>>> sccs = Tarjan.tarjan(dg);
        List<Vertex<NodeType>> minScc = sccs.stream().filter(l -> l.size() >= minCircuit).reduce(Collections.emptyList(), (l1, l2) -> l1.isEmpty() || min(l2.stream().map(Vertex::getData).collect(Collectors.toList())).compareTo(min(l1.stream().map(Vertex::getData).collect(Collectors.toList()))) < 0 ? l2 : l1);
        return addEdges(minScc, dg);
    }

    private static <NodeType extends Comparable<? super NodeType>> DirectedGraph<NodeType> addEdges(List<Vertex<NodeType>> list, DirectedGraph<NodeType> dg) throws
            JohnsonIllegalStateException {
        if (list == null || dg == null) {
            throw new JohnsonIllegalStateException();
        }
        DirectedGraph<NodeType> result = new DirectedGraph<>();
        list.forEach(i -> i.getEdges().forEach(e -> {
            Vertex<NodeType> to = e.getVertex2();
            if (list.contains(to)) result.addEdge(i.getData(), to.getData());
        }));
        return result;
    }

    /**
     * Genera un subgrafo con todos los nodos mayores a in. En el grafo resultante solo se incluiran arcos que vinculen nodos
     * que cumplan esta restriccion. Si alguno de los dos nodos es menor a i, el arco no se incluira. Tampoco se incluiran nodos
     * que no tengan arcos salientes o entrantes en el grafo resultante, ya que no aportan posibles ciclos
     *
     * @param i          {@link Vertex<NodeType>} referencia para comparar.
     * @param in         {@link DirectedGraph<NodeType>} del que generar un nuevo grafo
     * @param <NodeType> tipo de elementos contenidos en los vertices del grafo. Debe implementar {@link Comparable}
     *                   para que los vertices puedan ordenarse de menor a mayor.
     * @return el subgrafo {@link DirectedGraph<NodeType>} generado
     */

    public static <NodeType extends Comparable<? super NodeType>> DirectedGraph<NodeType> subGraphFrom(Vertex<NodeType> i, DirectedGraph<NodeType> in) {
        DirectedGraph<NodeType> result = new DirectedGraph<>();
        Predicate<Vertex<NodeType>> filter = node -> i == null || node.getData().compareTo(i.getData()) > 0;
        in.getAllVertex().stream().filter(filter).forEach(from -> from.getAdjacentVertexes().stream().filter(filter).forEach(to -> result.addEdge(from.getData(), to.getData())));
        return result;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static <NodeType extends Comparable<? super NodeType>> Vertex<NodeType> minVertex(DirectedGraph<NodeType> in) {
        return in.getAllVertex().stream().min(Comparator.comparing(Vertex::getData)).get();
    }

    private static <NodeType extends Comparable<? super NodeType>> void unblock(Vertex<NodeType> u, Map<Vertex<NodeType>, Boolean> blocked, Map<Vertex<NodeType>, List<Vertex<NodeType>>> blockedNodes) {
        blocked.put(u, false);
        while (blockedNodes.get(u).size() > 0) {
            Vertex<NodeType> w = blockedNodes.get(u).remove(0);
            if (blocked.get(w)) {
                unblock(w, blocked, blockedNodes);
            }
        }
    }

    private static <NodeType extends Comparable<? super NodeType>> boolean circuit(DirectedGraph<NodeType> dg, Vertex<NodeType> v, Vertex<NodeType> s, List<Vertex<NodeType>> stack, Map<Vertex<NodeType>, Boolean> blocked, Map<Vertex<NodeType>, List<Vertex<NodeType>>> blockedNodes, List<List<Vertex<NodeType>>> circuits, int minCircuit, int maxCircuit) throws JohnsonIllegalStateException {
        if (dg == null) {
            throw new JohnsonIllegalStateException();
        }
        if (dg.getAllVertex().size() == 0) {
            return false;
        }
        boolean f = false;
        stack.add(v);
        blocked.put(v, true);
        for (Vertex<NodeType> w : v.getAdjacentVertexes()) {
            if (w.equals(s)) {
                if (stack.size() >= minCircuit && stack.size() <= maxCircuit) {
                    circuits.add(new ArrayList<>(stack));
                }
                f = true;
            } else {
                if (!blocked.get(w)) {
                    if (circuit(dg, w, s, stack, blocked, blockedNodes, circuits, minCircuit, maxCircuit)) {
                        f = true;
                    }
                }
            }
        }
        if (f) unblock(v, blocked, blockedNodes);
        else
            v.getAdjacentVertexes().stream().filter(w -> !blockedNodes.get(w).contains(v)).forEach(w -> blockedNodes.get(w).add(v));
        stack.remove(stack.size() - 1);
        return f;
    }

    private static <NodeType extends Comparable<? super NodeType>> boolean circuit(DirectedGraph<NodeType> dg, Vertex<NodeType> v, Vertex<NodeType> s, List<Vertex<NodeType>> stack, Map<Vertex<NodeType>, Boolean> blocked, Map<Vertex<NodeType>, List<Vertex<NodeType>>> blockedNodes, int minCircuit, int maxCircuit, Reciver reciver) throws JohnsonIllegalStateException {
        if (dg == null) {
            throw new JohnsonIllegalStateException();
        }
        if (dg.getAllVertex().size() == 0) {
            return false;
        }
        boolean f = false;
        stack.add(v);
        blocked.put(v, true);
        for (Vertex<NodeType> w : v.getAdjacentVertexes()) {
            if (w.equals(s)) {
                if (stack.size() >= minCircuit && stack.size() <= maxCircuit) {
                    reciver.newCycle(new ArrayList<>(stack).stream().map(Vertex::getData).collect(toList()));
                }
                f = true;
            } else {
                if (!blocked.get(w)) {
                    if (circuit(dg, w, s, stack, blocked, blockedNodes, minCircuit, maxCircuit,reciver)) {
                        f = true;
                    }
                }
            }
        }
        if (f) unblock(v, blocked, blockedNodes);
        else
            v.getAdjacentVertexes().stream().filter(w -> !blockedNodes.get(w).contains(v)).forEach(w -> blockedNodes.get(w).add(v));
        stack.remove(stack.size() - 1);
        return f;
    }

    public static <NodeType extends Comparable<? super NodeType>> List<List<NodeType>> findCircuits(DirectedGraph<NodeType> dg) throws JohnsonIllegalStateException {
        return findCircuits(dg, NO_MIN_LIMIT, NO_MAX_LIMIT);
    }

    public static <NodeType extends Comparable<? super NodeType>> void findCircuits(DirectedGraph<NodeType> dg, Reciver reciver) throws JohnsonIllegalStateException {
        findCircuits(dg, NO_MIN_LIMIT, NO_MAX_LIMIT, reciver);
    }

    public static <NodeType extends Comparable<? super NodeType>> List<List<NodeType>> findCircuits(DirectedGraph<NodeType> dg, int minCircuit, int maxCircuit) throws JohnsonIllegalStateException {
        Map<Vertex<NodeType>, Boolean> blocked = new HashMap<>(dg.getAllVertex().size() * 2);
        Map<Vertex<NodeType>, List<Vertex<NodeType>>> blockedNodes = new HashMap<>(dg.getAllVertex().size() * 2);
        List<List<Vertex<NodeType>>> circuits = new ArrayList<>();
        List<Vertex<NodeType>> stack = new ArrayList<>();
        Vertex<NodeType> min;
        DirectedGraph<NodeType> subGraph;
        DirectedGraph<NodeType> minScc = minSCC(dg, minCircuit);
        while (minScc.getAllVertex().size() > 0) {
            min = minVertex(minScc);
            minScc.getAllVertex().forEach(i -> {
                blocked.put(i, false);
                blockedNodes.put(i, new ArrayList<>());
            });
            circuit(minScc, min, min, stack, blocked, blockedNodes, circuits, minCircuit, maxCircuit);
            subGraph = subGraphFrom(min, dg);
            minScc = minSCC(subGraph, minCircuit);
        }
        return circuits.stream().map(l -> l.stream().map(Vertex::getData).collect(Collectors.toList())).collect(Collectors.toList());
    }

    public static <NodeType extends Comparable<? super NodeType>> void findCircuits(DirectedGraph<NodeType> dg, int minCircuit, int maxCircuit, Reciver reciver) throws JohnsonIllegalStateException {
        Map<Vertex<NodeType>, Boolean> blocked = new HashMap<>(dg.getAllVertex().size() * 2);
        Map<Vertex<NodeType>, List<Vertex<NodeType>>> blockedNodes = new HashMap<>(dg.getAllVertex().size() * 2);
        List<Vertex<NodeType>> stack = new ArrayList<>();
        Vertex<NodeType> min;
        DirectedGraph<NodeType> subGraph;
        DirectedGraph<NodeType> minScc = minSCC(dg, minCircuit);
        while (minScc.getAllVertex().size() > 0) {
            min = minVertex(minScc);
            minScc.getAllVertex().forEach(i -> {
                blocked.put(i, false);
                blockedNodes.put(i, new ArrayList<>());
            });
            circuit(minScc, min, min, stack, blocked, blockedNodes, minCircuit, maxCircuit, reciver);
            subGraph = subGraphFrom(min, dg);
            minScc = minSCC(subGraph, minCircuit);
        }
    }

    public static class JohnsonIllegalStateException extends Throwable {
    }
}
