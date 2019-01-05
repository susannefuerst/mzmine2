package net.sf.mzmine.modules.isotopeincorporation.simulation.util;

import junit.framework.TestCase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IsotopeFormula;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IsotopeList;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MyLogger;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Element;

public class MathUtilsTest extends TestCase {
	
	public static final MyLogger LOG = MyLogger.getLogger(MathUtilsTest.class);
	
	/*
	 * test the MathUtils.naturalAbundance method for isotopologues consisting of isotopes that correspond all to the same kind of element,
	 * where only elements with two existing isotopes are considered
	 */
	public void testNaturalAbundance() {
		int totalElements = 10;
		for (Element element : Element.values()) {
			IsotopeList isotopes = element.getIsotopes();
			double firstIsotopeAbundance = isotopes.get(0).getAbundance();
			if (isotopes.size() == 2) {
				for (int firstIsotopeNumber = 0; firstIsotopeNumber <= totalElements; firstIsotopeNumber++) {
					IsotopeFormula isotopologue = new IsotopeFormula();
					isotopologue.put(isotopes.get(0), firstIsotopeNumber);
					isotopologue.put(isotopes.get(1), totalElements - firstIsotopeNumber);
					Double expectedAbundance = MathUtils.binom(totalElements, firstIsotopeNumber)
							* Math.pow(1 - firstIsotopeAbundance, totalElements - firstIsotopeNumber)
							* Math.pow(firstIsotopeAbundance, firstIsotopeNumber);
					LOG.infoValue("expectedAbundance", expectedAbundance);
					Double actualAbundance = MathUtils.naturalAbundance(isotopologue);
					LOG.infoValue("actualAbundance", actualAbundance);
					assertEquals(MathUtils.round(expectedAbundance, 6), MathUtils.round(actualAbundance, 6));
				}
			}
		}
	}

}
