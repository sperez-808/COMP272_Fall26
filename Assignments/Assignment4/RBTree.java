/**
 * COMP 272 Assignment 4 — Exercise 2.
 *
 * A red-black tree of distinct ints that extends BinarySearchTree.
 * The algorithms follow the Module 4 Part 2 slides (CLRS-style, null leaves
 * count as black).
 *
 * PROVIDED: node colors, color helpers, rotations, delete() and its fix-up.
 * YOUR TASK: implement add() (BST insertion followed by the insertion fix-up).
 * Do not change any provided method, name or signature. You may add private helpers.
 *
 * Note: the red-black algorithms do not use the "height" field inherited from
 * BinarySearchTree, so RBTree does not keep it up to date. Use height() (which
 * recomputes the height of the whole tree) if you need the tree's height.
 */
public class RBTree extends BinarySearchTree {

    protected static final boolean RED = true;
    protected static final boolean BLACK = false;

    /** A BST node with a color. New nodes are red. */
    protected static class RBNode extends Node {
        protected boolean color = RED;

        protected RBNode(int value, Node parent) {
            super(value, parent);
        }
    }

    /** Every node created by BinarySearchTree.add is an RBNode. */
    @Override
    protected Node newNode(int value, Node parent) {
        return new RBNode(value, parent);
    }

    // ------------------------------------------------------------------
    // Color helpers — null (external) nodes are black.
    // ------------------------------------------------------------------

    protected boolean colorOf(Node node) {
        return node == null ? BLACK : ((RBNode) node).color;
    }

    protected boolean isRed(Node node) {
        return colorOf(node) == RED;
    }

    /** Set the color of node; does nothing when node is null. */
    protected void setColor(Node node, boolean color) {
        if (node != null) {
            ((RBNode) node).color = color;
        }
    }

    @Override
    protected String label(Node node) {
        return node.value + (isRed(node) ? "(R)" : "(B)");
    }

    // ------------------------------------------------------------------
    // Exercise 2 — insertion
    // ------------------------------------------------------------------

    /**
     * Insert value. Return the newly inserted node, or null for a duplicate
     * (the tree is then unchanged).
     *
     * Step 1: ordinary BST insertion (the new node is red).
     * Step 2: fix any red-red violation (Cases 1, 2, 3 and their mirrors).
     * Step 3: color the root black.
     */
    @Override
    public Node add(int value) {
        // inerted = Bst-NSERT(value)
        Node inserted = super.add(value);
        // if inserted = NIL then return NIL
        if (inserted == null) {
            return null;
        }
        Node z = inserted;
        // while z isn't equal to root and color parent z it'll equal red
        while (z != root && isRed(z.parent)) {
            Node p = z.parent;
            Node g = p.parent;
            if (p == g.left) {
                Node u = g.right;
                // if color is red, Case 1: red uncle
                if (isRed(u)) {
                    setColor(p, BLACK);
                    setColor(u, BLACK);
                    setColor(g, RED);
                z = g;
                } else {
                    // if z is right p, Case 2: triangle - Left Rotate
                    if (z == p.right) {
                        z = p;
                        rotateLeft(z);
                        // p is parent and g is grandparent
                        p = z.parent;
                        g = p.parent;
                }
                // Case 3: line - Right Rotate
                setColor(p, BLACK);
                setColor(g, RED);
                rotateRight(g);
                // terminate fix-up
                z = root;
            }
            } else {
                // Mirror case: p is right grandparent
                // u is left grandparent
                Node u = g.left;
                if (isRed(u)) {
                    // Case 1: red uncle
                    setColor(p, BLACK);
                    setColor(u, BLACK);
                    setColor(g, RED);
                    z = g;
            } else {
                // if z = left parent
                if (z == p.left) {
                    // Case 2: triangle - rotate right
                    z = p;
                    rotateRight(z);
                    p = z.parent;
                    g = p.parent;
                }
            // Case 3: line - rotate left
                setColor(p, BLACK);
                setColor(g, RED);
                rotateLeft(g);
                z = root;
            }
        }
    }
    // set the root color to black and return inserted
    setColor(root, BLACK);
    return inserted;
}

