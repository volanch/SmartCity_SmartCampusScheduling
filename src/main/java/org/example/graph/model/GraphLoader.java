package org.example.graph.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
            return buildGraph(data);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON: " + e.getMessage(), e);
        }
    }

    public static List<Graph> loadAllFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<GraphData>>(){}.getType();
            List<GraphData> list = gson.fromJson(reader, listType);

            List<Graph> graphs = new ArrayList<>();
            for (GraphData data : list) {
                graphs.add(buildGraph(data));
            }
            return graphs;
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON list: " + e.getMessage(), e);
        }
    }

    private static Graph buildGraph(GraphData data) {
        Graph g = new Graph(data.n, data.directed);
        for (EdgeData e : data.edges) {
            g.addEdge(e.u, e.v, e.w);
        }
        return g;
    }
}
