package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import junit.framework.TestCase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Isotope;

public class IsotopeMassComparatorTest extends TestCase {

    public void testCompare() {
        assertEquals(-1, new IsotopeMassComparator().compare(Isotope.H_1, Isotope.H_2));
        assertEquals(0, new IsotopeMassComparator().compare(Isotope.C_12, Isotope.C_12));
        assertEquals(1, new IsotopeMassComparator().compare(Isotope.N_15, Isotope.N_14));
    }

}
