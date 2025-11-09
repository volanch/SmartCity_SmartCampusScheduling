package org.example.tests;


import org.example.graph.model.Graph;
import org.example.graph.topo.TopologicalSort;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopologicalSortTest {

    @Test
    void testSimpleDAG() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);

        List<Integer> order = TopologicalSort.kahn(g, new Metrics());

        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }

    @Test
    void testSingleNode() {
        Graph g = new Graph(1, true);
        List<Integer> order = TopologicalSort.kahn(g, new Metrics());
        assertEquals(List.of(0), order);
    }

    @Test
    void testCycleThrows() {
        Graph g = new Graph(2, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);

        assertThrows(IllegalStateException.class,
                () -> TopologicalSort.kahn(g, new Metrics()));
    }
}
