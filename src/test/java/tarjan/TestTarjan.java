package tarjan;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import org.junit.jupiter.api.Test;
import utils.DummyEdge;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestTarjan {

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
    public void testBinarySCC() {
        DirectedGraph<String, DummyEdge> dg = new DirectedSparseGraph<>();
        dg.addEdge(new DummyEdge(), STRING_1, STRING_2);
        dg.addEdge(new DummyEdge(), STRING_2, STRING_1);
        Tarjan<String, DummyEdge> t = new Tarjan<>(dg);
        List<List<String>> sccs = t.tarjan();
        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).contains(STRING_1));
        assertTrue(sccs.get(0).contains(STRING_2));
    }

    /**
     * This test asserts that a graph with a sinlge edge does not contain strongly connected components.
     */

    @Test
    public void testSingleEdge() {
        DirectedGraph<String, DummyEdge> dg = new DirectedSparseGraph<>();
        dg.addEdge(new DummyEdge(), STRING_5, STRING_6);
        Tarjan<String, DummyEdge> t = new Tarjan<>(dg);
        List<List<String>> sccs = t.tarjan();
        assertEquals(0, sccs.size());
    }

    /**
     * Test for discovery of a strongly connected component with 4 nodes (a cycle).
     */

    @Test
    public void test4NodesCycle() {
        DirectedGraph<String, DummyEdge> dg = new DirectedSparseGraph<>();
        dg.addEdge(new DummyEdge(), STRING_5, STRING_6);
        dg.addEdge(new DummyEdge(), STRING_6, STRING_7);
        dg.addEdge(new DummyEdge(), STRING_7, STRING_8);
        dg.addEdge(new DummyEdge(), STRING_8, STRING_5);
        Tarjan<String, DummyEdge> t = new Tarjan<>(dg);
        List<List<String>> sccs = t.tarjan();
        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).contains(STRING_5));
        assertTrue(sccs.get(0).contains(STRING_6));
        assertTrue(sccs.get(0).contains(STRING_7));
        assertTrue(sccs.get(0).contains(STRING_8));
    }

    /**
     * Test for discovery of two binary strongly connected components.
     */

    @Test
    public void test2BinarySCC() {
        DirectedGraph<String, DummyEdge> dg = new DirectedSparseGraph<>();
        dg.addEdge(new DummyEdge(), STRING_5, STRING_6);
        dg.addEdge(new DummyEdge(), STRING_6, STRING_5);
        dg.addEdge(new DummyEdge(), STRING_7, STRING_8);
        dg.addEdge(new DummyEdge(), STRING_8, STRING_7);
        Tarjan<String, DummyEdge> t = new Tarjan<>(dg);
        List<List<String>> sccs = t.tarjan();
        assertEquals(2, sccs.size());
        assertTrue(sccs.get(1).contains(STRING_5));
        assertTrue(sccs.get(1).contains(STRING_6));
        assertTrue(sccs.get(0).contains(STRING_7));
        assertTrue(sccs.get(0).contains(STRING_8));
    }

    /*
     *	Test for discovering lower level Strongly Connected Components
     */

    @Test
    public void testLowerLevelComponents() {
        DirectedGraph<String, DummyEdge> dg = new DirectedSparseGraph<>();
        dg.addEdge(new DummyEdge(), STRING_1, STRING_2);
        dg.addEdge(new DummyEdge(), STRING_2, STRING_3);
        dg.addEdge(new DummyEdge(), STRING_2, STRING_4);
        dg.addEdge(new DummyEdge(), STRING_3, STRING_2);
        dg.addEdge(new DummyEdge(), STRING_3, STRING_4);
        dg.addEdge(new DummyEdge(), STRING_4, STRING_3);

        // for this graph, all nodes are discovered in one "top-level" strongConnect() call
        // (specifically the call to strongConnect(STRING_1)) and the actual component is added to
        // the components list at a "lower-level" call to strongConnect()

        Tarjan<String, DummyEdge> tarjan = new Tarjan<>(dg);
        List<List<String>> sccs = tarjan.tarjan();
        assertEquals(1, sccs.size());
        assertTrue(sccs.get(0).contains(STRING_2));
        assertTrue(sccs.get(0).contains(STRING_3));
        assertTrue(sccs.get(0).contains(STRING_4));
    }

}
