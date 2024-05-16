package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> indexMap;

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        indexMap = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        if (indexMap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        indexMap.put(item, pointers.size());
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (!indexMap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = indexMap.get(item);
        Set<Integer> childIndices = new HashSet<>();
        while (pointers.get(index) >= 0) {
            childIndices.add(index);
            index = pointers.get(index);
        }
        for (Integer child : childIndices) {
            pointers.set(child, index);
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!indexMap.containsKey(item1) || !indexMap.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int root1 = findSet(item1);
        int root2 = findSet(item2);
        if (root1 == root2) {
            return false;
        }
        int weight1 = -1 * pointers.get(root1);
        int weight2 = -1 * pointers.get(root2);
        if (weight1 > weight2) {
            pointers.set(root1, pointers.get(root1) + pointers.get(root2));
            pointers.set(root2, root1);
        } else {
            pointers.set(root2, pointers.get(root1) + pointers.get(root2));
            pointers.set(root1, root2);
        }
        return true;
    }
}
