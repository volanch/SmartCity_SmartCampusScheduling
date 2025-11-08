package org.example.tests;

import org.example.graph.dagsp.DagLongestPath;
import org.example.graph.model.Graph;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DagLongestPathTest {

    @Test
    void pickLongestBranch() {
        Graph g = new Graph(4, true);
        g.addEdge(0,1,5); g.addEdge(1,3,5);
        g.addEdge(0,2,7); g.addEdge(2,3,1);

        List<Integer> topo = List.of(0,1,2,3);
        DagLongestPath.Result r = DagLongestPath.run(g, topo, new Metrics());

        assertEquals(10L, r.best[r.argmax]);
        assertEquals(List.of(0,1,3), r.reconstructCriticalPath());
    }

    @Test
    void multipleSources() {
        Graph g = new Graph(5, true);
        g.addEdge(0,1,1); g.addEdge(1,2,2);
        g.addEdge(3,4,10);

        List<Integer> topo = List.of(0,1,2,3,4);
        DagLongestPath.Result r = DagLongestPath.run(g, topo, new Metrics());

        assertEquals(10L, r.best[r.argmax]);
        assertEquals(List.of(3,4), r.reconstructCriticalPath());
    }
}
