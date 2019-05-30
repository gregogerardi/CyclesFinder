package tarjan;

import edu.uci.ics.jung.graph.DirectedGraph;

import java.util.*;


/**
 * From Wikipedia @See <a href="https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm">Finding All the SCC</a>:
 *
 * algorithm tarjan is
 * input: graph G = (V, E)
 * output: set of strongly connected components (sets of vertices)
 *
 * index := 0
 * S := empty stack
 * for each v in V do
 * if (v.index is undefined) then
 * strongconnect(v)
 * end if
 * end for
 *
 * function strongconnect(v)
 * // Set the depth index for v to the smallest unused index
 * v.index := index
 * v.lowlink := index
 * index := index + 1
 * S.push(v)
 * v.onStack := true
 *
 * // Consider successors of v
 * for each (v, w) in E do
 * if (w.index is undefined) then
 * // Successor w has not yet been visited; recurse on it
 * strongconnect(w)
 * v.lowlink  := min(v.lowlink, w.lowlink)
 * else if (w.onStack) then
 * // Successor w is in stack S and hence in the current SCC
 * // If w is not on stack, then (v, w) is a cross-edge in the DFS tree and must be ignored
 * // Note: The next line may look odd - but is correct.
 * // It says w.index not w.lowlink; that is deliberate and from the original paper
 * v.lowlink  := min(v.lowlink, w.index)
 * end if
 * end for
 * <p>
 * // If v is a root node, pop the stack and generate an SCC
 * if (v.lowlink = v.index) then
 * start a new strongly connected component
 * repeat
 * w := S.pop()
 * w.onStack := false
 * add w to current strongly connected component
 * while (w != v)
 * output the current strongly connected component
 * end if
 * end function
 *
 *
 * The index variable is the depth-first search node number counter. S is the node stack, which starts out empty and stores
 * the history of nodes explored but not yet committed to a strongly connected component. Note that this is not the normal
 * depth-first search stack, as nodes are not popped as the search returns up the tree; they are only popped when an entire
 * strongly connected component has been found. The outermost loop searches each node that has not yet been visited, ensuring
 * that nodes which are not reachable from the first node are still eventually traversed. The function strongconnect performs a
 * single depth-first search of the graph, finding all successors from the node v, and reporting all strongly connected
 * components of that subgraph. When each node finishes recursing, if its lowlink is still set to its index, then it is the
 * root node of a strongly connected component, formed by all of the nodes above it on the stack. The algorithm pops the
 * stack up to and including the current node, and presents all of these nodes as a strongly connected component. Note that
 * v.lowlink := min(v.lowlink, w.index) is the correct way to update v.lowlink if w is on stack. Because w is on the stack
 * already, (v, w) is a back-edge in the DFS tree and therefore w is not in the subtree of v. Because v.lowlink takes into
 * account nodes reachable only through the nodes in the subtree of v we must stop at w and use w.index instead of w.lowlink.
 *
 * Time Complexity: The Tarjan procedure is called once for each node; the for all statement considers each edge at most once.
 * The algorithm's running time is therefore linear in the number of edges and nodes in G, i.e. O(|V|+|E|)
 * In order to achieve this complexity, the test for whether w is on the stack should be done in constant time.
 *
 * Space Complexity: The Tarjan procedure requires supplementary data per vertex for the index, lowlink and onStack fields.
 * The worst-case size of the stack S must be |V| (i.e. when the graph is one giant component). This gives a final analysis of O(|V|)).
 **/

public class Tarjan {

    public static <NodeType> List<List<NodeType>> tarjan(DirectedGraph<NodeType, ?> dg) {
        int[] index = new int[1];
        Stack<NodeType> stack = new Stack<>();
        Set<NodeType> stackSet = new HashSet<>();
        Map<NodeType, Integer> indexMap = new HashMap<>();
        Map<NodeType, Integer> lowLinkMap = new HashMap<>();
        List<List<NodeType>> result = new ArrayList<>();
        dg.getVertices().forEach(v -> {
            if (indexMap.get(v) == null) {
                result.addAll(Tarjan.strongConnect(v, index, stack, stackSet, indexMap, lowLinkMap, dg));
            }
        });
        return result;
    }

    private static <NodeType> List<List<NodeType>> strongConnect(NodeType v, int[] index, Stack<NodeType> stack, Set<NodeType> stackSet, Map<NodeType, Integer> indexMap, Map<NodeType, Integer> lowLinkMap, DirectedGraph<NodeType, ?> dg) {
        indexMap.put(v, index[0]);
        lowLinkMap.put(v, index[0]);
        index[0]++;
        stack.push(v);
        stackSet.add(v);
        List<List<NodeType>> result = new ArrayList<>();
        for (NodeType nodeType : dg.getSuccessors(v)) {
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
            List<NodeType> sccList = new ArrayList<>();
            while (true) {
                NodeType w = stack.pop();
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
