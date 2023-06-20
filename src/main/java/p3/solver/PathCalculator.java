package p3.solver;

import p3.graph.Graph;

import java.util.List;

public interface PathCalculator<N> {

    /**
     * Calculate an optimal path between two nodes in a graph.
     *
     * <p>
     * The criteria for optimality is up to the implementation.
     * </p>
     *
     * <p>
     * The result is a list of along the path from the start node to the end node.
     * The start element is not included in the list, but the end element is.
     * Hence, the length of the list of is the the number of hops along the path.
     * </p>
     *
     * @return A list representing the path found between the start and end nodes
     */
    List<N> calculatePath(N start, N end);

    interface Factory {
        <N> PathCalculator<N> create(Graph<N> graph);
    }
}
