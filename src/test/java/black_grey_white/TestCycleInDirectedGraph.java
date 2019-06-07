package black_grey_white;

import graph.DirectedGraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static black_grey_white.CycleInDirectedGraph.NO_MAX;
import static black_grey_white.CycleInDirectedGraph.NO_MIN;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Utils.compareCircuitsLists;

@DisplayName("Tests for class CycleInDirectedGraph")
class TestCycleInDirectedGraph {

    private static final String STRING_1 = "string1";
    private static final String STRING_2 = "string2";
    private static final String STRING_3 = "string3";
    private static final String STRING_4 = "string4";
    private static final String STRING_5 = "string5";
    private static final String STRING_6 = "string6";
    private static final int NUMBER_OF_NODES = 30;
    private static final int NUMBER_OF_EDGES = 80;

    @Nested
    @DisplayName("Tests for method findCycles")
    class TestsForFindCycles {

        /**
         * Test for detection of two binary cycles.
         **/
        @Test
        void test2BinaryCycle() {
            DirectedGraph<String> dg = new DirectedGraph<>();
            dg.addEdge(STRING_1, STRING_3);
            dg.addEdge(STRING_3, STRING_1);
            dg.addEdge(STRING_3, STRING_2);
            dg.addEdge(STRING_2, STRING_3);
            List<List<String>> circuits = CycleInDirectedGraph.findCycles(dg);
            List<String> expected1 = new ArrayList<>(Arrays.asList(STRING_1, STRING_3));
            List<String> expected2 = new ArrayList<>(Arrays.asList(STRING_2, STRING_3));
            assertSame(2, circuits.size());
            assertTrue(circuits.containsAll(Arrays.asList(expected1, expected2)));
        }

        /**
         * Test with one ternary cycle.
         */
        @Test
        void test1TernaryCycle() {
            DirectedGraph<String> dg = new DirectedGraph<>();
            dg.addEdge(STRING_1, STRING_2);
            dg.addEdge(STRING_2, STRING_1);
            dg.addEdge(STRING_2, STRING_3);
            dg.addEdge(STRING_4, STRING_5);
            dg.addEdge(STRING_5, STRING_6);
            dg.addEdge(STRING_6, STRING_4);
            List<String> expected1 = new ArrayList<>(Arrays.asList(STRING_1, STRING_2));
            List<String> expected2 = new ArrayList<>(Arrays.asList(STRING_4, STRING_5, STRING_6));
            List<List<String>> expecteds = Arrays.asList(expected1, expected2);
            List<List<String>> circuits = CycleInDirectedGraph.findCycles(dg);
            assertSame(2, circuits.size());
            assertTrue(compareCircuitsLists(expecteds, circuits));
        }

        /**
         * Test for a graph with one binary cycle.
         */
        @Test
        void test1BinaryCycle() {
            DirectedGraph<String> dg = new DirectedGraph<>();
            dg.addEdge(STRING_2, STRING_1);
            dg.addEdge(STRING_1, STRING_2);
            dg.addEdge(STRING_3, STRING_2);
            dg.addEdge(STRING_3, STRING_1);
            List<List<String>> circuits = CycleInDirectedGraph.findCycles(dg);
            List<String> expected1 = new ArrayList<>(Arrays.asList(STRING_1, STRING_2));
            assertEquals(1, circuits.size());
            assertTrue(compareCircuitsLists(circuits, Collections.singletonList(expected1)));
        }

        /**
         * Test for a graph with one binary cycle and one ternary cycle using the same nodes
         */
        @Test
        void test1Binary1ternary() {
            DirectedGraph<String> dg = new DirectedGraph<>();
            dg.addEdge(STRING_1, STRING_3);
            dg.addEdge(STRING_3, STRING_2);
            dg.addEdge(STRING_3, STRING_1);
            dg.addEdge(STRING_2, STRING_1);
            List<List<String>> circuits = CycleInDirectedGraph.findCycles(dg);
            List<String> expected1 = new ArrayList<>(Arrays.asList(STRING_1, STRING_3));
            List<String> expected2 = new ArrayList<>(Arrays.asList(STRING_1, STRING_3, STRING_2));
            assertEquals(2, circuits.size());
            assertTrue(compareCircuitsLists(circuits, Arrays.asList(expected1, expected2)));
        }

        /**
         * Test for random graph with a minimum length for the circuits
         **/
        //this test could take a while for a large graph
        @Test
        void testMinLimit() {
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
            List<List<String>> circuitsWithoutLimit = CycleInDirectedGraph.findCycles(dg);
            List<List<String>> circuitsWithLimits = CycleInDirectedGraph.findCycles(dg, NO_MAX, limit);
            circuitsWithoutLimit.removeIf(l -> l.size() < limit);
            assertTrue(compareCircuitsLists(circuitsWithLimits, circuitsWithoutLimit));
        }

