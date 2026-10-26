package movies;

import java.util.List;

/**
 * One movie of the catalogue. A "record" is a small class that only holds data:
 * Java creates the constructor, the accessors ({@code movie.id()}, {@code movie.title()}, ...),
 * equals, hashCode and toString for you.
 *
 * @param id     the MovieLens movie ID
 * @param title  the title, without the year (e.g. "Toy Story")
 * @param year   the release year, or 0 if the dataset does not give one
 * @param genres the genres (e.g. ["Adventure", "Animation"]); may be empty
 */
public record Movie(int id, String title, int year, List<String> genres) { }
