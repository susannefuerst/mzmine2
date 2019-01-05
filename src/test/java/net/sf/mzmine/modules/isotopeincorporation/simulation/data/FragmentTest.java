package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.HashMap;
import java.util.InputMismatchException;

import junit.framework.TestCase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Element;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.ErrorMessage;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FragmentKey;
import net.sf.mzmine.modules.isotopeincorporation.simulation.util.MathUtils;

public class FragmentTest extends TestCase {

    public void testFragment() {
        String formula = "C5N3H14";
        String capacityFormula = "C2N";
        Fragment fragment = new Fragment(FragmentKey.UNKNOWN, formula, capacityFormula);
        HashMap<Element, Integer> expectedFormulaMap = new HashMap<>();
        expectedFormulaMap.put(Element.C, 5);
        expectedFormulaMap.put(Element.N, 3);
        expectedFormulaMap.put(Element.H, 14);
        HashMap<Element, Integer> expectedCapacity = new HashMap<>();
        expectedCapacity.put(Element.C, 2);
        expectedCapacity.put(Element.N, 1);
        assertEquals(expectedFormulaMap, fragment.getComponents());
        assertEquals(expectedCapacity, fragment.getExperimentalIncorporationCapacity());
    }

    public void testFragmentFail() {
        String formula = "C5N3(H14)2";
        String capacityFormula = "C2N";
        try {
            new Fragment(FragmentKey.UNKNOWN, formula, capacityFormula);
            fail("Should throw an exception because of the brackets in " + formula);
        } catch (Exception e) {
            assertTrue(e instanceof InputMismatchException);
            assertTrue(e.getMessage().contains(ErrorMessage.INVALID_FORMULA.getMessage()));
        }
    }

    public void testRelativeMass() {
        Fragment fragment = new Fragment(FragmentKey.UNKNOWN, "C5NH12", "CN");
        assertEquals(86.1565, fragment.relativeMass());
    }

    public void testLowestMass() {
        Fragment fragment = new Fragment(FragmentKey.UNKNOWN, "C5NH12", "CN");
        assertEquals(86.096974, fragment.lowestMass());
    }

    public void testHighestMass() {
        Fragment fragment = new Fragment(FragmentKey.UNKNOWN, "C5NH12", "CN");
        double roundedMass = (double) (MathUtils.round(fragment.highestMass(), 6));
        assertEquals(104.186108, roundedMass);
    }

    public void testlowestFullIncorporatedMass() {
        Fragment fragment = new Fragment(FragmentKey.UNKNOWN, "C5NH12", "CN");
        assertEquals(88.097364, fragment.lowestFullIncorporatedMass());
    }

    public void testChangeCapacity() {
        String formula = "C5N3H14";
        String capacityFormula = "C2N";
        Fragment fragment = new Fragment(FragmentKey.UNKNOWN, formula, capacityFormula);
        HashMap<Element, Integer> expectedCapacity = new HashMap<>();
        expectedCapacity.put(Element.C, 2);
        expectedCapacity.put(Element.N, 1);
        assertEquals(expectedCapacity, fragment.getExperimentalIncorporationCapacity());
        fragment.changeCapacity("CH");
        expectedCapacity.clear();
        expectedCapacity.put(Element.C, 1);
        expectedCapacity.put(Element.H, 1);
        assertEquals(expectedCapacity, fragment.getExperimentalIncorporationCapacity());
    }

}
