package org.example.graph.model;

import com.google.gson.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public final class UnifiedLoader {
    public static final class Loaded {
        public final Graph graph;
        public final int source;
        public final String weightModel;
        public Loaded(Graph g, int s, String wm) {
            this.graph = g; this.source = s; this.weightModel = wm;
        }
    }
    public static Loaded load(String path) {
        try (FileReader r = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
            if (root.has("datasets") && root.get("datasets").isJsonArray()) {
                JsonArray arr = root.getAsJsonArray("datasets");
                if (arr.size() == 0) throw new IllegalArgumentException("datasets[] is empty in " + path);
                JsonObject ds = arr.get(0).getAsJsonObject();
                boolean directed = ds.get("directed").getAsBoolean();
                int n = ds.get("n").getAsInt();
                Graph g = new Graph(n, directed);
                JsonArray edges = ds.getAsJsonArray("edges");
                for (JsonElement el : edges) {
                    JsonObject e = el.getAsJsonObject();
                    int u = e.get("u").getAsInt();
                    int v = e.get("v").getAsInt();
                    int w = e.get("w").getAsInt();
                    g.addEdge(u, v, w);
                }
                int source = ds.has("source") ? ds.get("source").getAsInt() : 0;
                String wm = ds.has("weight_model") ? ds.get("weight_model").getAsString() : "edge";
                return new Loaded(g, source, wm);
            }
            boolean directed = root.get("directed").getAsBoolean();
            int n = root.get("n").getAsInt();
            Graph g = new Graph(n, directed);
            JsonArray edges = root.getAsJsonArray("edges");
            for (JsonElement el : edges) {
                JsonObject e = el.getAsJsonObject();
                int u = e.get("u").getAsInt();
                int v = e.get("v").getAsInt();
                int w = e.get("w").getAsInt();
                g.addEdge(u, v, w);
            }
            int source = root.has("source") ? root.get("source").getAsInt() : 0;
            String wm = root.has("weight_model") ? root.get("weight_model").getAsString() : "edge";
            return new Loaded(g, source, wm);
        } catch (Exception ex) {
            throw new RuntimeException("UnifiedLoader failed for "+path+": "+ex.getMessage(), ex);
        }
    }
    private UnifiedLoader() {}
}
