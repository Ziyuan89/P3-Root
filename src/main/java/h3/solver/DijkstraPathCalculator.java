package h3.solver;

import h3.graph.AdjacencyGraph;
import h3.graph.EdgeImpl;
import h3.graph.Graph;
import h3.graph.Graph.Edge;

import java.util.*;

public class DijkstraPathCalculator<N> implements PathCalculator<N> {

    public static void main(String[] args) {
        Character a = 'A', b = 'B', c = 'C', d = 'D';
        Graph<Character> graph = new AdjacencyGraph<>(Set.of(a, b, c, d), Set.of(
            new EdgeImpl<>(a, b, 1),
            new EdgeImpl<>(a, c, 4),
            new EdgeImpl<>(b, c, 5),
            new EdgeImpl<>(b, d, 1)
        ));
        List<Character> shortestPath = new DijkstraPathCalculator<>(graph).calculatePath(a, d);
        System.out.println();
    }

    private final Graph<N> graph;
    private Map<N, Integer> distance = new HashMap<>();
    private Map<N, N> precedessors = new HashMap<>();

    public DijkstraPathCalculator(Graph<N> graph) {
        this.graph = graph;
    }

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

    private void init(Graph<N> graph, N start) {
        distance.clear();
        precedessors.clear();
        for (N node : graph.getNodes()) {
            distance.put(node, Integer.MAX_VALUE);
            precedessors.put(node, null);
        }
        distance.put(start, 0);
    }

    private N extractMin(Set<N> remainingNodes) {
        return distance.entrySet()
            .stream()
            .filter(entry -> remainingNodes.contains(entry.getKey()))
            .min(Comparator.comparingInt(Map.Entry::getValue))
            .orElseThrow()
            .getKey();
    }

    private void relax(N via, N dest, Edge<N> edge) {
        int newDistance = distance.get(via) + edge.getWeight();
        if (newDistance < distance.get(dest)) {
            distance.put(dest, newDistance);
            precedessors.put(dest, via);
        }
    }
}
