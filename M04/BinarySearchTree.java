public class BinarySearchTree {

    protected Node root;

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
    }

    /**
     * Insert value using ordinary BST insertion.
     *
     * Return the newly inserted node, or null for a duplicate.
     * Set its parent pointer and update root when necessary.
     */
    public Node add(int value) {
        // If no root on tree, this value becomes the root
        if (root == null) {
        // The parent of the root is null
            root = new Node(value, null);
        // Return the new root node
            return root;
        }
        // Prep to searh for correct insertion point
        // Start at root, track the parent to link new node
        Node current = root;
        Node parent = null;
        // Walk down tree to find new area - until fall off tree
        while (current != null) {
            parent = current;
        // If new value is smaller, go left
            if (value < current.value) {
                current = current.left;
            }
        // If new value is larger, go right
            else if (value > current.value) {
                current = current.right;
            }
        // If value already exists, reject duplicate, return null
            else {
                return null;
            }
        }
        // Create new node - its parent is the last visited node
        Node inserted = new Node(value, parent);
        // Attach new node to its parent on correct side
            // Smaller than parent, left, larger, right
        if (value < parent.value) {
            parent.left = inserted;
        }
        else {
            parent.right = inserted;
        }
        return inserted;
            }
    /**
     * Remove value if present.
     * Return true if a node was removed, otherwise false.
     * Maintain parent pointers and root when necessary.
     */
    public boolean delete(int value) {
        // Locate node with value to remove
        Node node = findNode(value);
        // If no node found, false - nothing to delete
        if (node == null) {
            return false;
        }
        // If node found, remove from tree
        // refer to helper method
        deleteNode(node);
        // When successful, true
        return true;
    }

    /**
     * Return whether value occurs in the tree.
     */
    public boolean search(int value) {
        // refer to helper method
        return findNode(value) != null;
    }
// Made some helper methods
    protected Node findNode(int value) {
         // Search root of tree, walk down until find value or null
        Node current = root;
        while (current != null) {
            // smaller, left
            if (value < current.value) {
                current = current.left;
            }
            // larger, right
            else if (value > current.value) {
                current = current.right;
            }
            // matches, found - return node
            else {
                return current;
            }
        }
        return null;
            }
    protected void deleteNode(Node node) {
    // Case 1: node has 2 children
        // Copy in-order successor's value into node, then delete successor instead
        if (node. left != null && node.right != null) {
            // refer to helper method
            Node successor = minimum(node.right);
            // Overwrite node value with successor value
            node.value = successor.value;
            // Make node successor
            node = successor;
        }
    // Case 2/3: node has 1 or no child
        // Pick existing child, or none if null
        Node child = (node.left != null) ? node.left : node.right;
        Node parent = node.parent;
        // If there is a child, reattack to grandparent - so it doesn't get orphaned
        if (child != null) {
            child.parent = parent;
        }
        // Have parent point at child instead of node
            // If parent is null and node is the root, child becomes new root
        if (parent == null) {
            root = child;
        }
            // If node was left child, replace it with child on the left
        else if (parent.left == node) {
            parent.left = child;
        }
            // Otherwise, node was right, so replace right
        else {
            parent.right = child;
        }
    // Clean up removed pointers
        node.parent = null;
        node.left = null;
        node.right = null;
    }
    protected Node minimum(Node node) {
        // Smallest value is on the very left, so keep walking left until no child
        while (node.left != null) {
            node = node.left;
        }
        // Reached leftmost node, return the minimum
        return node;
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
}
