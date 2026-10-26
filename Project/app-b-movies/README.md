# COMP 272/400C — Term Project — Starter Code

## Application B — Movie Catalogue Search Backend

Read this file, the main project handout, and the application sheet before you start.

### What is in this folder

| File | What it is |
|---|---|
| `src/main/java/movies/MovieCatalogue.java` | The interface: the **functional requirements** of the application. Do not change it. |
| `Movie.java` | The movie record used by the interface. Do not change it. |
| `src/main/java/movies/Solution1.java`, `Solution2.java` | Your two solutions. Every method currently throws "Not implemented yet". |
| `src/main/java/movies/DatasetReader.java` | Reads the dataset. You write readMovies and readRatings. |
| `src/test/java/movies/MovieCatalogueTest.java` | The tests shared by both solutions: a **starting point**. You must add more. |
| `src/test/java/movies/Solution1Test.java`, `Solution2Test.java` | Run the shared tests on each solution. |
| `data/` | Put the dataset files here. Do **not** submit them. |
| `pom.xml` | The build file (Maven). It downloads JUnit for you. Do not edit it. |

### Opening the project

You need Java 17 or later.

- **IntelliJ IDEA:** *File → Open*, select this folder (the one that contains `pom.xml`), and accept "Open as Project" / "Trust Project". Wait until the dependencies are downloaded.
- **Eclipse:** *File → Import → Maven → Existing Maven Projects*, and select this folder.
- **VS Code:** install the "Extension Pack for Java", then *File → Open Folder* and select this folder.

### Running the tests

- **In the IDE:** right-click `src/test/java` → *Run 'All Tests'* (IntelliJ) or *Run As → JUnit Test* (Eclipse), or use the Testing panel (VS Code).
- **On the command line** (if Maven is installed): `mvn test`.

At the start, **every test fails** with "Not implemented yet". This is normal. The tests marked `@Disabled` are skipped until you enable them (see below).

The tests read files from the `data` folder with relative paths such as `data/...`. Run them from this folder (the IDEs do this by default).

### What you must do

1. **Download the dataset** into `data/`:
- MovieLens from https://grouplens.org/datasets/movielens/ (CSV files): **ml-latest-small** while developing,
  **ml-32m** for your experiments. Unzip; you need `movies.csv` and `ratings.csv`.
2. **Write `DatasetReader`** (readMovies and readRatings).
3. **Implement `Solution1` and `Solution2`**. Each may use code written by your team, code from class, a library class (e.g. from `java.util`), or another source. Whatever the source, you must:
   - cite it, in the class comment and in your report;
   - explain in your report how it is implemented (data structures, implementation choices, what it uses internally) and its **expected performance** (time complexity of each method, memory).
   You may rename the two classes (e.g. `HashDictionary`); rename them in the test classes too.
4. **Extend the tests:**
   - Remove the `@Disabled` line of the dataset test once `DatasetReader` works and `movies.csv` of **ml-latest-small** (small enough for tests) is in `data/`.
   - Add **tests from the dataset**: tests whose expected values you read in the dataset files yourselves (see the TODO list at the end of `MovieCatalogueTest`).
   - Add tests for edge cases and for the special cases of your own implementations.
5. Make sure **all tests pass on both solutions** before you start measuring performance (Stage 4).

### Rules

- Do not change the interface or the signatures of the `DatasetReader` methods, and do not delete or weaken the provided tests. You may **add** methods, classes, and tests.
- `java.util` classes used inside the tests (e.g. `TreeMap`, `LinkedHashMap`) are only **references** to check your answers.
