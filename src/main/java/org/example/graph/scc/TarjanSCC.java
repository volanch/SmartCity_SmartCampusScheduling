package org.example.graph.scc;

import org.example.graph.model.Edge;
import org.example.graph.model.Graph;
import org.example.util.Metrics;

import java.util.*;

public class TarjanSCC {
    private final Graph g;
    private final Metrics metrics;

    private int time = 0;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private final List<List<Integer>> components = new ArrayList<>();

    public TarjanSCC(Graph g, Metrics metrics) {
        this.g = Objects.requireNonNull(g, "graph is null");
        this.metrics = metrics == null ? new Metrics() : metrics;
        int n = g.size();
        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        Arrays.fill(disc, -1);
    }

    public List<List<Integer>> run() {
        components.clear();
        Arrays.fill(disc, -1);
        Arrays.fill(low, 0);
        Arrays.fill(onStack, false);
        stack.clear();
        time = 0;

        for (int v = 0; v < g.size(); v++) {
            if (disc[v] == -1) dfs(v);
        }
        return components;
    }

    private void dfs(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        onStack[u] = true;
        metrics.inc("scc.dfs.visits");

        for (Edge e : g.edgesFrom(u)) {
            metrics.inc("scc.dfs.edges");
            int v = e.to;
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        // If u is root of SCC
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int w = stack.pop();
                onStack[w] = false;
                comp.add(w);
                if (w == u) break;
            }
            components.add(comp);
        }
    }

    public CondensationResult buildCondensation() {
        if (components.isEmpty()) run();
        int k = components.size();
        int n = g.size();

        int[] compId = new int[n];
        for (int i = 0; i < components.size(); i++) {
            for (int v : components.get(i)) {
                compId[v] = i;
            }
        }

        Graph dag = new Graph(k, true);
        Set<String> addedEdges = new HashSet<>();

        for (int u = 0; u < n; u++) {
            for (Edge e : g.edgesFrom(u)) {
                int cu = compId[u];
                int cv = compId[e.to];
                if (cu != cv) {
                    String key = cu + "-" + cv;
                    if (addedEdges.add(key)) {
                        dag.addEdge(cu, cv, e.weight);
                    }
                }
            }
        }

        return new CondensationResult(components, compId, dag);
    }
}
