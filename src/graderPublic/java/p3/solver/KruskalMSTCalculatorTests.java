package p3.solver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p3.graph.Graph;
import p3.util.SerializedGraph;
import p3.util.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class KruskalMSTCalculatorTests {

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
}
