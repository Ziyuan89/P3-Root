package p3.gui.dijkstra;

import p3.graph.Edge;
import p3.graph.Graph;
import p3.gui.Animation;
import p3.solver.DijkstraPathCalculator;
import javafx.application.Platform;

import java.util.*;

public class AnimatedDijkstraPathCalculator<N> extends DijkstraPathCalculator<N> implements Animation {

    private static final Object syncObject = new Object();

    private final DijkstraScene<N> dijkstraScene;
    private final N start;
    private final N end;

    public AnimatedDijkstraPathCalculator(Graph<N> graph, DijkstraScene<N> dijkstraScene, N start, N end) {
        super(graph);
        this.dijkstraScene = dijkstraScene;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void relax(N from, N to, Edge<N> edge) {
        super.relax(from, to, edge);

        Platform.runLater(() -> dijkstraScene.refresh(edge, from));
        waitUntilNextStep();
    }

    @Override
    protected void init(N start) {
        super.init(start);

        Platform.runLater(() -> dijkstraScene.refresh(null, null));
        waitUntilNextStep();
    }

    @Override
    public void start() {
        List<N> path = super.calculatePath(start, end);

        Platform.runLater(() -> {
            dijkstraScene.refresh(null, null);
            dijkstraScene.showResult(path);
        });
    }

    @Override
    public Object getSyncObject() {
        return syncObject;
    }

    public String getDistance(N node) {
        return Objects.toString(distances.get(node));
    }

    public String getPredecessor(N node) {
        return Objects.toString(predecessors.get(node));
    }

    public N getPredecessorNode(N node) {
        return predecessors.get(node);
    }

    public Set<N> getRemainingNodes() {
        return remainingNodes;
    }

    private void waitUntilNextStep() {
        synchronized (syncObject) {
            try {
                syncObject.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
