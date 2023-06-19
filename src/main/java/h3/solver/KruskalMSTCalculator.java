package h3.solver;

import h3.graph.Edge;
import h3.graph.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A factory for creating a Minimum Spanning Tree using Kruskal's algorithm.
 */
public class KruskalMSTCalculator<N> implements MSTCalculator<N> {

    public static MSTCalculator.Factory FACTORY = KruskalMSTCalculator::new;

    protected final Graph<N> graph;
    protected final Set<Edge<N>> mstEdges;
    protected final List<Set<N>> mstGroups;

    public KruskalMSTCalculator(Graph<N> graph) {
        this.graph = graph;
        this.mstEdges = new HashSet<>();
        this.mstGroups = new ArrayList<>();
    }

    @Override
    public Graph<N> calculateMST() {

        init();

        graph.getEdges().stream().sorted().forEachOrdered(edge -> {
            if (acceptEdge(edge)) {
                mstEdges.add(edge);
            }
        });

        return Graph.of(graph.getNodes(), mstEdges);
    }

    protected void init() {
        mstEdges.clear();
        mstGroups.clear();
        graph.getNodes()
            .stream().map(n -> new HashSet<>(List.of(n)))
            .forEach(mstGroups::add);
    }

    /**
     * Processes an edge during Kruskal's algorithm.
     * <p> If the edge's nodes are in the same MST, the edge is skipped.
     * <p> If the edge's nodes are in different MSTs, the MST's are merged by joining the corresponding sets.
     *
     * @param edge The edge to process.
     * @return {@code true} if the edge was accepted and the two MST's were merged,
     * {@code false} if it was skipped.
     */
    protected boolean acceptEdge(Edge<N> edge) {
        int aIndex = -1;
        int bIndex = -1;

        // check if edge's nodes are in the same MST
        for (int i = 0; i < mstGroups.size(); i++) {
            Set<N> group = mstGroups.get(i);
            if (group.contains(edge.a())) {
                aIndex = i;
            }
            if (group.contains(edge.b())) {
                bIndex = i;
            }
            if (aIndex != -1 && bIndex != -1) {
                break;
            }
        }

        if (aIndex == -1 || bIndex == -1) {
            // edge's nodes are not in any MST
            // this should never happen
            throw new IllegalStateException("Edge's nodes are not in any MST");
        }

        if (aIndex == bIndex) {
            // edge's nodes are in the same MST
            // skip this edge, or it will create a cycle
            return false;
        }

        joinSets(aIndex, bIndex);

        return true;
    }

    /**
     * Join two sets in the list of all MST Groups.
     * <p> after joining the larger set will additionally contain all elements of the smaller set
     * the smaller set will be removed from the list.
     *
     * @param aIndex The index of the first set to join.
     * @param bIndex The index of the second set to join.
     */
    protected void joinSets(int aIndex, int bIndex) {
        if (mstGroups.get(aIndex).size() < mstGroups.get(bIndex).size()) {
            mstGroups.get(aIndex).addAll(mstGroups.get(bIndex));
            mstGroups.remove(bIndex);
        } else {
            mstGroups.get(bIndex).addAll(mstGroups.get(aIndex));
            mstGroups.remove(aIndex);
        }
    }
}
