# COMP 272/400C — Balanced Trees Assignment

## AVL Trees, Red-Black Trees, and Java's `TreeSet` / `TreeMap`

### Submission

Submit the following three files:

1. `AVLTree.java` — the class from Module 4 with `delete` completed for Exercise 1.
2. `RBTree.java` — the provided red-black tree with `add` completed for Exercise 2.
3. `LockerAssigner.java` — your nearest-free-locker class for Exercise 3.

Your written answers go in **comments inside your Java files**, in the spaces marked `Exercise N, Part B — Analysis`. Your explanations must identify the input size and justify every complexity claim. A bound without an explanation is not a complete answer.

Your code will be graded with the provided public tests **and with additional hidden tests**, including tests on large inputs with a time limit. Passing all public tests does not guarantee full marks. Code that does not compile receives 0 for the automated tests.

In Exercises 1 and 2, do not use `java.util.TreeSet`, `TreeMap`, or any other library tree or sorted structure. In Exercise 3, you **must** use `TreeSet` and/or `TreeMap`.

---

## The Provided `BinarySearchTree`

The provided `BinarySearchTree.java` is the class from Module 3 and Assignment 3, now complete. Both `AVLTree` and `RBTree` inherit from it. Do **not** modify it and do **not** submit it.

Compared with the version from class, it adds:

- `add`, `delete`, and `search`, implemented. `add` returns the new node, or `null` for a duplicate. Both `add` and `delete` keep the parent pointers, `root`, `size`, and the stored heights up to date.
- `size()`, `height()`, `inorder()`, and `printTree()`.
- Protected helpers that subclasses can reuse:

| Helper | What it does |
|---|---|
| `findNode(value)` | Returns the node containing `value`, or `null`. |
| `minimum(node)` | Returns the leftmost (smallest) node of `node`'s subtree. |
| `transplant(u, v)` | Puts the subtree `v` (possibly `null`) in `u`'s place under `u`'s parent. Updates `root` when `u` is the root. |
| `newNode(value, parent)` | The method `add` uses to create every node. `RBTree` overrides it. |

---

## Exercise 1 — Deletion in an AVL Tree

The provided `AVLTree.java` is the class we wrote in Module 4, unchanged except for `delete`, which throws `UnsupportedOperationException`. It already contains `add`, `balanceFactor`, `rebalanceUpward`, `rotateLeft`, `rotateRight`, and `replaceAtParent`.

Do **not** modify the provided code or any method name or signature. You may add private helper methods.

### Part A — Implement `delete`

#### Background: the node that is physically removed

As seen in class, deletion starts as in any BST. If the node containing the value has **zero or one** child, that node is removed and its child (if any) takes its place. If it has **two** children, it receives the value of its **inorder successor**: the smallest value in its right subtree. The successor, which has at most one child, is then removed instead.

In both cases, the node that is **physically removed** has at most one child. Its parent is the **lowest node whose height can change**, so this is where rebalancing must start. Read `BinarySearchTree.delete`: it follows exactly these steps.

#### Background: why deletion can cascade

After an **insertion**, one restructure gives the subtree back the height it had **before** the insertion, so nothing above it changes. After a **deletion**, a rotation can leave the subtree **one level shorter** than before. An ancestor higher up may then become unbalanced too. That is why rebalancing must continue all the way to the root. `rebalanceUpward` already does this.

#### Your task

1. Read the provided `BinarySearchTree.java` and `AVLTree.java` files.
2. Implement the following method:

```java
@Override
public boolean delete(int value)
```

The method removes `value` and restores the AVL balance condition:

- If `value` is not in the tree, return `false` and leave the tree unchanged.
- Otherwise, remove it as in a BST (with the inorder successor when its node has two children).
- Starting from the **parent of the physically removed node**, recompute heights and rotate where needed, up to the **root**.
- Return `true`. After the call, the parent pointers, `root`, `size()`, and every stored height must be correct.

The method must run in $O(\log n)$ time. Do **not** rebuild the tree or recompute every height.

**Example 1 — one rotation.** We use the removal tree from the slides:

```text
40
├── L: 20
│   ├── L: 10
│   │   └── R: 15
│   └── R: 30
└── R: 60
    ├── L: 50
    │   └── L: 45
    └── R: 70
        ├── L: 65
        └── R: 80
            └── R: 90
```

`delete(45)` removes a leaf. The walk back up starts at its parent, `50`:

| Step | Node | Height | Balance | Action |
|---|---|---|---|---|
| 1 | `50` | 0 | 0 | balanced, move up |
| 2 | `60` | 3 | −2 | right child `70` has balance −1 → **RR** case, `rotateLeft(60)` |
| 3 | `40` | 3 | 0 | root reached, stop |

The result is:

