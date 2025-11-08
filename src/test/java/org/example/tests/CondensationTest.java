package org.example.tests;

import org.example.graph.model.Edge;
import org.example.graph.model.Graph;
import org.example.graph.scc.CondensationResult;
import org.example.graph.scc.TarjanSCC;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CondensationTest {

    @Test
    void edgesBetweenComponentsAreUnique() {
        Graph g = new Graph(6, true);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        g.addEdge(3,4,1); g.addEdge(4,5,1); g.addEdge(5,3,1);
        g.addEdge(0,3,2); g.addEdge(1,4,2); g.addEdge(2,5,2);
        TarjanSCC t = new TarjanSCC(g, new Metrics());
        t.run();
        CondensationResult cr = t.buildCondensation();
        assertEquals(2, cr.condensationGraph.size());
        int compA = cr.compId[0];
        int compB = cr.compId[3];
        assertNotEquals(compA, compB, "Компоненты A и B должны быть разными");
        long edgesAB = cr.condensationGraph
                .adjacency()
                .get(compA)
                .stream()
                .filter(e -> e.to == compB)
                .count();
        assertEquals(1, edgesAB, "В конденсации должно быть ровно одно ребро A->B");
        long edgesBA = cr.condensationGraph
                .adjacency()
                .get(compB)
                .stream()
                .filter(e -> e.to == compA)
                .count();
        assertEquals(0, edgesBA, "Не должно быть ребра B->A в этом кейсе");
    }
}
