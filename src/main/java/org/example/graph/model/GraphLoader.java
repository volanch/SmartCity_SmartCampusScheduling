package org.example.graph.model;


import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class GraphLoader {
    public static class EdgeData {
        int u;
        int v;
        int w;
    }

    public static class GraphData {
        boolean directed;
        int n;
        List<EdgeData> edges;
        int source;
        String weight_model;
    }

    public static Graph loadFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            GraphData data = gson.fromJson(reader, GraphData.class);

            Graph g = new Graph(data.n, data.directed);
            for (EdgeData e : data.edges) {
                g.addEdge(e.u, e.v, e.w);
            }
            return g;
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON: " + e.getMessage(), e);
        }
    }

}
