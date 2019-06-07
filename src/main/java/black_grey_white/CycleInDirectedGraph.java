package black_grey_white;

import graph.DirectedGraph;
import graph.Vertex;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * implementación de un algoritmo basado en una busqueda DFS con marcas de visita para encontrar todos los circuitos en un grafo
 */

/*
pseudocodigo, complejidad temporal y estructuras utilizadas:
para encontrar todos los ciclos dado un largo maximo y uno minimo

   fun findCycles(Grafo graph, max, min): ListaDeCircuitos
    {
        Set blackSet = {} // implementado como HashSet con capacidad inicial igual al doble de vertices del grafo
                            //al finalizar el algoritmo todos los vertices estaran en blackset)
        Set graySet = {} // implementado como HashSet con capacidad inicial igual a la cantidad de vertices del grafo
                            //en el peor de los caso todos los vertices estaran en gray set (unico ciclo que recorra todos los
                            //vertices una vez) pero habitualmente la cantidad sera mucho menor.
        Set whiteSet = {} // implementado como HashSet con capacidad inicial igual al doble de vertices del grafo
                            //al iniciar el algoritmo todos los vertices estaran en whiteSet)
        Stack currentCycle = {} // implementado como ArrayList<>() para evitar el overhead de la clase Stack por ser synchronized;
                                // siempre se agrega y elimina el ultimo elemnto, en O(1)
        ListaDeCircuitos results = {} // implementado como HashSet de Arraylists;
        Mientras (whiteSet.notEmpty()) {
            Vertice current = whiteSet.first()
            findCycles(current, whiteSet, graySet, blackSet, currentCycle, results, max, min);
        }
        return results;
    }

    fun findCycles(Vertice current, Set whiteSet, Set graySet, Set blackSet, Stack currentCycle, ListDeCircuitos results, int max, int min): boolean {
        if (currentCycle.tamaño >= max) { // si alcanzamos el largo maximo para el circuito, este camino no nos va a dar ningun ciclo de tamaño
                                          // menor al limite, entonces no continuamos explorando este vertice. Como el vertice no fue completamente
                                          // explorado (agotados recursivamente todos los hijos) devolvemos false para indicar al nodo padre que el
                                          // tampoco fue completamente explorado. Los nodos no completamente explorados son candidatos para ser raiz de
                                          // un futuro DFS, siempre y cuando no lo fuesen ya.
            return false;
        }

        moveVertex(current, whiteSet, graySet); //move current to gray set from white set and then explore it.
                                          //se realiza en O(1), los accesos a hashset para inserción y borrado son ctes
        currentCycle.push(current); // implementado como arratylist con add en O(1)
        List successors = current.getAdyacentes(); // se realiza en O(1)
        boolean isClosed = true; // usado para controlar si el vertice actual es completamente explorado
        for (Vertice neighbor : successors) {
            if (graySet.contains(neighbor))//si el hijo esta en graySet significa que ya fue visitado en este dfs
                                           // y que encontramos un ciclo
             {
                results.add(currentCycle.copiarHastaNeighbor().invertir().minVerticeFirst()); //El ciclo encontrdo se encuentra en el stack de currentCycle
                                            //se encuentra removiendo elementos del tope hasta llegar nuevamente el neighbor actual
                                            //esto nos da una lista en orden inverso del ciclo. Se la invierte, y luego rota hasta que el nodo mas chico
                                            //quede primero para poder compararlo con los resultados ya agregados y
                                            //evitar agregar permutaciones de ciclos repetidos. Se agrega a los resultados si no esta ya
                                            //copiarHastaNeighbor(), invertir() se, y minVerticeFirst() realizan en
                                            //O(largo del ciclo) que en el peor caso  O(min(N,max)). buscar en resultados
                                            // si el circuito ya esta agregado y agregarlo se realiza en O(1)                }
                isClosed = false; //encontre un circuito entonces no sigo explorando, lo marcamos como no explorado completo
            }
            else if (!blackSet.contains(neighbor)) {//si esta en blackset es que ya se exploro completo y todos los circuitos
                                            // que lo incluyen fuerone encontrado, no explorar de nuevo
                boolean closeChilds = findCycles(neighbor, whiteSet, graySet, blackSet, currentCycle, results, max, min);
                                            //exploramos el hijo aun no explorado
                isClosed = isClosed AND closeChilds; //si el hijo no fue completamente explroado, marcamos al actual como
                                            //tampoco completamente explorado
            }
        }
        //move vertex away from gray set when done exploring.
        // if this vertex is ready being explored (without unexplored path trunked by the max restriction and when no
        // cycle was found or even if this happened but we start the search with this vertex), we move it to the
        //black set so it would never be explored again. Otherwise, we move it again to the white set
        if (isClosed or currentCycle.size() == 1) {
            moveVertex(current, graySet, blackSet);
        } else {
            moveVertex(current, graySet, whiteSet);
        }
        currentCycle.pop;
        return isClosed;
    }

   la complejidad temporal resultante es: por cada Nodo N, se recorren todos los vecino menos los ya recorridos
   (Maximo de N vecinos menos el actual) recursivamente (O(N!) con una profundidad maxima de max (O(N*(N-1)*(N-2)*...*(N-max))
   o O(N^max)) y en los casos donde se encuentre un ciclo se debe copiar el stack actual de circuito (de largo maximo = min(N,max)).
   Resulta en O(N*N^max*max)= O(N^max). En un caso sin maximo, el maximo nunca sera mas largo que el total de nodos N
   Resultando en O(N^N)


   para saber si algun ciclo pasa por dos vertices, utilizamos una version modificada del algoritmo anterior, que incluye
   optimizaciones como:
        No continuar con el recorrido una vez que se detecto el primer ciclo que incluya los vertices pedidos
        Iniciar el dfs siempre por uno de los vertices a incluir, si este dfs no encuentra el ciclo, no es posible que se lo halle
        iniciando desde ningun otro vertice, esta optimización achica el grafo a recorrer a solo la componente fuertemente
        conectada que incluya al vertice inicial. No preguntar por todos los ciclos durante el DFS, sino solo por aquellos que
        terminen en el mismo vertice que iniciaron (el resto de los ciclos nunca incluira al vertice original que es uno de los
        solicitados en el ciclo).
   Aun con estas optimizaciones, en el peor de los casos la complejidad temporal sigue siendo la misma: O(N^max)
 */

