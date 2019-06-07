package tarjan;

import graph.DirectedGraph;
import graph.Vertex;

import java.util.*;

/**
 * Implementacion del algoritmo de tarjan para encontrar las componentes fuertemente conectadas de un grafo
 */
/*
 algorithm tarjan es
  input: graph G = (V, E) //V set of vértices, E set of edges
  output: set of strongly connected components (sets of vertices)

  index := 0 //used to numerate the nodes
  S := empty stack //used to keep track of the invariant property of the algorithm
  for each v in V do
    if (v.index is undefined) then //vertex not visited yet and so had not been enumerated
      strongconnect(v)
    end if
  end for

  function strongconnect(v)
    v.index := index     // Set the index for v to the smallest unused index
    v.lowlink := index  // represents the smallest index of any node known to be reachable
from v through v's DFS subtree, including v itself.
    index := index + 1
    S.push(v) // add the current vertex to the stack
    v.onStack := true //flag used to recognize if a vertex is on the stack in linear time
    for each (v, w) in E do     // Consider successors of v
      if (w.index is undefined) then // Successor w has not yet been visited; recurse on it
        strongconnect(w)
        v.lowlink  := min(v.lowlink, w.lowlink) //update the lowlink of the parent vertex taking in
concideration the current child and its lowlink
      else if (w.onStack) then
        // Successor w is in stack S and hence in the current SCC
        // If w is not on stack, then (v, w) is a cross-edge in the DFS tree and must be ignored
      v.lowlink  := min(v.lowlink, w.index) //update v lowlink taking in concideration the index
of w
      end if
    end for

      if (v.lowlink = v.index) then  // If v is a root node, pop the stack and generate an SCC
      start a new strongly connected component
      repeat
        w := S.pop() //remove from the stack
        w.onStack := false
        add w to current strongly connected component
      while (w != v) //until we reach the root node
      output the current strongly connected component
    end if
  end function

La variable de index es el contador del número de nodo de la búsqueda en profundidad. S es la pila de nodos, que comienza
 vacía y almacena el historial de nodos explorados pero aún no vinculado con un componente fuertemente conectado. Tener en
 cuenta que esta no es la pila de DFS, ya que los nodos no se retiran cuando la búsqueda vuelve al árbol; solo se extraen
 cuando se ha encontrado un componente completo fuertemente conectado.
El bucle más externo busca en cada nodo que aún no ha sido visitado, lo que garantiza que los nodos a los que no se puede
acceder desde el primer nodo aún se atraviesen. La función strongconnect realiza una única búsqueda en profundidad del
gráfico, encontrando a todos los sucesores del nodo v e informando todos los componentes fuertemente conectados de ese subgrafo.
Cuando cada nodo termina su recursión, si su lowlink todavía es igual a su índice, entonces es el nodo raíz de un componente
 fuertemente conectado, formado por todos los nodos que se encuentran sobre él en la pila. El algoritmo retira nodos de
 la pila hasta el nodo actual, incluido este, y presenta todos estos nodos como un componente fuertemente conectado.
Tener en cuenta que v.lowlink: = min (v.lowlink, w.index) es la forma correcta de actualizar v.lowlink si w está en
la pila. Debido a que w ya está en la pila, (v, w) es un arco posterior en el árbol DFS y, por lo tanto, w no está en
el subárbol de v. Porque v.lowlink toma en cuenta los nodos accesibles solo a través de los nodos en el subárbol de v
debemos detenernos en w y usar w.index en lugar de w.lowlink.


Complejidad
Complejidad temporal: el procedimiento de Tarjan se llama una vez para cada nodo; la instrucción foreach considera cada
arco como máximo una vez. El tiempo de ejecución del algoritmo es, por lo tanto, lineal en el número de arcos y nodos en
 G, es decir, O (| V | + | E |)
Para lograr esta complejidad, la prueba de si w está en la pila debe realizarse en un tiempo constante. Esto se puede
hacer, por ejemplo, almacenando una bandera en cada nodo que indica si está en la pila y realizando esta prueba examinando
 la bandera. En nuestra implementación se realizó mediante un hashmap de tiempo de acceso constante

Complejidad espacial: el procedimiento de Tarjan requiere 2 datos suplementarios por vértice para los campos de índice
y low link, junto con un booleano para onStack. Además, se requiere un espacio en la pila para mantener v y otro dato
para la posición actual en la lista de arcos. Finalmente, el tamaño del peor de los casos de la pila S debe ser | V |
(es decir, cuando el grafo es un componente gigante). Esto da un análisis final de O (| V |).

*/


public class Tarjan {

    public static <NodeType extends Comparable<? super NodeType>> List<List<Vertex<NodeType>>> tarjan(DirectedGraph<NodeType> dg) {
        int[] index = new int[1];
        List<Vertex<NodeType>> stack = new ArrayList<>();
        Set<Vertex<NodeType>> stackSet = new HashSet<>();
        Map<Vertex<NodeType>, Integer> indexMap = new HashMap<>();
        Map<Vertex<NodeType>, Integer> lowLinkMap = new HashMap<>();
        List<List<Vertex<NodeType>>> result = new ArrayList<>();
        dg.getAllVertex().forEach(v -> {
            if (indexMap.get(v) == null) {
                result.addAll(Tarjan.strongConnect(v, index, stack, stackSet, indexMap, lowLinkMap, dg));
            }
        });
        return result;
    }

    private static <NodeType extends Comparable<? super NodeType>> List<List<Vertex<NodeType>>> strongConnect(Vertex<NodeType> v, int[] index, List<Vertex<NodeType>> stack, Set<Vertex<NodeType>> stackSet, Map<Vertex<NodeType>, Integer> indexMap, Map<Vertex<NodeType>, Integer> lowLinkMap, DirectedGraph<NodeType> dg) {
        indexMap.put(v, index[0]);
        lowLinkMap.put(v, index[0]);
        index[0]++;
        stack.add(v);
        stackSet.add(v);
        List<List<Vertex<NodeType>>> result = new ArrayList<>();
        for (Vertex<NodeType> nodeType : v.getAdjacentVertexes()) {
            if (indexMap.get(nodeType) == null) {
                result.addAll(Tarjan.strongConnect(nodeType, index, stack, stackSet, indexMap, lowLinkMap, dg));
                lowLinkMap.put(v, Math.min(lowLinkMap.get(v), lowLinkMap.get(nodeType)));
            } else {
                if (stackSet.contains(nodeType)) {
                    lowLinkMap.put(v, Math.min(lowLinkMap.get(v), indexMap.get(nodeType)));
                }
            }
        }

        if (lowLinkMap.get(v).equals(indexMap.get(v))) {
            List<Vertex<NodeType>> sccList = new ArrayList<>();
            while (true) {
                Vertex<NodeType> w = stack.remove(stack.size() - 1);
                stackSet.remove(w);
                sccList.add(w);
                if (w.equals(v)) break;
            }
            if (sccList.size() > 1) {
                result.add(sccList);
            } // don't return trivial sccs in the form of single nodes.
        }
        return result;
    }

}
