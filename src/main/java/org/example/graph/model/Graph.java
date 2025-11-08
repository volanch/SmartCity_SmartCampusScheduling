package org.example.graph.model;

import java.util.*;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public int size() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(v, w));
        if (!directed) {
            adj.get(v).add(new Edge(u, w));
        }
    }

    public List<Edge> edgesFrom(int u) {
        if (u < 0 || u >= n){
            throw new IndexOutOfBoundsException("Invalid vertex " + u);
        }
        return adj.get(u);
    }

    public List<List<Edge>> adjacency() {
        return adj;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph (n=")
                .append(n).append(", directed=").append(directed).append(")\n");
        for (int i = 0; i < n; i++) {
            sb.append(i).append(" → ").append(adj.get(i)).append('\n');
        }
        return sb.toString();
    }
}
