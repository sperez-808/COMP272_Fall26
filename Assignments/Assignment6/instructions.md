# COMP 272: Data Structures II — Assignment 6

**Loyola University Chicago · Department of Computer Science · Fall 2026**

This is an individual assignment.

---

## Submission

1. **Do not modify `Main.java`** in your submitted version.
2. Before you submit, make sure your **code passes all the supplied tests**. Keep in mind that passing the tests does not guarantee that your code is correct or efficient. Your assignment will be graded on test results, correctness, and efficiency.
3. Include your **name** as a comment at the top of `ProblemSolutions.java`, `BloomFilter.java`, and `CuckooHash.java`.

---

## Assignment

This assignment contains three short problems for you to solve. Use the `HashMap` and `PriorityQueue` classes from the Java Collections Framework where appropriate. Two additional classes are provided, `BloomFilter` and `CuckooHash`; for these, you will complete the template methods.

### Problem 1 — Hash Table (21 points)

Given two arrays `A` and `B`, return whether array `B` is a subset of array `A`.

| Input | Output |
|---|---|
| `[1, 50, 55, 80, 90]`, `[55, 90]` | `true` |
| `[1, 50, 55, 80, 90]`, `[55, 90, 99]` | `false` |

### Problem 2 — Priority Queue 1 (21 points)

Given an array `A` and an integer `k`, return the **k-th maximum** element in the array.

| Input | Output |
|---|---|
| `[1, 7, 3, 10, 34, 5, 8]`, `4` | `7` |

### Problem 3 — Priority Queue 2 (21 points)

Given two arrays `A` and `B` with `n` and `m` integers respectively, return all the elements in **sorted order**.

| Input | Output |
|---|---|
| `[4, 1, 5]`, `[3, 2]` | `1 2 3 4 5` |

### Problem 4 — Bloom Filter (21 points)

Implement the following method:

- **`contains()`** — Check the bits in the Bloom filter for each of the `k` hash codes computed from the given parameter. Return `false` if the element is **not** in the set; otherwise return `true` (the element is **most probably** in the set).

### Problem 5 — Cuckoo Hash Map (16 points)

Implement the following method:

- **`put()`** — Add a key-value pair to the table using cuckoo hashing.

> **Note**
>
> - For this **cuckoo hashing problem**, the goal is for you to study the existing code in order to write `put()`. Do not wait until the last minute to work on this method, and plan your **time management** accordingly. It will likely take more effort to get correct and to pass all tests than the previous problems. Because it may be slightly more difficult, it is worth the fewest points, so you are not penalized too much if your code does not pass the cuckoo hashing tests.
> - **Read the prologue comment for `put()`, including the two HINTs.**

---

## Grading Summary

| Problem | Points |
|---|---|
| 1 — Hash Table (subset) | 21 |
| 2 — Priority Queue 1 (k-th maximum) | 21 |
| 3 — Priority Queue 2 (merge in sorted order) | 21 |
| 4 — Bloom Filter `contains()` | 21 |
| 5 — Cuckoo Hash `put()` | 16 |
| **Total** | **100** |
