/**
 * COMP 272 Assignment 4 — Exercise 1.
 *
 * This is the AVLTree class from Module 4, unchanged except for delete().
 * Implement delete(). Do not change any other method, name or signature.
 * You may add private helper methods.
 */
public class AVLTree extends BinarySearchTree {

    @Override
    public Node add(int value) {
        // First perform ordinary BST insertion.
        Node inserted = super.add(value);

        if (inserted != null) {
            // The new leaf already has height 0.
            rebalanceUpward(inserted.parent);
        }

        return inserted;
    }

    /**
     * Positive: left subtree is taller.
     * Negative: right subtree is taller.
     */
    private int balanceFactor(Node node) {
        return height(node.left) - height(node.right);
    }

    /**
     * Update heights and restore balance from bottom to top.
     */
    private void rebalanceUpward(Node node) {
        while (node != null) {
            updateHeight(node);

            Node subtreeRoot = node;
            int balance = balanceFactor(node);

            if (balance > 1) {
                // Left-right case.
                if (balanceFactor(node.left) < 0) {
                    rotateLeft(node.left);
                }

                // Left-left case, or second left-right rotation.
                subtreeRoot = rotateRight(node);

            } else if (balance < -1) {
                // Right-left case.
                if (balanceFactor(node.right) > 0) {
                    rotateRight(node.right);
                }

                // Right-right case, or second right-left rotation.
                subtreeRoot = rotateLeft(node);
            }

            node = subtreeRoot.parent;
        }
    }

    /**
     * Rotate a subtree left and return its new root.
     */
    private Node rotateLeft(Node oldRoot) {
        Node newRoot = oldRoot.right;
        Node middle = newRoot.left;

        replaceAtParent(oldRoot, newRoot);

        newRoot.left = oldRoot;
        oldRoot.parent = newRoot;

        oldRoot.right = middle;
        if (middle != null) {
            middle.parent = oldRoot;
        }

        // Order matters: update the lower node first.
        updateHeight(oldRoot);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Rotate a subtree right and return its new root.
     */
    private Node rotateRight(Node oldRoot) {
        Node newRoot = oldRoot.left;
        Node middle = newRoot.right;

        replaceAtParent(oldRoot, newRoot);

        newRoot.right = oldRoot;
        oldRoot.parent = newRoot;

        oldRoot.left = middle;
        if (middle != null) {
            middle.parent = oldRoot;
        }

        // Order matters: update the lower node first.
        updateHeight(oldRoot);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Connect the new subtree root to the original parent.
     */
    private void replaceAtParent(Node oldRoot, Node newRoot) {
        Node parent = oldRoot.parent;
        newRoot.parent = parent;

        if (parent == null) {
            root = newRoot;
        } else if (parent.left == oldRoot) {
            parent.left = newRoot;
        } else {
            parent.right = newRoot;
        }
    }

    @Override
    public boolean delete(int value) {
        // TODO (Exercise 1): AVL deletion also requires rebalancing.
        // Remove value as in a BST, then restore the AVL balance condition
        // on every node from the right starting point up to the root.
        Node target = findNode(value);
        if (target == null) {
            return false;
        }
        // If the target has two children, copy the inorder successor's value into target and physically remove the successor instead
        if (target.left != null && target.right != null) {
            Node successor = minimum(target.right);
            target.value = successor.value;
            target = successor;
        }
        // The physically removed node has at most one child
        Node child = (target.left != null) ? target.left : target.right;
        Node start = target.parent;
        transplant(target, child);
        size--;
        // Rebalance from the parent of the physically removed node upward.
        rebalanceUpward(start);
        return true;
    }
}

    /*
     * ======================== Exercise 1, Part B — Analysis ========================
     * 1.
     * Worst case running time of deletion is O(log n),letting n be the number of nodes, finding the node with
     * findNode is O(h), where h is the AVL height. An AVL tree has h = O(log n), so this is O(log n).
     * Finding the successor with minimum(target.right) is also O(h) = O(log n), and the actual removal is O(1)
     * so the transplant only changes a constant numberically removed node up to the root. There are O(h) = O(log n)
     * ancestors, and each ancestor costs O(1) to update its height and check its balance factor. A single or double
     * rotation costs O(1), so the total worst case running time is O(log n).
     * 2. 
     * One deletion can perform O(log n) rotations in the worst case, unlike insertion, deletion can shorten a subtree.
     * After a rotation, the rebalanced subtree may still be one level shorter than before the deletion, so an ancestor
     * can become unbalanced too, so a rotation may be needed at many lev els along the path to the root. Since the AVL
     * tree height O(log n), one deletion can cause O(log n) rotations/ reconstructures. One insertion never needs more
     * than one restructure, and the first rotation - single or double - at the lowest unbalanced ancestor restores that
     * that subtree to the height it had before the insertion. Since that subtree's height is restored, the balance factors
     * of all its ancestors stay unchanged, so no further rotations are needed above it.
     * ===============================================================================
     */
