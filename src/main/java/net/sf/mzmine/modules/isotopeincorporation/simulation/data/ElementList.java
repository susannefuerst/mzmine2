package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Element;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.ErrorMessage;

@SuppressWarnings("serial")
public class ElementList extends ArrayList<Element> {
	
	public ElementList() {
		
	}
	
	public ElementList(Element... elements) {
		for (Element element : elements) {
			this.add(element);
		}
	}

	public static ElementList fromFormula(String fragmentFormula) {
		ElementList elements = new ElementList();
		if (fragmentFormula.contains("(")) {
			throw new InputMismatchException(ErrorMessage.INVALID_FORMULA.getMessage() + "[" + fragmentFormula + "]");
		}
		Matcher formulaMatcher = Pattern.compile(Fragment.FORMULA_REG_EX).matcher(fragmentFormula);
		ArrayList<String> elementTokens = new ArrayList<String>();
		while (formulaMatcher.find()) {
			elementTokens.add(formulaMatcher.group());
		}
		for (String elementToken : elementTokens) {
			Matcher elementMatcher = Pattern.compile(Fragment.FORMULA_REG_EX).matcher(elementToken);
			if (elementMatcher.matches()) {
				Element element = Element.valueOf(elementMatcher.group(1));
				elements.add(element);
			}
		}
		return elements;
	}

}
