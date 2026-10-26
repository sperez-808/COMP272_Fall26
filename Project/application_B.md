# COMP 272/400C — Term Project

## Application B — Movie Catalogue Search Backend

This sheet describes one of the three proposed applications. Read it together with the main project handout, which describes the five stages, the deliverables, and the grading.

**Starter code:** project `app-b-movies` — interface `MovieCatalogue`, record `Movie`, skeletons `Solution1` and `Solution2`, `DatasetReader`, and the tests `MovieCatalogueTest`.

---

### Scenario

A movie website needs an in-memory index over its catalogue and its users' ratings. Users:

- open a movie's page (lookup by ID),
- browse movies released in a range of years, and
- ask for the best-rated movies of a period.

New movies and new ratings arrive continually, and the index must stay up to date.

### Dataset

| Source | Content | Format |
|---|---|---|
| [MovieLens](https://grouplens.org/datasets/movielens/) by GroupLens (University of Minnesota). Use **ml-latest-small** while developing, then **ml-32m** for the experiments. | `movies.csv`: about 87,000 movies (ID, title with year, genres). `ratings.csv`: about 32 million ratings (user ID, movie ID, rating, timestamp). | CSV |
The dataset is free for educational use. Read and respect its license and citation requirements (in the README of each download), and cite it in your report.

**Tips:**

- The year is inside the title, e.g. `Toy Story (1995)`. Extract it with a regular expression. Some titles have no year: decide how to handle them and **report how many** there are.
- Some titles contain commas inside quotes. Use a CSV parser that respects quotes, or skip malformed lines and report how many you skipped.
- `ratings.csv` is large (close to 1 GB). Read it line by line with a `BufferedReader`; never load the whole file into memory as text.

### Functional requirements

The interface `MovieCatalogue` in the starter code is the exact specification; this table summarizes it. Reading the files is done by `DatasetReader`.

| Operation | Description |
|---|---|
| `insertMovie(movie)` | Add a movie, or replace the movie with the same ID (its ratings are kept). |
| `getById(id)` | Return the movie with this ID, or `null`. |
| `addRating(movieId, rating)` | Record one rating; return `false` if the movie does not exist. |
| `ratingCount(movieId)`, `averageRating(movieId)` | The movie's number of ratings and average rating (0 if none). |
| `moviesInYears(from, to)` | Return all movies released between `from` and `to` (inclusive), sorted by year, then by ID. |
| `topRated(from, to, k, minRatings)` | Return the `k` movies with the best average rating among those released between `from` and `to` that have at least `minRatings` ratings (ties by ID). |
| `size()` | Number of movies. |

**Watch out:** many movies share the same year. A structure keyed by year must handle duplicate keys, e.g. a list of movies per year, or the pair (year, ID) as the key.

### Non-functional requirements (targets)

Measured with the full **ml-32m** dataset, on your machine, with at most **4 GB** of heap (`-Xmx4g`).

| ID | Requirement | Target | Rationale |
|---|---|---|---|
| B-N1 | Average time of `getById` | ≤ 1 µs | Every page view needs it, possibly several times (related movies). |
| B-N2 | Average time of `moviesInYears` for a 10-year range | ≤ 10 ms | Interactive browsing page; must feel instantaneous (≪ 100 ms). |
| B-N3 | Average time of `topRated(from, to, 10, 50)` for a 10-year range | ≤ 50 ms | Interactive "best of the decade" page. |
| B-N4 | Throughput of `addRating` | ≥ 1,000,000 ratings per second | Replaying all 32 million ratings at start-up must take under a minute. |
| B-N5 | Time to load all movies | ≤ 1 s | Restart time after a crash. |
| B-N6 | Memory of the index (excluding title and genre strings) | ≤ 150 bytes per movie | Leaves room for other indexes on the same server. |

You may refine these targets or add your own; justify each change in your proposal.

### Workloads to test

- **Bulk load:** insert all movies (they are sorted by ID in the file), then replay all ratings.
- **Point lookups:** random IDs (fixed seed), and IDs in increasing order.
- **Range queries:** narrow (1 year), medium (10 years), wide (50 years).
- **Mixed workloads:** e.g. 90% lookups / 10% inserts, and 50% / 50%.
- **Sizes:** the first 10³, 10⁴, 10⁵ movies, and all of them; for ratings, 10⁵, 10⁶, 10⁷, and all of them.

### Suggested solution pairs

Each solution may come from your team, from class, from a library, or from another source, as long as you explain its implementation and expected performance (see Section 4.3 of the main handout).

| Solution 1 | Solution 2 | Main trade-off to investigate |
|---|---|---|
| Hash table (by ID) | Red-Black tree (by ID) | O(1) average lookup vs. ordered traversal |
| Hash table (by ID) + sorted array list (by year) | Hash table (by ID) + Red-Black tree (by year) | Secondary index: cost of keeping it sorted under insertions |
| AVL tree | Red-Black tree | Rotations per insert vs. lookup depth |
| Red-Black tree | Skip list *(not covered)* | Deterministic vs. randomized balance |
| Sort all candidates for `topRated` | Binary heap of size k | O(m log m) sorting vs. O(m log k) selection, for m candidates |

### Questions your analysis should answer

- Which operations does each design make fast, and which does it make slow? Is there a design that is good at **all** of them?
- How does the time of `moviesInYears` grow with the size of the **result**, not just with the size of the catalogue?
- The file is sorted by movie ID. What happens to each design when movies are inserted in increasing order?
- Which requirement is the hardest to meet, and what would you change in your design to meet it?
