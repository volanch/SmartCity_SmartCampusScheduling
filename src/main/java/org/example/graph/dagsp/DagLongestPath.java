package org.example.graph.dagsp;

import org.example.graph.model.Edge;
import org.example.graph.model.Graph;
import org.example.util.Metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DagLongestPath {
    public static final class Result {
        public final long[] best;
        public final int[] parent;
        public final int argmax;
        public Result(long[] best, int[] parent, int argmax) {
            this.best = best;
            this.parent = parent;
            this.argmax = argmax;
        }
        public List<Integer> reconstructCriticalPath() {
            List<Integer> path = new ArrayList<>();
            int v = argmax;
            while (v != -1) {
                path.add(v);
                v = parent[v];
            }
            Collections.reverse(path);
            return path;
        }
    }
    public static Result run(Graph dag, List<Integer> topoOrder, Metrics m) {
        if (m == null) m = new Metrics();
        final int n = dag.size();
        final long MINF = Long.MIN_VALUE / 4;
        long[] best = new long[n];
        int[] parent = new int[n];
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (Edge e : dag.edgesFrom(u)) indeg[e.to]++;
        }
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
            best[i] = (indeg[i] == 0) ? 0 : MINF;
        }
        long updates = 0;
        for (int u : topoOrder) {
            if (best[u] == MINF) continue;
            for (Edge e : dag.edgesFrom(u)) {
                long cand = best[u] + e.weight;
                if (cand > best[e.to]) {
                    best[e.to] = cand;
                    parent[e.to] = u;
                    updates++;
                }
            }
        }
        m.add("dagsp.longest.updates", updates);
        int argmax = 0;
        for (int i = 1; i < n; i++) if (best[i] > best[argmax]) argmax = i;
        return new Result(best, parent, argmax);
    }
    private DagLongestPath() {}
}
