# COMP 272 – Mid-Term Study Guide

The mid-term covers four topics: linked lists, stacks, queues and recursion; algorithm complexity; trees, binary trees and BSTs; and AVL trees. Most of the marks come from complexity and trees. Expect to trace small examples by hand.

## 1. Linked lists, stacks, queues and recursion

Know:

- A stack is last-in, first-out. A queue is first-in, first-out.
- ArrayList: get and set are O(1), adding at the end is O(1) on average, and inserting or removing at the front or middle is O(n).
- Linked list: adding at the front is O(1), get(i) is O(n), and adding at the end is O(1) only if you keep a tail reference.
- Arrays store elements next to each other in memory, so scanning them is cache-friendly. Linked-list nodes are scattered.
- Inserting a node in the middle of a doubly linked list changes four links. Save any neighbour you still need before you overwrite a link.
- A recursive method needs a stopping case that every input eventually reaches.

Be able to:

- Trace push, pop, enqueue and dequeue operations.
- Choose the correct link updates for a doubly linked list insert or remove.
- Say which inputs a recursive method's stopping case handles.

## 2. Algorithm complexity

Know:

- O is an upper bound, Ω is a lower bound, and Θ is a tight bound (both at once).
- Keep the biggest term and drop constants: 3n³ + 8n + 2 is O(n³).
- Growth order: 1 < log n < n < n log n < n² < 2ⁿ.
- Loops one after another add. Nested loops multiply. If the inner loop depends on the outer one, add up the inner counts.
- A loop that doubles or halves its counter runs about log n times.
- Big-O describes growth for large n. For small n, a "slower" algorithm can be faster.
- Recursion uses stack space. It is not automatically slower than a loop.

Be able to:

- Give the Big-O of an expression.
- Give O, Ω and Θ for simple and nested loops.
- Count how many times a loop runs.

## 3. Trees, binary trees and BSTs

Know:

- Terms: root, parent, child, leaf, depth, height. A leaf has height 0.
- A complete binary tree fills every level except possibly the last, from left to right.
- Preorder: node, left, right. Inorder: left, node, right. Postorder: left, right, node. These three are depth-first. Level order is breadth-first.
- Every traversal visits each node once, so it is O(n). Computing the height is also O(n).
- In a BST, smaller keys go left and larger keys go right, and this holds for whole subtrees. Inorder lists the keys in sorted order.
- Search, insert and delete cost O(h). h is about log n for a balanced tree and n − 1 for a chain.
- To delete a node with two children, replace it with its inorder successor (smallest key on the right) or its inorder predecessor (largest key on the left).

Be able to:

- Write the preorder, inorder and postorder of a tree.
- Insert keys into a BST and draw the result.
- Delete the root of a BST and draw the result.

## 4. AVL trees

Know:

- An AVL tree is a BST where, at every node, the left and right subtree heights differ by at most 1.
- Balance factor = height(left) − height(right). It must be −1, 0 or +1.
- Because of this rule the height stays O(log n), so search, insert and delete are O(log n).
- After an insert or delete, walk up the tree. The first node with balance factor +2 or −2 needs a rotation.

| Balance factor of node | Balance factor of its child | Fix |
| --- | --- | --- |
| +2 | 0 or +1 | Rotate right at the node |
| +2 | −1 | Rotate left at the child, then right at the node |
| −2 | 0 or −1 | Rotate left at the node |
| −2 | +1 | Rotate right at the child, then left at the node |

- A rotation changes only a few links, so it is O(1).

Be able to:

- Compute balance factors and say whether a tree is a valid AVL tree.
- Insert or delete a key and do the rotation(s).
- Pick the correct link-update code for a rotation.

## Watch out for

- O is not "exact" and not "always the worst case".
- Height counts edges, not nodes.
- Preorder starts with the root; postorder ends with it.
- A zig-zag shape in an AVL tree needs two rotations, not one.
- In linked-list code, read a link before you overwrite it.