```text
40
├── L: 20
│   ├── L: 10
│   │   └── R: 15
│   └── R: 30
└── R: 70
    ├── L: 60
    │   ├── L: 50
    │   └── R: 65
    └── R: 80
        └── R: 90
```

**Example 2 — a cascade.** Starting again from the same tree, `delete(30)` removes a leaf. The walk starts at `20`:

| Step | Node | Height | Balance | Action |
|---|---|---|---|---|
| 1 | `20` | 2 | +2 | left child `10` has balance −1 → **LR** case: `rotateLeft(10)`, then `rotateRight(20)` |
| 2 | `40` | 4 | −2 | its left subtree became shorter; right child `60` has balance −1 → **RR** case, `rotateLeft(40)` |
| 3 | — | — | — | `60` is the new root, stop |

One deletion caused rotations at **two** different levels. The result is:

```text
60
├── L: 40
│   ├── L: 15
│   │   ├── L: 10
│   │   └── R: 20
│   └── R: 50
│       └── L: 45
└── R: 70
    ├── L: 65
    └── R: 80
        └── R: 90
```

3. Compile and run the public tests for Exercise 1:

```text
javac *.java
java Main avl
```

**Point to consider:** When the deleted node has two children, the node that contained `value` is **not** the node that is physically removed. Starting the walk from the wrong node can leave an unbalanced node, or a wrong stored height, below your starting point.

### Part B — Analysis

Let:

- $n$ be the number of nodes in the tree.

1. Determine the worst-case running time of `delete` in terms of $n$. Justify the cost of each step: finding the node, finding the successor, removing it, and rebalancing.
2. How many rotations can one deletion perform in the worst case? Why does one insertion never need more than one restructure (single or double rotation)?

Write your answers as a comment in `AVLTree.java`, directly after the `delete` method, in the space marked `Exercise 1, Part B`.

---

## Exercise 2 — Insertion in a Red-Black Tree

The provided `RBTree.java` inherits from `BinarySearchTree`. The following parts are **provided**:

- `RBNode`, a node with a `color` field. New nodes are **red**.
- An override of `newNode`, so every node created by `BinarySearchTree.add` is an `RBNode`.
- `colorOf`, `isRed`, and `setColor`. A `null` (external) node counts as **black**.
- `rotateLeft` and `rotateRight`: the same pointer changes as in `AVLTree`, without heights.
- `delete`. Deletion was not covered in detail in class, so you do not need to study it. Your `add` must work with it.
- `printTree()`, which shows colors: `20(B)`, `10(R)`.

Do **not** modify the provided code or any method name or signature. You may add private helper methods (for example `insertFixup`).

### Part A — Implement `add`

#### Background: the insertion fix-up

A new node `z` is inserted as a **red** leaf. A red node adds no black to any path, so the black-height property still holds. The only possible problem is **two reds in a row**: `z` and its parent `p` are both red. The fix depends on the **uncle** `u` (the sibling of `p`) and on the shape formed by `g` (the grandparent), `p`, and `z`:

| Case | When | Fix |
|---|---|---|
| Case 1 | `u` is red | Color `p` and `u` black and `g` red. Continue with `z = g`. |
| Case 2 | `u` is black, and `z`, `p`, `g` form a **triangle** | Rotate at `p` so that the shape becomes a line, then apply Case 3. |
| Case 3 | `u` is black, and `z`, `p`, `g` form a **line** | Color `p` black and `g` red, then rotate at `g`. Stop. |

Each case has a **mirror** version, used when `p` is a **right** child of `g`. After the loop, color the root black.

#### Your task

1. Read the provided `RBTree.java` file.
2. Implement the following method:

```java
@Override
public Node add(int value)
```

The method inserts `value` and restores the red-black properties:

- Call `super.add(value)` to perform the BST insertion. It creates a red `RBNode`, or returns `null` if `value` is already in the tree.
- If `value` was a duplicate, return `null` and change nothing.
- Otherwise, fix any red-red violation with the cases above, then color the root black.
- Return the new node.

Use the provided `rotateLeft` and `rotateRight`. The tests count their calls: an insertion must perform **at most 2 rotations**.

**Example.** We use the insertion tree from the slides (built by adding `20, 10, 30, 5, 15, 25, 3, 7`):

```text
20(B)
├── L: 10(R)
│   ├── L: 5(B)
│   │   ├── L: 3(R)
│   │   └── R: 7(R)
│   └── R: 15(B)
└── R: 30(B)
    └── L: 25(R)
```

`add(1)` attaches `1` as a red left child of `3`:

| Step | `z` | Parent `p` | Uncle `u` | Case | Action |
|---|---|---|---|---|---|
| 1 | `1` | `3` (red) | `7` (red) | Case 1 | `3` and `7` black, `5` red; `z = 5` |
| 2 | `5` | `10` (red) | `30` (black) | Case 3 (line) | `10` black, `20` red, `rotateRight(20)` |
| 3 | — | — | — | — | loop ends; root `10` is black |

