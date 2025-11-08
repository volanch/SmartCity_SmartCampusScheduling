package org.example.graph.scc;

import org.example.graph.model.Graph;

import java.util.Collections;
import java.util.List;

public class CondensationResult {
    public final List<List<Integer>> components;
    public final int[] compId;             // original vertex -> component index
    public final Graph condensationGraph;  // DAG with k nodes (k = components.size())

    public CondensationResult(List<List<Integer>> components, int[] compId, Graph condensationGraph) {
        this.components = Collections.unmodifiableList(components);
        this.compId = compId.clone();
        this.condensationGraph = condensationGraph;
    }
}
