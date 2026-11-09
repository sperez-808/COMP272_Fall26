import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class HashingProblems {
  // Average the map values for keys that show up in both the array and the map
  // Each matching key counts once, even if it appears multiple times in the array
  // Returns 0.0 if nothing matches
  public static double getAverage(int[] keys, HashMap<Integer, Integer> map) {
    // Track keys we've already counted
      // Duplicates in the array aren't counted twice
      HashSet<Integer> seen = new HashSet<>();
        long sum = 0;
        int count = 0;
        // Only count a key if it's new and in the map
        for (int key : keys) {
          if (seen.add(key) && map.containsKey(key)) {
            sum += map.get(key);
            count ++;
          }
        }
    // Avoid divide-by-zero
    // Cast to double so real average is obtained
      return count == 0 ? 0.0 : (double) sum / count;
  }

  // Collect the map values whose keys are odd
    // Including negatives
  // Order of the results doesn't matter
  public static ArrayList<Integer> odd(HashMap<Integer, Integer> map) {
    ArrayList<Integer> result = new ArrayList<>();
    // Keep any key where the remainder of the key isn't 0
      // Also catches negative odd keys
    for (Integer key : map.keySet()) {
      if (key % 2 != 0) {
        result.add(map.get(key));
      }
    }
    return result;
  }
  // Count distinct unordered pairs of values whose absolute difference is k
  // Count how often each value occurs first, then
    // For each value x, count it if x + k exists
      // Each pair counted once
    // A value pairs with itself only if it appears at least twice
  // Runs in O(n) time and O(n) extra space
  public static int twoSums(int[] numbers, int k) {
    // Negative difference can never happen
    if (k < 0) return 0;
    // Frequency map of value
    HashMap<Integer, Integer> freq = new HashMap<>();
    for (int x : numbers) {
      freq.put(x, freq.getOrDefault(x, 0) + 1);
    }
    int count = 0;
    if (k == 0) {
      // Only values that repeat at least twice form a pair with themselves
      for (int f : freq.values()) {
        if (f >= 2) count++;
      }
    } else {
      // For each value, check whether value + k exists
        // This counts each pair once
      for (int x : freq.keySet()) {
        if (freq.containsKey(x + k)) {
          count++;
        }
      }
    }
    return count;
  }
  // Runs the tests for the HashingProblems methods
  public static void main(String[] args) {
    System.out.println("HashingProblems tests");
    // Map used for the getAverage tests
    HashMap<Integer, Integer> avgMap = new HashMap<>();
      avgMap.put(1, 10);
      avgMap.put(2, 20);
      avgMap.put(3, 30);
      // Duplicate key 2 in the array should still only count once
      int[] avgKeys1 = {1, 2, 2, 4};
      System.out.println("getAverage duplicate matching key: expected 15.0, actual " + getAverage(avgKeys1, avgMap));
      int[] avgKeys2 = {99};
      System.out.println("getAverage no matches: expected 0.0, actual " + getAverage(avgKeys2, avgMap));
      // Map with a mix of odd and even keys
        // Including a negative odd one
      HashMap<Integer, Integer> oddMap = new HashMap<>();
        oddMap.put(1, 10);
        oddMap.put(-3, 20);
        oddMap.put(2, 30);
        oddMap.put(7, 40);

        // Sort first so the printed order is stable across runs
        ArrayList<Integer> oddResult = odd(oddMap);
        Collections.sort(oddResult);
        System.out.println("odd: expected [10, 20, 40], actual " + oddResult);

        // Empty map - empty list
        HashMap<Integer, Integer> emptyMap = new HashMap<>();
        System.out.println("odd empty: expected [], actual " + odd(emptyMap));

        // Pairs with difference 2:
        int[] nums1 = {1, 1, 3, 5};
        System.out.println("twoSums [1, 1, 3, 5], k = 2: expected 2, actual " + twoSums(nums1, 2));

        // k = 0 and 1 appears 3 times - one distinct self-pair
        int[] nums2 = {1, 1, 1};
        System.out.println("twoSums [1, 1, 1], k = 0: expected 1, actual " + twoSums(nums2, 0));

        // Difference 1
        int[] nums3 = {1, 2, 3};
        System.out.println("twoSums [1, 2, 3], k = 1: expected 2, actual " + twoSums(nums3, 1));

        // All distinct, so no value repeats
        int[] nums4 = {1, 2, 3};
        System.out.println("twoSums [1, 2, 3], k = 0: expected 0, actual " + twoSums(nums4, 0));

        // Empty array
        int[] nums5 = {};
        System.out.println("twoSums empty, k = 0: expected 0, actual " + twoSums(nums5, 0));
  }
}
