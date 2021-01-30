package graph.shortest_paths;

import java.util.*;

/**
 * DetectCycle
 *
 * Note that:
 * - vertices indexes should be unique
 */
public class DetectCycle<E> {
    public boolean hasCycle(Map<Integer, List<Integer>> g) {
        Set<Integer> globalSeen = new HashSet<>();
        for (int v : g.keySet()) {
            if (hasCycle(v, g, globalSeen, new HashSet<>())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycle(int v, Map<Integer, List<Integer>> g, Set<Integer> globalSeen, Set<Integer> localSeen) {
        if (localSeen.contains(v)) {
            return true;
        }
        if (globalSeen.contains(v)) {
            return false;
        }
        localSeen.add(v);
        globalSeen.add(v);

        for (int w : g.getOrDefault(v, new LinkedList<>())) {
            if (hasCycle(w, g, globalSeen, localSeen)) {
                return true;
            }
        }
        localSeen.remove(v);
        return false;
    }

    public boolean hasCycleWeightedDigraph(Map<Integer, Map<Integer, E>> g) {
        Set<Integer> globalSeen = new HashSet<>();
        for (int v : g.keySet()) {
            if (hasCycleWeightedDigraph(v, g, globalSeen, new HashSet<>())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycleWeightedDigraph(int v, Map<Integer, Map<Integer, E>> g, Set<Integer> globalSeen, Set<Integer> localSeen) {
        if (localSeen.contains(v)) {
            return true;
        }
        if (globalSeen.contains(v)) {
            return false;
        }
        localSeen.add(v);
        globalSeen.add(v);

        for (int w : g.getOrDefault(v, new HashMap<>()).keySet()) {
            if (hasCycleWeightedDigraph(w, g, globalSeen, localSeen)) {
                return true;
            }
        }
        localSeen.remove(v);
        return false;
    }
}
