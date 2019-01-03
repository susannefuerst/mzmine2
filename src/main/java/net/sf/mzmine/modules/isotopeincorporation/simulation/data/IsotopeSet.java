package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Element;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FrequencyType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.IncorporationType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Isotope;

/**
 * An IsotopeSet represents a number of fragments with a natural or experimental isotope distribution.
 * All fragments are viewed as a collection of isotopes mapped to their total number in the set.
 * So we (nearly) forget about the fragments and just see all the atoms.
 * 
 * @author sfuerst
 *
 */
@SuppressWarnings("serial")
public class IsotopeSet extends HashMap<Isotope, Integer> {
	
	public static final MyLogger LOGGER = MyLogger.getLogger(IsotopeSet.class);
	
	private Fragment fragmentAssociatedWithTheSet;
	private double numberOfFragmentsInTheSet;
	private IncorporationType incorporationType;
	
	public IsotopeSet() {
		
	}
	
	/**
	 * All fragments are viewed as a collection of isotopes mapped to their total number in the set.
	 * The experimental incorporation will be simulated using the capacity from the fragment.
	 * 
	 * @param fragment: The fragment associated with this set.
	 * @param numberOfFragmentsInTheSet: Number of associated fragments, that can be composed by the isotopes in this set.
	 * @param incorporationType: Simulate an experimental or natural isotope incorporation.
	 */
	public IsotopeSet(Fragment fragment, double numberOfFragmentsInTheSet, IncorporationType incorporationType) {
		this.fragmentAssociatedWithTheSet = fragment;
		this.numberOfFragmentsInTheSet = numberOfFragmentsInTheSet;
		this.incorporationType = incorporationType;
		ElementFormula components = fragment.getComponents();
		ExperimentalIncorporationCapacity capacity = fragment.getExperimentalIncorporationCapacity();
		Set<Entry<Element, Integer>> componentEntries = components.entrySet();
		for (Entry<Element, Integer> componentEntry : componentEntries) {
			Element element = componentEntry.getKey();
			Integer numberOfElementsPerFragment = componentEntry.getValue();
			double totalElementNumberInTheSet = numberOfFragmentsInTheSet * numberOfElementsPerFragment;
			double incorporationCapacityForOneFragment = capacity.get(element) != null ? capacity.get(element) : 0;
			double totalIncorporationCapacityInTheSet = incorporationCapacityForOneFragment * numberOfFragmentsInTheSet;
			ArrayList<Isotope> existingIsotopes = element.getIsotopes();
			for (Isotope isotope : existingIsotopes) {
				double isotopeAbundance = isotope.getAbundance();
				double numberOfIsotopesInTheSet = 0;
				if (incorporationType.equals(IncorporationType.NATURAL)) {
					numberOfIsotopesInTheSet = totalElementNumberInTheSet * isotopeAbundance;	
				} else if (incorporationType.equals(IncorporationType.EXPERIMENTAL) && isotope.isTracer()) {
					numberOfIsotopesInTheSet = totalIncorporationCapacityInTheSet + (numberOfElementsPerFragment - incorporationCapacityForOneFragment) * numberOfFragmentsInTheSet * isotopeAbundance;
				} else {
					numberOfIsotopesInTheSet = (numberOfElementsPerFragment - incorporationCapacityForOneFragment) * numberOfFragmentsInTheSet * isotopeAbundance;
				}
				int roundedNumberOfIsotopes = (int) Math.round(numberOfIsotopesInTheSet);
				put(isotope, roundedNumberOfIsotopes);
			}
		}
	}
	
