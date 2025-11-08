package org.example;

import org.example.graph.model.Graph;
import org.example.graph.scc.CondensationResult;
import org.example.graph.scc.TarjanSCC;
import org.example.graph.topo.TopologicalSort;
import org.example.util.Metrics;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.graph.dagsp.DagShortestPath;
import org.example.graph.dagsp.DagLongestPath;
import org.example.graph.model.UnifiedLoader;



import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data/small_graphs.json";
        UnifiedLoader.Loaded loaded = UnifiedLoader.load(path);
        Graph g = loaded.graph;
        Metrics metrics = new Metrics();
        System.out.println("Loaded graph: n=" + g.size() + ", directed=" + g.isDirected());
        long t0 = System.nanoTime();
        TarjanSCC tarjan = new TarjanSCC(g, metrics);
        List<List<Integer>> sccs = tarjan.run();
        long t1 = System.nanoTime();
        System.out.println("\n=== Strongly Connected Components (SCCs) ===");
        for (int i = 0; i < sccs.size(); i++) {
            List<Integer> c = sccs.get(i);
            System.out.printf("Component %d: vertices=%s size=%d%n", i, c, c.size());
        }
        long t2 = System.nanoTime();
        CondensationResult cr = tarjan.buildCondensation();
        long t3 = System.nanoTime();
        System.out.println("\n=== Condensation Graph ===");
        System.out.println("Nodes (components): " + cr.condensationGraph.size());
        System.out.println(cr.condensationGraph);
        long t4 = System.nanoTime();
        List<Integer> topoOrder = TopologicalSort.kahn(cr.condensationGraph, metrics);
        long t5 = System.nanoTime();
        System.out.println("\n=== Topological order of components ===");
        System.out.println(topoOrder);
        System.out.println("\n=== Derived order of original tasks (expand components in topo order) ===");
        for (int cid : topoOrder) {
            System.out.printf("Component %d -> %s%n", cid, cr.components.get(cid));
        }
        System.out.println("\n=== Timings (ms) ===");
        System.out.printf("Tarjan SCC:         %.3f ms%n", (t1 - t0) / 1e6);
        System.out.printf("Build Condensation: %.3f ms%n", (t3 - t2) / 1e6);
        System.out.printf("Topological Sort:   %.3f ms%n", (t5 - t4) / 1e6);
        System.out.println();
        metrics.print();
        try {
            int sourceVertex = -1;
            try (java.io.FileReader r = new java.io.FileReader(path)) {
                JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
                if (root.has("datasets")) {
                    var arr = root.getAsJsonArray("datasets");
                    if (arr != null && arr.size() > 0) {
                        JsonObject ds0 = arr.get(0).getAsJsonObject();
                        if (ds0.has("source")) sourceVertex = ds0.get("source").getAsInt();
                    }
                } else if (root.has("source")) {
                    sourceVertex = root.get("source").getAsInt();
                }
            } catch (Exception ex) {

            }

            if (sourceVertex < 0 || sourceVertex >= g.size()) {
                System.out.println("\n[WARN] Source в JSON не найден или вне диапазона. Использую source=0 (по умолчанию).");
                sourceVertex = 0;
            }
            int sourceComp = cr.compId[sourceVertex];
            DagShortestPath.Result sp = DagShortestPath.run(cr.condensationGraph, topoOrder, sourceComp, metrics);

            System.out.println("\n=== DAG Shortest Paths (from component " + sourceComp + ") ===");
            for (int i = 0; i < sp.dist.length; i++) {
                String d = (sp.dist[i] >= DagShortestPath.INF) ? "INF" : Long.toString(sp.dist[i]);
                System.out.printf("dist[%d] = %s%n", i, d);
            }
            for (int t = 0; t < sp.dist.length;t++) {
                if (sp.dist[t] < DagShortestPath.INF) {
                    System.out.printf("Path %d -> %d: %s%n", sourceComp, t, sp.reconstructPath(sourceComp, t));
                }
            }
            DagLongestPath.Result lp = DagLongestPath.run(cr.condensationGraph, topoOrder, metrics);
            System.out.println("\n=== Critical (Longest) Path in condensation DAG ===");
            System.out.println("Best length = " + lp.best[lp.argmax] + ", endpoint component = " + lp.argmax);
            System.out.println("Critical path (component ids): " + lp.reconstructCriticalPath());
        } catch (Exception ex) {
            System.out.println("\n[ERROR] DAG SP/LP section failed: " + ex.getMessage());
            ex.printStackTrace(System.out);
        }


    }

}