```text
10(B)
├── L: 5(R)
│   ├── L: 3(B)
│   │   └── L: 1(R)
│   └── R: 7(B)
└── R: 20(R)
    ├── L: 15(B)
    └── R: 30(B)
        └── L: 25(R)
```

Starting again from the same tree, `add(27)` attaches `27` as a red right child of `25`. The uncle of `27` is the empty left child of `30`, which counts as black. `27` → `25` → `30` is a triangle, so Case 2 applies: `rotateLeft(25)`. Then Case 3 applies: `rotateRight(30)`. That is two rotations:

```text
20(B)
├── L: 10(R)
│   ├── L: 5(B)
│   │   ├── L: 3(R)
│   │   └── R: 7(R)
│   └── R: 15(B)
└── R: 27(B)
    ├── L: 25(R)
    └── R: 30(R)
```

3. Compile and run the public tests for Exercise 2:

```text
javac *.java
java Main rb
```

**Point to consider:** In Case 2, the roles change after the rotation: the old parent becomes the child. Make sure your variables name the right nodes before you apply Case 3.

### Part B — Analysis

Let:

- $n$ be the number of nodes in the tree.

1. Using your code, explain why one insertion performs **at most 2 rotations**, but may perform $O(\log n)$ recolorings.
2. Determine the worst-case running time of `add` in terms of $n$. Use the fact that a red-black tree with $n$ nodes has height $h \le 2 \log_2(n + 1)$.

Write your answers as a comment in `RBTree.java`, directly after the `add` method, in the space marked `Exercise 2, Part B`.

---

## Exercise 3 — Nearest Free Locker with `TreeSet` / `TreeMap`

`java.util.TreeSet` and `java.util.TreeMap` **are red-black trees**: every `add`, `remove`, or `put` you call on them runs the algorithms of Exercise 2. Besides `add`, `remove`, and `contains`, they answer **ordered** questions that a `HashSet` (next module) cannot:

| `TreeSet` method | Returns |
|---|---|
| `floor(x)` | the greatest element $\le x$, or `null` |
| `ceiling(x)` | the smallest element $\ge x$, or `null` |
| `lower(x)` / `higher(x)` | the greatest element $< x$ / the smallest element $> x$ |
| `first()` / `last()` | the smallest / greatest element |

`TreeMap` offers the same methods for keys: `floorKey`, `ceilingKey`, and so on.

### Part A — Implement `LockerAssigner`

The library has $n$ lockers numbered $1, 2, \dots, n$, all free at the start. A student asks for a preferred number and receives the **free locker closest to it**. If two free lockers are equally close, the student receives the one with the **smaller** number.

1. Read the provided `LockerAssigner.java` file. Choose your structure (`TreeSet`, `TreeMap`, or both) and declare your field(s).
2. Implement the following methods:

```java
public LockerAssigner(int n)
public int assign(int preferred)
public boolean release(int locker)
```

- `LockerAssigner(n)` creates lockers $1..n$, all free. It throws `IllegalArgumentException` if `n < 0`.
- `assign(preferred)` returns the free locker closest to `preferred` and marks it as taken. `preferred` may be any `int`, even outside $1..n$. It returns `-1` if no locker is free.
- `release(locker)` frees a taken locker and returns `true`. It returns `false` and changes nothing if the locker is already free or is not in $1..n$.

**Example.** With $n = 10$:

| Call | Result | Free lockers afterwards |
|---|---|---|
| `assign(5)` | `5` | 1 2 3 4 6 7 8 9 10 |
| `assign(5)` | `4` | 1 2 3 6 7 8 9 10 (4 and 6 tie: the smaller wins) |
| `assign(5)` | `6` | 1 2 3 7 8 9 10 |
| `assign(42)` | `10` | 1 2 3 7 8 9 |
| `release(5)` | `true` | 1 2 3 5 7 8 9 |
| `release(5)` | `false` | 1 2 3 5 7 8 9 (already free) |

3. Compile and run the public tests for Exercise 3:

```text
javac *.java
java Main locker
```

**Point to consider:** Distances such as `preferred - locker` can overflow an `int` when `preferred` is close to `Integer.MIN_VALUE` or `Integer.MAX_VALUE`.

### Part B — Analysis

Let:

- $n$ be the number of lockers.

1. Which structure did you use (`TreeSet`, `TreeMap`, or both), what does it store, and why does it fit this problem?
2. If you had stored the free lockers in an **unsorted `ArrayList`** instead, what would be the worst-case running time of `assign` and of `release`? Justify your answer.
3. What is the worst-case running time of `assign` and `release` with your solution? Justify it using the fact that `TreeSet` and `TreeMap` are red-black trees.

Write your answers as a comment at the end of `LockerAssigner.java`, in the space marked `Exercise 3, Part B`.