public class CycleInDirectedGraph {

    public static final int NO_MAX = (Integer.MAX_VALUE - 1);
    public static final int NO_MIN = 0;

    /**
     * Metodo para allar todos los ciclos en un grafo sin tener en cuenta longitudes minimas o maximas para el mismo.
     * Tener en cuenta que sin tener longitud maxima que acote la cantidad de niveles de la recursion de este algoritmo
     * para grafos particularmente largo es recomendable utilizar en su lugar en algoritmo de {@link johnson.Johnson}
     *
     * @param graph en el que buscar los ciclos
     * @param <T>   tipo de nodo contenido en el grafo. Debe implementar {@link Comparable} para ordenar los nodos
     *              en los ciclos encontrados y evitar almacenar ciclos repetidos.
     * @return {@link List} una lista concentiendo los ciclos encontrados de elementos de tipo {@link T}
     */
    public static <T extends Comparable<? super T>> List<List<T>> findCycles(DirectedGraph<T> graph) {
        return findCycles(graph, NO_MAX, NO_MIN);
    }

    /**
     * Metodo para allar todos los ciclos en un grafo teniendo en cuenta longitudes minimas y maximas para el mismo.
     * Tener en cuenta que para longitudes maximas altas la cantidad de niveles de la recursion de este algoritmo es alta.
     * para grafos particularmente largos con longitudes maximas altas es recomendable utilizar en su lugar en algoritmo
     * de {@link johnson.Johnson}
     *
     * @param graph grafo en el que buscar los ciclos
     * @param max   longitud maxima de los ciclos a buscar
     * @param min   longitud minima de los ciclos a buscar
     * @param <T>   tipo de nodo contenido en el grafo. Debe implementar {@link Comparable} para ordenar los nodos
     *              en los ciclos encontrados y evitar almacenar ciclos repetidos.
     * @return {@link List} una lista concentiendo los ciclos encontrados de elementos de tipo {@link T}
     */
    public static <T extends Comparable<? super T>> List<List<T>> findCycles(DirectedGraph<T> graph, int max, int min) {
        Set<Vertex<T>> whiteSet = new HashSet<>(graph.getAllVertex());
        Set<Vertex<T>> graySet = new HashSet<>(graph.getAllVertex().size());
        Set<Vertex<T>> blackSet = new HashSet<>(graph.getAllVertex().size() * 2);
        List<Vertex<T>> currentCycle = new ArrayList<>();
        Set<List<Vertex<T>>> results = new HashSet<>();
        while (!whiteSet.isEmpty()) {
            Vertex<T> current = whiteSet.iterator().next();
            CycleInDirectedGraph.findCycles(current, whiteSet, graySet, blackSet, currentCycle, results, max, min);
        }
        return results.stream().map(list -> list.stream().map(Vertex::getData).collect(toList())).collect(toList());
    }

