package movies;

import java.util.List;

/**
 * Solution 2 for the movie catalogue.
 *
 * <p>TODO: Replace this comment with a short description of your solution:
 * <ul>
 *   <li>the data structure(s) used (e.g. "a Red-Black tree by ID, plus a sorted array list by year");</li>
 *   <li>where the code comes from: written by your team, adapted from course code,
 *       or a library class (e.g. java.util.TreeMap). Cite the source;</li>
 *   <li>the expected time complexity of each method below.</li>
 * </ul>
 * The full explanation of the implementation and of its expected performance
 * goes in your report.
 *
 * <p>You may rename this class to something more descriptive (e.g. TreeCatalogue).
 * If you do, also rename it in Solution2Test.
 */
public class Solution2 implements MovieCatalogue {

    // TODO: declare the fields of your data structure(s) here.

    public Solution2() {
        // TODO: initialize your data structure(s).
    }

    @Override
    public void insertMovie(Movie movie) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Movie getById(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean addRating(int movieId, double rating) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int ratingCount(int movieId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public double averageRating(int movieId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Movie> moviesInYears(int from, int to) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Movie> topRated(int from, int to, int k, int minRatings) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
