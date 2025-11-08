package org.example.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Metrics {
    private final Map<String, Long> counters = new ConcurrentHashMap<>();
    public void inc(String key) {
        counters.merge(key, 1L, Long::sum);
    }
    public void add(String key, long val) {
        counters.merge(key, val, Long::sum);
    }
    public long get(String key) {
        return counters.getOrDefault(key, 0L);
    }
    public Map<String, Long> snapshot() { return Map.copyOf(counters); }
    public void print() {
        System.out.println("=== Metrics ===");
        snapshot().forEach((k, v) -> System.out.printf("%s: %d%n", k, v));
    }
}