---

## Testing Your Code

Testing is part of programming, not a step you do at the very end. Programmers who test a little at a time find their mistakes while they are still small and easy to locate. This section explains how to use the tests in the starter package and how to go beyond them.

### Test early, test often

Do not write all your methods and then run the tests once. Instead, work in small steps:

1. Implement **one** method (for example, `AVLTree.delete`).
2. Compile and run the tests for that exercise right away.
3. Fix any failing test before moving on to the next method.

A good order for this assignment is: `AVLTree.delete`, then `RBTree.add`, then `LockerAssigner`.

### Running the public tests

The starter package contains two provided files besides the ones you edit. Keep them in the same folder as your code, but do **not** modify or submit them.

| File | Purpose |
|---|---|
| `BinarySearchTree.java` | The complete BST that `AVLTree` and `RBTree` inherit from. |
| `Main.java` | The public tests for all three exercises. It runs each test and prints the results. |

The starter package compiles as provided. To run the tests:

```text
javac *.java
java Main            # all exercises
java Main avl        # Exercise 1 only
java Main rb         # Exercise 2 only
java Main locker     # Exercise 3 only
```

If `javac` reports an error, fix it first: the tests cannot run until all your files compile.

### Reading the test output

Each test prints one line. Its **name** tells you what behaviour is being checked, so read it carefully. Trees are shown by their **preorder** listing, which fixes their shape. For red-black trees, each value is followed by its color: `20B 10R` means that `20` is black and `10` is red.

At the start, before you have written any code, every test fails with `not implemented`. This is normal:

```text
  FAIL  delete the only node empties the tree  (not implemented: Implement AVL deletion with rebalancing)
```

When a method runs but gives a wrong result, the line shows what the test **expected** and what your code **produced**:

```text
  FAIL  at most 2 rotations: triangle uses exactly 2  -> expected <2> but got <3>
```

Here, the tree that was built is valid, but the insertion used three rotations instead of two: Case 2 was probably skipped, and the loop repaired the tree on a later iteration. When you see a message like this, open `Main.java`, find the test with the same name, and read the tree it builds (the large trees are drawn in comments). Then trace your code on that tree **by hand**, on paper, and compare your result with the expected one.

When all the tests of an exercise pass, its summary line shows no failures:

```text
Exercise 1 public tests: 7 passed, 0 failed (of 7)
```

### Writing your own tests

The public tests do not cover every situation. **Your code will also be graded with additional hidden tests**, so passing the public tests is necessary but not sufficient. Get into the habit of asking: *"What input could break my method?"*

Write a small `main` method (in a separate file, e.g. `MyTests.java`, which you do not submit) that builds trees, calls your methods, and displays the result with `printTree()`. Compare what you see with what you expect **before** running the code. `Main.assertValidAVL(tree)` and `Main.assertValidRB(tree)` check every invariant of a tree for you. Try at least these cases:

- an **empty** tree, a tree with a **single** node, and deleting the **root**;
- deleting a node with two children whose successor is (and is not) its right child;
- the **mirror** image of every case shown in the examples;
- values added in increasing or decreasing order, for example $1, 2, \dots, 100$;
- for `LockerAssigner`: no lockers, `preferred` outside $1..n$, ties, and a **large** input (for example $200{,}000$ lockers) to check that your methods stay fast.

For example:

```java
public class MyTests {
    public static void main(String[] args) {
        RBTree t = new RBTree();
        for (int i = 1; i <= 10; i++) {
            t.add(i);
        }
        t.printTree();               // does the shape match your drawing?
        Main.assertValidRB(t);       // throws an error if a property is broken

        AVLTree a = new AVLTree();
        for (int i = 1; i <= 10; i++) {
            a.add(i);
        }
        a.delete(4);                 // the root: it has two children
        a.printTree();
        Main.assertValidAVL(a);
    }
}
```

### Before you submit

- [ ] All your files compile without errors.
- [ ] All public tests pass for the three exercises.
- [ ] You tested the edge cases listed above with your own tests.
- [ ] Your analysis answers are written in the three spaces marked `Part B`.
- [ ] You submit only `AVLTree.java`, `RBTree.java`, and `LockerAssigner.java`.

---

## Grading Rubric

| Exercise | Part | Points |
|---|---|---|
| Exercise 1 | Part A — AVL `delete` | 25 |
| | Part B — Analysis (Q1: 3, Q2: 2) | 5 |
| Exercise 2 | Part A — Red-black `add` | 25 |
| | Part B — Analysis (Q1: 3, Q2: 2) | 5 |
| Exercise 3 | Part A — `LockerAssigner` | 20 |
| | Part B — Analysis (Q1: 3, Q2: 3, Q3: 4) | 10 |
| Code quality | | 10 |
| **Total** | | **100** |
