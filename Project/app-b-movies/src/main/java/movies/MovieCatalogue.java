package movies;

import java.util.List;

/**
 * The in-memory movie index of Application B (movie catalogue search backend).
 *
 * <p>This interface IS the functional requirements specification. Both of your
 * solutions ({@link Solution1} and {@link Solution2}) must implement it exactly
 * as documented here. Do not change the method names or signatures.
 *
 * <p>Design hint: many movies share the same release year. A structure keyed by
 * year must handle duplicate keys, for example by storing a list of movies per
 * year, or by using the pair (year, id) as the key.
 */
public interface MovieCatalogue {

    /**
     * Adds a movie. If a movie with the same ID is already present, its information
     * is replaced by the new one; its ratings are kept.
     */
    void insertMovie(Movie movie);

    /** Returns the movie with this ID, or null if there is none. */
    Movie getById(int id);

    /**
     * Records one rating for a movie.
     *
     * @param rating a rating between 0.5 and 5.0
     * @return true if the movie exists; false if it does not (the rating is then ignored)
     */
    boolean addRating(int movieId, double rating);

    /** Returns the number of ratings of the movie, or 0 if it has none or does not exist. */
    int ratingCount(int movieId);

    /** Returns the average rating of the movie, or 0.0 if it has no ratings or does not exist. */
    double averageRating(int movieId);

    /**
     * Returns all movies released between the years from and to, both included.
     *
     * <ul>
     *   <li>The result is sorted by year, then by ID (both increasing).</li>
     *   <li>If from is greater than to, the result is an empty list.</li>
     * </ul>
     *
     * @return a new list; never null
     */
    List<Movie> moviesInYears(int from, int to);

    /**
     * Returns the k best-rated movies released between the years from and to
     * (both included), among the movies that have at least minRatings ratings
     * and at least one rating.
     *
     * <ul>
     *   <li>The result is sorted by average rating, highest first.</li>
     *   <li>Movies with the same average are sorted by ID, smallest first.</li>
     *   <li>If fewer than k movies qualify, all of them are returned.</li>
     *   <li>If k is 0 or less, the result is an empty list.</li>
     * </ul>
     *
     * @return a new list; never null
     */
    List<Movie> topRated(int from, int to, int k, int minRatings);

    /** Returns the number of movies in the catalogue. */
    int size();
}
