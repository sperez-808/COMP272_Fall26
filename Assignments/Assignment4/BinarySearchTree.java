import java.util.ArrayList;
import java.util.List;

/**
 * COMP 272 Assignment 4. 
 * Do NOT modify and do NOT submit.
 *
 * A complete Binary Search Tree of distinct ints.
 * Every node stores a parent pointer and its height.
 *
 * AVLTree and RBTree both extend this class. They reuse:
 *   - add(value)            ordinary BST insertion (returns the new node)
 *   - findNode(value)       the node containing value, or null
 *   - minimum(node)         the leftmost node of a subtree
 *   - transplant(u, v)      put subtree v in u's place under u's parent
 *   - newNode(value, p)     factory used by add (RBTree overrides it)
 *
 * READ CAREFULLY BEFORE IMPLEMENTING EXERCISES 1 AND 2.
 *
 * This class provides helper methods for the classes that extend it
 * (AVLTree and RBTree). Using them properly will make it easier to
 * implement the missing methods. 
 */
public class BinarySearchTree {

    protected Node root;
    protected int size;

    public static class Node {
        protected int value;
        protected Node left;
        protected Node right;
        protected Node parent;

        // Empty subtree: height -1. Leaf: height 0.
        protected int height = 0;

        protected Node(int value, Node parent) {
            this.value = value;
            this.parent = parent;
        }

        public int getValue() { return value; }
        public Node getLeft() { return left; }
        public Node getRight() { return right; }
        public Node getParent() { return parent; }
    }

    // ------------------------------------------------------------------
    // Public operations
    // ------------------------------------------------------------------

    /**
     * Insert value using ordinary BST insertion.
     *
     * Return the newly inserted node, or null for a duplicate.
     * Set its parent pointer and update root when necessary.
     * Stored heights on the path back to the root are updated.
     */
    public Node add(int value) {
        Node parent = null;
        Node current = root;

        while (current != null) {
            parent = current;
            if (value < current.value) {
                current = current.left;
            } else if (value > current.value) {
                current = current.right;
            } else {
                return null;                       // duplicate: tree unchanged
            }
        }

        Node inserted = newNode(value, parent);

        if (parent == null) {
            root = inserted;
        } else if (value < parent.value) {
            parent.left = inserted;
        } else {
            parent.right = inserted;
        }

        size++;
        updateHeightsUpward(parent);
        return inserted;
    }

    /**
     * Remove value if present.
     * Return true if a node was removed, otherwise false.
     * Maintain parent pointers and root when necessary.
     */
    public boolean delete(int value) {
        Node target = findNode(value);
        if (target == null) {
            return false;
        }

        // Two children: copy the successor's value, then remove the successor.
        if (target.left != null && target.right != null) {
            Node successor = minimum(target.right);
            target.value = successor.value;
            target = successor;
        }

        // Now target has at most one child.
        Node child = (target.left != null) ? target.left : target.right;
        Node parentOfRemoved = target.parent;

        transplant(target, child);
        size--;

        updateHeightsUpward(parentOfRemoved);
        return true;
    }

    /**
     * Return whether value occurs in the tree.
     */
    public boolean search(int value) {
        return findNode(value) != null;
    }

    public Node getRoot() {
        return root;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Height of the whole tree (-1 when empty). Computed by a full traversal,
     * so it is correct for every subclass, whatever it stores in the nodes.
     */
    public int height() {
        return computeHeight(root);
    }

    /** The values in sorted (inorder) order. */
    public List<Integer> inorder() {
        List<Integer> out = new ArrayList<>();
        inorder(root, out);
        return out;
    }

    /** Print the tree, one node per line (same format as Assignment 3). */
    public void printTree() {
        if (root == null) {
            System.out.println("(empty)");
            return;
        }
        System.out.println(label(root));
        printChildren(root, "");
    }

    // ------------------------------------------------------------------
    // Protected helpers for subclasses
    // ------------------------------------------------------------------

    /** Return the node containing value, or null. O(h). */
    protected Node findNode(int value) {
        Node current = root;
        while (current != null) {
            if (value < current.value) {
                current = current.left;
            } else if (value > current.value) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    /** Leftmost (smallest) node of the subtree rooted at node (node != null). */
    protected Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Replace the subtree rooted at oldNode by the subtree rooted at newNode
     * (newNode may be null). Fixes the parent's link, newNode.parent, and root.
     * Does not change oldNode's own links.
     */
    protected void transplant(Node oldNode, Node newNode) {
        Node parent = oldNode.parent;

        if (parent == null) {
            root = newNode;
        } else if (parent.left == oldNode) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        if (newNode != null) {
            newNode.parent = parent;
        }
    }

    /**
     * Factory used by add() to create every new node.
     * Subclasses that need extra fields in their nodes override this.
     */
    protected Node newNode(int value, Node parent) {
        return new Node(value, parent);
    }

    /** Recompute stored heights from node up to the root. O(h). */
    protected void updateHeightsUpward(Node node) {
        while (node != null) {
            updateHeight(node);
            node = node.parent;
        }
    }

    /**
     * Read the stored height in O(1).
     * This method does not recursively traverse the subtree.
     */
    protected int height(Node node) {
        return node == null ? -1 : node.height;
    }

    /**
     * Update one node's height in O(1).
     * Its children's stored heights must already be correct.
     */
    protected void updateHeight(Node node) {
        node.height = 1 + Math.max(
            height(node.left),
            height(node.right)
        );
    }

    /** How a node is shown by printTree(). RBTree overrides it to add the color. */
    protected String label(Node node) {
        return String.valueOf(node.value);
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    private static int computeHeight(Node node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(computeHeight(node.left), computeHeight(node.right));
    }

    private static void inorder(Node node, List<Integer> out) {
        if (node == null) {
            return;
        }
        inorder(node.left, out);
        out.add(node.value);
        inorder(node.right, out);
    }

    private void printChildren(Node node, String prefix) {
        if (node.left != null) {
            printChild(node.left, prefix, "L", node.right == null);
        }
        if (node.right != null) {
            printChild(node.right, prefix, "R", true);
        }
    }

    private void printChild(Node child, String prefix, String side, boolean last) {
        System.out.println(prefix + (last ? "└── " : "├── ") + side + ": " + label(child));
        printChildren(child, prefix + (last ? "    " : "│   "));
    }
}
