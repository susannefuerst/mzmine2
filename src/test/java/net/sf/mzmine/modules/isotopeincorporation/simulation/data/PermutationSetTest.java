package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.ArrayList;

import junit.framework.TestCase;

public class PermutationSetTest extends TestCase {

    public static final MyLogger LOG = MyLogger.getLogger(PermutationSetTest.class);

    private void log(ArrayList<PermutationSet> allIsotopeCombinations1) {
        for (PermutationSet permutations : allIsotopeCombinations1) {
            LOG.info(permutations);
        }
    }

    public void testAllIsotopeCombinations1() {
        ArrayList<PermutationSet> allIsotopeCombinations = PermutationSet.allIsotopeCombinations(3, 1);
        ArrayList<PermutationSet> expected = new ArrayList<>();
        expected.add(new PermutationSet(new Partition(0, 1, 0), new Partition(1, 0, 0), new Partition(0, 0, 1)));
        LOG.info("allIsotopeCombinations1");
        log(allIsotopeCombinations);
        assertEquals(expected, allIsotopeCombinations);
    }

    public void testAllIsotopeCombinations2() {
        ArrayList<PermutationSet> allIsotopeCombinations = PermutationSet.allIsotopeCombinations(3, 2);
        ArrayList<PermutationSet> expected = new ArrayList<>();
        expected.add(new PermutationSet(new Partition(0,1,1), new Partition(1,0,1), new Partition(1,1,0)));
        expected.add(new PermutationSet(new Partition(0,2,0), new Partition(2,0,0), new Partition(0,0,2)));
        LOG.info("allIsotopeCombinations2");
        log(allIsotopeCombinations);
        assertEquals(expected, allIsotopeCombinations);
    }

    public void testAllIsotopeCombinations3() {
        ArrayList<PermutationSet> allIsotopeCombinations = PermutationSet.allIsotopeCombinations(3, 3);
        ArrayList<PermutationSet> expected = new ArrayList<>();
        expected.add(new PermutationSet(new Partition(1,1,1)));
        expected.add(new PermutationSet(new Partition(0,2,1), new Partition(2, 0, 1), new Partition(2, 1, 0), new Partition(0, 1, 2), new Partition(1, 0, 2), new Partition(1, 2, 0)));
        expected.add(new PermutationSet(new Partition(0, 3, 0), new Partition(3, 0, 0), new Partition(0, 0, 3)));
        LOG.info("allIsotopeCombinations3");
        log(allIsotopeCombinations);
        assertEquals(expected, allIsotopeCombinations);
    }

    public void testAllIsotopeCombinations4() {
        ArrayList<PermutationSet> allIsotopeCombinations = PermutationSet.allIsotopeCombinations(3, 4);
        ArrayList<PermutationSet> expected = new ArrayList<>();
        expected.add(new PermutationSet(new Partition(1, 2, 1), new Partition(2, 1, 1), new Partition(1, 1, 2)));
        expected.add(new PermutationSet(new Partition(0, 3, 1), new Partition(3, 0, 1), new Partition(3, 1, 0), new Partition(0, 1, 3), new Partition(1, 0, 3), new Partition(1, 3, 0)));
        expected.add(new PermutationSet(new Partition(0, 2, 2), new Partition(2, 0, 2), new Partition(2, 2, 0)));
        expected.add(new PermutationSet(new Partition(0, 4, 0), new Partition(4, 0, 0), new Partition(0, 0, 4)));
        LOG.info("allIsotopeCombinations4");
        log(allIsotopeCombinations);
        assertEquals(expected, allIsotopeCombinations);
    }

    public void testAllIsotopeCombinations5() {
        ArrayList<PermutationSet> allIsotopeCombinations = PermutationSet.allIsotopeCombinations(3, 5);
        ArrayList<PermutationSet> expected = new ArrayList<>();
        expected.add(new PermutationSet(new Partition(1, 3, 1), new Partition(3, 1, 1), new Partition(1, 1, 3)));
        expected.add(new PermutationSet(new Partition(1, 2, 2), new Partition(2, 1, 2), new Partition(2, 2, 1)));
        expected.add(new PermutationSet(new Partition(0, 4, 1), new Partition(4, 0, 1), new Partition(4, 1, 0), new Partition(0, 1, 4), new Partition(1, 0, 4), new Partition(1, 4, 0)));
        expected.add(new PermutationSet(new Partition(0, 3, 2), new Partition(3, 0, 2), new Partition(3, 2, 0), new Partition(0, 2, 3), new Partition(2, 0, 3), new Partition(2, 3, 0)));
        expected.add(new PermutationSet(new Partition(0, 5, 0), new Partition(5, 0, 0), new Partition(0, 0, 5)));
        LOG.info("allIsotopeCombinations5");
        log(allIsotopeCombinations);
        assertEquals(expected, allIsotopeCombinations);
    }

}
