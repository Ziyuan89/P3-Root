package p3.graph;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.launcher.env.Jagr;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p3.util.SerializedEdge;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class BasicGraphTests {

    private static Field backingField;
    private static Field nodesField;
    private static Field edgesField;

    @BeforeAll
    public static void setup() {
        try {
            backingField = BasicGraph.class.getDeclaredField("backing");
            backingField.setAccessible(true);
            nodesField = BasicGraph.class.getDeclaredField("nodes");
            nodesField.setAccessible(true);
            edgesField = BasicGraph.class.getDeclaredField("edges");
            edgesField.setAccessible(true);
            throw new NoSuchFieldException("test");
        } catch (NoSuchFieldException | InaccessibleObjectException e) {
            Jagr.Default.getInjector().getInstance(Logger.class).error("An exception occurred during setup", e);
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "basicGraph.json", data = "twoNodes")
    public <N> void testTwoNodes(@Property("nodes") Set<N> nodes,
                                 @Property("edges") Set<SerializedEdge<N>> serializedEdges) {
        Set<Edge<N>> edges = deserializeEdges(serializedEdges);
        BasicGraph<N> basicGraph = new BasicGraph<>(nodes, edges);

        testNodesCorrect(nodes, basicGraph);
        testEdgesCorrect(edges, basicGraph);
    }

    private static <N> void testNodesCorrect(Set<N> expectedNodes, BasicGraph<N> basicGraphInstance) {
        Set<N> actualNodes = getFieldValue(nodesField, basicGraphInstance);
        Context context = contextBuilder()
            .add("expected nodes", expectedNodes)
            .add("actual nodes", actualNodes)
            .build();

        assertEquals(expectedNodes.size(), actualNodes.size(), context,
            result -> "Size of the set of actual nodes ([[[actualNodes.size()]]]) differs from the expected value");
        Set<N> leftoverNodes = actualNodes.stream()
            .filter(node -> !expectedNodes.contains(node))
            .collect(Collectors.toSet());
        assertEquals(0, leftoverNodes.size(), context,
            result -> "actualNodes contains unexpected nodes: " + leftoverNodes);
    }

    private static <N> void testEdgesCorrect(Set<Edge<N>> expectedEdges, BasicGraph<N> basicGraphInstance) {
        Set<Edge<N>> actualEdges = getFieldValue(edgesField, basicGraphInstance);
        Context context = contextBuilder()
            .add("expected edges", expectedEdges)
            .add("actual edges", actualEdges)
            .build();

        assertEquals(expectedEdges.size(), actualEdges.size(), context,
            result -> "Size of the set of actual edges ([[[actualEdges.size()]]]) differs from the expected value");

    }

    @SuppressWarnings("unchecked")
    private static <T> T getFieldValue(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <N> Set<Edge<N>> deserializeEdges(Set<SerializedEdge<N>> serializedEdges) {
        return serializedEdges.stream()
            .map(SerializedEdge::toEdge)
            .collect(Collectors.toSet());
    }
}
