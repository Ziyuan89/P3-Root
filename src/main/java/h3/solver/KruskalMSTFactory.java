package h3.solver;

import h3.graph.BasicGraph;
import h3.graph.Edge;
import h3.graph.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A factory for creating a Minimum Spanning Tree using Kruskal's algorithm.
 */
public class KruskalMSTFactory<N> implements MSTFactory<N> {

    @Override
    public Graph<N> createMST(Graph<N> graph) {
        final Set<Edge<N>> mstEdges = new HashSet<>();
        // TODO: List<BinaryTree<T>> mstGroups
        final List<Set<N>> mstGroups = initGroups(graph);

        graph.getEdges().stream().sorted().forEach(edge -> {
            if (acceptEdge(mstGroups, edge)) {
                mstEdges.add(edge);
            }
        });

        return new BasicGraph<>(graph.getNodes(), mstEdges);
    }

    /**
     * Processes an edge during Kruskal's algorithm.
     * <p> If the edge's nodes are in the same MST, the edge is skipped.
     * <p> If the edge's nodes are in different MSTs, the MST's are merged by joining the corresponding sets.
     *
     * @param mstGroups The list of all MST Groups.
     * @param edge      The edge to process.
     * @return {@code true} if the edge was accepted and the two MST's were merged,
     * {@code false} if it was skipped.
     */
    protected boolean acceptEdge(List<Set<N>> mstGroups, Edge<N> edge) {
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

        if (aIndex == bIndex) {
            // edge's nodes are in the same MST
            // skip this edge, or it will create a cycle
            return false;
        }

        joinSets(mstGroups, aIndex, bIndex);

        return true;
    }

    /**
     * Initialize the list of all MST Groups.
     * <p> Each MST Group contains exactly one node of the graph and for every node is in exactly one group.
     *
     * @param graph The graph to create the MST Groups for.
     * @return The initialized list of all MST Groups.
     */
    protected List<Set<N>> initGroups(Graph<N> graph) {
        return graph.getNodes()
            .stream().map(n -> new HashSet<>(List.of(n)))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Join two sets in the list of all MST Groups.
     * <p> after joining the larger set will additionally contain all elements of the smaller set
     * the smaller set will be removed from the list.
     *
     * @param mstGroups The list of all MST Groups.
     * @param aIndex    The index of the first set to join.
     * @param bIndex    The index of the second set to join.
     */
    protected void joinSets(List<Set<N>> mstGroups, int aIndex, int bIndex) {
        if (mstGroups.get(aIndex).size() < mstGroups.get(bIndex).size()) {
            mstGroups.get(aIndex).addAll(mstGroups.get(bIndex));
            mstGroups.remove(bIndex);
        } else {
            mstGroups.get(bIndex).addAll(mstGroups.get(aIndex));
            mstGroups.remove(aIndex);
        }
    }
}