	/**
	 * If we imagine the collection of associated fragments, that can be composed by
	 * the isotopes of this set, this method returns all detectable fragment masses
	 * and their frequency as number of fragments with this mass.
	 * 
	 * The method randomly selects isotopes (weighted by abundance) from this set to
	 * recompose the fragment and measures its weight.
	 * 
	 * @return fragment masses and their frequency
	 */
	public MassSpectrum simulateSpectrum(int charge) {
		ElementFormula fragmentComponents = fragmentAssociatedWithTheSet.getComponents();
		MassSpectrum spectrum = new MassSpectrum(FrequencyType.ABSOLUTE);
		for (int i = 1; i <= numberOfFragmentsInTheSet; i++) {
			double massOfFragment = 0.0;
			for (Entry<Element, Integer> componentEntry : fragmentComponents.entrySet()) {
				// select randomly isotopes of the current element from this set to compose the fragment
				Element elementInFragment = componentEntry.getKey();
				ArrayList<Isotope> existingIsotopes = elementInFragment.getIsotopes();
				ExperimentalIncorporationCapacity capacity = fragmentAssociatedWithTheSet.getExperimentalIncorporationCapacity();
				Integer numberOfIsotopicallyVariableElementsInFragment = componentEntry.getValue();
				Integer numberOfExperimentallyIncorporatedElements = capacity.get(elementInFragment) == null ? 0 : capacity.get(elementInFragment);
				if (this.incorporationType.equals(IncorporationType.EXPERIMENTAL) && numberOfExperimentallyIncorporatedElements > 0) {
					numberOfIsotopicallyVariableElementsInFragment = numberOfIsotopicallyVariableElementsInFragment - numberOfExperimentallyIncorporatedElements;
					Isotope heaviestIsotope = elementInFragment.heaviestIsotope();
					Integer remainingNumberOfIsotopesInTheSet = Integer.valueOf(get(heaviestIsotope) - numberOfExperimentallyIncorporatedElements);
					put(heaviestIsotope,remainingNumberOfIsotopesInTheSet);
					massOfFragment = massOfFragment + heaviestIsotope.getAtomicMass() * numberOfExperimentallyIncorporatedElements;
				}
				
				// check if there are isotopes that are no more available in this set, so we cannot choose them to 
				// compose our fragment
				ArrayList<Integer> indicesToRemove = new ArrayList<Integer>();
				for (Isotope isotope : existingIsotopes) {
					if (this.get(isotope) < 1) {
						indicesToRemove.add(Integer.valueOf(existingIsotopes.indexOf(isotope)));
					}
				}
				@SuppressWarnings("unchecked")
				ArrayList<Isotope> availableIsotopes = (ArrayList<Isotope>) existingIsotopes.clone();
				for (Integer index : indicesToRemove) {
					availableIsotopes.remove(existingIsotopes.get(index));
				}
				int numberOfAvailableIsotopes = availableIsotopes.size();
				if (numberOfAvailableIsotopes > 0) {
					for (int k = 1; k <= numberOfIsotopicallyVariableElementsInFragment; k++) {
						Isotope isotopeToChoose = chooseIsotopeWeightedByAbundance(availableIsotopes);
//						Isotope isotopeToChoose = chooseIsotopeRandomly(availableIsotopes);
						Integer remainingNumberOfIsotopesInTheSet = Integer.valueOf(get(isotopeToChoose) - 1);
						put(isotopeToChoose,remainingNumberOfIsotopesInTheSet);
						massOfFragment = massOfFragment + isotopeToChoose.getAtomicMass();
					}
				}
			}
			if (massOfFragment > 0.0) {
				if (spectrum.get(massOfFragment) == null /*count each mass only once*/) {
					spectrum.put(massOfFragment, 1.0);
				} else {
					spectrum.put(massOfFragment, spectrum.get(massOfFragment) + 1);
				}
			}
		}
		return spectrum.adjustToCharge(charge);
	}
	
	@SuppressWarnings("unused")
	private Isotope chooseIsotopeRandomly(ArrayList<Isotope> availableIsotopes) {
		int numberOfAvailableIsotopes = availableIsotopes.size();
		int index = (int) (Math.random() * numberOfAvailableIsotopes);
		Isotope isotopeToChoose = availableIsotopes.get(index);
		return isotopeToChoose;
	}

	private Isotope chooseIsotopeWeightedByAbundance(ArrayList<Isotope> availableIsotopes) {
		int numberOfAvailableIsotopes = availableIsotopes.size();
		availableIsotopes.sort(new IsotopeAbundancyComparator());
		double sumOfAbundancies = 0;
		ArrayList<Double> bounds = new ArrayList<>();
		bounds.add(0.0);
		for (int index = 0; index < numberOfAvailableIsotopes; index++) {
			sumOfAbundancies = sumOfAbundancies + availableIsotopes.get(index).getAbundance();
			bounds.add(sumOfAbundancies);
		}
		double randomNumber = Math.random() * sumOfAbundancies;
		int index = 0;
		for (int i = 0; i < numberOfAvailableIsotopes; i++) {
			Double lowerBound = bounds.get(i);
			Double upperBound = bounds.get(i + 1);
			if (lowerBound <= randomNumber && randomNumber < upperBound) {
				index = i;
				break;
			}
		}
		Isotope isotopeToChoose = availableIsotopes.get(index);
		return isotopeToChoose;
	}

}