    private static <T extends Comparable<? super T>> boolean findCycles(Vertex<T> current, Set<Vertex<T>> whiteSet,
                                                                        Set<Vertex<T>> graySet, Set<Vertex<T>> blackSet, List<Vertex<T>> currentCycle, Set<List<Vertex<T>>> results, int max, int min) {
        if (currentCycle.size() >= max) {
            return false;
        }
        moveVertex(current, whiteSet, graySet);
        currentCycle.add(current);
        boolean isClosed = true;
        for (Vertex<T> neighbor : current.getAdjacentVertexes()) {
            if (graySet.contains(neighbor)) {
                int i = currentCycle.size() - 1;
                ArrayList<Vertex<T>> localResult = new ArrayList<>(currentCycle.size());
                Vertex<T> localVertex;
                do {
                    localVertex = currentCycle.get(i);
                    localResult.add(localVertex);
                    i--;
                } while (localVertex != neighbor);
                if (localResult.size() >= min) {
                    localResult.trimToSize();
                    Collections.reverse(localResult);
                    rotateUntilMinFirst(localResult);
                    results.add(localResult);
                }
                isClosed = false;
                continue;
            }
            if (!blackSet.contains(neighbor)) {
                boolean closeChilds = findCycles(neighbor, whiteSet, graySet, blackSet, currentCycle, results, max, min);
                isClosed = isClosed && closeChilds;
            }
        }
        if (isClosed || currentCycle.size() == 1) {
            //uncoment next line to show a progress percentage by the console
            System.out.println(100 * blackSet.size() / (whiteSet.size() + graySet.size() + blackSet.size()) + "%");
            moveVertex(current, graySet, blackSet);
        } else {
            moveVertex(current, graySet, whiteSet);
        }
        currentCycle.remove(currentCycle.size() - 1);
        return isClosed;
    }

    /**
     * Metodo para averiguar si existe algun ciclo que pase por un par dado de vertices.
     *
     * @param v1  el {@link Vertex<T>} desde donde iniciar la busqueda y uno de los vertices que deben incluirse en el ciclo
     * @param v2  otro {@link Vertex<T>} vertice que deben incluirse en el ciclo
     * @param max longitud maxima de los ciclos a buscar
     * @param min longitud minima de los ciclos a buscar
     * @param <T> tipo de nodo contenido en el grafo. Debe implementar {@link Comparable}
     * @return boolean indicando si se encontro o no ciclo
     */
    public static <T extends Comparable<? super T>> boolean findCyclesThrought(DirectedGraph<T> graph, Vertex<T> v1, Vertex<T> v2, int max, int min) {
        Set<Vertex<T>> blackSet = new HashSet<>();
        Set<Vertex<T>> graySet = new HashSet<>();
        int[] currentSize = {0};
        Set<Vertex<T>> whiteSet = new HashSet<>(graph.getAllVertex());
        boolean[] v2Found = {false};
        boolean[] isCycle = {false};
        CycleInDirectedGraph.findCyclesThrought(v1, v1, v2, v2Found, isCycle, whiteSet, graySet, blackSet, currentSize, max, min);
        return isCycle[0];
    }

    private static <T extends Comparable<? super T>> boolean findCyclesThrought(Vertex<T> current, Vertex<T> v1, Vertex<T> v2, boolean[] v2Found, boolean[] isCycle, Set<Vertex<T>> whiteSet, Set<Vertex<T>> graySet, Set<Vertex<T>> blackSet, int[] currentSize, int max, int min) {
        if (currentSize[0] >= max) {
            return false;
        }
        moveVertex(current, whiteSet, graySet);
        currentSize[0]++;
        v2Found[0] = v2Found[0] || current == v2;
        boolean isClosed = true;
        for (Vertex<T> neighbor : current.getAdjacentVertexes()) {
            if (isCycle[0]) return true;
            if (graySet.contains(neighbor)) {
                if (neighbor == v1 && v2Found[0] && currentSize[0] >= min) {
                    isCycle[0] = true;
                }
                isClosed = false;
                continue;
            }
            if (!blackSet.contains(neighbor)) {
                boolean closeChilds = findCyclesThrought(neighbor, v1, v2, v2Found, isCycle, whiteSet, graySet, blackSet, currentSize, max, min);
                isClosed = isClosed && closeChilds;
            }
        }
        if (isClosed || currentSize[0] == 1) {
            moveVertex(current, graySet, blackSet);
        } else {
            moveVertex(current, graySet, whiteSet);
        }
        currentSize[0]--;
        if (current == v2) v2Found[0] = false;
        return isClosed;
    }


    private static <T extends Comparable<? super T>> void moveVertex(Vertex<T> vertex, Set<Vertex<T>> sourceSet, Set<Vertex<T>> destinationSet) {
        sourceSet.remove(vertex);
        destinationSet.add(vertex);
    }

    private static <T extends Comparable<? super T>> void rotateUntilMinFirst(List<Vertex<T>> l) {
        Vertex<T> minElem = l.get(0);
        int distanceBackwards = 0;
        for (int i = 0; i < l.size(); i++) {
            Collections.rotate(l, 1);
            if (l.get(0).compareTo(minElem) < 0) {
                minElem = l.get(0);
                distanceBackwards = 0;
            } else distanceBackwards++;
        }
        Collections.rotate(l, -distanceBackwards);
    }

    public static <T extends Comparable<? super T>> boolean findCyclesThrought(DirectedGraph<T> dg, Vertex<T> v1, Vertex<T> v2) {
        return findCyclesThrought(dg, v1, v2, NO_MAX, NO_MIN);
    }
}

