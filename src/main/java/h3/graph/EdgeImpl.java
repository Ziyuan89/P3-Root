package h3.graph;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeImpl<?> edge = (EdgeImpl<?>) o;
        return weight == edge.weight &&
            ((Objects.equals(A, edge.A) && Objects.equals(B, edge.B)) ||
                (Objects.equals(A, edge.B) && Objects.equals(B, edge.A)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(A, B, weight) + Objects.hash(B, A, weight);
    }
}
