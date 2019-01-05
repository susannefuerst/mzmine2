package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import junit.framework.TestCase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Isotope;

public class IsotopeAbundancyComparatorTest extends TestCase {

    public void testCompare() {
        IsotopeAbundancyComparator comparator = new IsotopeAbundancyComparator();
        assertEquals(1, comparator.compare(Isotope.C_12, Isotope.C_13));
        assertEquals(-1, comparator.compare(Isotope.N_15, Isotope.N_14));
        assertEquals(0, comparator.compare(Isotope.P_31, Isotope.P_31));
    }

}
