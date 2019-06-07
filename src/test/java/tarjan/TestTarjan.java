package tarjan;

import graph.DirectedGraph;
import graph.Vertex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for class Tarjan")
class TestTarjan {

    private static final String STRING_1 = "string1";
    private static final String STRING_2 = "string2";
    private static final String STRING_3 = "string3";
    private static final String STRING_4 = "string4";
    private static final String STRING_5 = "string5";
    private static final String STRING_6 = "string6";
    private static final String STRING_7 = "string7";
    private static final String STRING_8 = "string8";

    /**
     * Test for a binary strongly connected component.
     */
    @Test
    void testBinarySCC() {
        DirectedGraph<String> dg = new DirectedGraph<>();
        dg.addEdge(STRING_1, STRING_2);
        dg.addEdge(STRING_2, STRING_1);
        List<List<Vertex<String>>> sccs = Tarjan.tarjan(dg);
        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_1)));
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_2)));
    }

    /**
     * This test asserts that a graph with a sinlge edge does not contain strongly connected components.
     */
    @Test
    void testSingleEdge() {
        DirectedGraph<String> dg = new DirectedGraph<>();
        dg.addEdge(STRING_5, STRING_6);
        List<List<Vertex<String>>> sccs = Tarjan.tarjan(dg);
        assertEquals(0, sccs.size());
    }

    /**
     * Test for discovery of a strongly connected component with 4 nodes (a cycle).
     */
    @Test
    void test4NodesCycle() {
        DirectedGraph<String> dg = new DirectedGraph<>();
        dg.addEdge(STRING_5, STRING_6);
        dg.addEdge(STRING_6, STRING_7);
        dg.addEdge(STRING_7, STRING_8);
        dg.addEdge(STRING_8, STRING_5);
        List<List<Vertex<String>>> sccs = Tarjan.tarjan(dg);
        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_5)));
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_6)));
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_7)));
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_8)));
    }

    /**
     * Test for discovery of two binary strongly connected components.
     */
    @Test
    void test2BinarySCC() {
        DirectedGraph<String> dg = new DirectedGraph<>();
        dg.addEdge(STRING_5, STRING_6);
        dg.addEdge(STRING_6, STRING_5);
        dg.addEdge(STRING_7, STRING_8);
        dg.addEdge(STRING_8, STRING_7);
        List<List<Vertex<String>>> sccs = Tarjan.tarjan(dg);
        assertEquals(2, sccs.size());
        assertTrue(sccs.get(1).contains(new Vertex<>(STRING_5)));
        assertTrue(sccs.get(1).contains(new Vertex<>(STRING_6)));
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_7)));
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_8)));
    }

    /**
     * Test for discovering lower level Strongly Connected Components
     */
    @Test
    void testLowerLevelComponents() {
        DirectedGraph<String> dg = new DirectedGraph<>();
        dg.addEdge(STRING_1, STRING_2);
        dg.addEdge(STRING_2, STRING_3);
        dg.addEdge(STRING_2, STRING_4);
        dg.addEdge(STRING_3, STRING_2);
        dg.addEdge(STRING_3, STRING_4);
        dg.addEdge(STRING_4, STRING_3);
        // for this graph, all nodes are discovered in one "top-level" strongConnect() call
        // (specifically the call to strongConnect(STRING_1)) and the actual component is added to
        // the components list at a "lower-level" call to strongConnect()
        List<List<Vertex<String>>> sccs = Tarjan.tarjan(dg);
        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_2)));
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_3)));
        assertTrue(sccs.get(0).contains(new Vertex<>(STRING_4)));
    }

}
