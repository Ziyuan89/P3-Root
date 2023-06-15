package h3.solver;

import h3.graph.Graph;
import h3.graph.Graph.Edge;

import java.util.*;

/**
 * Implementation of Dijkstra's algorithm, a single-source shortest path algorithm.
 *
 * @param <N> node identifier
 */
public class DijkstraPathCalculator<N> implements PathCalculator<N> {

    private final Graph<N> graph;
    private Map<N, Integer> distance = new HashMap<>();
    private Map<N, N> precedessors = new HashMap<>();

    public DijkstraPathCalculator(Graph<N> graph) {
        this.graph = graph;
    }

    /**
     * Calculate the shortest path between two given nodes, {@code start} and {@code end}, using Dijkstra's algorithm.
     *
     * @param start the start node, first node in the returned list
     * @param end   the end node, last node in the returned list
     * @return a list of nodes, from {@code start} to {@code end}, in the order they need to be traversed to get the
     * shortest path between those two nodes
     */
    @Override
    public List<N> calculatePath(final N start, final N end) {
        init(graph, start);
        Set<N> remainingNodes = new HashSet<>(graph.getNodes());

        while (!remainingNodes.isEmpty()) {
            N node = extractMin(remainingNodes);
            remainingNodes.remove(node);

            Set<Edge<N>> neighbors = graph.getAdjacentEdges(node);
            for (Edge<N> neighborEdge : neighbors) {
                N neighborNode = neighborEdge.getA() == node ? neighborEdge.getB() : neighborEdge.getA();
                if (remainingNodes.contains(neighborNode)) {
                    relax(node, neighborNode, neighborEdge);
                }
            }
        }

        // reconstruct path
        LinkedList<N> shortestPath = new LinkedList<>();
        N current = end;
        while (current != start) {
            shortestPath.addFirst(current);
            current = precedessors.get(current);
        }
        shortestPath.addFirst(start);
        return shortestPath;
    }

    /**
     * Initializes the {@link #distance} and {@link #precedessors} maps, i.e., resets and repopulates them with
     * default values.
     *
     * @param graph the graph which nodes to use
     * @param start the start node
     */
    private void init(Graph<N> graph, N start) {
        // reset
        distance.clear();
        precedessors.clear();

        // repopulate with default values
        for (N node : graph.getNodes()) {
            distance.put(node, Integer.MAX_VALUE);
            precedessors.put(node, null);
        }
        distance.put(start, 0);
    }

    /**
     * Select the next node from the set of remaining nodes.
     *
     * @param remainingNodes the set of remaining nodes
     * @return the next unprocessed node with minimal weight
     */
    private N extractMin(Set<N> remainingNodes) {
        return distance.entrySet()
            .stream()
            .filter(entry -> remainingNodes.contains(entry.getKey()))
            .min(Comparator.comparingInt(Map.Entry::getValue))
            .orElseThrow()
            .getKey();
    }

    /**
     * Update the {@link #distance} and {@link #precedessors} maps if a shorter path between {@code via} and
     * {@code dest} is found.
     *
     * @param via  the node that is used to reach {@code dest}
     * @param dest the target node for this update
     * @param edge the edge between {@code via} and {@code dest}.
     */
    private void relax(N via, N dest, Edge<N> edge) {
        int newDistance = distance.get(via) + edge.getWeight();
        if (newDistance < distance.get(dest)) {
            distance.put(dest, newDistance);
            precedessors.put(dest, via);
        }
    }
}
