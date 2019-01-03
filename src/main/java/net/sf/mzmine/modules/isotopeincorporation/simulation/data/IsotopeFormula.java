package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Element;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Isotope;
import net.sf.mzmine.modules.isotopeincorporation.simulation.util.StringUtils;

/**
 * A map of each isotope in a molecule to its total number in the molecule.
 * @author sfuerst
 *
 */
@SuppressWarnings("serial")
public class IsotopeFormula extends LinkedHashMap<Isotope, Integer> {
	
	/**
	 * An {@link IsotopeFormula} with string representation {C_13=2, C_12=3} will be converted to an {@link ElementFormula} with string representation
	 * {C=5}
	 * @return An {@link ElementFormula} representation of this {@link IsotopeFormula}
	 */
	public ElementFormula toElementFormula() {
		ElementFormula elementFormula = new ElementFormula();
		for (Entry<Isotope, Integer> entry : this.entrySet()) {
			Element element = entry.getKey().getElement();
			Integer value = entry.getValue();
			if (elementFormula.get(element) == null) {
				elementFormula.put(element, value);
			} else {
				Integer oldValue = elementFormula.get(element);
				elementFormula.put(element, oldValue + value);
			}
		}
		return elementFormula;
	}
	
	public List<Entry<Isotope, Integer>> toEntryList() {
		List<Entry<Isotope, Integer>> entryList = new ArrayList<>(this.entrySet());
		return entryList;
	}
	
	/**
	 * Collects only the keys (isotopes) of this {@link IsotopeFormula} in an {@link IsotopeList}
	 * @return An {@link IsotopeList} that contains all the keys (isotopes) of this {@link IsotopeFormula}.
	 */
	public IsotopeList toIsotopeList() {
		IsotopeList isotopeList = new IsotopeList();
		for (Entry<Isotope, Integer> entry : this.entrySet()) {
			isotopeList.add(entry.getKey());
		}
		return isotopeList;
	}

	public boolean mattersForCorrectionOf(IsotopeFormula currentCorrectionIndex) {
		boolean allSameCounts = true;
		for (Entry<Isotope, Integer> thisEntry : this.entrySet()) {
			Isotope thisIsotope = thisEntry.getKey();
			Integer thisCount = thisEntry.getValue();
			if (thisCount > currentCorrectionIndex.get(thisIsotope)) {
				return false;
			}
			if (thisCount < currentCorrectionIndex.get(thisIsotope)) {
				allSameCounts = false;
			}
		}
		return !allSameCounts;
	}

	/**
	 * 
	 * @return a string representation using sub- and superscript.
	 */
	public String toNiceFormattedFormula() {
		StringBuffer buffer = new StringBuffer();
		for (Entry<Isotope, Integer> entry : this.entrySet()) {
			Integer countValue = entry.getValue();
			Isotope isotope = entry.getKey();
			if (countValue == 1) {
				buffer.append(isotope.toNiceFormattedString());
			} else {
				buffer.append("(" + isotope.toNiceFormattedString() + ")" + StringUtils.subscript(String.valueOf(countValue)));
			}
		}
		return buffer.toString();
	}

}
