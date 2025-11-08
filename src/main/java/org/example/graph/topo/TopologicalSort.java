package org.example.graph.topo;

import org.example.graph.model.Edge;
import org.example.graph.model.Graph;
import org.example.util.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TopologicalSort {
    public static List<Integer> kahn(Graph dag, Metrics metrics) {
        if (metrics == null) metrics = new Metrics();
        int n = dag.size();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (Edge e : dag.edgesFrom(u)) indeg[e.to]++;
        }

        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.addLast(i);

        List<Integer> order = new ArrayList<>(n);
        while (!q.isEmpty()) {
            metrics.inc("topo.kahn.pops");
            int u = q.removeFirst();
            order.add(u);
            for (Edge e : dag.edgesFrom(u)) {
                metrics.inc("topo.kahn.edges");
                indeg[e.to]--;
                if (indeg[e.to] == 0) {
                    q.addLast(e.to);
                    metrics.inc("topo.kahn.pushes");
                }
            }
        }

        if (order.size() != n) throw new IllegalStateException("Graph is not a DAG (cycle detected)");
        return order;
    }
}
