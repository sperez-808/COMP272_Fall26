package cache;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Reads the Wikimedia pageview dataset of Application C and builds the request stream.
 * You must write the two methods below.
 *
 * <p>Dataset (text files): https://dumps.wikimedia.org/other/pageviews/ — choose a year,
 * a month, and download one or more hourly files, e.g. {@code pageviews-20250901-120000.gz}.
 * Each file is plain text compressed with gzip: either decompress it first (7-Zip on
 * Windows, double-click on macOS, {@code gunzip} on Linux), or read it directly in Java
 * with {@code new GZIPInputStream(Files.newInputStream(file))}.
 *
 * <p>Each line has four fields separated by spaces:
 * <pre>
 *     domain_code page_title count_views total_response_size
 * </pre>
 * Example of the format: {@code en Main_Page 250000 0}. The domain code {@code en} is the
 * English Wikipedia (desktop) and {@code en.m} the English Wikipedia (mobile).
 *
 * <p>Hints:
 * <ul>
 *   <li>Read line by line with a {@code BufferedReader}; {@code line.split(" ")}.</li>
 *   <li>A few lines may be malformed: skip them, and count how many you skipped.</li>
 * </ul>
 */
public final class DatasetReader {

    /** One line of a pageview file (only the fields this application needs). */
    public record PageCount(String page, int views) { }

    private DatasetReader() { }   // no objects: only static methods

    /**
     * Reads at most maxLines lines of a pageview file and keeps only the English
     * Wikipedia lines (domain code "en" or "en.m"), in file order.
     * Use maxLines = Integer.MAX_VALUE to read the whole file.
     */
    public static List<PageCount> readPageCounts(Path file, int maxLines) throws IOException {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Builds the request stream: each PageCount(page, views) becomes "views" requests
     * for that page, and all requests are then shuffled with new Random(seed).
     *
     * <p>Hints: count the total number of requests first and create a String array of
     * that size; put the SAME String object in it several times (Java copies only the
     * reference, 4 or 8 bytes, not the text). Then shuffle the array with the
     * Fisher-Yates algorithm: for i from the end down to 1, swap element i with
     * element random.nextInt(i + 1).
     */
    public static String[] buildRequests(List<PageCount> counts, long seed) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
