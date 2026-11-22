import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MergeSortExamples {

    // 1. RECURSIVE MERGE SORT (top-down)
    // Split the array into smaller ranges until each has one element,
    // then merge the ranges back together in sorted order.
    public static void recursiveMergeSort(int[] a) {
        recursiveSort(a, 0, a.length - 1);
    }

    private static void recursiveSort(int[] a, int left, int right) {
        // A range with zero or one element is already sorted.
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        // Sort each half before combining them.
        recursiveSort(a, left, mid);
        recursiveSort(a, mid + 1, right);
        mergeRange(a, left, mid, right);
    }

    // 2. ITERATIVE MERGE SORT (bottom-up)
    // Merge adjacent sorted ranges of size 1, then 2, then 4, etc.
    // Unlike the recursive version, this uses loops to grow the ranges.
    public static void iterativeMergeSort(int[] a) {
        int n = a.length;
        for (long size = 1; size < n; size *= 2) {
            for (long start = 0; start + size < n; start += 2 * size) {
                int left = (int) start;
                int mid = (int) (start + size - 1);
                int right = (int) Math.min(start + 2 * size - 1, n - 1);
                mergeRange(a, left, mid, right);
            }
        }
    }

    
    // SHARED IN-MEMORY MERGE
    // Both halves are already sorted. Compare their next elements, place
    // the smaller one into temp, and copy the merged result back to a.
    // used by 1 and 2
    private static void mergeRange(int[] a, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            temp[k++] = a[i] <= a[j] ? a[i++] : a[j++];
        }
        while (i <= mid) temp[k++] = a[i++];
        while (j <= right) temp[k++] = a[j++];
        System.arraycopy(temp, 0, a, left, temp.length);
    }





  
    // 3. EXTERNAL MERGE SORT (file-based, not multithreaded)
    // Use this when the input is too large to fit in memory. Input files
    // contain one integer per line. Sort up to chunkSize integers at a time,
    // save each sorted chunk to disk, then merge those files in rounds.
    // This avoids loading the full input into RAM.
    public static void externalMergeSort(String[] inputFiles, String outputFile,
                                         int chunkSize) throws IOException {
        if (chunkSize < 1) throw new IllegalArgumentException("chunkSize must be positive");
        // Store intermediate sorted files (runs) in a temporary directory.
        // The directory and its files are removed in the finally block.
        Path tempDir = Files.createTempDirectory("merge-runs-");
        List<Path> runs = new ArrayList<>();
        try {
            // Reuse one fixed-size array to read data across all input files.
            // Whenever it fills up, sort its contents and write a run to disk.
            int[] chunk = new int[chunkSize];
            int count = 0;
            for (String inputFile : inputFiles) {
                try (Scanner scanner = new Scanner(new File(inputFile))) {
                    while (scanner.hasNextInt()) {
                        chunk[count++] = scanner.nextInt();
                        if (count == chunkSize) {
                            runs.add(writeSortedRun(chunk, count, tempDir));
                            count = 0;
                        }
                    }
                }
            }
            // Save the final partially filled chunk, if there is one.
            if (count > 0) runs.add(writeSortedRun(chunk, count, tempDir));

            // Merge sorted runs in pairs, writing intermediate runs to disk.
            // Each round reduces the number of runs until only one remains.
            // If a round has an odd number of runs, carry the last one forward.
            while (runs.size() > 1) {
                List<Path> next = new ArrayList<>();
                for (int i = 0; i < runs.size(); i += 2) {
                    if (i + 1 == runs.size()) {
                        next.add(runs.get(i));
                    } else {
                        Path merged = Files.createTempFile(tempDir, "merged-", ".txt");
                        mergeFiles(runs.get(i), runs.get(i + 1), merged);
                        Files.delete(runs.get(i));
                        Files.delete(runs.get(i + 1));
                        next.add(merged);
                    }
                }
                runs = next;
            }
            // The final run is fully sorted; copy it to the requested output.
            // For empty input, create an empty output file instead.
            Path output = Paths.get(outputFile);
            if (runs.isEmpty()) {
                Files.write(output, new byte[0]);
            } else {
                Files.copy(runs.get(0), output, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            // Clean up temporary files even if sorting or merging fails.
            try (java.util.stream.Stream<Path> paths = Files.list(tempDir)) {
                for (Path path : (Iterable<Path>) paths::iterator) Files.deleteIfExists(path);
            }
            Files.deleteIfExists(tempDir);
        }
    }

    // Sort one memory-sized chunk using our recursive Merge Sort,
    // write its sorted values to a temporary file, and return that path.
    private static Path writeSortedRun(int[] chunk, int count, Path dir)
            throws IOException {
        int[] values = Arrays.copyOf(chunk, count);
        recursiveMergeSort(values); // Reuse implementation 1.
        Path run = Files.createTempFile(dir, "run-", ".txt");
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(run))) {
            for (int value : values) out.println(value);
        }
        return run;
    }

    // MERGE TWO SORTED FILES
    // Keep only the next integer from each file in memory. Write the
    // smaller one, advance that file, and repeat. Copy any remainder.
    // The full files are never loaded into arrays.
    private static void mergeFiles(Path first, Path second, Path output)
            throws IOException {
        try (Scanner a = new Scanner(first.toFile());
             Scanner b = new Scanner(second.toFile());
             PrintWriter out = new PrintWriter(Files.newBufferedWriter(output))) {
            Integer x = a.hasNextInt() ? a.nextInt() : null;
            Integer y = b.hasNextInt() ? b.nextInt() : null;
            while (x != null && y != null) {
                if (x <= y) {
                    out.println(x);
                    x = a.hasNextInt() ? a.nextInt() : null;
                } else {
                    out.println(y);
                    y = b.hasNextInt() ? b.nextInt() : null;
                }
            }
            while (x != null) {
                out.println(x);
                x = a.hasNextInt() ? a.nextInt() : null;
            }
            while (y != null) {
                out.println(y);
                y = b.hasNextInt() ? b.nextInt() : null;
            }
        }
    }

    // 4. PARALLEL MERGE SORT (in memory)
    // Use two worker threads to sort separate halves concurrently.
    // Unlike external sorting, this aims for speed, not lower memory use.
    // Each worker calls our recursive Merge Sort on its own array.
    public static void parallelMergeSort(int[] a) throws InterruptedException {
        if (a.length <= 1) return;
        int mid = a.length / 2;
        int[] left = Arrays.copyOfRange(a, 0, mid);
        int[] right = Arrays.copyOfRange(a, mid, a.length);

        // new Thread(...) creates a worker with a task (the lambda).
        // Each task sorts a different array, so workers do not overwrite
        // each other's data. start() schedules both tasks to run concurrently
        // (potentially in parallel on separate CPU cores).
        Thread worker1 = new Thread(() -> recursiveMergeSort(left));
        Thread worker2 = new Thread(() -> recursiveMergeSort(right));
        worker1.start();
        worker2.start();

        // join() blocks the calling thread until each worker finishes.
        // We cannot merge until BOTH halves are sorted.
        worker1.join();
        worker2.join();
        // Combine the two sorted halves in the calling thread.
        mergeHalves(a, left, right);
    }

    // Merge two sorted arrays into the original destination array.
    private static void mergeHalves(int[] result, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            result[k++] = left[i] <= right[j] ? left[i++] : right[j++];
        }
        while (i < left.length) result[k++] = left[i++];
        while (j < right.length) result[k++] = right[j++];
    }

    // Demonstrate each implementation on the same example values.
    public static void main(String[] args) throws Exception {
        int[] original = {8, 3, 5, 1, 7, 2, 6, 4};
        int[] a = original.clone();
        recursiveMergeSort(a);
        System.out.println("Recursive: " + Arrays.toString(a));

        a = original.clone();
        iterativeMergeSort(a);
        System.out.println("Iterative: " + Arrays.toString(a));

        a = original.clone();
        parallelMergeSort(a);
        System.out.println("Parallel:  " + Arrays.toString(a));

        // Create two small example files for the external sorting demo.
        // The same method also works with files larger than available RAM.
        Files.write(Paths.get("part1.txt"), Arrays.asList("8", "3", "5", "1"));
        Files.write(Paths.get("part2.txt"), Arrays.asList("7", "2", "6", "4"));
        externalMergeSort(new String[]{"part1.txt", "part2.txt"}, "sorted.txt", 3);
        System.out.println("External:  " + Files.readAllLines(Paths.get("sorted.txt")));
    }
}
