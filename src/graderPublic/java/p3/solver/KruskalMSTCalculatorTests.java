package p3.solver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p3.graph.Edge;
import p3.graph.Graph;
import p3.util.KruskalAcceptEdgeExtension;
import p3.util.SerializedEdge;
import p3.util.SerializedGraph;
import p3.util.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class KruskalMSTCalculatorTests {

    public static boolean calledJoinGroups;
    public static List<Object[]> acceptEdgeParameters = new ArrayList<>();

    private static Field mstEdgesField;
    private static Field mstGroupsField;

    @BeforeAll
    public static void setup() {
        try {
            mstEdgesField = KruskalMSTCalculator.class.getDeclaredField("mstEdges");
            mstEdgesField.setAccessible(true);
            mstGroupsField = KruskalMSTCalculator.class.getDeclaredField("mstGroups");
            mstGroupsField.setAccessible(true);
        } catch (NoSuchFieldException | InaccessibleObjectException e) {
            // FIXME: use Jagr logger when working again
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "kruskalMSTCalculator.json", data = "init")
    public <N> void testInit(SerializedGraph<N> serializedGraph) {
        Graph<N> graph = serializedGraph.toGraph();
        KruskalMSTCalculator<N> kruskalMSTCalculatorInstance = new KruskalMSTCalculator<>(graph);
        kruskalMSTCalculatorInstance.init();
        List<Set<N>> mstGroups = Utils.getFieldValue(mstGroupsField, kruskalMSTCalculatorInstance);
        Context context = contextBuilder()
            .add("graph", serializedGraph)
            .add("mstGroups", mstGroups)
            .build();
        Map<N, Integer> nodeOccurrences = serializedGraph.nodes()
            .stream()
            .map(node -> Map.entry(node, 0))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        assertEquals(0, Utils.<Set<?>>getFieldValue(mstEdgesField, kruskalMSTCalculatorInstance).size(), context,
            result -> "[[[mstEdges]]] contains elements after calling [[[init]]]");
        assertEquals(serializedGraph.nodes().size(), mstGroups.size(), context,
            result -> "[[[mstGroups]]] does not have the correct size");
        for (Set<N> mstGroup : mstGroups) {
            Context mstGroupContext = contextBuilder()
                .add(context)
                .add("mstGroup", mstGroup)
                .build();
            assertEquals(1, mstGroup.size(), mstGroupContext,
                result -> "[[[mstGroup]]] does not have the correct size");
            N node = mstGroup.stream().findAny().get();
            nodeOccurrences.computeIfPresent(node, (key, value) -> value + 1);
            assertTrue(nodeOccurrences.get(node) == 1, mstGroupContext,
                result -> "[[[mstGroup]]] has more than one node");
        }
        Set<N> remainingNodes = serializedGraph.nodes()
            .stream()
            .filter(node -> nodeOccurrences.get(node) == 0)
            .collect(Collectors.toSet());
        assertTrue(remainingNodes.isEmpty(), context,
            result -> "[[[mstGroups]]] does not contain all nodes. Remaining nodes: " + remainingNodes);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "kruskalMSTCalculator.json", data = "joinGroups")
    public <N> void testJoinGroups(@Property("initialMstGroups") List<Set<N>> initialMstGroups,
                                   @Property("expectedMstGroups") List<Set<N>> expectedMstGroups,
                                   @Property("indexA") int indexA,
                                   @Property("indexB") int indexB) {
        Context context = contextBuilder()
            .add("initial mstGroups", initialMstGroups)
            .add("expected mstGroups", expectedMstGroups)
            .add("index A", indexA)
            .add("index B", indexB)
            .build();
        KruskalMSTCalculator<N> kruskalMSTCalculatorInstance = new KruskalMSTCalculator<>(Graph.of());
        Utils.setFieldValue(mstGroupsField, kruskalMSTCalculatorInstance, initialMstGroups);
        kruskalMSTCalculatorInstance.joinGroups(indexA, indexB);
        List<Set<N>> actualMstGroups = Utils.getFieldValue(mstGroupsField, kruskalMSTCalculatorInstance);

        assertEquals(expectedMstGroups.size(), actualMstGroups.size(), context,
            result -> "Field [[[mstGroups]]] does not have the expected size after calling [[[joinGroups]]]");
        for (int i = 0; i < expectedMstGroups.size(); i++) {
            final int finalI = i;
            Set<N> expectedMstGroup = expectedMstGroups.get(i);
            Set<N> actualMstGroup = actualMstGroups.get(i);

            assertEquals(expectedMstGroup.size(), actualMstGroup.size(), context,
                result -> "The size of the MST group at index %d does not match the expected one".formatted(finalI));
            assertTrue(actualMstGroup.containsAll(expectedMstGroup), context,
                result -> "The MST group at index %d does not contain all expected nodes".formatted(finalI));
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "kruskalMSTCalculator.json", data = "joinGroupsInsertion")
    public <N> void testJoinGroupsInsertion(@Property("mstGroups") List<Set<N>> mstGroups,
                                            @Property("smallerGroupIndex") int smallerGroupIndex,
                                            @Property("largerGroupIndex") int largerGroupIndex) {
        Context context = contextBuilder()
            .add("initial mstGroups", mstGroups)
            .add("index A", smallerGroupIndex)
            .add("index B", largerGroupIndex)
            .build();
        Set<N> largerMstGroup = mstGroups.get(largerGroupIndex);
        KruskalMSTCalculator<N> kruskalMSTCalculatorInstance = new KruskalMSTCalculator<>(Graph.of());
        Utils.setFieldValue(mstGroupsField, kruskalMSTCalculatorInstance, mstGroups);
        kruskalMSTCalculatorInstance.joinGroups(largerGroupIndex, smallerGroupIndex);

        assertTrue(mstGroups.stream().anyMatch(mstGroup -> mstGroup == largerMstGroup), context,
            result -> "[[[joinGroups]]] did not insert the values of the smaller group into the larger one");
    }

    @ParameterizedTest
    @ExtendWith(KruskalAcceptEdgeExtension.class)
    @JsonClasspathSource(value = "kruskalMSTCalculator.json", data = "acceptEdgeSameGroup")
    public <N> void testAcceptEdgeSameGroup(@Property("mstGroups") List<Set<N>> mstGroups,
                                            @Property("edge") SerializedEdge<N> serializedEdge) {
        Context context = contextBuilder()
            .add("mstGroups", mstGroups)
            .add("edge", serializedEdge)
            .build();
        KruskalMSTCalculator<N> kruskalMSTCalculatorInstance = new KruskalMSTCalculator<>(Graph.of());
        Utils.setFieldValue(mstGroupsField, kruskalMSTCalculatorInstance, mstGroups);

        assertFalse(kruskalMSTCalculatorInstance.acceptEdge(serializedEdge.toEdge()), context,
            result -> "[[[acceptEdge]]] did not return the expected value");
    }

    @ParameterizedTest
    @ExtendWith(KruskalAcceptEdgeExtension.class)
    @JsonClasspathSource(value = "kruskalMSTCalculator.json", data = "acceptEdgeDifferentGroup")
    public <N> void testAcceptEdgeDifferentGroup(@Property("mstGroups") List<Set<N>> mstGroups,
                                                 @Property("edge") SerializedEdge<N> serializedEdge) {
        Context context = contextBuilder()
            .add("mstGroups", mstGroups)
            .add("edge", serializedEdge)
            .build();
        KruskalMSTCalculator<N> kruskalMSTCalculatorInstance = new KruskalMSTCalculator<>(Graph.of());
        Utils.setFieldValue(mstGroupsField, kruskalMSTCalculatorInstance, mstGroups);

        assertTrue(kruskalMSTCalculatorInstance.acceptEdge(serializedEdge.toEdge()), context,
            result -> "[[[acceptEdge]]] did not return the expected value");
    }

    @ParameterizedTest
    @ExtendWith(JagrExecutionCondition.class)
    @JsonClasspathSource(value = "kruskalMSTCalculator.json", data = "acceptEdgeCallJoinGroups")
    public <N> void testAcceptEdgeCallJoinGroups(@Property("mstGroups") List<Set<N>> mstGroups,
                                                 @Property("edge") SerializedEdge<N> serializedEdge,
                                                 @Property("callJoinGroups") boolean callJoinGroups) {
        Context context = contextBuilder()
            .add("mstGroups", mstGroups)
            .add("edge", serializedEdge)
            .build();
        KruskalMSTCalculator<N> kruskalAcceptEdgeExtensionInstance = new KruskalMSTCalculator<>(Graph.of());
        Utils.setFieldValue(mstGroupsField, kruskalAcceptEdgeExtensionInstance, mstGroups);
        calledJoinGroups = false;
        kruskalAcceptEdgeExtensionInstance.acceptEdge(serializedEdge.toEdge());

        if (callJoinGroups) {
            assertTrue(calledJoinGroups, context,
                result -> "[[[acceptEdge]]] did not call [[[joinGroups]]] when it should have");
        } else {
            assertFalse(calledJoinGroups, context,
                result -> "[[[acceptEdge]]] called [[[joinGroups]]] when it should not have");
        }
    }

    @ParameterizedTest
    @ExtendWith(JagrExecutionCondition.class)
    @JsonClasspathSource(value = "kruskalMSTCalculator.json", data = "calculateMSTAcceptEdge")
    public <N> void testCalculateMSTAcceptEdge(@Property("graph") SerializedGraph<N> serializedGraph) {
        Context context = contextBuilder()
            .add("graph", serializedGraph)
            .build();
        Graph<N> graph = serializedGraph.toGraph();
        KruskalMSTCalculator<N> kruskalMSTCalculatorInstance = new KruskalMSTCalculator<>(graph);
        acceptEdgeParameters.clear();
        kruskalMSTCalculatorInstance.calculateMST();

        assertEquals(graph.getEdges().size(), acceptEdgeParameters.size(), context,
            result -> "[[[acceptEdge]]] was not invoked the expected number of times");
        @SuppressWarnings("unchecked") List<Edge<N>> actualAcceptEdgeParameters = acceptEdgeParameters.stream()
            .map(objects -> (Edge<N>) objects[0])
            .toList();
        for (Edge<N> edge : graph.getEdges()) {
            assertTrue(actualAcceptEdgeParameters.contains(edge), context,
                result -> "[[[calculateMST]]] did not call [[[acceptEdge]]] with edge " + edge);
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "kruskalMSTCalculator.json", data = "calculateMST")
    public <N> void testCalculateMST(@Property("graph") SerializedGraph<N> serializedGraph,
                                     @Property("expectedMST") SerializedGraph<N> expectedMST) {
        Context context = contextBuilder()
            .add("graph", serializedGraph)
            .add("expected MST", expectedMST)
            .build();
        KruskalMSTCalculator<N> kruskalMSTCalculatorInstance = new KruskalMSTCalculator<>(serializedGraph.toGraph());
        Graph<N> actualMST = kruskalMSTCalculatorInstance.calculateMST();

        assertEquals(expectedMST.nodes().size(), actualMST.getNodes().size(), context,
            result -> "The returned graph does not have the expected number of nodes");
        assertTrue(actualMST.getNodes().containsAll(expectedMST.nodes()), context,
            result -> "The returned graph does not contain all expected nodes");
        assertEquals(expectedMST.edges().size(), actualMST.getEdges().size(), context,
            result -> "The returned graph does not have the expected number of edges");
        assertTrue(actualMST.getEdges().containsAll(expectedMST.edges().stream().map(SerializedEdge::toEdge).toList()), context,
            result -> "The returned graph does not contain all expected edges");
    }
}
