package graph;

import java.util.*;

/**
 * Representacion de un grafo dirigido con arcos sin pesos
 *
 * @param <T> Tipo de elemento contenido en los vertices del grafo
 */

public class DirectedGraph<T extends Comparable<? super T>> {

    private Set<Edge<T>> allEdges;
    private Map<T, Vertex<T>> allVertex;

    public DirectedGraph() {
        allEdges = new HashSet<>();
        allVertex = new HashMap<>();
    }

    private void addVertex(Vertex<T> vertex) {
        if (allVertex.containsKey(vertex.getData())) {
            return;
        }
        allVertex.put(vertex.getData(), vertex);
        allEdges.addAll(vertex.getEdges());
    }

    public Vertex<T> getVertex(T data) {
        return allVertex.get(data);
    }

    public void addEdge(T data1, T data2) {
        if (data1.equals(data2)) return;
        Vertex<T> vertex1 = allVertex.get(data1);
        Vertex<T> vertex2 = allVertex.get(data2);
        if (vertex1 == null) {
            vertex1 = new Vertex<>(data1);
            addVertex(vertex1);
        }
        if (vertex2 == null) {
            vertex2 = new Vertex<>(data2);
            addVertex(vertex2);
        }
        if (vertex1.getAdjacentVertexes().contains(vertex2)) return;
        Edge<T> edge = new Edge<>(vertex1, vertex2);
        allEdges.add(edge);
        vertex1.addAdjacentVertex(edge, vertex2);
    }


    public Set<Edge<T>> getAllEdges() {
        return allEdges;
    }

    public Collection<Vertex<T>> getAllVertex() {
        return allVertex.values();
    }

    @Override
    public java.lang.String toString() {
        StringBuilder buffer = new StringBuilder();
        for (Edge<T> edge : getAllEdges()) {
            buffer.append(edge.getVertex1()).append(" ").append(edge.getVertex2());
            buffer.append("\n");
        }
        return buffer.toString();
    }
}

