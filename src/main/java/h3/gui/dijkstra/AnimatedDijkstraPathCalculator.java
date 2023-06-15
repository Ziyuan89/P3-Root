package h3.gui.dijkstra;

import h3.graph.Graph;
import h3.gui.Animation;
import h3.solver.DijkstraPathCalculator;
import javafx.application.Platform;

import java.util.*;

public class AnimatedDijkstraPathCalculator<N> extends DijkstraPathCalculator<N> implements Animation<N> {

    public static final Object syncObject = new Object();

    private final DijkstraScene<N> dijkstraScene;

    public AnimatedDijkstraPathCalculator(Graph<N> graph, DijkstraScene<N> dijkstraScene) {
        super(graph);
        this.dijkstraScene = dijkstraScene;
    }

    @Override
    protected void relax(N via, N dest, Graph.Edge<N> edge) {
        super.relax(via, dest, edge);

        Platform.runLater(() -> dijkstraScene.refresh(edge, via));
        waitUntilNextStep();
    }

    @Override
    protected void init(Graph<N> graph, N start) {
        super.init(graph, start);

        Platform.runLater(() -> dijkstraScene.refresh(null, null));
        waitUntilNextStep();
    }

    @Override
    public void start(N start, N end) {
        List<N> path = super.calculatePath(start, end);
        dijkstraScene.showResult(path);
    }

    @Override
    public Object getSyncObject() {
        return syncObject;
    }

    public String getDistance(N node) {
        return Objects.toString(distance.get(node));
    }

    public String getPredecessor(N node) {
        return Objects.toString(predecessors.get(node));
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
