package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        // return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @SuppressWarnings("checkstyle:LocalVariableName")
    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        DisjointSets<V> disjointSets = createDisjointSets();
        List<E> mstDone = new ArrayList<>();
        List<V> vertices = new ArrayList<>(graph.allVertices());
        for (V vertex : vertices) {
            disjointSets.makeSet(vertex);
        }

        // sort edges in the graph in ascending weight
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        int firstSet = 0;
        int secondSet = 0;

        for (E edge : edges) {
            int fromSet = disjointSets.findSet(edge.from());
            int toSet = disjointSets.findSet(edge.to());
            if (fromSet != toSet) {
                mstDone.add(edge);
                disjointSets.union(edge.from(), edge.to());
                if (disjointSets.findSet(edge.to()) == fromSet) {
                    firstSet = fromSet;
                    secondSet = toSet;
                } else {
                    firstSet = toSet;
                    secondSet = fromSet;
                }
                if (map.containsKey(firstSet) && map.containsKey(secondSet)) {
                    map.put(firstSet, map.get(firstSet) + map.get(secondSet));
                    if (map.get(firstSet) > max) {
                        max = map.get(firstSet);
                    }
                } else if (map.containsKey(disjointSets.findSet(edge.to()))) {
                    map.put(disjointSets.findSet(edge.to()),
                        map.get(disjointSets.findSet(edge.to())) + 1);
                    if (map.get(disjointSets.findSet(edge.to())) > max) {
                        max = map.get(disjointSets.findSet(edge.to()));
                    }
                } else {
                    map.put(disjointSets.findSet(edge.to()), 2);
                }
            }
        }
        MinimumSpanningTree<V, E> mst;
        if (max != vertices.size() && vertices.size() > 1) {
            mst = new MinimumSpanningTree.Failure<>();
        } else {
            mst = new MinimumSpanningTree.Success<>(mstDone);
        }
        return mst;

    }
}
