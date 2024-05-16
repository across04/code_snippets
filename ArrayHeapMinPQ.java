package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;

    int size;
    HashMap<T, Double> itemsCopy;
    HashMap<T, Integer> itemIndex;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        size = 0;

        itemsCopy = new HashMap<>();
        itemIndex = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        T item = items.get(a).getItem();
        itemIndex.put(items.get(b).getItem(), a);
        items.set(a, items.get(b));
        itemIndex.put(item, b);
        items.set(b, temp);


    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        PriorityNode<T> addedItem = new PriorityNode<>(item, priority);
        items.add(addedItem);
        size++;
        itemsCopy.put(item, priority);
        itemIndex.put(item, size - 1);

        percolateUp(size - 1);
    }

    private void percolateUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (items.get(index).getPriority() < items.get(parentIndex).getPriority()) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }


    @Override
    public boolean contains(T item) { //hash table/set contains
        return itemsCopy.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return getMin().getItem();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        itemsCopy.remove(getMin().getItem());
        PriorityNode<T> min = getMin();
        swap(0, size() - 1);
        items.remove(size() - 1);
        size--;
        percolateDown(0);
        return min.getItem();
    }

    private void percolateDown(int index) {
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        int smallest = index;

        if (leftChild < size() && items.get(leftChild).getPriority() < items.get(smallest).getPriority()) {
            smallest = leftChild;
        }

        if (rightChild < size() && items.get(rightChild).getPriority() < items.get(smallest).getPriority()) {
            smallest = rightChild;
        }

        if (smallest != index) {
            swap(index, smallest);
            percolateDown(smallest);
        }
    }

    @SuppressWarnings("checkstyle:CommentsIndentation")
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }


        double oldPriority = itemsCopy.get(item);
        items.get(itemIndex.get(item)).setPriority(priority);
        itemsCopy.put(item, priority);

        if (priority < oldPriority) {
            percolateUp(itemIndex.get(item));
        } else if (priority > oldPriority) {
            percolateDown(itemIndex.get(item));

        }


    }

    @Override
    public int size() {
        return size;
    }

    private PriorityNode<T> getMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }

        return items.get(0);
    }
}
