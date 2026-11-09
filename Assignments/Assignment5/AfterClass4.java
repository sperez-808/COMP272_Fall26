import java.util.Arrays;
import java.util.PriorityQueue;

public class KLargest {
  public static int[] findKLargest(int[] numbers, int k) {
    // Nothing to return for a non-positive k
    if (k <= 0) return new int[0];
    // If k covers the whole array, just hand back a copy of it
    if (k >= numbers.length) return numbers.clone();

    // Min-heap: its smallest element is always at the top
    PriorityQueue<Integer> heap = new PriorityQueue<>();
    // Scan once
      // Keeps only the largest k
      for (int x : numbers) {
        heap.add(x);
        // More than k candidates - discard the smallest
        if (heap.size() > k) heap.poll();
      }
      // Drain the heap into the result array
      int[] result = new int[k];
      for (int i = 0; i < k; i++) {
        result[i] = heap.poll();
      }
      return result;
  }
  // Runs tests for findKLargest
  public static void main(String[] args) {
    System.out.println("findKLargest tests");

    // Basic case: 3 largest of [3, 1, 4, 1, 5, 9, 2, 6] are 9, 6, 5
    int[] a = {3, 1, 4, 1, 5, 9, 2, 6};
    System.out.println("basic k = 3: expected [5, 6, 9], actual " + show(findKLargest(a, 3)));

    // k = 1 - the maximum
    System.out.println("k = 1: expected [9], actual " + show(findKLargest(a, 1)));

    // k = n - whole array back
    System.out.println("k = n: expected [1, 1, 2, 3, 4, 5, 6, 9], actual " + show(findKLargest(a, a.length)));

    // Duplicates
    int[] dup = {4, 4, 4, 1, 2};
    System.out.println("duplicates k = 3: expected [2, 4, 4], actual " + show(findKLargest(dup, 3)));

    // Negative number
    int[] neg = {-5, -1, -3, -2, -4};
    System.out.println("negatives k = 2: expected [-2, -1], actual " + show(findKLargest(neg, 2)));

    // k = 0 - empty result
    System.out.println("k = 0: expected [], actual " + show(findKLargest(a, 0)));

    // Empty array - empty result
    int[] empty = {};
    System.out.println("empty array: expected [], actual " + show(findKLargest(empty, 3)));

    // Input must not be changed by the call
    int[] before = {7, 2, 9, 4};
    int[] snapshot = before.clone();
    findKLargest(before, 2);
    System.out.println("input unchanged: expected " + show(snapshot) + ", actual " + show(before));
  }

  // Helper for tests
    // Copy, sort ascending, and format as a string
  private static String show(int[] a) {
    int[] copy = a.clone();
    Arrays.sort(copy);
    return Arrays.toString(copy);
  }
}
