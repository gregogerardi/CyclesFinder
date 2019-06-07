package johnson;

import graph.DirectedGraph;
import graph.Vertex;
import johnson.Johnson.JohnsonIllegalStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static johnson.Johnson.NO_MAX_LIMIT;
import static johnson.Johnson.NO_MIN_LIMIT;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Utils.compareCircuitsLists;

@DisplayName("Tests for class Johnson")
class TestJohnson {

    private static final String STRING_1 = "string1";
    private static final String STRING_2 = "string2";
    private static final String STRING_3 = "string3";
    private static final String STRING_4 = "string4";
    private static final String STRING_5 = "string5";
    private static final String STRING_6 = "string6";
    private static final int NUMBER_OF_NODES = 10;
    private static final int NUMBER_OF_EDGES = 50;

    @Nested
    @DisplayName("Tests for method findCircuits")
    class FindCircuits {

        /**
         * Test for detection of two binary cycles.
         *
         * @throws JohnsonIllegalStateException exception
         */
        @Test
        void testJohnson2BinaryCycle() throws JohnsonIllegalStateException {
            DirectedGraph<String> dsg = new DirectedGraph<>();
            dsg.addEdge(STRING_1, STRING_3);
            dsg.addEdge(STRING_3, STRING_1);
            dsg.addEdge(STRING_3, STRING_2);
            dsg.addEdge(STRING_2, STRING_3);
            List<List<String>> circuits = Johnson.findCircuits(dsg, NO_MIN_LIMIT, NO_MAX_LIMIT);
            List<String> expected1 = new ArrayList<>(Arrays.asList(STRING_1, STRING_3));
            List<String> expected2 = new ArrayList<>(Arrays.asList(STRING_2, STRING_3));
            assertSame(circuits.size(), 2);
            assertTrue(compareCircuitsLists(Arrays.asList(expected1, expected2), circuits));
        }

        /**
         * Test with one ternary cycle.
         *
         * @throws JohnsonIllegalStateException exception
         */
        @Test
        void testJohnson1TernaryCycle() throws JohnsonIllegalStateException {
            DirectedGraph<String> dg = new DirectedGraph<>();
            dg.addEdge(STRING_1, STRING_2);
            dg.addEdge(STRING_2, STRING_1);
            dg.addEdge(STRING_2, STRING_3);
            dg.addEdge(STRING_4, STRING_5);
            dg.addEdge(STRING_5, STRING_6);
            dg.addEdge(STRING_6, STRING_4);
            List<String> expected1 = new ArrayList<>(Arrays.asList(STRING_1, STRING_2));
            List<String> expected2 = new ArrayList<>(Arrays.asList(STRING_4, STRING_5, STRING_6));
            List<List<String>> circuits = Johnson.findCircuits(dg, NO_MIN_LIMIT, NO_MAX_LIMIT);
            assertSame(2, circuits.size());
            assertTrue(circuits.containsAll(Arrays.asList(expected1, expected2)));
        }

        /**
         * Test for a graph with one binary cycle.
         *
         * @throws JohnsonIllegalStateException exception
         */
        @Test
        void testJohnson1BinaryCycle() throws JohnsonIllegalStateException {
            DirectedGraph<String> dsg = new DirectedGraph<>();
            dsg.addEdge(STRING_2, STRING_1);
            dsg.addEdge(STRING_1, STRING_2);
            dsg.addEdge(STRING_3, STRING_2);
            dsg.addEdge(STRING_3, STRING_1);
            List<List<String>> circuits = Johnson.findCircuits(dsg, NO_MIN_LIMIT, NO_MAX_LIMIT);
            List<String> expected1 = new ArrayList<>(Arrays.asList(STRING_1, STRING_2));
            assertEquals(1, circuits.size());
            assertTrue(compareCircuitsLists(Collections.singletonList(expected1), circuits));
        }

        /**
         * Test for a graph with one binary cycle and one ternary cycle using the same nodes from the binary one
         *
         * @throws JohnsonIllegalStateException exception
         */
        @Test
        void testJohnson1Binary1ternary() throws JohnsonIllegalStateException {
            DirectedGraph<String> dsg = new DirectedGraph<>();
            dsg.addEdge(STRING_1, STRING_3);
            dsg.addEdge(STRING_3, STRING_2);
            dsg.addEdge(STRING_3, STRING_1);
            dsg.addEdge(STRING_2, STRING_1);
            List<List<String>> circuits = Johnson.findCircuits(dsg, NO_MIN_LIMIT, NO_MAX_LIMIT);
            List<String> expected1 = new ArrayList<>(Arrays.asList(STRING_1, STRING_3));
            List<String> expected2 = new ArrayList<>(Arrays.asList(STRING_1, STRING_3, STRING_2));
            assertEquals(2, circuits.size());
            assertTrue(compareCircuitsLists(Arrays.asList(expected1, expected2), circuits));
        }

