package org.example.tests;


import org.example.graph.model.Graph;
import org.example.graph.topo.TopologicalSort;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopologicalSortTest {

    @Test
    void simpleDag() {
        Graph g = new Graph(4, true);
        g.addEdge(0,1,1);
        g.addEdge(1,2,1);
        g.addEdge(1,3,1);

        List<Integer> order = TopologicalSort.kahn(g, new Metrics());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertEquals(4, order.size());
    }

    @Test
    void throwsOnCycle() {
        Graph g = new Graph(3, true);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        assertThrows(IllegalStateException.class, () -> TopologicalSort.kahn(g, new Metrics()));
    }
}
