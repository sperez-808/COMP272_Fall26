public class BinaryTree<T> {
    public static class Node<T> {
        private T element;
        private Node<T> left;
        private Node<T> right;

        public Node(T element) {
            this.element = element;
        }

        public T getElement() { return element; }
        public Node<T> getLeft() { return left; }
        public Node<T> getRight() { return right; }
        public void setLeft(Node<T> left) { this.left = left; }
        public void setRight(Node<T> right) { this.right = right; }
    }

    private Node<T> root;

    public BinaryTree(Node<T> root) {
        this.root = root;
    }

    public Node<T> getRoot() { return root; }

    // Height is measured in edges: a leaf has height 0.
    // An empty tree has height -1.
    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return -1;
        int leftHeight = height(node.getLeft());
        int rightHeight = height(node.getRight());
        return 1 + Math.max(leftHeight, rightHeight);
    }

    // Display the structure of any binary tree.
    // This is provided code; students can focus on the traversal idea.
    public void printTree() {
        if (root == null) {
            System.out.println("(empty tree)");
            return;
        }
        System.out.println(root.getElement());
        printChildren(root, "");
    }

    private void printChildren(Node<T> node, String prefix) {
        Node<T> left = node.getLeft();
        Node<T> right = node.getRight();

        if (left != null) {
            boolean last = right == null;
            System.out.println(prefix + (last ? "└── " : "├── ")
                    + "L: " + left.getElement());
            printChildren(left, prefix + (last ? "    " : "│   "));
        }
        if (right != null) {
            System.out.println(prefix + "└── R: " + right.getElement());
            printChildren(right, prefix + "    ");
        }
    }

}
