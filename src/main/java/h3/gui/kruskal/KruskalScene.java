package h3.gui.kruskal;

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

import static h3.gui.GraphStyle.*;

public class KruskalScene<N> extends AnimationScene {

    private final BorderPane root;

    private GraphPane<N> graphPane;
    private Graph<N> graph;
    private Map<N, Location> nodeLocations;
    private KruskalInfoPane<N> infoPane;
    private ControlPane controlPane;
    private AnimatedKruskalMSTCalculator<N> animation;

    private final List<Edge<N>> acceptedEdges = new ArrayList<>();
    private final List<Edge<N>> rejectedEdges = new ArrayList<>();

    public KruskalScene() {
        super(new BorderPane());
        root = (BorderPane) getRoot();
        root.setPrefSize(700, 700);
    }

    public void init(Graph<N> graph, Map<N, Location> nodeLocations) {
        this.graph = graph;
        this.nodeLocations = nodeLocations;

        graphPane = new GraphPane<>(graph, nodeLocations);
        root.setCenter(graphPane);

        animation = new AnimatedKruskalMSTCalculator<>(graph, this);

        infoPane = new KruskalInfoPane<>(graph, animation);
        root.setRight(infoPane);

        controlPane = new ControlPane();
        controlPane.init(animation);
        root.setBottom(controlPane);

        new Thread(() -> animation.start()).start();
    }

    public void refresh(Edge<N> visitedEdge, boolean accepted) {
        if (visitedEdge != null) {
            for (Edge<N> edge : graph.getEdges()) {
                if (edge.equals(visitedEdge)) {
                    graphPane.setEdgeColor(edge, accepted ? KRUSKAL_ACCEPTED_EDGE : KRUSKAL_REJECTED_EDGE);
                    graphPane.setEdgeDash(edge, accepted ? KRUSKAL_ACCEPTED_EDGE_DASHED : KRUSKAL_REJECTED_EDGE_DASHED,
                            accepted ? KRUSKAL_ACCEPTED_EDGE_DASH_LENGTH : KRUSKAL_REJECTED_EDGE_DASH_LENGTH,
                            accepted ? KRUSKAL_ACCEPTED_EDGE_GAP_LENGTH : KRUSKAL_REJECTED_EDGE_GAP_LENGTH);
                } else if (acceptedEdges.contains(edge)) {
                    graphPane.setEdgeColor(edge, KRUSKAL_ACCEPTED_EDGE);
                    graphPane.setEdgeDash(edge, KRUSKAL_ACCEPTED_EDGE_DASHED, KRUSKAL_ACCEPTED_EDGE_DASH_LENGTH, KRUSKAL_ACCEPTED_EDGE_GAP_LENGTH);
                } else if (rejectedEdges.contains(edge)) {
                    graphPane.setEdgeColor(edge, KRUSKAL_REJECTED_EDGE);
                    graphPane.setEdgeDash(edge, KRUSKAL_REJECTED_EDGE_DASHED, KRUSKAL_REJECTED_EDGE_DASH_LENGTH, KRUSKAL_REJECTED_EDGE_GAP_LENGTH);
                } else {
                    graphPane.setEdgeColor(edge, KRUSKAL_UNVISITED_EDGE);
                    graphPane.setEdgeDash(edge, KRUSKAL_UNVISITED_EDGE_DASHED, KRUSKAL_UNVISITED_EDGE_DASH_LENGTH, KRUSKAL_UNVISITED_EDGE_GAP_LENGTH);
                }
            }

            if (accepted) {
                acceptedEdges.add(visitedEdge);
            } else {
                rejectedEdges.add(visitedEdge);
            }
        } else {
            for (Edge<N> edge : graph.getEdges()) {
                graphPane.setEdgeColor(edge, KRUSKAL_UNVISITED_EDGE);
                graphPane.setEdgeDash(edge, KRUSKAL_UNVISITED_EDGE_DASHED, KRUSKAL_UNVISITED_EDGE_DASH_LENGTH, KRUSKAL_UNVISITED_EDGE_GAP_LENGTH);
            }
        }

        infoPane.refresh();
    }

    public void showResult(Graph<N> graph) {
        graphPane = new GraphPane<>(graph, nodeLocations);

        for (Edge<N> edge : graph.getEdges()) {
            graphPane.setEdgeColor(edge, KRUSKAL_RESULT_EDGE);
            graphPane.setEdgeDash(edge, KRUSKAL_RESULT_EDGE_DASHED, KRUSKAL_RESULT_EDGE_DASH_LENGTH, KRUSKAL_RESULT_EDGE_GAP_LENGTH);
        }

        root.setCenter(graphPane);

        controlPane.disableNextStepButton();
    }

    public String getTitle() {
        return "Kruskal Animation";
    }
}
