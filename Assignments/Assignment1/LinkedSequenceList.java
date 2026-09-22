/**
 * LinkedSequenceList: a SequenceList backed by singly linked nodes.
 *
 * Each element lives in its own Node<T>. The list itself only remembers
 * "head" (a reference to the first node) and "size". Everything else is
 * reached by following "next" references, one node at a time.
 *
 * Node is declared private and static, nested inside this class:
 *   - private: outside code can never see or touch a Node directly. All
 *     access happens through the methods below, which is what lets this
 *     class guarantee its own invariant (following head -> next -> ...
 *     visits exactly the stored elements, in order, ending at null).
 *   - static: a Node does not need a reference to its enclosing
 *     LinkedSequenceList, so it does not carry one.
 */
public class LinkedSequenceList<T> implements SequenceList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private int size;

    public LinkedSequenceList() {
        head = null;
        size = 0;
    }

    // Append at the end of the list.
    // Given as a worked example -- study how it walks to the last node
    // before writing add(int, T) and remove(int) below.
    @Override
    public void add(T item) {
        Node<T> newNode = new Node<>(item);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * TODO 1: Insert item so it ends up at position index.
     *
     * Hints:
     *  - If index == 0, the new node becomes the new head.
     *  - Otherwise, find the node currently at position (index - 1) --
     *    you can reuse the private helper method node(int) below -- and
     *    splice the new node in right after it.
     *  - Remember to increase size.
     *
     * The index check below is already written for you.
     */
    @Override
    public void add(int index, T item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        Node<T> newNode = new Node<>(item);
        if (index ==0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node<T> previous = node(index -1);
            newNode.next = previous.next;
            previous.next = newNode;
        }
        size++;
    }
            
        // TODO: implement insertion at an arbitrary position
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return node(index).data;
    }

    /**
     * TODO 2: Remove the element at position index and return it.
     *
     * Hints:
     *  - If index == 0, head itself must change.
     *  - Otherwise, find the node at (index - 1) and redirect its
     *    "next" to skip over the node being removed.
     *  - Remember to decrease size.
     *
     * checkIndex(index) has already validated index for you above.
     */
    @Override
    public T remove(int index) {
        checkIndex(index);
        // TODO: implement removal at an arbitrary position
        Node<T> removed;
            if (index ==0) {
                removed = head
                head = head.next;
            } else {
                Node<T> previous = node(index -1);
                removed = previous.next;
                previous.next = removed.next;
            }
            size--;
            return removed.data;
        }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * TODO 3: Reverse the list in place, using only the existing nodes
     * -- no new nodes, and no other data structure such as a stack or a
     * second list. After this call, head must point at what used to be
     * the last element, and the last element must point at null.
     *
     * Example: [1, 2, 3, 4]  ->  [4, 3, 2, 1]
     *
     * Hint: walk the list once, keeping track of three references as
     * you go -- the previous node, the current node, and the next node
     * -- and relink "next" pointers as you pass each node.
     */
    public void reverseInPlace() {
        // TODO: implement in-place reversal
        Node<T> previous = null;
        Note<T> current = head;

        while (current != null) {
            Node<T> next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        head = previous;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            result.append(current.data);
            current = current.next;
            if (current != null) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    // ----- private helpers --------------------------------------------

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    // Returns the node currently at position index, reached by walking
    // from head. Useful inside add(int, T) and remove(int).
    private Node<T> node(int index) {
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }
}
