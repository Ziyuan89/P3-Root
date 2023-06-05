package h3.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KruskalMSTFactory implements MSTFactory {
    @Override
    public <N> Graph<N> createMST(Graph<N> graph) {
        final Set<Graph.Edge<N>> mstEdges = new HashSet<>();
        // TODO: List<BinaryTree<T>> mstGroups
        final List<Set<N>> mstGroups = graph.getNodes()
            .stream().map(n -> Stream.of(n).collect(Collectors.toCollection(HashSet::new)))
            .collect(Collectors.toCollection(ArrayList::new));
        graph.getEdges().stream().sorted().forEach(edge -> {
            int aIndex = -1;
            int bIndex = -1;
            // check if edge's nodes are in the same MST
            for (int i = 0; i < mstGroups.size(); i++) {
                Set<N> group = mstGroups.get(i);
                if (group.contains(edge.getA())) {
                    aIndex = i;
                }
                if (group.contains(edge.getB())) {
                    bIndex = i;
                }
                if (aIndex != -1 && bIndex != -1) {
                    break;
                }
                if (aIndex == bIndex) {
                    // edge's nodes are in the same MST
                    // skip this edge or it will create a cycle
                    return;
                }
            }

            // add edge to larger MST
            if (mstGroups.get(aIndex).size() < mstGroups.get(bIndex).size()) {
                mstGroups.get(aIndex).addAll(mstGroups.get(bIndex));
                mstGroups.remove(bIndex);
            } else {
                mstGroups.get(bIndex).addAll(mstGroups.get(aIndex));
                mstGroups.remove(aIndex);
            }
            mstEdges.add(edge);
        });

        return new BasicGraph<>(graph.getNodes(), mstEdges);
    }
}
