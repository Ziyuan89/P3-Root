package p3.graph;

import java.util.Objects;

record EdgeImpl<N>(N a, N b, int weight) implements Edge<N> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeImpl<?> edge = (EdgeImpl<?>) o;
        return weight == edge.weight &&
            ((Objects.equals(a, edge.a) && Objects.equals(b, edge.b)) ||
                (Objects.equals(a, edge.b) && Objects.equals(b, edge.a)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, weight) + Objects.hash(a, b, weight);
    }
}
