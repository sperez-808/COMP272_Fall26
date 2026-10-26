import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * PROVIDED — automated checks for Assignment 4. Do not modify or submit.
 *
 *     javac *.java
 *     java Main            (all exercises)
 *     java Main avl        (Exercise 1 only; also: rb, locker)
 *
 * Trees are shown by their PREORDER listing. For red-black trees each value is
 * followed by its color: "20B 10R" means 20 is black and 10 is red.
 * (TreeSet is used here only as a reference answer.)
 */
public class Main {

    static int passed = 0, failed = 0;

    public static void main(String[] args) {
        String only = args.length > 0 ? args[0].toLowerCase() : "all";
        if (only.equals("all") || only.equals("avl"))    run("Exercise 1", Main::avlTests);
        if (only.equals("all") || only.equals("rb"))     run("Exercise 2", Main::rbTests);
        if (only.equals("all") || only.equals("locker")) run("Exercise 3", Main::lockerTests);
    }

    /** Runs one exercise's tests and prints its summary line. */
    static void run(String exercise, Runnable tests) {
        int p0 = passed, f0 = failed;
        tests.run();
        int p = passed - p0, f = failed - f0;
        System.out.println(exercise + " public tests: " + p + " passed, " + f + " failed (of " + (p + f) + ")");
    }

    // =====================================================================
    // Exercise 1 — AVLTree.delete
    // =====================================================================

    /*
     * The removal tree from the slides:
     *                40
     *            /        \
     *          20          60
     *         /  \       /    \
     *       10    30   50      70
     *         \        /      /  \
     *          15    45     65    80
     *                               \
     *                                90
     */
    static AVLTree slidesAVL() {
        return avl(40, 20, 60, 10, 30, 50, 70, 15, 45, 65, 80, 90);
    }

    static void avlTests() {
        System.out.println("\nExercise 1 - AVLTree.delete");

        test("delete a missing value returns false and changes nothing", () -> {
            AVLTree t = slidesAVL();
            eq(false, t.delete(42));
            eq("40 20 10 15 30 60 50 45 70 65 80 90", preorder(t));
            eq(false, new AVLTree().delete(1));
        });

        test("delete the only node empties the tree", () -> {
            AVLTree t = avl(7);
            eq(true, t.delete(7));
            eq(null, t.getRoot());
            eq(0, t.size());
        });

        test("delete a leaf that needs no rotation", () -> {
            AVLTree t = slidesAVL();
            eq(true, t.delete(90));
            eq("40 20 10 15 30 60 50 45 70 65 80", preorder(t));
            assertValidAVL(t);
        });

        test("slides example 1: delete(45) -> RR rotation at 60", () -> {
            AVLTree t = slidesAVL();
            t.delete(45);
            eq("40 20 10 15 30 70 60 50 65 80 90", preorder(t));
            assertValidAVL(t);
        });

        test("slides example 2: delete(30) -> LR at 20, then RR at the root", () -> {
            AVLTree t = slidesAVL();
            t.delete(30);
            eq("60 40 15 10 20 50 45 70 65 80 90", preorder(t));
            assertValidAVL(t);
        });

        test("delete a node with two children (inorder successor)", () -> {
            AVLTree t = slidesAVL();
            eq(true, t.delete(60));
            eq(65, at(t, "R"));
            assertValidAVL(t);
        });

        test("random inserts and deletes keep a valid AVL tree", () -> {
            Random rnd = new Random(272);
            AVLTree t = new AVLTree();
            TreeSet<Integer> ref = new TreeSet<>();
            for (int i = 0; i < 300; i++) { int v = rnd.nextInt(1000); t.add(v); ref.add(v); }
            for (int i = 0; i < 300; i++) {
                int v = rnd.nextInt(1000);
                eq(ref.remove(v), t.delete(v));
                assertValidAVL(t);
            }
            eq(new ArrayList<>(ref), t.inorder());
        });
    }

    // =====================================================================
    // Exercise 2 — RBTree.add
    // =====================================================================

    /*
     * The insertion tree from the slides:
     *                 20(B)
     *               /       \
     *           10(R)        30(B)
     *          /     \       /
     *       5(B)    15(B)  25(R)
     *      /    \
     *    3(R)   7(R)
     */
    static final int[] SLIDES_RB = {20, 10, 30, 5, 15, 25, 3, 7};

