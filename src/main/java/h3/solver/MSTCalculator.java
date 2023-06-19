package h3.solver;

import h3.graph.Graph;

public interface MSTCalculator<N> {

    Graph<N> calculateMST();

    interface Factory {
        <N> MSTCalculator<N> create(Graph<N> graph);
    }
}
