package org.example;

import org.example.graph.model.Graph;
import org.example.graph.model.GraphLoader;
import org.example.graph.scc.CondensationResult;
import org.example.graph.scc.TarjanSCC;
import org.example.graph.topo.TopologicalSort;
import org.example.util.Metrics;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data/tasks.json";

        Graph g = GraphLoader.loadFromJson(path);
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
    }
}