    static void rbTests() {
        System.out.println("\nExercise 2 - RBTree.add");

        test("first value becomes a BLACK root; duplicates return null", () -> {
            RBTree t = rb(42);
            eq("42B", preorderColors(t));
            eq(null, t.add(42));
            eq(1, t.size());
        });

        test("Case 1 (red uncle): recolor only", () -> {
            eq("20B 10B 5R 30B", preorderColors(rb(20, 10, 30, 5)));
        });

        test("Case 3 (line): 1, 2, 3", () -> {
            eq("2B 1R 3R", preorderColors(rb(1, 2, 3)));
        });

        test("Case 2 then Case 3 (triangle): 30, 10, 20", () -> {
            eq("20B 10R 30R", preorderColors(rb(30, 10, 20)));
        });

        test("at most 2 rotations: triangle uses exactly 2", () -> {
            CountingRBTree t = new CountingRBTree();
            t.add(30); t.add(10);
            t.rotations = 0;
            t.add(20);
            eq(2, t.rotations);
        });

        test("the slides' tree is built with the expected colors", () -> {
            eq("20B 10R 5B 3R 7R 15B 30B 25R", preorderColors(rb(SLIDES_RB)));
        });

        test("slides example 1: insert(1)", () -> {
            RBTree t = rb(SLIDES_RB);
            t.add(1);
            eq("10B 5R 3B 1R 7B 20R 15B 30B 25R", preorderColors(t));
        });

        test("slides example 2: insert(27)", () -> {
            RBTree t = rb(SLIDES_RB);
            t.add(27);
            eq("20B 10R 5B 3R 7R 15B 27B 25R 30R", preorderColors(t));
        });

        test("random inserts (and the provided delete) keep a valid red-black tree", () -> {
            Random rnd = new Random(272);
            RBTree t = new RBTree();
            TreeSet<Integer> ref = new TreeSet<>();
            for (int i = 0; i < 1000; i++) {
                int v = rnd.nextInt(2000);
                if (i % 3 == 2) eq(ref.remove(v), t.delete(v));
                else eq(ref.add(v), t.add(v) != null);
                assertValidRB(t);
            }
            eq(new ArrayList<>(ref), t.inorder());
        });
    }

    // =====================================================================
    // Exercise 3 — LockerAssigner
    // =====================================================================

    static void lockerTests() {
        System.out.println("\nExercise 3 - LockerAssigner");

        test("assign gives the preferred locker when it is free", () -> {
            eq(4, new LockerAssigner(10).assign(4));
        });

        test("assign gives the closest free locker, ties to the smaller number", () -> {
            LockerAssigner l = new LockerAssigner(10);
            eq(5, l.assign(5));
            eq(4, l.assign(5));      // 4 and 6 are both at distance 1
            eq(6, l.assign(5));
            eq(10, l.assign(42));    // outside the range: closest is 10
            eq(1, l.assign(-7));
        });

        test("assign returns -1 when every locker is taken", () -> {
            LockerAssigner l = new LockerAssigner(2);
            l.assign(1);
            l.assign(1);
            eq(-1, l.assign(1));
        });

        test("release frees a taken locker only", () -> {
            LockerAssigner l = new LockerAssigner(5);
            eq(3, l.assign(3));
            eq(true, l.release(3));
            eq(false, l.release(3));     // already free
            eq(false, l.release(0));     // not a locker
            eq(false, l.release(6));
            eq(3, l.assign(3));
        });
    }

    // =====================================================================
    // Helpers (you may use them in your own tests)
    // =====================================================================

    @FunctionalInterface
    interface Test { void run() throws Exception; }

    static void test(String name, Test t) {
        try {
            t.run();
            passed++;
            System.out.println("  PASS  " + name);
        } catch (UnsupportedOperationException e) {
            failed++;
            System.out.println("  FAIL  " + name + "  (not implemented: " + e.getMessage() + ")");
        } catch (AssertionError e) {
            failed++;
            System.out.println("  FAIL  " + name + "  -> " + e.getMessage());
        } catch (Throwable e) {
            failed++;
            System.out.println("  FAIL  " + name + "  -> unexpected " + e);
        }
    }

    static void check(boolean cond, String msg) {
        if (!cond) throw new AssertionError(msg);
    }

