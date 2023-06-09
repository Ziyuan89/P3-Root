package h3.graph;

public class EdgeImpl<N> implements Graph.Edge<N> {

    private final N A;
    private final N B;
    private final int weight;

    public EdgeImpl(N a, N b, int weight) {
        A = a;
        B = b;
        this.weight = weight;
    }

    @Override
    public N getA() {
        return A;
    }

    @Override
    public N getB() {
        return B;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
