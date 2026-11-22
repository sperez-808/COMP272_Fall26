# COMP 272 – Sorting Algorithms Assignment

## Instructions

- Complete all three exercises.
- For **Exercises 1 and 2**, implement the requested Java methods and **test your code** using the provided examples and additional test cases of your own. Include your test code in `Solutions.java`.
- Explain your approach and analyze the time and space complexity where requested.
- Do not use built-in sorting methods for Exercise 2.

### Submission

Submit **two files**:

1. **`Solutions.java`** — Contains your implementations for Exercises 1 and 2, along with code that tests both methods.
2. **A PDF file** — Contains your written answers to the explanation, complexity-analysis, and algorithm-selection questions in all three exercises. For Exercises 1 and 2, include a brief description of your tests and their results.

---

## Exercise 1: Destroying Asteroids

You are given an integer `mass`, representing the initial mass of a planet, and an integer array `asteroids`, where `asteroids[i]` represents the mass of the *i*-th asteroid.

The planet must collide with every asteroid, but **you can choose the order of collisions**.

For each collision:

- If the planet's mass is **greater than or equal to** the asteroid's mass, the asteroid is destroyed, and its mass is added to the planet's mass.
- Otherwise, the planet is destroyed.

**Your task:** Determine whether there is an order in which the planet can destroy all the asteroids.

### Example 1

**Input:**

```java
mass = 10;
asteroids = new int[]{3, 9, 19, 5, 21};
```

**Output:** `true`

**Explanation:** One possible collision order is `[9, 19, 5, 3, 21]`.

| Asteroid | Planet mass before | Planet mass after |
|---|---:|---:|
| 9 | 10 | 19 |
| 19 | 19 | 38 |
| 5 | 38 | 43 |
| 3 | 43 | 46 |
| 21 | 46 | 67 |

All asteroids are destroyed.

### Example 2

**Input:**

```java
mass = 5;
asteroids = new int[]{4, 9, 23, 4};
```

**Output:** `false`

**Explanation:** Even if the planet destroys the other asteroids, its mass reaches only `5 + 4 + 9 + 4 = 22`, which is insufficient to destroy the asteroid of mass `23`.

### Constraints

- `1 <= mass <= 100000`
- `1 <= asteroids.length <= 100000`
- `1 <= asteroids[i] <= 100000`

### Hints

- Destroying a smaller asteroid never reduces the planet's ability to destroy a larger one. Every successful collision increases the planet's mass.
- If the planet cannot destroy the smallest remaining asteroid, can it destroy any of the larger ones?
- Think about how the **order of collisions** can help you solve the problem efficiently.

This problem can be solved using a **greedy strategy**: an approach that makes the best choice at each step to work toward an overall solution. Optional reading: [Introduction to Greedy Algorithms](https://www.geeksforgeeks.org/dsa/greedy-algorithms/).

### Questions

1. Implement the following method in `Solutions.java`:

   ```java
   public static boolean canDestroyAll(int mass, int[] asteroids)
   ```

2. **Test your method** using both examples and additional cases of your own. Include your test code in `Solutions.java` and summarize your tests and results in the PDF.
3. Briefly explain how your algorithm chooses the collision order and why your approach works.
4. Analyze the **time and space complexity** of your solution using Big-O notation.

---

## Exercise 2: Modified Merge Sort

Modify the **Merge Sort algorithm** to sort an integer array according to the following rules:

- All numbers **divisible by `k` appear first**, sorted in ascending order.
- All remaining numbers appear afterward, also sorted in ascending order.

Implement this behavior **within Merge Sort**, rather than sorting first and then rearranging the array. Your solution must retain Merge Sort's **O(n log n)** time complexity.

### Example 1

**Input:**

```java
values = new int[]{10, 3, 25, 8, 6};
k = 5;
```

**Result:**

```java
{10, 25, 3, 6, 8}
```

`10` and `25` are divisible by `5`, so they come first in ascending order. The remaining values follow in ascending order.

### Example 2

**Input:**

```java
values = new int[]{30, 45, 22, 9, 18, 39, 6, 12};
k = 6;
```

**Result:**

```java
{6, 12, 18, 30, 9, 22, 39, 45}
```

### Questions

1. Implement the following method in `Solutions.java`:

   ```java
   public static void modifiedMergeSort(int[] values, int k)
   ```

   The method must modify the original array. You may define additional helper methods. Assume `k > 0`.

2. **Test your method** using both examples and additional cases of your own. Include your test code in `Solutions.java` and summarize your tests and results in the PDF.
3. Explain what you changed in the standard Merge Sort algorithm to obtain the required ordering.
4. Analyze the **time and space complexity** of your implementation using Big-O notation.

### Hints

- Recall that Merge Sort recursively divides an array into smaller parts and then merges sorted sequences.
- Think about how to modify the **comparison logic during the merge step** to prioritize numbers divisible by `k`.
- You do not need two separate sorting algorithms.

---

## Exercise 3: Choosing the Right Sorting Algorithm

An online shopping platform maintains a collection of **100,000 products**. Each product has a price, and the platform needs to sort the products in ascending order so customers can browse from cheapest to most expensive.

Initially, the products are stored in **random order**. The platform needs an efficient sorting algorithm, and all the data fits in main memory.

### Questions

1. Which **sorting algorithm covered in class** would you recommend? Justify your choice based on its characteristics, including **time and space complexity**.
2. After several months, the platform changes how it manages its products. The product list is now **already sorted most of the time**, with only a few new products occasionally inserted in the wrong positions. **Would you change your recommendation?** Explain why or why not, considering the **adaptivity** of sorting algorithms.
3. Suppose the platform also wants products with the same price to retain their **original relative order**. Would this additional requirement affect your recommendation? Explain your reasoning using the concept of **stability**.

**Note:** More than one algorithm may be appropriate, provided your reasoning is well justified.

**Submit your answers to Exercise 3 in the PDF. No Java implementation is required for this exercise.**
