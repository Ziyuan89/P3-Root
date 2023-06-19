package h3.gui.dijkstra;

import h3.graph.Edge;
import h3.graph.Graph;
import h3.gui.AnimationScene;
import h3.gui.ControlPane;
import h3.gui.GraphPane;
import h3.gui.Location;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static h3.gui.GraphStyle.*;

public class DijkstraScene<N> extends AnimationScene {

    private final BorderPane root;

    private GraphPane<N> graphPane;
    private Graph<N> graph;
    private DijkstraInfoPane<N> infoPane;
    private AnimatedDijkstraPathCalculator<N> animation;

    private final List<N> visitedNodes = new ArrayList<>();
    private ControlPane controlPane;

    public DijkstraScene() {
        super(new BorderPane());
        root = (BorderPane) getRoot();
        root.setPrefSize(700, 700);
    }

    public void init(Graph<N> graph, Map<N, Location> nodeLocations, N start, N end) {
        this.graph = graph;

        graphPane = new GraphPane<>(graph, nodeLocations);
        root.setCenter(graphPane);


        animation = new AnimatedDijkstraPathCalculator<>(graph, this, start, end);

        infoPane = new DijkstraInfoPane<>(graph, animation);
        root.setRight(infoPane);

        controlPane = new ControlPane();
        controlPane.init(animation);
        root.setBottom(controlPane);

        new Thread(() -> animation.start()).start();
    }

    public void refresh(Edge<N> visitedEdge, N viaNode) {
        if (visitedEdge != null) {

            for (Edge<N> edge : graph.getEdges()) {
                if (edge.equals(visitedEdge)) {
                    graphPane.setEdgeColor(edge, DIJKSTRA_CURRENT_EDGE);
                } else if (Objects.equals(animation.getPredecessorNode(edge.a()), edge.b()) ||
                Objects.equals(animation.getPredecessorNode(edge.b()), edge.a())) {
                    graphPane.setEdgeColor(edge, DIJKSTRA_PREDECESSOR_EDGE);
                } else {
                    graphPane.setEdgeColor(edge, DIJKSTRA_UNVISITED_EDGE);
                }
            }

            for (N node : graph.getNodes()) {
                if (node.equals(viaNode)) {
                    graphPane.setNodeColor(node, DIJKSTRA_CURRENT_NODE);
                } else if (visitedNodes.contains(node)) {
                    graphPane.setNodeColor(node, DIJKSTRA_VISITED_NODE);
                } else {
                    graphPane.setNodeColor(node, DIJKSTRA_UNVISITED_NODE);
                }
            }
        }

        visitedNodes.add(viaNode);

        infoPane.refresh();
    }

    public void showResult(List<N> path) {
        for (Edge<N> edge : graph.getEdges()) {
            graphPane.resetEdgeColor(edge);
        }

        for (N node : graph.getNodes()) {
            graphPane.resetNodeColor(node);
        }

        for (int i = 0; i < path.size() - 1; i++) {
            N node = path.get(i);
            N nextNode = path.get(i + 1);

            Edge<N> edge = graph.getAdjacentEdges(node).stream()
                    .filter(e -> e.a().equals(nextNode) || e.b().equals(nextNode))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No edge between " + node + " and " + nextNode + " found"));
            graphPane.setEdgeColor(edge, DIJKSTRA_RESULT_EDGE);

            graphPane.setNodeColor(node, DIJKSTRA_RESULT_NODE);
        }

        graphPane.setNodeColor(path.get(path.size() - 1), DIJKSTRA_RESULT_NODE);

        controlPane.disableNextStepButton();
    }

    public String getTitle() {
        return "Dijkstra Animation";
    }
}
