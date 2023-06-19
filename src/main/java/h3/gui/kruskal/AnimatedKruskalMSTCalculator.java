package h3.gui.kruskal;

import h3.graph.Edge;
import h3.graph.Graph;
import h3.gui.Animation;
import h3.solver.KruskalMSTCalculator;
import javafx.application.Platform;

import java.util.List;
import java.util.Set;

public class AnimatedKruskalMSTCalculator<N> extends KruskalMSTCalculator<N> implements Animation {

    private static final Object syncObject = new Object();

    private final KruskalScene<N> kruskalScene;

    public AnimatedKruskalMSTCalculator(Graph<N> graph, KruskalScene<N> kruskalScene) {
        super(graph);
        this.kruskalScene = kruskalScene;
    }

    @Override
    protected boolean acceptEdge(Edge<N> edge) {
        boolean accepted = super.acceptEdge(edge);

        Platform.runLater(() -> kruskalScene.refresh(edge, accepted));
        waitUntilNextStep();

        return accepted;
    }

    @Override
    protected void init() {
        super.init();

        Platform.runLater(() -> kruskalScene.refresh(null, false));
        waitUntilNextStep();
    }

    @Override
    public void start() {
        Graph<N> mst = super.calculateMST();

        Platform.runLater(() -> kruskalScene.showResult(mst));
    }

    @Override
    public Object getSyncObject() {
        return syncObject;
    }

    public List<Set<N>> getMstGroups() {
        return mstGroups;
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
