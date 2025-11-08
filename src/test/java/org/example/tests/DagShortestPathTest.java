package org.example.tests;


import org.example.graph.dagsp.DagShortestPath;
import org.example.graph.model.Graph;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DagShortestPathTest {

    @Test
    void chain() {
        Graph g = new Graph(3, true);
        g.addEdge(0,1,2);
        g.addEdge(1,2,3);

        List<Integer> topo = List.of(0,1,2);
        DagShortestPath.Result r = DagShortestPath.run(g, topo, 0, new Metrics());

        assertEquals(0L, r.dist[0]);
        assertEquals(2L, r.dist[1]);
        assertEquals(5L, r.dist[2]);
        assertEquals(List.of(0,1,2), r.reconstructPath(0,2));
    }

    @Test
    void unreachable() {

        Graph g = new Graph(3, true);
        g.addEdge(0,1,5);
        List<Integer> topo = List.of(0,1,2);

        DagShortestPath.Result r = DagShortestPath.run(g, topo, 0, new Metrics());
        assertTrue(r.dist[2] >= DagShortestPath.INF);
        assertEquals(List.of(), r.reconstructPath(0,2));
    }
}
