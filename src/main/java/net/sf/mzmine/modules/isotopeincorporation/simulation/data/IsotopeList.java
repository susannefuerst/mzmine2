package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.ArrayList;
import java.util.Map.Entry;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Isotope;
/**
 * A list of Isotopes
 * @author sfuerst
 *
 */
@SuppressWarnings("serial")
public class IsotopeList extends ArrayList<Isotope> {
	
	public IsotopeList() {
	}
	
	public IsotopeList(Isotope...isotopes ) {
		for(int i = 0; i < isotopes.length; i++) {
			this.add(isotopes[i]);
		}
	}
	
	public IsotopeList(IsotopeList...isotopeLists) {
		for (IsotopeList isotopeList : isotopeLists) {
			this.addAll(isotopeList);
		}
	}

	/**
	 * Converts as list containing C_13, C_13, N_15, H_1 to the following string : C_13|C_13|N_15|H_1
	 */
	@Override
	public String toString() {
		String inBrackets = super.toString().replaceAll(",", LIST_SEPARATOR).replaceAll(" ", "");
		return inBrackets.substring(1, inBrackets.length() - 1);
	}
	
	private static final String LIST_SEPARATOR = "\\|";
	
	/**
	 * Creates an IsotopeList from a string of isotope representations separated by "|"
	 * @param isotopesListString, e.g C_13|H_1|N_15
	 * @return An IsotopeList corresponding to the string representation
	 */
	public static IsotopeList fromString(String isotopesListString) {
		String[] isotopeNames = isotopesListString.split(LIST_SEPARATOR);
		IsotopeList isotopeList = new IsotopeList();
		for (int index = 0; index < isotopeNames.length; index++) {
			isotopeList.add(Isotope.byName(isotopeNames[index]));
		}
		return isotopeList;
	}
	
	/**
	 * 
	 * @return a map of the isotopes in this list to their abundance in this list
	 */
	public IsotopeFormula toIsotopeFormula() {
		IsotopeFormula isotopeCount = new IsotopeFormula();
		for (Isotope isotope : this ) {
			if (isotopeCount.get(isotope) == null) {
				isotopeCount.put(isotope, 1);
			} else {
				isotopeCount.put(isotope, isotopeCount.get(isotope) + 1);
			}
		}
		return isotopeCount;
	}
	 /**
	  * 
	  * @return a string representation of isotope counts in this list. E.g.
	  * C_13: 2
	  *  H_2: 3
	  * N_15: 3
	  * O_17: 1
	  */
	public String toVerticalCountString() {
		StringBuilder strBuilder = new StringBuilder();
		IsotopeFormula isotopeFormula = this.toIsotopeFormula();
		for (Entry<Isotope, Integer> entry : isotopeFormula.entrySet()) {
			String key = String.format("%5s: ", entry.getKey());
			strBuilder.append(key + entry.getValue() + "\n");
		}
		return strBuilder.toString();
	}
	
	 /**
	  * 
	  * @return a comma separated representation of isotope counts in this list. E.g.
	  * C_13: 2, H_2: 3, N_15: 3, O_17: 1
	  */
	public String toCommaSeparatedCountString() {
		StringBuilder strBuilder = new StringBuilder();
		IsotopeFormula isotopeFormula = this.toIsotopeFormula();
		for (Entry<Isotope, Integer> entry : isotopeFormula.entrySet()) {
			strBuilder.append(entry.getKey() + ": " + entry.getValue() + ", ");
		}
		String withCommaAtTheEnd = strBuilder.toString();
		return strBuilder.toString().substring(0, withCommaAtTheEnd.length() - 2);
	}
	
}
