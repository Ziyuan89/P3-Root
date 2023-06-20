package p3.solver;

import p3.graph.Graph;

public interface MSTCalculator<N> {

    Graph<N> calculateMST();

    interface Factory {
        <N> MSTCalculator<N> create(Graph<N> graph);
    }
}
