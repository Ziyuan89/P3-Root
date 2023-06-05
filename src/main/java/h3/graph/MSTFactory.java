package h3.graph;

public interface MSTFactory {

    <T> Graph<T> createMST(Graph<T> graph);
}
