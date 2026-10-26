package movies;

/** Runs all the tests of MovieCatalogueTest on Solution1. You may add tests specific to Solution1 here. */
class Solution1Test extends MovieCatalogueTest {

    @Override
    protected MovieCatalogue createCatalogue() {
        return new Solution1();
    }
}
