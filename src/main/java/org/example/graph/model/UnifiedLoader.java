package org.example.graph.model;

import com.google.gson.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UnifiedLoader {

    public static final class Loaded {
        public final Graph graph;
        public final int source;
        public final String weightModel;

        public Loaded(Graph g, int s, String wm) {
            this.graph = g;
            this.source = s;
            this.weightModel = wm;
        }
    }

    public static Loaded fromJson(JsonObject ds) {
        boolean directed = ds.has("directed") && ds.get("directed").getAsBoolean();
        int n = ds.get("n").getAsInt();
        Graph g = new Graph(n, directed);

        JsonArray edges = ds.getAsJsonArray("edges");
        if (edges != null) {
            for (JsonElement el : edges) {
                JsonObject e = el.getAsJsonObject();
                int u = e.get("u").getAsInt();
                int v = e.get("v").getAsInt();
                int w = e.has("w") ? e.get("w").getAsInt() : 1;
                g.addEdge(u, v, w);
            }
        }

        int source = ds.has("source") ? ds.get("source").getAsInt() : 0;
        String wm = ds.has("weight_model") ? ds.get("weight_model").getAsString() : "edge";
        return new Loaded(g, source, wm);
    }

    private UnifiedLoader() {}
}