        /**
         * Test for random graph with a minimum length for the circuits
         *
         * @throws JohnsonIllegalStateException exception
         */
        //this test could take a while for a dense graph
        @Test
        void testMinLimit() throws JohnsonIllegalStateException {
            int limit = 3;
            DirectedGraph<String> dg = new DirectedGraph<>();
            Random r = new Random();
            int nodes = NUMBER_OF_NODES;
            int edges = NUMBER_OF_EDGES;
            while (edges > 0) {
                String from = String.valueOf(r.nextInt(nodes) + 1);
                String to = String.valueOf(r.nextInt(nodes) + 1);
                if (!from.equals(to)) {
                    dg.addEdge(from, to);
                    edges--;
                }
            }
            List<List<String>> circuitsWithoutLimit = Johnson.findCircuits(dg, NO_MIN_LIMIT, NO_MAX_LIMIT);
            List<List<String>> circuitsWithLimits = Johnson.findCircuits(dg, limit, NO_MAX_LIMIT);
            circuitsWithoutLimit.removeIf(l -> l.size() < limit);
            assertTrue(compareCircuitsLists(circuitsWithoutLimit, circuitsWithLimits));
        }

        /**
         * Test for random graph with a maximum length for the circuits
         *
         * @throws JohnsonIllegalStateException exception
         */
        //this test could take a while for a dense graph
        @Test
        void testMaxLimit() throws JohnsonIllegalStateException {
            int limit = 5;
            DirectedGraph<String> dg = new DirectedGraph<>();
            Random r = new Random();
            int nodes = NUMBER_OF_NODES;
            int edges = NUMBER_OF_EDGES;
            while (edges > 0) {
                String from = String.valueOf(r.nextInt(nodes) + 1);
                String to = String.valueOf(r.nextInt(nodes) + 1);
                if (!from.equals(to)) {
                    dg.addEdge(from, to);
                    edges--;
                }
            }
            List<List<String>> circuitsWithoutLimits = Johnson.findCircuits(dg, NO_MIN_LIMIT, NO_MAX_LIMIT);
            List<List<String>> circuitsWithLimits = Johnson.findCircuits(dg, NO_MIN_LIMIT, limit);
            circuitsWithoutLimits.removeIf(l -> l.size() > limit);
            assertTrue(compareCircuitsLists(circuitsWithoutLimits, circuitsWithLimits));
        }

    }

    @Nested
    @DisplayName("Tests for method subGraphFrom")
    class SubGraphFrom {

        @Test
        void testSubGraphFrom() {
            DirectedGraph<String> dsg = new DirectedGraph<>();
            dsg.addEdge(STRING_1, STRING_3);
            dsg.addEdge(STRING_3, STRING_1);
            dsg.addEdge(STRING_3, STRING_2);
            dsg.addEdge(STRING_2, STRING_3);
            DirectedGraph<String> subGraph = Johnson.subGraphFrom(new Vertex<>(STRING_1), dsg);
            assertFalse(subGraph.getAllVertex().contains(new Vertex<>(STRING_1)));
            assertTrue(subGraph.getAllVertex().contains(new Vertex<>(STRING_2)));
            assertTrue(subGraph.getAllVertex().contains(new Vertex<>(STRING_3)));
        }
    }

    @Nested
    @DisplayName("Tests for method minSCC")
    class MinSCC {

        /**
         * Test for detection of strongly connected components.
         *
         * @throws JohnsonIllegalStateException exception
         */
        @Test
        void testLeastSCC() throws JohnsonIllegalStateException {
            DirectedGraph<String> dsg = new DirectedGraph<>();
            dsg.addEdge(STRING_1, STRING_2);
            dsg.addEdge(STRING_2, STRING_1);
            dsg.addEdge(STRING_2, STRING_3);
            dsg.addEdge(STRING_2, STRING_4);
            dsg.addEdge(STRING_4, STRING_2);
            DirectedGraph<String> leastScc = Johnson.minSCC(dsg, NO_MIN_LIMIT);
            assertTrue(leastScc.getAllVertex().contains(new Vertex<>(STRING_1)));
            assertTrue(leastScc.getAllVertex().contains(new Vertex<>(STRING_2)));
            assertTrue(leastScc.getAllVertex().contains(new Vertex<>(STRING_4)));
            assertEquals(3, leastScc.getAllVertex().size());
        }
    }
}
