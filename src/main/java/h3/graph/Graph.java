package h3.graph;

import java.util.Set;

public interface Graph<N> {

    Set<N> getNodes();

    Set<Edge<N>> getEdges();

    Set<Edge<N>> getAdjacentEdges(N node);

    // TODO: Decide on List or Set for AdjacencyGraph constructor
//    static <N> Graph<N> fromAdjacencyMatrix(int[][] matrix, List<N> nodes) {
//        return new AdjacencyGraph<>(matrix, nodes);
//    }

    interface Edge<N> extends Comparable<Edge<N>> {

        N getA();

        N getB();

        int getWeight();

        @Override
        default int compareTo(Graph.Edge<N> other) {
            return Integer.compare(this.getWeight(), other.getWeight());
        }
    }

    interface Builder<N> {

        Builder<N> addNode(N node);

        Builder<N> addEdge(N a, N b, int weight);

        Builder<N> addEdgesAndNodes(N a, N b, int weight);

        Graph<N> build();
    }
}
