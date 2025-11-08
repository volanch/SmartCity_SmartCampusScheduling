package org.example.tests;


import org.example.graph.model.Graph;
import org.example.graph.scc.TarjanSCC;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TarjanSCCTest {

    @Test
    void twoCyclesAndTail() {
        Graph g = new Graph(7, true);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        g.addEdge(3,4,1); g.addEdge(4,3,1);
        g.addEdge(5,6,1);
        TarjanSCC t = new TarjanSCC(g, new Metrics());
        List<List<Integer>> comps = t.run();
        assertTrue(comps.stream().anyMatch(c -> c.size() == 3));
        assertTrue(comps.stream().anyMatch(c -> c.size() == 2));
        long singles = comps.stream().filter(c -> c.size() == 1).count();
        assertTrue(singles >= 2);
    }
}
