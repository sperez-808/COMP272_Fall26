package movies;

/** Runs all the tests of MovieCatalogueTest on Solution2. You may add tests specific to Solution2 here. */
class Solution2Test extends MovieCatalogueTest {

    @Override
    protected MovieCatalogue createCatalogue() {
        return new Solution2();
    }
}
