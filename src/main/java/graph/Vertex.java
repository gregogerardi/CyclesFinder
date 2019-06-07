package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Representacion de un Vertice
 *
 * @param <T> tipo de elemento contenido dentro de cada vertice
 */
public class Vertex<T extends Comparable<? super T>> implements Comparable<Vertex<T>> {
    private T data;
    private List<Edge<T>> edges = new ArrayList<>();
    private List<Vertex<T>> adjacentVertex = new ArrayList<>();

    public Vertex(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    void addAdjacentVertex(Edge<T> e, Vertex<T> v) {
        edges.add(e);
        adjacentVertex.add(v);
    }

    public String toString() {
        return data.toString();
    }

    public List<Vertex<T>> getAdjacentVertexes() {
        return adjacentVertex;
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        return data.equals(other.getData());
    }

    public int compareTo(Vertex<T> v2) {
        return this.getData().compareTo(v2.getData());
    }
}
