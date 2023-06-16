package h3.solver;

import h3.graph.Graph;

public interface MSTFactory<N> {

    Graph<N> createMST(Graph<N> graph);
}
