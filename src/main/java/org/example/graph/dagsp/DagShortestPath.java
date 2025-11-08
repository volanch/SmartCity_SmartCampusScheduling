package org.example.graph.dagsp;
import org.example.graph.model.Edge;
import org.example.graph.model.Graph;
import org.example.util.Metrics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class DagShortestPath {
    public static final long INF = Long.MAX_VALUE / 4;
    public static final class Result {
        public final long[] dist;
        public final int[] parent;
        public Result(long[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }
        public List<Integer> reconstructPath(int s, int t) {
            if (dist[t] >= INF) return Collections.emptyList();
            List<Integer> path = new ArrayList<>();
            for (int v = t; v != -1; v = parent[v]) {
                path.add(v);
                if (v == s) break;
            }
            Collections.reverse(path);
            if (path.isEmpty() || path.get(0) != s) return Collections.emptyList();
            return path;
        }
    }
    public static Result run(Graph dag, List<Integer> topoOrder, int source, Metrics m) {
        if (m == null) m = new Metrics();
        final int n = dag.size();
        long[] dist = new long[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) { dist[i] = INF; parent[i] = -1; }
        dist[source] = 0;
        long relaxes = 0;
        for (int u : topoOrder) {
            if (dist[u] >= INF) continue;
            for (Edge e : dag.edgesFrom(u)) {
                long cand = dist[u] + e.weight;
                if (cand < dist[e.to]) {
                    dist[e.to] = cand;
                    parent[e.to] = u;
                    relaxes++;
                }
            }
        }
        m.add("dagsp.shortest.relax", relaxes);
        return new Result(dist, parent);
    }
    private DagShortestPath() {}
}
