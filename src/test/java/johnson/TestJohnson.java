package johnson;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import johnson.Johnson.JohnsonIllegalStateException;
import org.junit.jupiter.api.Test;
import utils.DummyEdge;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import static johnson.Johnson.NO_MAX_LIMIT;
import static johnson.Johnson.NO_MIN_LIMIT;
import static org.junit.jupiter.api.Assertions.*;

public class TestJohnson {

    private static final String STRING_1 = "string1";
    private static final String STRING_2 = "string2";
    private static final String STRING_3 = "string3";
    private static final String STRING_4 = "string4";
    private static final String STRING_5 = "string5";
    private static final String STRING_6 = "string6";
    private static final int NUMBER_OF_TESTS = 1;
    private static final int MAX_NODES = 10;
    private static final int MAX_EDGES = 50;

    /**
     * Test for detection of two binary cycles.
     *
     * @throws JohnsonIllegalStateException
     */

    @Test
    public void testJohnson2BinaryCycle() throws JohnsonIllegalStateException {
        DirectedGraph<String, DummyEdge> dsg = new DirectedSparseGraph<>();
        dsg.addEdge(new DummyEdge(), STRING_1, STRING_3);
        dsg.addEdge(new DummyEdge(), STRING_3, STRING_1);
        dsg.addEdge(new DummyEdge(), STRING_3, STRING_2);
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_3);
        Johnson<String, DummyEdge> j = new Johnson<>();
        List<List<String>> circuits = j.findCircuits(dsg, NO_MIN_LIMIT, NO_MAX_LIMIT);
        Stack<String> expected1 = new Stack<>();
        Stack<String> expected2 = new Stack<>();
        expected1.addAll(Arrays.asList(STRING_1, STRING_3));
        expected2.addAll(Arrays.asList(STRING_2, STRING_3));
        assertSame(circuits.size(), 2);
        assertTrue(circuits.containsAll(Arrays.asList(expected1, expected2)));
    }

    /**
     * Test with one ternary cycle.
     *
     * @throws JohnsonIllegalStateException
     */

    @Test
    public void testJohnson1TernaryCycle() throws JohnsonIllegalStateException {
        DirectedGraph<String, DummyEdge> dsg = new DirectedSparseGraph<>();
        dsg.addEdge(new DummyEdge(), STRING_1, STRING_2);
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_1);
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_3);
        dsg.addEdge(new DummyEdge(), STRING_4, STRING_5);
        dsg.addEdge(new DummyEdge(), STRING_5, STRING_6);
        dsg.addEdge(new DummyEdge(), STRING_6, STRING_4);
        Johnson<String, DummyEdge> j = new Johnson<>();
        Stack<String> expected1 = new Stack<>();
        Stack<String> expected2 = new Stack<>();
        expected1.addAll(Arrays.asList(STRING_1, STRING_2));
        expected2.addAll(Arrays.asList(STRING_4, STRING_5, STRING_6));
        List<List<String>> circuits = j.findCircuits(dsg, NO_MIN_LIMIT, NO_MAX_LIMIT);
        assertSame(circuits.size(), 2);
        assertTrue(circuits.containsAll(Arrays.asList(expected1, expected2)));
    }

    /**
     * test for the method subGraphFrom.
     */
    //todo comentar que es un from no inclusivo, va desde ese nodo en adelante sin incluirlo por orden lexicografico de los strings
    @Test
    public void testSubGraphFrom() {
        DirectedGraph<String, DummyEdge> dsg = new DirectedSparseGraph<>();
        dsg.addEdge(new DummyEdge(), STRING_1, STRING_3);
        dsg.addEdge(new DummyEdge(), STRING_3, STRING_1);
        dsg.addEdge(new DummyEdge(), STRING_3, STRING_2);
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_3);
        DirectedGraph<String, DummyEdge> subGraph = Johnson.subGraphFrom(STRING_1, dsg);
        assertFalse(subGraph.containsVertex(STRING_1));
        assertTrue(subGraph.containsVertex(STRING_2));
        assertTrue(subGraph.containsVertex(STRING_3));
    }

    /**
     * Test for a graph with one binary cycle.
     *
     * @throws JohnsonIllegalStateException
     */

    @Test
    public void testJohnson1BinaryCycle() throws JohnsonIllegalStateException {
        DirectedGraph<String, DummyEdge> dsg = new DirectedSparseGraph<>();
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_1);
        dsg.addEdge(new DummyEdge(), STRING_1, STRING_2);
        dsg.addEdge(new DummyEdge(), STRING_3, STRING_2);
        dsg.addEdge(new DummyEdge(), STRING_3, STRING_1);
        Johnson<String, DummyEdge> j = new Johnson<>();
        List<List<String>> circuits = j.findCircuits(dsg, NO_MIN_LIMIT, NO_MAX_LIMIT);
        Stack<String> expected1 = new Stack<>();
        expected1.addAll(Arrays.asList(STRING_1, STRING_2));
        assertEquals(1, circuits.size());
        assertTrue(circuits.contains(expected1));

    }

    /**
     * Test for a graph with one binary cycle and one ternary cycle using the same nodes from the binary one
     *
     * @throws JohnsonIllegalStateException
     */

    @Test
    public void testJohnson1Binary1ternarySameNodes() throws JohnsonIllegalStateException {
        DirectedGraph<String, DummyEdge> dsg = new DirectedSparseGraph<>();
        dsg.addEdge(new DummyEdge(), STRING_1, STRING_3);
        dsg.addEdge(new DummyEdge(), STRING_3, STRING_2);
        dsg.addEdge(new DummyEdge(), STRING_3, STRING_1);
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_1);
        Johnson<String, DummyEdge> j = new Johnson<>();
        List<List<String>> circuits = j.findCircuits(dsg, NO_MIN_LIMIT, NO_MAX_LIMIT);
        Stack<String> expected1 = new Stack<>();
        Stack<String> expected2 = new Stack<>();
        expected1.addAll(Arrays.asList(STRING_1, STRING_3));
        expected2.addAll(Arrays.asList(STRING_1, STRING_3, STRING_2));
        assertEquals(2, circuits.size());
        assertTrue(circuits.containsAll(Arrays.asList(expected1, expected2)));
    }

    /**
     * Test for detection of strongly connected components.
     *
     * @throws JohnsonIllegalStateException
     */

    @Test
    public void testLeastSCC() throws JohnsonIllegalStateException {
        DirectedGraph<String, DummyEdge> dsg = new DirectedSparseGraph<>();
        dsg.addEdge(new DummyEdge(), STRING_1, STRING_2);
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_1);
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_3);
        dsg.addEdge(new DummyEdge(), STRING_2, STRING_4);
        dsg.addEdge(new DummyEdge(), STRING_4, STRING_2);
        DirectedGraph<String, DummyEdge> leastScc = Johnson.minSCC(dsg, NO_MIN_LIMIT);
        assertTrue(leastScc.getVertices().contains(STRING_1));
        assertTrue(leastScc.getVertices().contains(STRING_2));
        assertTrue(leastScc.getVertices().contains(STRING_4));
        assertEquals(3, leastScc.getVertexCount());
    }

    /**
     * Test for random graph with a minimun length for the circuits
     *
     * @throws JohnsonIllegalStateException
     */
    @Test
    public void testMinLimit() throws JohnsonIllegalStateException {
        int MAX_MINIMUN = NUMBER_OF_TESTS;
        for (int i = 0; i < MAX_MINIMUN; i++) {
            DirectedGraph<String, DummyEdge> dg = new DirectedSparseGraph<>();
            Random r = new Random();
            int nodes = 30;
            int edges = 200;
            while (edges > 0) {
                String from = String.valueOf(r.nextInt(nodes) + 1);
                String to = String.valueOf(r.nextInt(nodes) + 1);
                if (dg.findEdge(from, to) == null && !from.equals(to)) {
                    dg.addEdge(new DummyEdge(), from, to);
                    edges--;
                }
            }
            Johnson<String, DummyEdge> j = new Johnson<>();
            List<List<String>> circuitsWithoutLimit = j.findCircuits(dg, NO_MIN_LIMIT, NO_MIN_LIMIT);
            List<List<String>> circuitsWithLimits = j.findCircuits(dg, i, NO_MIN_LIMIT);
            int finalI = i;
            circuitsWithoutLimit.removeIf(l -> l.size() < finalI);
            assertEquals(circuitsWithLimits, circuitsWithoutLimit);
        }
    }

    /**
     * Test for random graph with a maximun length for the circuits
     */
    @Test
    public void testMaxLimit() throws JohnsonIllegalStateException {
        int MAX_MAXIMUN = NUMBER_OF_TESTS;
        for (int i = 1; i <= MAX_MAXIMUN; i++) {
            DirectedGraph<String, DummyEdge> dg = new DirectedSparseGraph<>();
            Random r = new Random();
            int nodes = r.nextInt(MAX_NODES) + 1;
            int edges = r.nextInt(MAX_EDGES) + 1;
            while (edges > 0) {
                String from = String.valueOf(r.nextInt(nodes) + 1);
                String to = String.valueOf(r.nextInt(nodes) + 1);
                if (dg.findEdge(from, to) == null && !from.equals(to)) {
                    dg.addEdge(new DummyEdge(), from, to);
                    edges--;
                }
            }
            Johnson<String, DummyEdge> j = new Johnson<>();
            List<List<String>> circuitsWithoutLimits = j.findCircuits(dg, NO_MIN_LIMIT, NO_MAX_LIMIT);
            List<List<String>> circuitsWithLimits = j.findCircuits(dg, NO_MIN_LIMIT, i);
            int finalI = i;
            circuitsWithoutLimits.removeIf(l -> l.size() > finalI);
            assertEquals(circuitsWithoutLimits, circuitsWithLimits);
        }
    }
}

