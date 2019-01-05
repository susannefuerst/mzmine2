package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import junit.framework.TestCase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FragmentKey;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FragmentNotFoundException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.util.MathUtils;

public class FragmentsDatabaseTest extends TestCase {

    public static final MyLogger LOGGER = MyLogger.getLogger(FragmentsDatabaseTest.class);

    public void testCreateDatabase() {
        new FragmentsDatabase();
    }

    public void testGetFragment() throws FragmentNotFoundException {
        Fragment fragment = FragmentsDatabase.getFragment(FragmentKey.LAC_117);
        assertEquals(fragment.getFormula(), "C5H13OSi");
        assertEquals(fragment.metaboliteAbbreviation(), "Lac");
        assertEquals(fragment.baseMass(), 117);
        assertEquals(fragment.metaboliteName(), "Lactic acid");
    }

    public void testMassesAndFormulaFit() {
        for (Fragment fragment : FragmentsDatabase.getAllFregments()) {
            double writtenMass = (double) fragment.baseMass();
            Double calculatedMass = MathUtils.round(fragment.lowestMass(), 0);
            LOGGER.info(fragment.metaboliteName());
            LOGGER.info("calculated mass: " + calculatedMass);
            LOGGER.info("written mass: " + writtenMass);
            assertEquals(writtenMass, calculatedMass);
        }
    }

    public void testIfAllKeysAreDefined() {
        for (Fragment fragment : FragmentsDatabase.getAllFregments()) {
            LOGGER.info(fragment.metaboliteName());
            assertFalse(FragmentKey.UNKNOWN.equals(fragment.getFragmentKey()));
        }
    }

}
