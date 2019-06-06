package black_grey_white;

import graph.DirectedGraph;
import graph.Vertex;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * http://www.geeksforgeeks.org/detect-cycle-in-a-graph/
 */

public class CycleInDirectedGraph {

    public static final int NO_MAX = (Integer.MAX_VALUE - 1);
    public static final int NO_MIN = 0;
    private static final int CLOSED = 0;

    static public <T extends Comparable<? super T>> List<List<T>> findCycles(DirectedGraph<T> graph) {
        return findCycles(graph, NO_MAX, NO_MIN);
    }

    static public <T extends Comparable<? super T>> List<List<T>> findCycles(DirectedGraph<T> graph, int max, int min) {
        Map<Vertex<T>, Integer> blackSet = new HashMap<>();
        Set<Vertex<T>> graySet = new HashSet<>();
        Set<Vertex<T>> closed = new HashSet<>();
        Map<Vertex<T>, Integer> closeables = new HashMap<>();
        List<Vertex<T>> currentCycle = new ArrayList<>();
        List<List<Vertex<T>>> results = new ArrayList<>();
        Set<Vertex<T>> whiteSet = new HashSet<>(graph.getAllVertex());
        while (whiteSet.size() > 0) {
            Vertex<T> current = whiteSet.iterator().next();
            CycleInDirectedGraph.findCycles(current, whiteSet, graySet, blackSet, currentCycle, results, closeables, closed, max, min);
            closed.forEach(v -> blackSet.put(v, CLOSED));
            closeables.forEach(blackSet::put);
        }
        return results.stream().map(list -> list.stream().map(Vertex::getData).collect(toList())).map(CycleInDirectedGraph::rotateUntilMinFirst).distinct().collect(toList());
    }

    //el boolean lo usamos solo para marcar si ya todos los caminos posibles fueron explorados sin frenar por el largo
    //maximo y entonces pasar a negro completo asi no volvemos a entrar aun desde un camino mas corto.
    //solo lo hacemos sin no hubo caminos no explorados por max, sino en otra pasada desde un camino mas corto
    //puede que ese camino que ahora nos freno por max nos de un ciclo
    static private <T> boolean findCycles(Vertex<T> current, Set<Vertex<T>> whiteSet,
                                          Set<Vertex<T>> graySet, Map<Vertex<T>, Integer> blackSet, List<Vertex<T>> currentCycle, List<List<Vertex<T>>> results, Map<Vertex<T>, Integer> closeables, Set<Vertex<T>> closed, int max, int min) {
        //move current to gray set from white set and then explore it.
        if (currentCycle.size() >= max) {
            return false;
        }
        moveVertex(current, whiteSet, graySet);
        currentCycle.add(current);
        List<Vertex<T>> successors = current.getAdjacentVertexes();
        boolean isClosed = true;
        boolean cycle = false;
        for (Vertex<T> neighbor : successors) {
            //if in gray set then cycle found.
            if (graySet.contains(neighbor)) {
                int i = currentCycle.size() - 1;
                Vertex<T> localVertex = currentCycle.get(i);
                List<Vertex<T>> localResult = new ArrayList<>();
                localResult.add(localVertex);
                while (localVertex != neighbor) {
                    i--;
                    localVertex = currentCycle.get(i);
                    localResult.add(localVertex);
                }
                if (localResult.size() >= min) {
                    Collections.reverse(localResult);
                    results.add(localResult);
                }
                cycle = true;
                continue;
            }
            //if in black set means already explored totally or at least already explored from a shorter path, so we could never find
            // a new cycle if before we already explored fared from the max length, so continue.
            if (!blackSet.containsKey(neighbor) || (blackSet.get(neighbor) > currentCycle.size())) {
                boolean closeChilds = findCycles(neighbor, whiteSet, graySet, blackSet, currentCycle, results, closeables, closed, max, min);
                isClosed = isClosed && closeChilds && !cycle;
            }
        }
        //move vertex away from gray set when done exploring.
        // if this vertex is closed (without unexplored path trunked by the max restriction), we mark it in the
        //black set so it would never be explored again. Otherwise, we move it again to the white set
        if (isClosed) {
            graySet.remove(current);
            closed.add(current);
        } else {
            moveVertex(current, graySet, whiteSet);
            closeables.put(current, (closeables.containsKey(current)) ? Math.min(currentCycle.size() - 1, closeables.get(current)) : currentCycle.size() - 1);
        }
        currentCycle.remove(currentCycle.size() - 1);
        return isClosed;
    }

    private static <T> void moveVertex(Vertex<T> vertex, Set<Vertex<T>> sourceSet,
                                       Set<Vertex<T>> destinationSet) {
        sourceSet.remove(vertex);
        destinationSet.add(vertex);
    }

    private static <T extends Comparable<? super T>> List<T> rotateUntilMinFirst(List<T> l) {
        T minElem = l.get(0);
        int distance = 1;
        int distanceBackwards = 0;
        for (int i = 0; i < l.size(); i++) {
            Collections.rotate(l, 1);
            if (l.get(0).compareTo(minElem) < 0) {
                minElem = l.get(0);
                distanceBackwards = distance;
            }
            distance++;
        }
        Collections.rotate(l, -distanceBackwards);
        return l;
    }
}

