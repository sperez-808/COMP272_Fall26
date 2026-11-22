import java.util.*;

public class KLargestElements {

// Solution 1: Max-heap of all n elements
    public static List<Integer> findKLargestMaxHeap(int[] arr, int k) {
        PriorityQueue<Integer> max =
                new PriorityQueue<>(Comparator.reverseOrder());

        // Build max-heap
        for (int x : arr) {
            max.add(x);
        }

        // Extract k largest elements
        List<Integer> topK = new ArrayList<>();
        for (int i = 0; i < k && !max.isEmpty(); i++) {
            topK.add(max.poll());
        }

        return topK;
    }
// Time: O(n log n + k log n)
// Space: O(n + k)

  
// Solution 2: Min-heap of size k
    public static List<Integer> findKLargestMinHeap(int[] arr, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        // Keep only the k largest elements
        for (int num : arr) {
            pq.offer(num);

            if (pq.size() > k) {
                pq.poll();
            }
        }

        return pq;
    }
// Time: O(n log(k + 1) + k log k)
// Space: O(k)

  
    public static void main(String[] args) {
        int[] arr = {12, 3, 17, 8, 25, 1, 14, 9};
        int k = 3;

        System.out.println("Solution 1: " + findKLargestMaxHeap(arr, k));

        System.out.println("Solution 2: " + findKLargestMinHeap(arr, k));
    }
}
