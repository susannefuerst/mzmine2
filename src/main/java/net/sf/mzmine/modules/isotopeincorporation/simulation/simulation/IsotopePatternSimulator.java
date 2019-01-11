package net.sf.mzmine.modules.isotopeincorporation.simulation.simulation;

import java.util.InputMismatchException;
import java.util.Map.Entry;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.ExperimentalIncorporationCapacity;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IsotopeSet;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabaseList;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSShiftDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MassSpectrum;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MyLogger;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.Element;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FrequencyType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.IncorporationType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;

public class IsotopePatternSimulator {

    public static final MyLogger LOGGER = MyLogger
            .getLogger(IsotopePatternSimulator.class);

    public static IsotopePatternSimulatorResponse simulate(
            IsotopePatternSimulatorRequest request)
            throws FrequencyTypeMismatchException {
        final double incRate = request.getIncorporationRate().getRateValue();
        final double naturalFragments = request.getTotalNumberOfFragments()
                * (1 - incRate);
        final double experimentalFragments = request.getTotalNumberOfFragments()
                * incRate;
        final boolean analyzeMassShifts = request.getAnalyzeMassShifts();
        final int charge = request.getCharge();
        final FrequencyType frequencyType = request.getTargetFrequencyType();
        final Integer roundMassesPrecision = request.getRoundedMassPrecision();
        final Integer roundFrequenciesPrecision = request
                .getRoundedFrequenciesPrecision();
        final Double minimalRelativeFrequency = request.getMinimalFrequency();
        MSDatabaseList msDatabaseList = new MSDatabaseList();
        for (Fragment fragment : request.getFragments()) {
            IsotopeSet naturalSet = new IsotopeSet(fragment, naturalFragments,
                    IncorporationType.NATURAL);
            IsotopeSet markedSet = new IsotopeSet(fragment,
                    experimentalFragments, IncorporationType.EXPERIMENTAL);
            MassSpectrum naturalSpectrum = naturalSet.simulateSpectrum(charge);
            MassSpectrum markedSpectrum = markedSet.simulateSpectrum(charge);
            MassSpectrum mixedSpectrum = naturalSpectrum.merge(markedSpectrum);
            naturalSpectrum = prepareSpectrum(naturalSpectrum,
                    roundMassesPrecision, roundFrequenciesPrecision,
                    minimalRelativeFrequency, frequencyType);
            markedSpectrum = prepareSpectrum(markedSpectrum,
                    roundMassesPrecision, roundFrequenciesPrecision,
                    minimalRelativeFrequency, frequencyType);
            mixedSpectrum = prepareSpectrum(mixedSpectrum, roundMassesPrecision,
                    roundFrequenciesPrecision, minimalRelativeFrequency,
                    frequencyType);
            if (analyzeMassShifts) {
                MSShiftDatabase msShiftDatabase = new MSShiftDatabase();
                msShiftDatabase
                        .setIncorporatedTracers(fragment.getCapacityFormula());
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
                msDatabase
                        .setIncorporatedTracers(fragment.getCapacityFormula());
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

    public static IsotopePatternSimulatorResponse simulateIndependentTracerIncorporation(
            IsotopePatternSimulatorRequest request)
            throws FrequencyTypeMismatchException {
        final double tracer1Inc = request.getTracer1Inc().getRateValue();
        final double tracer2Inc = request.getTracer2Inc().getRateValue();
        final double tracerAllInc = request.getTracerAllInc().getRateValue();
        final double incRate = tracer1Inc + tracer2Inc + tracerAllInc;
        if (incRate > 1) {
            throw new InputMismatchException("Total incorporation rate value "
                    + incRate + " is not greater than 1!");
        }
        final boolean analyzeMassShifts = request.getAnalyzeMassShifts();
        final int charge = request.getCharge();
        final FrequencyType frequencyType = request.getTargetFrequencyType();
        final Element tracer1 = request.getTracer1();
        final Element tracer2 = request.getTracer2();
        final double numberOfFragments = request.getTotalNumberOfFragments();
        final Integer roundMassesPrecision = request.getRoundedMassPrecision();
        final Integer roundFrequenciesPrecision = request
                .getRoundedFrequenciesPrecision();
        final Double minimalFrequency = request.getMinimalFrequency();
        MSDatabaseList msDatabaseList = new MSDatabaseList();

        for (Fragment fragment : request.getFragments()) {
            ExperimentalIncorporationCapacity capacity = fragment
                    .getExperimentalIncorporationCapacity();
            String capacity1 = tracer1.name() + capacity.get(tracer1);
            String capacity2 = tracer2.name() + capacity.get(tracer2);
            Fragment fragmentAll = fragment.copy();
            Fragment fragment1 = fragment.copy();
            fragment1.changeCapacity(capacity1);
            Fragment fragment2 = fragment.copy();
            fragment2.changeCapacity(capacity2);

            IsotopeSet naturalSet = new IsotopeSet(fragmentAll,
                    numberOfFragments * (1 - incRate),
                    IncorporationType.NATURAL);
            IsotopeSet markedSetTracerALL = new IsotopeSet(fragmentAll,
                    numberOfFragments * (tracerAllInc),
                    IncorporationType.EXPERIMENTAL);
            IsotopeSet markedSetTracer1 = new IsotopeSet(fragment1,
                    numberOfFragments * (tracer1Inc),
                    IncorporationType.EXPERIMENTAL);
            IsotopeSet markedSetTracer2 = new IsotopeSet(fragment2,
                    numberOfFragments * (tracer2Inc),
                    IncorporationType.EXPERIMENTAL);

            MassSpectrum naturalSpectrum = naturalSet.simulateSpectrum(1);
            MassSpectrum markedSpectrumTracerAll = markedSetTracerALL
                    .simulateSpectrum(1);
            MassSpectrum markedSpectrumTracer1 = markedSetTracer1
                    .simulateSpectrum(1);
            MassSpectrum markedSpectrumTracer2 = markedSetTracer2
                    .simulateSpectrum(1);
            MassSpectrum mixedSpectrum = naturalSpectrum
                    .merge(markedSpectrumTracerAll);
            mixedSpectrum = mixedSpectrum.merge(markedSpectrumTracer1);
            mixedSpectrum = mixedSpectrum.merge(markedSpectrumTracer2);

            naturalSpectrum = IsotopePatternSimulator.prepareSpectrum(
                    naturalSpectrum, roundMassesPrecision,
                    roundFrequenciesPrecision, minimalFrequency, frequencyType);
            markedSpectrumTracerAll = IsotopePatternSimulator.prepareSpectrum(
                    naturalSpectrum, roundMassesPrecision,
                    roundFrequenciesPrecision, minimalFrequency, frequencyType);
            markedSpectrumTracer1 = IsotopePatternSimulator.prepareSpectrum(
                    naturalSpectrum, roundMassesPrecision,
                    roundFrequenciesPrecision, minimalFrequency, frequencyType);
            markedSpectrumTracer2 = IsotopePatternSimulator.prepareSpectrum(
                    naturalSpectrum, roundMassesPrecision,
                    roundFrequenciesPrecision, minimalFrequency, frequencyType);
            mixedSpectrum = IsotopePatternSimulator.prepareSpectrum(
                    naturalSpectrum, roundMassesPrecision,
                    roundFrequenciesPrecision, minimalFrequency, frequencyType);

            MSShiftDatabase msShiftDatabase = new MSShiftDatabase();
            msShiftDatabase.setIncorporatedTracers(
                    capacity1 + ", " + capacity2 + " independently");
            msShiftDatabase.setIncorporationRate(incRate);
            msShiftDatabase.setFragmentKey(fragmentAll.getFragmentKey());
            msShiftDatabase.setNaturalSpectrum(naturalSpectrum);
            msShiftDatabase.setMarkedSpectrum(markedSpectrumTracer1);
            msShiftDatabase.setMixedSpectrum(mixedSpectrum);
            msShiftDatabase.setFragmentFormula(fragmentAll.getFormula());
            if (analyzeMassShifts) {
                msShiftDatabase.analyseAllShifts();
            }
            msDatabaseList.add(msShiftDatabase);
        }
        IsotopePatternSimulatorResponse simulationResponse = new IsotopePatternSimulatorResponse();
        simulationResponse.setMsDatabaseList(msDatabaseList);
        return simulationResponse;

    }

    /**
     * Returns a new spectrum that resulted from a manipulation of the parameter
     * spectrum.
     * 
     * @param spectrum,
     *            spectrum to be prepared
     * @param roundMassesPrecision,
     * @param roundFrequenciesPrecision,
     * @param minimaFrequency,
     * @param frequencyType,
     * @return
     */
    public static MassSpectrum prepareSpectrum(MassSpectrum spectrum,
            Integer roundMassesPrecision, Integer roundFrequenciesPrecision,
            Double minimalFrequency, FrequencyType frequencyType) {
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
        if (minimalFrequency != null) {
            spectrum = spectrum.skipLowFrequency(minimalFrequency);
        }
        Double sumOfFrequencies = 0.0;
        for (Entry<Double, Double> entry : spectrum.entrySet()) {
            sumOfFrequencies = sumOfFrequencies + entry.getValue();
        }
        return spectrum.sortAscendingByMass();
    }
}
