package h3.gui.dijkstra;

import h3.graph.Graph;
import h3.gui.AnimationScene;
import h3.gui.ControlPane;
import h3.gui.GraphPane;
import h3.gui.Location;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void refresh(Graph.Edge<N> visitedEdge, N viaNode) {
        if (visitedEdge != null) {

            for (Graph.Edge<N> edge : graph.getEdges()) {
                if (edge.equals(visitedEdge)) {
                    graphPane.setEdgeColor(edge, Color.GREEN);
                } else if (animation.getPredecessor(edge.getA()).equals(edge.getB()) || animation.getPredecessor(edge.getB()).equals(edge.getA())) {
                    graphPane.setEdgeColor(edge, Color.RED);
                } else {
                    graphPane.resetEdgeColor(edge);
                }
            }

            for (N node : graph.getNodes()) {
                if (node.equals(viaNode)) {
                    graphPane.setNodeColor(node, Color.GREEN);
                } else if (visitedNodes.contains(node)) {
                    graphPane.setNodeColor(node, Color.RED);
                } else {
                    graphPane.resetNodeColor(node);
                }
            }
        }

        visitedNodes.add(viaNode);

        infoPane.refresh();
    }

    public void showResult(List<N> path) {
        for (Graph.Edge<N> edge : graph.getEdges()) {
            graphPane.resetEdgeColor(edge);
        }

        for (N node : graph.getNodes()) {
            graphPane.resetNodeColor(node);
        }

        for (int i = 0; i < path.size() - 1; i++) {
            N node = path.get(i);
            N nextNode = path.get(i + 1);

            Graph.Edge<N> edge = graph.getAdjacentEdges(node).stream()
                    .filter(e -> e.getA().equals(nextNode) || e.getB().equals(nextNode))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No edge between " + node + " and " + nextNode + " found"));
            graphPane.setEdgeColor(edge, Color.GREEN);

            graphPane.setNodeColor(node, Color.GREEN);
        }

        graphPane.setNodeColor(path.get(path.size() - 1), Color.GREEN);

        controlPane.disableNextStepButton();
    }

    public String getTitle() {
        return "Dijkstra Animation";
    }
}
