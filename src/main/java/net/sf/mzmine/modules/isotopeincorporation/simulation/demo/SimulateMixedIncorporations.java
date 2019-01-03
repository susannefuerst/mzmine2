package net.sf.mzmine.modules.isotopeincorporation.simulation.demo;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentsDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IsotopeSet;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSShiftDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MassSpectrum;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MyLogger;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FragmentKey;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FrequencyType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.IncorporationType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.MSBarChartType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FragmentNotFoundException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulator;
import net.sf.mzmine.modules.isotopeincorporation.simulation.visualisation.MSBarChartApplicationWindow;

public class SimulateMixedIncorporations {
	
	public static final MyLogger LOGGER = MyLogger.getLogger(SimulateMixedIncorporations.class);
	
	public static final double INC_C2N = 0.0;
	public static final double INC_C2 = 0.01;
	public static final double INC_N = 0.5;
	public static final double NUMBER_OF_FRAGMENTS = 100000.0;
	public static final Integer PRECISION = 4;
	public static final double MIN_FREQUENCY = 0.003;
	public static final FrequencyType FREQUENCY_TYPE = FrequencyType.MID;
	
	public static final double INC = INC_C2 + INC_C2N + INC_N;
	
	public static void main(String[] args) throws FragmentNotFoundException, FrequencyTypeMismatchException {
		Fragment fragmentC2N = FragmentsDatabase.getFragment(FragmentKey.GLY_276);
		fragmentC2N.changeCapacity("CN");
		Fragment fragmentC2 = FragmentsDatabase.getFragment(FragmentKey.GLY_276);
		fragmentC2.changeCapacity("C");
		Fragment fragmentN = FragmentsDatabase.getFragment(FragmentKey.GLY_276);
		fragmentN.changeCapacity("N");
		
//		Fragment fragmentC2N = FragmentsDatabase.getFragment(FragmentKey.SER_116);
//		fragmentC2N.changeCapacity("C2N");
//		Fragment fragmentC2 = FragmentsDatabase.getFragment(FragmentKey.SER_116);
//		fragmentC2.changeCapacity("C2");
//		Fragment fragmentN = FragmentsDatabase.getFragment(FragmentKey.SER_116);
//		fragmentN.changeCapacity("N");

		IsotopeSet naturalSet = new IsotopeSet(fragmentC2N, NUMBER_OF_FRAGMENTS * (1 - INC), IncorporationType.NATURAL);
		IsotopeSet markedSetC2N = new IsotopeSet(fragmentC2N, NUMBER_OF_FRAGMENTS * (INC_C2N), IncorporationType.EXPERIMENTAL);
		IsotopeSet markedSetC2 = new IsotopeSet(fragmentC2, NUMBER_OF_FRAGMENTS * (INC_C2), IncorporationType.EXPERIMENTAL);
		IsotopeSet markedSetN = new IsotopeSet(fragmentN, NUMBER_OF_FRAGMENTS * (INC_N), IncorporationType.EXPERIMENTAL);
		
		MassSpectrum naturalSpectrum = naturalSet.simulateSpectrum(0);
		MassSpectrum markedSpectrumC2N = markedSetC2N.simulateSpectrum(0);
		MassSpectrum markedSpectrumC2 = markedSetC2.simulateSpectrum(0);
		MassSpectrum markedSpectrumN = markedSetN.simulateSpectrum(0);
		MassSpectrum mixedSpectrum = naturalSpectrum.merge(markedSpectrumC2N);
		mixedSpectrum = mixedSpectrum.merge(markedSpectrumC2);
		mixedSpectrum = mixedSpectrum.merge(markedSpectrumN);
//		MassSpectrum mixedSpectrum = naturalSpectrum.merge(markedSpectrumN);
//		mixedSpectrum = mixedSpectrum.merge(markedSpectrumC2);
		
		naturalSpectrum = IsotopePatternSimulator.prepareSpectrum(naturalSpectrum, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		markedSpectrumC2N = IsotopePatternSimulator.prepareSpectrum(markedSpectrumC2N, PRECISION, PRECISION,
				MIN_FREQUENCY, FREQUENCY_TYPE);
		markedSpectrumC2 = IsotopePatternSimulator.prepareSpectrum(markedSpectrumC2, PRECISION, PRECISION,
				MIN_FREQUENCY, FREQUENCY_TYPE);
		markedSpectrumN = IsotopePatternSimulator.prepareSpectrum(markedSpectrumN, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		mixedSpectrum = IsotopePatternSimulator.prepareSpectrum(mixedSpectrum, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		
		MSShiftDatabase msShiftDatabase = new MSShiftDatabase();
		msShiftDatabase.setIncorporatedTracers("CN,C,N");
		msShiftDatabase.setIncorporationRate(INC);
		msShiftDatabase.setFragmentKey(fragmentC2N.getFragmentKey());
		msShiftDatabase.setNaturalSpectrum(naturalSpectrum);
		msShiftDatabase.setMarkedSpectrum(markedSpectrumC2);
		msShiftDatabase.setMixedSpectrum(mixedSpectrum);
		msShiftDatabase.setFragmentFormula(fragmentC2N.getFormula());
		msShiftDatabase.analyseAllShifts();
		
		LOGGER.info(msShiftDatabase);
		MSBarChartApplicationWindow demo = new MSBarChartApplicationWindow("Bar Demo 1", msShiftDatabase, MSBarChartType.ALL_SPECTRA);
		demo.pack();
		demo.setVisible(true);
	}
}
