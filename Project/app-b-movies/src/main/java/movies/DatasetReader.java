package movies;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Reads the MovieLens dataset of Application B. You must write the two methods below.
 *
 * <p>Dataset (CSV files): https://grouplens.org/datasets/movielens/ — use
 * "ml-latest-small" while developing and "ml-32m" for your experiments.
 * Unzip the download; you need movies.csv and ratings.csv.
 *
 * <ul>
 *   <li>movies.csv — {@code movieId,title,genres}. Example lines:
 *     <pre>
 *     1,Toy Story (1995),Adventure|Animation|Children|Comedy|Fantasy
 *     11,"American President, The (1995)",Comedy|Drama|Romance
 *     </pre>
 *     The year is at the end of the title, in parentheses. A title that contains a
 *     comma is written between double quotes. Genres are separated by '|';
 *     "(no genres listed)" means no genre.</li>
 *   <li>ratings.csv — {@code userId,movieId,rating,timestamp}. Format of a line:
 *     {@code 1,17,4.0,944249077} (user 1 gave movie 17 the rating 4.0).</li>
 * </ul>
 * The first line of each file is a header: skip it.
 *
 * <p>Hints:
 * <ul>
 *   <li>Read line by line with {@code java.nio.file.Files.newBufferedReader(file)};
 *       ratings.csv for ml-32m is close to 1 GB: never read it all as one String.</li>
 *   <li>For movies.csv, the ID is before the FIRST comma and the genres after the LAST
 *       comma: everything in between is the title (remove the quotes, if any).</li>
 *   <li>The regular expression {@code "\\((\\d{4})\\)\\s*$"} finds a year such as "(1995)"
 *       at the end of the title. Some titles have no year: use 0, and count them.</li>
 * </ul>
 */
public final class DatasetReader {

    /** One line of ratings.csv (only the fields this application needs). */
    public record Rating(int movieId, double rating) { }

    private DatasetReader() { }   // no objects: only static methods

    /** Reads every movie of movies.csv, in file order. */
    public static List<Movie> readMovies(Path moviesCsv) throws IOException {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Reads at most maxLines ratings of ratings.csv, in file order.
     *
     * <p>Memory warning: a list of all 32 million ratings of ml-32m needs well over
     * 1 GB. Use maxLines (e.g. 5,000,000) to read part of the file. To replay the
     * whole file, write a second method that calls catalogue.addRating(...) for
     * each line while reading, without storing the ratings in a list.
     */
    public static List<Rating> readRatings(Path ratingsCsv, int maxLines) throws IOException {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
