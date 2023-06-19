package h3.gui.kruskal;

import h3.graph.Edge;
import h3.graph.Graph;
import h3.gui.Animation;
import h3.solver.KruskalMSTFactory;
import javafx.application.Platform;

import java.util.List;
import java.util.Set;

public class AnimatedKruskalMSTFactory<N> extends KruskalMSTFactory<N> implements Animation {

    private static final Object syncObject = new Object();

    private final KruskalScene<N> kruskalScene;
    private final Graph<N> graph;

    private List<Set<N>> mstGroups = List.of();

    public AnimatedKruskalMSTFactory(Graph<N> graph, KruskalScene<N> kruskalScene) {
        super();
        this.graph = graph;
        this.kruskalScene = kruskalScene;
    }

    @Override
    protected boolean acceptEdge(List<Set<N>> mstGroups, Edge<N> edge) {
        boolean accepted = super.acceptEdge(mstGroups, edge);
        this.mstGroups = mstGroups;

        Platform.runLater(() -> kruskalScene.refresh(edge, accepted));
        waitUntilNextStep();

        return accepted;
    }

    @Override
    protected List<Set<N>> initGroups(Graph<N> graph) {
        List<Set<N>> mstGroups =  super.initGroups(graph);
        this.mstGroups = mstGroups;

        Platform.runLater(() -> kruskalScene.refresh(null, false));
        waitUntilNextStep();

        return mstGroups;
    }

    @Override
    public void start() {
        Graph<N> mst = super.createMST(graph);

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
