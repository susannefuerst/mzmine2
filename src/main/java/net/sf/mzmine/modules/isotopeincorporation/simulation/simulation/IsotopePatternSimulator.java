package net.sf.mzmine.modules.isotopeincorporation.simulation.simulation;

import java.util.Map.Entry;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IsotopeSet;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabaseList;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSShiftDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MassSpectrum;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MyLogger;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FrequencyType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.IncorporationType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;

public class IsotopePatternSimulator {
	
	public static final MyLogger LOGGER = MyLogger.getLogger(IsotopePatternSimulator.class);
	
	public static IsotopePatternSimulatorResponse simulate(IsotopePatternSimulatorRequest request) throws FrequencyTypeMismatchException {
		final double incRate = request.getIncorporationRate().getRateValue();
		final double naturalFragments = request.getTotalNumberOfFragments() * (1 - incRate);
		final double experimentalFragments = request.getTotalNumberOfFragments() * incRate;
		final boolean analyzeMassShifts = request.getAnalyzeMassShifts();
		final FrequencyType frequencyType = request.getTargetFrequencyType();
		final int charge = request.getCharge();
		MSDatabaseList msDatabaseList = new MSDatabaseList();
		for (Fragment fragment : request.getFragments()) {
			IsotopeSet naturalSet = new IsotopeSet(fragment, naturalFragments, IncorporationType.NATURAL);
			IsotopeSet markedSet = new IsotopeSet(fragment, experimentalFragments, IncorporationType.EXPERIMENTAL);
			MassSpectrum naturalSpectrum = naturalSet.simulateSpectrum(charge);
			MassSpectrum markedSpectrum = markedSet.simulateSpectrum(charge);
			MassSpectrum mixedSpectrum = naturalSpectrum.merge(markedSpectrum);
			Integer roundMassesPrecision = request.getRoundedMassPrecision();
			Integer roundFrequenciesPrecision = request.getRoundedFrequenciesPrecision();
			Double minimalRelativeFrequency = request.getMinimalRelativeFrequency();
			naturalSpectrum = prepareSpectrum(naturalSpectrum, roundMassesPrecision, roundFrequenciesPrecision,
					minimalRelativeFrequency, frequencyType);
			markedSpectrum = prepareSpectrum(markedSpectrum, roundMassesPrecision, roundFrequenciesPrecision,
					minimalRelativeFrequency, frequencyType);
			mixedSpectrum = prepareSpectrum(mixedSpectrum, roundMassesPrecision, roundFrequenciesPrecision,
					minimalRelativeFrequency, frequencyType);
			if (analyzeMassShifts) {
				MSShiftDatabase msShiftDatabase = new MSShiftDatabase();
				msShiftDatabase.setIncorporatedTracers(fragment.getCapacityFormula());
				msShiftDatabase.setIncorporationRate(incRate);
				msShiftDatabase.setFragmentKey(fragment.getFragmentKey());
				msShiftDatabase.setNaturalSpectrum(naturalSpectrum);
				msShiftDatabase.setMarkedSpectrum(markedSpectrum);
				msShiftDatabase.setMixedSpectrum(mixedSpectrum);
				msShiftDatabase.setFragmentFormula(fragment.getFormula());
				msShiftDatabase.analyseAllShifts();
				LOGGER.debug("\n" + msShiftDatabase);
				msDatabaseList.add(msShiftDatabase);
			} else {
				MSDatabase msDatabase = new MSDatabase();
				msDatabase.setIncorporatedTracers(fragment.getCapacityFormula());
				msDatabase.setIncorporationRate(incRate);
				msDatabase.setFragmentKey(fragment.getFragmentKey());
				msDatabase.setNaturalSpectrum(naturalSpectrum);
				msDatabase.setMarkedSpectrum(markedSpectrum);
				msDatabase.setMixedSpectrum(mixedSpectrum);
				msDatabase.setFragmentFormula(fragment.getFormula());
				LOGGER.debug("\n" + msDatabase);
				msDatabaseList.add(msDatabase);
			}
		}
		IsotopePatternSimulatorResponse simulationResponse = new IsotopePatternSimulatorResponse();
		simulationResponse.setMsDatabaseList(msDatabaseList);
		return simulationResponse;
	}
	
	public static MassSpectrum prepareSpectrum(MassSpectrum spectrum, Integer roundMassesPrecision,
			Integer roundFrequenciesPrecision, Double minimaFrequency, FrequencyType frequencyType) {
		if (roundMassesPrecision != null) {
			spectrum = spectrum.roundMasses(roundMassesPrecision);
		}
		if (frequencyType.equals(FrequencyType.MID)) {
			spectrum = spectrum.toMIDFrequency();
		} else if (frequencyType.equals(FrequencyType.RELATIVE)) {
			spectrum = spectrum.toRelativeFrequency();
		}
		if (roundFrequenciesPrecision != null) {
			spectrum = spectrum.roundFrequencies(roundFrequenciesPrecision);
		}
		if (minimaFrequency != null) {
			spectrum = spectrum.skipLowFrequency(minimaFrequency);
		}
		Double sumOfFrequencies = 0.0;
		for (Entry<Double,Double> entry : spectrum.entrySet()) {
			sumOfFrequencies = sumOfFrequencies + entry.getValue();
		}
		return spectrum.sortAscendingByMass();
	}
}
