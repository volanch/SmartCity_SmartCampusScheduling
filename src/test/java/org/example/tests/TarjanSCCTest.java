package org.example.tests;


import org.example.graph.model.Graph;
import org.example.graph.scc.CondensationResult;
import org.example.graph.scc.TarjanSCC;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TarjanSCCTest {

    @Test
    void testSingleNode() {
        Graph g = new Graph(1, true);
        TarjanSCC scc = new TarjanSCC(g, new Metrics());
        List<List<Integer>> components = scc.run();

        assertEquals(1, components.size());
        assertEquals(List.of(0), components.get(0));
    }

    @Test
    void testSimpleCycle() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        TarjanSCC scc = new TarjanSCC(g, new Metrics());
        List<List<Integer>> components = scc.run();

        assertEquals(1, components.size());
        assertTrue(components.get(0).containsAll(List.of(0,1,2)));
    }

    @Test
    void testDisconnectedGraph() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(2, 3, 1);

        TarjanSCC scc = new TarjanSCC(g, new Metrics());
        List<List<Integer>> components = scc.run();

        assertEquals(4, components.size());
    }

    @Test
    void testCondensationDag() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1); // cycle 0-1
        g.addEdge(2, 3, 1);

        TarjanSCC scc = new TarjanSCC(g, new Metrics());
        CondensationResult cr = scc.buildCondensation();

        assertEquals(3, cr.condensationGraph.size());
    }
}