    /*
     * ======================== Exercise 2, Part B — Analysis ========================
     * 1.
     * One insertion performs at most 2 rotations, but may perform O(log n) recolorings. In Case 1, the uncle is
     * red, so we recolor the parent, uncle, and grandparent, then continue with z = grandparent. This case performs
     * no rotations, it can repeat at each level, so there may be O(log n) recolorings. If the uncle is black, then 
     * Case 2 or 3 apply instead. Case 2 does one rotation to turn the triangle into a line, and Case 3 does one
     * rotation. After Case 3, the red-red violation is fixed and the loop stops, so one insertion performs at most 2
     * rotations.
     * 2. 
     * Worst case running time of add is O(log n), letting n be the number of nodes, ordinary BST insertion takes O(h),
     * where h is the height of the tree. For a red-black tree, h <= 2 log2(n + 1), so insertion is O(log n), and the
     * fix-up loop moves upward one level per iteration. Each iteration does O(1) color changes and at most one rotation.
     * The loop can run O(h) = O(log n) times because Case 1 may repeat upward. The rotations are O(1), so the total worst
     * case running time is O(log n).
     * ===============================================================================
     */


    // ------------------------------------------------------------------
    // PROVIDED — deletion (not covered in detail in class; read it, do not change it)
    // ------------------------------------------------------------------

    /**
     * Remove value if present. Return true if a node was removed.
     *
     * Step 1: BST deletion. A node with two children receives its successor's
     *         value, and the successor (at most one child) is physically removed.
     * Step 2: if the removed node was black, the child x that took its place
     *         carries an "extra black"; deleteFixup removes it.
     */
    @Override
    public boolean delete(int value) {
        Node target = findNode(value);
        if (target == null) {
            return false;
        }

        if (target.left != null && target.right != null) {
            Node successor = minimum(target.right);
            target.value = successor.value;
            target = successor;
        }

        Node x = (target.left != null) ? target.left : target.right;
        Node xParent = target.parent;
        boolean removedColor = colorOf(target);

        transplant(target, x);
        size--;

        if (removedColor == BLACK) {
            deleteFixup(x, xParent);
        }
        return true;
    }

    /**
     * x may be null (a black external node), so its parent is passed separately.
     */
    private void deleteFixup(Node x, Node parent) {
        while (x != root && !isRed(x)) {
            if (x == parent.left) {
                Node w = parent.right;
                if (isRed(w)) {                                   // Case 1
                    setColor(w, BLACK);
                    setColor(parent, RED);
                    rotateLeft(parent);
                    w = parent.right;
                }
                if (!isRed(w.left) && !isRed(w.right)) {          // Case 2
                    setColor(w, RED);
                    x = parent;
                    parent = x.parent;
                } else {
                    if (!isRed(w.right)) {                        // Case 3
                        setColor(w.left, BLACK);
                        setColor(w, RED);
                        rotateRight(w);
                        w = parent.right;
                    }
                    setColor(w, colorOf(parent));                 // Case 4
                    setColor(parent, BLACK);
                    setColor(w.right, BLACK);
                    rotateLeft(parent);
                    x = root;
                    parent = null;
                }
            } else {
                Node w = parent.left;
                if (isRed(w)) {                                   // Case 1 (mirror)
                    setColor(w, BLACK);
                    setColor(parent, RED);
                    rotateRight(parent);
                    w = parent.left;
                }
                if (!isRed(w.left) && !isRed(w.right)) {          // Case 2 (mirror)
                    setColor(w, RED);
                    x = parent;
                    parent = x.parent;
                } else {
                    if (!isRed(w.left)) {                         // Case 3 (mirror)
                        setColor(w.right, BLACK);
                        setColor(w, RED);
                        rotateLeft(w);
                        w = parent.left;
                    }
                    setColor(w, colorOf(parent));                 // Case 4 (mirror)
                    setColor(parent, BLACK);
                    setColor(w.left, BLACK);
                    rotateRight(parent);
                    x = root;
                    parent = null;
                }
            }
        }
        setColor(x, BLACK);
    }

    // ------------------------------------------------------------------
    // PROVIDED — rotations (same pointer changes as in AVLTree, no heights)
    // ------------------------------------------------------------------

    /** Rotate the subtree rooted at oldRoot to the left and return its new root. */
    protected Node rotateLeft(Node oldRoot) {
        Node newRoot = oldRoot.right;
        Node middle = newRoot.left;

        transplant(oldRoot, newRoot);   // newRoot takes oldRoot's place (fixes root too)

        newRoot.left = oldRoot;
        oldRoot.parent = newRoot;

        oldRoot.right = middle;
        if (middle != null) {
            middle.parent = oldRoot;
        }
        return newRoot;
    }

    /** Rotate the subtree rooted at oldRoot to the right and return its new root. */
    protected Node rotateRight(Node oldRoot) {
        Node newRoot = oldRoot.left;
        Node middle = newRoot.right;

        transplant(oldRoot, newRoot);

        newRoot.right = oldRoot;
        oldRoot.parent = newRoot;

        oldRoot.left = middle;
        if (middle != null) {
            middle.parent = oldRoot;
        }
        return newRoot;
    }
}
