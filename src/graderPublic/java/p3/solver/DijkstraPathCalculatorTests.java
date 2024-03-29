package p3.solver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p3.graph.Graph;
import p3.util.SerializedEdge;
import p3.util.SerializedEntry;
import p3.util.SerializedGraph;
import p3.util.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class DijkstraPathCalculatorTests {

    private static Field distancesField;
    private static Field predecessorsField;
    private static Field remainingNodesField;

    @BeforeAll
    public static void setup() {
        try {
            distancesField = DijkstraPathCalculator.class.getDeclaredField("distances");
            distancesField.setAccessible(true);
            predecessorsField = DijkstraPathCalculator.class.getDeclaredField("predecessors");
            predecessorsField.setAccessible(true);
            remainingNodesField = DijkstraPathCalculator.class.getDeclaredField("remainingNodes");
            remainingNodesField.setAccessible(true);
        } catch (NoSuchFieldException | InaccessibleObjectException e) {
            // FIXME: use Jagr logger when working again
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "init")
    public <N> void testInit(@Property("graph") SerializedGraph<N> serializedGraph,
                             @Property("startNode") N startNode) {
        Context context = contextBuilder()
            .add("graph", serializedGraph)
            .add("start node", startNode)
            .build();
        DijkstraPathCalculator<N> dijkstraPathCalculatorInstance = new DijkstraPathCalculator<>(serializedGraph.toGraph());
        dijkstraPathCalculatorInstance.init(startNode);
        Map<N, Integer> distances = Utils.getFieldValue(distancesField, dijkstraPathCalculatorInstance);
        Map<N, N> predecessors = Utils.getFieldValue(predecessorsField, dijkstraPathCalculatorInstance);
        Set<N> remainingNodes = Utils.getFieldValue(remainingNodesField, dijkstraPathCalculatorInstance);

        assertEquals(serializedGraph.nodes().size(), distances.size(), context,
            result -> "[[[distances]]] does not have the expected number of entries");
        assertEquals(serializedGraph.nodes().size(), predecessors.size(), context,
            result -> "[[[predecessors]]] does not have the expected number of entries");
        assertEquals(serializedGraph.nodes().size(), remainingNodes.size(), context,
            result -> "[[[remainingNodes]]] does not have the expected number of entries");

        for (N node : serializedGraph.nodes()) {
            assertTrue(distances.containsKey(node), context,
                result -> "[[[distances]]] does not contain node (key) " + node);
            assertEquals(node.equals(startNode) ? 0 : Integer.MAX_VALUE, distances.get(node), context,
                result -> "The value mapped to %s in [[[distances]]] does not equal the expected value".formatted(node));

            assertTrue(predecessors.containsKey(node), context,
                result -> "[[[predecessors]]] does not contain node (key) " + node);
            assertNull(predecessors.get(node), context,
                result -> "The value mapped to %s in [[[predecessors]]] does not equal the expected value".formatted(node));

            assertTrue(remainingNodes.containsAll(serializedGraph.nodes()), context,
                result -> "");
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "extractMinSimple")
    public <N> void testExtractMinSimple(@Property("graph") SerializedGraph<N> serializedGraph,
                                         @Property("distances") List<SerializedEntry<N, Integer>> serializedDistances,
                                         @Property("expected") N expectedNode) {
        testExtractMin(serializedGraph, serializedDistances, serializedGraph.nodes(), expectedNode);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "extractMinFull")
    public <N> void testExtractMinFull(@Property("graph") SerializedGraph<N> serializedGraph,
                                       @Property("distances") List<SerializedEntry<N, Integer>> serializedDistances,
                                       @Property("remainingNodes") Set<N> remainingNodes,
                                       @Property("expected") N expectedNode) {
        testExtractMin(serializedGraph, serializedDistances, remainingNodes, expectedNode);
    }

    private static <N> void testExtractMin(SerializedGraph<N> serializedGraph,
                                           List<SerializedEntry<N, Integer>> serializedDistances,
                                           Set<N> remainingNodes,
                                           N expectedNode) {
        Context context = contextBuilder()
            .add("graph", serializedGraph)
            .add("distances", serializedDistances)
            .build();
        DijkstraPathCalculator<N> dijkstraPathCalculatorInstance = new DijkstraPathCalculator<>(serializedGraph.toGraph());
        Utils.setFieldValue(distancesField, dijkstraPathCalculatorInstance, Utils.deserializeMap(serializedDistances));
        Utils.setFieldValue(remainingNodesField, dijkstraPathCalculatorInstance, remainingNodes);

        assertEquals(expectedNode, dijkstraPathCalculatorInstance.extractMin(), context,
            result -> "[[[extractMin]]] did not return the expected node");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "relaxDistances")
    public <N> void testRelaxDistances(@Property("edge") SerializedEdge<N> serializedEdge,
                                       @Property("initialDistances") List<SerializedEntry<N, Integer>> serializedInitialDistances,
                                       @Property("initialPredecessors") List<SerializedEntry<N, N>> serializedInitialPredecessors,
                                       @Property("expectedDistances") List<SerializedEntry<N, Integer>> serializedExpectedDistances) {
        Context context = contextBuilder()
            .add("edge", serializedEdge)
            .add("initial distances", serializedInitialDistances)
            .add("initial predecessors", serializedInitialPredecessors)
            .add("expected distances", serializedExpectedDistances)
            .build();
        DijkstraPathCalculator<N> dijkstraPathCalculatorInstance = new DijkstraPathCalculator<>(Graph.of());
        Utils.setFieldValue(distancesField, dijkstraPathCalculatorInstance, Utils.deserializeMap(serializedInitialDistances));
        Utils.setFieldValue(predecessorsField, dijkstraPathCalculatorInstance, Utils.deserializeMap(serializedInitialPredecessors));
        dijkstraPathCalculatorInstance.relax(serializedEdge.a(), serializedEdge.b(), serializedEdge.toEdge());
        Map<N, Integer> expectedDistances = Utils.deserializeMap(serializedExpectedDistances);
        Map<N, Integer> actualDistances = Utils.getFieldValue(distancesField, dijkstraPathCalculatorInstance);

        assertEquals(expectedDistances.size(), actualDistances.size(), context,
            result -> "[[[distances]]] does not have the correct size");
        assertTrue(actualDistances.keySet().containsAll(expectedDistances.keySet()), context,
            result -> "[[[distances]]] does not contain all expected keys (nodes)");
        for (N node : expectedDistances.keySet()) {
            assertEquals(expectedDistances.get(node), actualDistances.get(node), context,
                result -> "[[[distances]]] does not contain the correct mapping for node " + node);
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "relaxPredecessors")
    public <N> void testRelaxPredecessors(@Property("edge") SerializedEdge<N> serializedEdge,
                                          @Property("initialDistances") List<SerializedEntry<N, Integer>> serializedInitialDistances,
                                          @Property("initialPredecessors") List<SerializedEntry<N, N>> serializedInitialPredecessors,
                                          @Property("expectedPredecessors") List<SerializedEntry<N, N>> serializedExpectedPredecessors) {
        Context context = contextBuilder()
            .add("edge", serializedEdge)
            .add("initial distances", serializedInitialDistances)
            .add("initial predecessors", serializedInitialPredecessors)
            .add("expected predecessors", serializedExpectedPredecessors)
            .build();
        DijkstraPathCalculator<N> dijkstraPathCalculatorInstance = new DijkstraPathCalculator<>(Graph.of());
        Utils.setFieldValue(distancesField, dijkstraPathCalculatorInstance, Utils.deserializeMap(serializedInitialDistances));
        Utils.setFieldValue(predecessorsField, dijkstraPathCalculatorInstance, Utils.deserializeMap(serializedInitialPredecessors));
        dijkstraPathCalculatorInstance.relax(serializedEdge.a(), serializedEdge.b(), serializedEdge.toEdge());
        Map<N, N> expectedPredecessors = Utils.deserializeMap(serializedExpectedPredecessors);
        Map<N, N> actualPredecessors = Utils.getFieldValue(predecessorsField, dijkstraPathCalculatorInstance);

        assertEquals(expectedPredecessors.size(), actualPredecessors.size(), context,
            result -> "[[[predecessors]]] does not have the expected size");
        assertTrue(actualPredecessors.keySet().containsAll(expectedPredecessors.keySet()), context,
            result -> "[[[predecessors]]] does not contain all expected keys (nodes)");
        for (N node : expectedPredecessors.keySet()) {
            assertEquals(expectedPredecessors.get(node), actualPredecessors.get(node), context,
                result -> "[[[predecessors]]] does not contain the correct mapping for node " + node);
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "reconstructPath")
    public <N> void testReconstructPath(@Property("startNode") N start,
                                        @Property("endNode") N end,
                                        @Property("predecessors") List<SerializedEntry<N, N>> serializedPredecessors,
                                        @Property("expectedPath") List<N> expectedPath) {
        DijkstraPathCalculator<N> dijkstraPathCalculatorInstance = new DijkstraPathCalculator<>(Graph.of());
        Utils.setFieldValue(predecessorsField, dijkstraPathCalculatorInstance, Utils.deserializeMap(serializedPredecessors));

        N startNodeInPredecessor = serializedPredecessors.stream()
            .filter(entry -> entry.value() != null)
            .filter(entry -> entry.value().equals(start))
            .map(SerializedEntry::value)
            .findFirst()
            .orElse(null);

        N endNodeInPredecessor = serializedPredecessors.stream()
            .filter(entry -> entry.key() != null)
            .filter(entry -> entry.key().equals(end))
            .map(SerializedEntry::key)
            .findFirst()
            .orElse(null);

        List<N> actualPath = dijkstraPathCalculatorInstance.reconstructPath(startNodeInPredecessor, endNodeInPredecessor);
        Context context = contextBuilder()
            .add("start node", start)
            .add("end node", end)
            .add("predecessors", serializedPredecessors)
            .add("expected path", expectedPath)
            .add("actual path (returned value)", actualPath)
            .build();

        assertEquals(expectedPath.size(), actualPath.size(), context,
            result -> "The returned path does not have the correct length");
        assertTrue(actualPath.containsAll(expectedPath), context,
            result -> "The returned path does not contain all expected nodes");
        for (int i = 0; i < expectedPath.size(); i++) {
            final int finalI = i;
            assertEquals(expectedPath.get(i), actualPath.get(i), context,
                result -> "The returned path differs from the expected one at index " + finalI);
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "calculatePathTwoNodes")
    public <N> void testCalculatePathTwoNodes(@Property("graph") SerializedGraph<N> serializedGraph,
                                              @Property("startNode") N startNode,
                                              @Property("endNode") N endNode,
                                              @Property("expectedPath") List<N> expectedPath) {
        testCalculatePathReferenceEquality(serializedGraph, startNode, endNode, expectedPath);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "calculatePathThreeNodes")
    public <N> void testCalculatePathThreeNodes(@Property("graph") SerializedGraph<N> serializedGraph,
                                                @Property("startNode") N startNode,
                                                @Property("endNode") N endNode,
                                                @Property("expectedPath") List<N> expectedPath) {
        testCalculatePathReferenceEquality(serializedGraph, startNode, endNode, expectedPath);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "dijkstraPathCalculator.json", data = "calculatePathAnyNodes")
    public <N> void testCalculatePathAnyNodes(@Property("graph") SerializedGraph<N> serializedGraph,
                                              @Property("startNode") N startNode,
                                              @Property("endNode") N endNode,
                                              @Property("expectedPath") List<N> expectedPath) {
        testCalculatePath(serializedGraph, startNode, endNode, expectedPath);
    }

    public <N> void testCalculatePathReferenceEquality(SerializedGraph<N> serializedGraph, N startNode, N endNode, List<N> expectedPath) {
        Graph<N> graph = serializedGraph.toGraph();
        N startNodeInGraph = graph.getNodes().stream().filter(node -> node.equals(startNode)).findFirst().orElseThrow();
        N endNodeInGraph = graph.getNodes().stream().filter(node -> node.equals(endNode)).findFirst().orElseThrow();
        testCalculatePath(serializedGraph, startNodeInGraph, endNodeInGraph, expectedPath);
    }

    public <N> void testCalculatePath(SerializedGraph<N> serializedGraph, N startNode, N endNode, List<N> expectedPath) {
        Graph<N> graph = serializedGraph.toGraph();
        List<N> actualPath = new DijkstraPathCalculator<>(graph).calculatePath(startNode, endNode);
        Context context = contextBuilder()
            .add("graph", serializedGraph)
            .add("startNode", startNode)
            .add("endNode", endNode)
            .add("expectedPath", expectedPath)
            .build();

        assertNotNull(actualPath, context, result -> "Returned object is null");
        assertEquals(expectedPath.size(), actualPath.size(), context,
            result -> "Returned path does not contain the expected number of nodes");
        for (int i = 0; i < expectedPath.size(); i++) {
            final int finalI = i;
            assertEquals(expectedPath.get(i), actualPath.get(i), context,
                result -> "The returned path differs from the expected one at index " + finalI);
        }
    }
}
