package h3.solver;

import h3.graph.Graph;

public interface MSTFactory {

    <T> Graph<T> createMST(Graph<T> graph);
}
