package p3.solver;

import p3.graph.Edge;
import p3.graph.Graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of Dijkstra's algorithm, a single-source shortest path algorithm.
 *
 * @param <N> node identifier
 */
public class DijkstraPathCalculator<N> implements PathCalculator<N> {

    public static PathCalculator.Factory FACTORY = DijkstraPathCalculator::new;

    protected final Graph<N> graph;
    protected final Map<N, Integer> distances = new HashMap<>();
    protected final Map<N, N> predecessors = new HashMap<>();
    protected final Set<N> remainingNodes = new HashSet<>();

    public DijkstraPathCalculator(Graph<N> graph) {
        this.graph = graph;
    }

    /**
     * Calculate the shortest path between two given nodes, {@code start} and {@code end}, using Dijkstra's algorithm.
     *
     * <p>
     * This method calculates the shortest path from {@code start} to all other nodes and saves the results
     * to {@link #distances} and {@link #predecessors}. All subsequent calls to this method with the <i>same</i>
     * start node will use the cached results instead of recalculating. Only when a subsequent call uses a
     * different start node, the algorithm is run again and those results are cached.
     * </p>
     *
     * @param start the start node, first node in the returned list
     * @param end   the end node, last node in the returned list
     * @return a list of nodes, from {@code start} to {@code end}, in the order they need to be traversed to get the
     * shortest path between those two nodes
     */
    @Override
    public List<N> calculatePath(final N start, final N end) {
        init(start);

        while (!remainingNodes.isEmpty()) {
            N node = extractMin();
            remainingNodes.remove(node);

            Set<Edge<N>> neighbors = graph.getAdjacentEdges(node);
            for (Edge<N> neighborEdge : neighbors) {
                N neighborNode = neighborEdge.a().equals(node) ? neighborEdge.b() : neighborEdge.a();
                relax(node, neighborNode, neighborEdge);
            }
        }

        return reconstructPath(start, end);
    }

    /**
     * Initializes the {@link #distances} and {@link #predecessors} maps, i.e., resets and repopulates them with
     * default values.
     *
     * @param start the start node
     */
    protected void init(N start) {
        // reset
        distances.clear();
        predecessors.clear();
        remainingNodes.clear();

        // repopulate with default values
        for (N node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }
        distances.put(start, 0);
        remainingNodes.addAll(graph.getNodes());
    }

    /**
     * Select the next node from the set of remaining nodes.
     *
     * @return the next unprocessed node with minimal weight
     */
    protected N extractMin() {
        return distances.entrySet()
            .stream()
            .filter(entry -> remainingNodes.contains(entry.getKey()))
            .min(Comparator.comparingInt(Map.Entry::getValue))
            .orElseThrow()
            .getKey();
    }

    /**
     * Update the {@link #distances} and {@link #predecessors} maps if a shorter path between {@code from} and
     * {@code to} is found.
     *
     * @param from the node that is used to reach {@code to}
     * @param to   the target node for this update
     * @param edge the edge between {@code from} and {@code to}.
     */
    protected void relax(N from, N to, Edge<N> edge) {
        int newDistance = distances.get(from) + edge.weight();
        if (newDistance < distances.get(to)) {
            distances.put(to, newDistance);
            predecessors.put(to, from);
        }
    }

    /**
     * Reconstruct the shortest path from {@code start} to {@code target}.
     *
     * @param start  the start node
     * @param target the target node
     * @return a list of nodes in the order they need to be traversed to get the shortest path between the two nodes
     */
    protected List<N> reconstructPath(N start, N target) {
        LinkedList<N> shortestPath = new LinkedList<>();
        N current = target;
        while (!current.equals(start)) {
            shortestPath.addFirst(current);
            current = predecessors.get(current);
        }
        shortestPath.addFirst(start);
        return shortestPath;
    }
}