        /**
         * Test for random graph with a maximum length for the circuits
         */
        //this test could take a while for a dense graph
        @Test
        void testMaxLimit() {
            int limit = 10;
            DirectedGraph<String> dg = new DirectedGraph<>();
            Random r = new Random();
            int nodes = NUMBER_OF_NODES;
            int edges = NUMBER_OF_EDGES;
            while (edges > 0) {
                String from = String.valueOf(r.nextInt(nodes) + 1);
                String to = String.valueOf(r.nextInt(nodes) + 1);
                if (!from.equals(to)) dg.addEdge(from, to);
                edges--;
            }
            List<List<String>> circuitsWithoutLimits = CycleInDirectedGraph.findCycles(dg);
            List<List<String>> circuitsWithLimits = CycleInDirectedGraph.findCycles(dg, limit, NO_MIN);
            circuitsWithoutLimits.removeIf(l -> l.size() > limit);
            assertTrue(compareCircuitsLists(circuitsWithoutLimits, circuitsWithLimits));
        }

        /**
         * Test for an non random graph with a maximum length for the circuits
         */
        @Test
        void testMaxLimitInstance() {
            int limit = 3;
            DirectedGraph<String> dg = new DirectedGraph<>();
            dg.addEdge(STRING_1, STRING_3);
            dg.addEdge(STRING_1, STRING_2);
            dg.addEdge(STRING_1, STRING_4);
            dg.addEdge(STRING_2, STRING_3);
            dg.addEdge(STRING_3, STRING_1);
            dg.addEdge(STRING_3, STRING_2);
            dg.addEdge(STRING_3, STRING_5);
            dg.addEdge(STRING_4, STRING_1);
            dg.addEdge(STRING_4, STRING_5);
            dg.addEdge(STRING_5, STRING_2);
            dg.addEdge(STRING_5, STRING_3);
            dg.addEdge(STRING_5, STRING_4);
            List<List<String>> circuitsWithoutLimits = CycleInDirectedGraph.findCycles(dg);
            List<List<String>> circuitsWithLimits = CycleInDirectedGraph.findCycles(dg, limit, NO_MIN);
            circuitsWithoutLimits.removeIf(l -> l.size() > limit);
            assertTrue(compareCircuitsLists(circuitsWithoutLimits, circuitsWithLimits));
        }
    }

    @Nested
    @DisplayName("Tests for method findCyclesThrough")
    class TestForFindCyclesThrough {

        /**
         * Test for a circuit through 2 nodes in a graph with 1 binary and 1 ternary cycles, using minimums and
         * maximums for the length of the cycle
         */
        @Test
        void testThrough1() {
            DirectedGraph<String> dg = new DirectedGraph<>();
            dg.addEdge(STRING_1, STRING_3);
            dg.addEdge(STRING_3, STRING_2);
            dg.addEdge(STRING_3, STRING_1);
            dg.addEdge(STRING_2, STRING_1);
            assertTrue(CycleInDirectedGraph.findCyclesThrought(dg, dg.getVertex(STRING_1), dg.getVertex(STRING_3)));
            assertTrue(CycleInDirectedGraph.findCyclesThrought(dg, dg.getVertex(STRING_2), dg.getVertex(STRING_3)));
            assertTrue(CycleInDirectedGraph.findCyclesThrought(dg, dg.getVertex(STRING_1), dg.getVertex(STRING_2)));
            assertFalse(CycleInDirectedGraph.findCyclesThrought(dg, dg.getVertex(STRING_1), dg.getVertex(STRING_2), 2, 0));
            assertFalse(CycleInDirectedGraph.findCyclesThrought(dg, dg.getVertex(STRING_1), dg.getVertex(STRING_2), 10, 4));
            assertTrue(CycleInDirectedGraph.findCyclesThrought(dg, dg.getVertex(STRING_1), dg.getVertex(STRING_2), 3, 0));
        }

        /**
         * Test for a circuit through 2 nodes in a random graph using 2 random nodes of each circuit into the graph
         */
        @Test
        void testCircuitsThroughRandom() {
            int limit = 50;
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
            List<List<String>> circuits = CycleInDirectedGraph.findCycles(dg, limit, limit);
            circuits.forEach(c -> assertTrue(CycleInDirectedGraph.findCyclesThrought(dg, dg.getVertex(c.get(r.nextInt(c.size()))), dg.getVertex(c.get(r.nextInt(c.size()))))));
        }
    }
}