    static void eq(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual))
            throw new AssertionError("expected <" + expected + "> but got <" + actual + ">");
    }

    static AVLTree avl(int... values) {
        AVLTree t = new AVLTree();
        for (int v : values) t.add(v);
        return t;
    }

    static RBTree rb(int... values) {
        RBTree t = new RBTree();
        for (int v : values) t.add(v);
        return t;
    }

    /** Value reached by following a path like "LR" from the root (null if none). */
    static Integer at(BinarySearchTree t, String path) {
        BinarySearchTree.Node u = t.getRoot();
        for (char c : path.toCharArray()) {
            if (u == null) return null;
            u = (c == 'L') ? u.left : u.right;
        }
        return u == null ? null : u.value;
    }

    static String preorder(BinarySearchTree t) {
        StringBuilder sb = new StringBuilder();
        pre(t.getRoot(), sb, null);
        return sb.toString().trim();
    }

    static String preorderColors(RBTree t) {
        StringBuilder sb = new StringBuilder();
        pre(t.getRoot(), sb, t);
        return sb.toString().trim();
    }

    private static void pre(BinarySearchTree.Node u, StringBuilder sb, RBTree colors) {
        if (u == null) return;
        sb.append(u.value);
        if (colors != null) sb.append(colors.isRed(u) ? "R" : "B");
        sb.append(' ');
        pre(u.left, sb, colors);
        pre(u.right, sb, colors);
    }

    /** Checks BST order, parent pointers and size(). Returns null if valid. */
    static String bstProblem(BinarySearchTree t) {
        BinarySearchTree.Node r = t.getRoot();
        if (r != null && r.parent != null) return "root.parent should be null";
        List<String> problems = new ArrayList<>();
        int count = walk(r, Long.MIN_VALUE, Long.MAX_VALUE, problems);
        if (!problems.isEmpty()) return problems.get(0);
        if (count != t.size()) return "size() is " + t.size() + " but the tree has " + count + " nodes";
        return null;
    }

    private static int walk(BinarySearchTree.Node u, long lo, long hi, List<String> problems) {
        if (u == null || !problems.isEmpty()) return 0;
        if (u.value <= lo || u.value >= hi) problems.add("BST order broken at node " + u.value);
        if (u.left != null && u.left.parent != u) problems.add("wrong parent pointer in left child of " + u.value);
        if (u.right != null && u.right.parent != u) problems.add("wrong parent pointer in right child of " + u.value);
        return 1 + walk(u.left, lo, u.value, problems) + walk(u.right, u.value, hi, problems);
    }

    /** BST checks + correct stored heights + |balance| <= 1 everywhere. */
    static void assertValidAVL(AVLTree t) {
        String p = bstProblem(t);
        List<String> problems = new ArrayList<>();
        if (p == null) avlWalk(t.getRoot(), problems);
        if (p == null && !problems.isEmpty()) p = problems.get(0);
        if (p != null) throw new AssertionError("not a valid AVL tree: " + p);
    }

    private static int avlWalk(BinarySearchTree.Node u, List<String> problems) {
        if (u == null) return -1;
        int hl = avlWalk(u.left, problems), hr = avlWalk(u.right, problems);
        int h = 1 + Math.max(hl, hr);
        if (problems.isEmpty() && u.height != h)
            problems.add("stored height of " + u.value + " is " + u.height + " but should be " + h);
        if (problems.isEmpty() && Math.abs(hl - hr) > 1)
            problems.add("node " + u.value + " is unbalanced (balance " + (hl - hr) + ")");
        return h;
    }

    /** BST checks + the red-black properties. */
    static void assertValidRB(RBTree t) {
        String p = bstProblem(t);
        if (p == null && t.isRed(t.getRoot())) p = "the root must be black";
        List<String> problems = new ArrayList<>();
        if (p == null) rbWalk(t, t.getRoot(), problems);
        if (p == null && !problems.isEmpty()) p = problems.get(0);
        if (p != null) throw new AssertionError("not a valid red-black tree: " + p);
    }

    private static int rbWalk(RBTree t, BinarySearchTree.Node u, List<String> problems) {
        if (u == null) return 1;
        if (t.isRed(u) && (t.isRed(u.left) || t.isRed(u.right)) && problems.isEmpty())
            problems.add("red node " + u.value + " has a red child");
        int bl = rbWalk(t, u.left, problems), br = rbWalk(t, u.right, problems);
        if (bl != br && problems.isEmpty())
            problems.add("different black counts below node " + u.value);
        return bl + (t.isRed(u) ? 0 : 1);
    }

    /** An RBTree that counts calls to the provided rotations. */
    static class CountingRBTree extends RBTree {
        int rotations = 0;

        @Override
        protected Node rotateLeft(Node oldRoot) { rotations++; return super.rotateLeft(oldRoot); }

        @Override
        protected Node rotateRight(Node oldRoot) { rotations++; return super.rotateRight(oldRoot); }
    }
}
