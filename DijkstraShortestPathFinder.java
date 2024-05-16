package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        HashMap<V, E> shortPathTree = new HashMap<>();
        if (Objects.equals(start, end)) {
            return shortPathTree;
        }

        Map<V, Double> distTo = new HashMap<>();
        Set<V> visited = new HashSet<>();
        ExtrinsicMinPQ<V> close = createMinPQ();

        distTo.put(start, 0.0);
        close.add(start, 0.0);

        while (!close.isEmpty() && !visited.contains(end)) {
            V currentVertex = close.removeMin();
            visited.add(currentVertex);
            for (E edge : graph.outgoingEdgesFrom(currentVertex)) {
                if (!distTo.containsKey(edge.to())) {
                    distTo.put(edge.to(), (distTo.get(edge.from()) + edge.weight()));
                    shortPathTree.put(edge.to(), edge);
                } else {
                    double oldDist = distTo.get(edge.to());
                    double newDist = distTo.get(edge.from()) + edge.weight();
                    if (newDist < oldDist) {
                        distTo.put(edge.to(), newDist);
                        shortPathTree.put(edge.to(), edge);
                    }
                }

                if (!visited.contains(edge.to())) {
                    if (!close.contains(edge.to())) {
                        close.add(edge.to(), distTo.get(edge.to()));
                    } else {
                        close.changePriority(edge.to(), distTo.get(edge.to()));
                    }
                }

            }

        }

        return shortPathTree;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }

        List<E> shortestPath = new ArrayList<>();
        V current = end;
        while (!Objects.equals(current, start)) {
            shortestPath.add(0, spt.get(current));
            current = spt.get(current).from();
        }

        return new ShortestPath.Success<>(shortestPath);
    }


}
