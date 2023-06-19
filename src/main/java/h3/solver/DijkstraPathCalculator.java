package h3.solver;

import h3.graph.Graph;
import h3.graph.Edge;

import java.util.*;

/**
 * Implementation of Dijkstra's algorithm, a single-source shortest path algorithm.
 *
 * @param <N> node identifier
 */
public class DijkstraPathCalculator<N> implements PathCalculator<N> {

    protected final Graph<N> graph;
    protected final Map<N, Integer> distance = new HashMap<>();
    protected final Map<N, N> predecessors = new HashMap<>();
    protected final Set<N> remainingNodes = new HashSet<>();

    public DijkstraPathCalculator(Graph<N> graph) {
        this.graph = graph;
    }

    /**
     * Calculate the shortest path between two given nodes, {@code start} and {@code end}, using Dijkstra's algorithm.
     *
     * <p>
     *     This method calculates the shortest path from {@code start} to all other nodes and saves the results
     *     to {@link #distance} and {@link #predecessors}. All subsequent calls to this method with the <i>same</i>
     *     start node will use the cached results instead of recalculating. Only when a subsequent call uses a
     *     different start node, the algorithm is run again and those results are cached.
     * </p>
     *
     * @param start the start node, first node in the returned list
     * @param end   the end node, last node in the returned list
     * @return a list of nodes, from {@code start} to {@code end}, in the order they need to be traversed to get the
     * shortest path between those two nodes
     */
    @Override
    public List<N> calculatePath(final N start, final N end) {
        if (distance.containsKey(start) && distance.get(start) == 0) {
            return reconstructPath(start, end);
        }

        init(graph, start);

        while (!remainingNodes.isEmpty()) {
            N node = extractMin(remainingNodes);
            remainingNodes.remove(node);

            Set<Edge<N>> neighbors = graph.getAdjacentEdges(node);
            for (Edge<N> neighborEdge : neighbors) {
                N neighborNode = neighborEdge.a() == node ? neighborEdge.b() : neighborEdge.a();
                if (remainingNodes.contains(neighborNode)) {
                    relax(node, neighborNode, neighborEdge);
                }
            }
        }

        return reconstructPath(start, end);
    }

    /**
     * Initializes the {@link #distance} and {@link #predecessors} maps, i.e., resets and repopulates them with
     * default values.
     *
     * @param graph the graph which nodes to use
     * @param start the start node
     */
    protected void init(Graph<N> graph, N start) {
        // reset
        distance.clear();
        predecessors.clear();
        remainingNodes.clear();

        // repopulate with default values
        for (N node : graph.getNodes()) {
            distance.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }
        distance.put(start, 0);
        remainingNodes.addAll(graph.getNodes());
    }

    /**
     * Select the next node from the set of remaining nodes.
     *
     * @param remainingNodes the set of remaining nodes
     * @return the next unprocessed node with minimal weight
     */
    protected N extractMin(Set<N> remainingNodes) {
        return distance.entrySet()
            .stream()
            .filter(entry -> remainingNodes.contains(entry.getKey()))
            .min(Comparator.comparingInt(Map.Entry::getValue))
            .orElseThrow()
            .getKey();
    }

    /**
     * Update the {@link #distance} and {@link #predecessors} maps if a shorter path between {@code via} and
     * {@code dest} is found.
     *
     * @param via  the node that is used to reach {@code dest}
     * @param dest the target node for this update
     * @param edge the edge between {@code via} and {@code dest}.
     */
    protected void relax(N via, N dest, Edge<N> edge) {
        int newDistance = distance.get(via) + edge.weight();
        if (newDistance < distance.get(dest)) {
            distance.put(dest, newDistance);
            predecessors.put(dest, via);
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
        while (current != start) {
            shortestPath.addFirst(current);
            current = predecessors.get(current);
        }
        shortestPath.addFirst(start);
        return shortestPath;
    }
}
