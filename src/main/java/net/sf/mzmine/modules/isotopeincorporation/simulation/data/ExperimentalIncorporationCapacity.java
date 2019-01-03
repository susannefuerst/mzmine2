package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.HashMap;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Element;
/**
 * This class is used to define how many elemnts of a certain type may be replaced by tracers from an isotope labeling
 * experiment. Each element is mapped to this number.
 * @author sfuerst
 *
 */
@SuppressWarnings("serial")
public class ExperimentalIncorporationCapacity extends HashMap<Element, Integer> {

}
