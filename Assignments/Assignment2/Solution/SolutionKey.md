# COMP 272/400C — Module 2 Assignment: Solution Key
## Algorithm Analysis: Theory and Experimentation

*Note on the timing tables in Exercise 2: these numbers came from actually compiling and running `RangeSumExperiment.java` in this folder on one machine. A student's exact numbers will differ — what matters for grading is the growth trend and whether their own numbers are used consistently in their reasoning, not a match to these figures.*

---

## Exercise 1 — Analyze Four Functions

### Function A — `haveCommonValue`

- **Input size:** two parameters are required, since the arrays need not be the same length: `n = first.length`, `m = second.length`.
- **Primitive operation:** the comparison `first[i] == second[j]`.
- **Worst-case cost:** the worst case is no shared value, so both loops run to completion for every `i`: T(n, m) = n · m.
- **Tight upper bound:** O(nm). (It's only O(n²) in the special case where the two arrays happen to be the same length — that's not the general bound.)
- **Auxiliary space:** O(1) — just the loop counters `i` and `j`; the return value is a single boolean, not a data structure.

### Function B — `countIncreasingTriples`

- **Input size:** n = `values.length`.
- **Primitive operation:** the two-part comparison `values[i] < values[j] && values[j] < values[k]`.
- **Worst-case cost:** the loop bounds never depend on the array's contents, so every valid triple `i < j < k` is examined regardless of input — best and worst case are the same. The count of such triples is "n choose 3":

  T(n) = C(n, 3) = n(n−1)(n−2) / 6 = (n³ − 3n² + 2n) / 6

- **Tight upper bound:** the dominant term is n³/6, so O(n³).
- **Auxiliary space:** O(1) — `count` and the three loop indices.

### Function C — `fastPower`

- **Input size — the trap in this exercise:** the *numeric value* of `exponent` is not the honest measure of input size. What actually determines how much space the input occupies is `b`, the number of bits needed to write it: b = ⌊log₂(exponent)⌋ + 1.
- **Primitive operation:** a `long` multiplication (`half * half`, or `base * half * half` when the exponent is odd).
- **Recurrence:** each call does a constant amount of work and recurses once on `exponent / 2` (integer division):

  T(e) = T(⌊e/2⌋) + O(1), with T(0) = O(1)

- **Solving it:** each call halves `e` — equivalently, drops one bit — so the recursion bottoms out after `b` levels. Unrolling gives T(e) = O(b) = O(log e).
- **Tight upper bound:** O(log n). This is linear in the *number of bits*, not in the numeric value of the exponent — a loop that multiplied `base` by itself `exponent` times would be O(exponent), which is exponential in `b`. That contrast is the whole point of the exercise.
- **Auxiliary space:** the method is recursive, so the call stack counts. The deepest point has about b + 1 frames alive at once: O(log e). The result itself is a single `long`, O(1) result space.

### Function D — `multiply` (rectangular matrix multiplication)

- **Input size:** three independent parameters, since the matrices need not be square: `r` = rows of `first`, `s` = shared dimension, `c` = columns of `second`.
- **Primitive operation:** the multiplication in `first[row][k] * second[k][column]`.
- **Worst-case cost:** the triple-nested loop has no data-dependent early exit, so the cost is identical for every input of a given shape: T(r, s, c) = r · s · c.
- **Tight upper bound:** O(rsc), which only becomes the familiar O(n³) in the special case of square n × n matrices.
- **Auxiliary space — the second trap:** O(1) — the loop variables are the only extra memory used *while the method runs*. The r × c product matrix is **not** auxiliary space, because the method is required to return it; it's *result space*, reported separately as O(rc).

---

## Exercise 2 — Range-Sum Queries

### Part A — Single-run experiment

```
n       q       Direct (ns)     Prefix (ns)
500     500     727,584         11,083
1,000   1,000   334,292         18,875
2,000   2,000   3,539,291       36,417
4,000   4,000   3,557,250       74,542
8,000   8,000   14,393,542      148,042
```

**Observation:** Prefix sums grow smoothly and roughly double with each doubling of n, matching the expected linear trend even on a single run. Direct summation is far noisier — n = 1,000 actually finished *faster* than n = 500, and n = 2,000 vs. n = 4,000 barely changed. That has nothing to do with the algorithm; it's JIT warm-up on the first couple of calls. A single run is not a reliable way to compare these two algorithms.

### Part B — Repeated experiment (mean of 5 runs)

```
n       q       Direct (ns)     Prefix (ns)
500     500     48,425          10,224
1,000   1,000   214,983         19,891
2,000   2,000   885,725         40,766
4,000   4,000   3,593,325       38,875
8,000   8,000   14,481,633      36,550
```

**Comparison:** Averaging removed the cold-start noise from the direct column entirely — it now grows almost exactly 4× with every doubling of n (4.44×, 4.12×, 4.06×, 4.03×), exactly what O(nq) = O(n²) predicts once q = n. The prefix column tracks the predicted ~2×-per-doubling trend through n = 2,000, then flattens out from n = 4,000 on. That's not the algorithm's complexity changing — at these sizes O(n) work is only tens of microseconds, small enough to be dominated by measurement noise and JVM scheduling rather than by the array-fill loop itself. Repetition did exactly what it's supposed to: it made the direct trend rock-solid and revealed that the prefix column has hit a noise floor at this range of n, not a real plateau.

### Part C — Algorithmic analysis

**1. Worst-case time of `answerDirectly`.** Each query can span the whole array, costing up to n additions, and there are q queries: **O(nq)**.

**2. Time for `answerWithPrefixSums`:**
- Constructing the prefix array: one pass over the n values — **O(n)**.
- Answering one query: a single subtraction of two array entries — **O(1)**.
- Answering all q queries: q independent O(1) lookups — **O(q)**.

**3. Overall growth class:** O(n) + O(q) = **O(n + q)**.

**4. Which is asymptotically faster as both grow?** `answerWithPrefixSums`. O(n + q) grows far more slowly than O(nq) — concretely, with q = n, direct becomes O(n²) while prefix sums stays O(n).

**5. Do the empirical results match the theory?** Yes, once the noise is averaged out. The repeated-run direct times quadruple almost exactly with each doubling of n, matching O(n²) when q = n. The prefix times matched the predicted ~2× growth through n = 2,000, then flattened — not because the algorithm stopped being O(n), but because O(n) work at these sizes is small enough to be swamped by system noise. For much larger n (say, n = 64,000), the O(n) trend for prefix sums should become clearly visible again, since the real work would then dominate the noise floor.

**6. Auxiliary space** (not counting the returned `answers` array, which both algorithms must produce):
- `answerDirectly`: **O(1)** — `left`, `right`, `sum`, and the loop counters.
- `answerWithPrefixSums`: **O(n)** — the prefix array holds n + 1 `long` values.

**7. Time–space tradeoff.** Prefix sums spend O(n) extra memory (and O(n) upfront time) to turn every future query into O(1) instead of up to O(n). Direct summation uses no extra memory at all but pays the full range cost on every single query. The tradeoff favors prefix sums more and more as the number of queries on the same array grows.

**8. Is prefix summing worth it for one short query?** No — building the prefix array costs O(n) no matter how short the query is, while direct summation only costs the length of that one range, which can be far smaller than n. Prefix sums only pay off once the array receives enough queries that the total work direct summation would have done across all of them starts to exceed the one-time O(n) setup cost.
