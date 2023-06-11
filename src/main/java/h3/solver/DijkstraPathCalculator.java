package h3.solver;

import h3.graph.Graph;

import java.util.List;

public class DijkstraPathCalculator<N> implements PathCalculator<N> {

    private final Graph<N> graph;

    public DijkstraPathCalculator(Graph<N> graph) {
        this.graph = graph;
    }

    @Override
    public List<N> calculatePath(final N start, final N end) {
        return null;
    }
}
