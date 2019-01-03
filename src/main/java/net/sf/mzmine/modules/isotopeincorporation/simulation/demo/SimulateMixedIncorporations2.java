package net.sf.mzmine.modules.isotopeincorporation.simulation.demo;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
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

public class SimulateMixedIncorporations2 {
	
	public static final MyLogger LOGGER = MyLogger.getLogger(SimulateMixedIncorporations2.class);
	
	public static final double INC_C = 0.5;
	public static final double NUMBER_OF_FRAGMENTS = 100000.0;
	public static final Integer PRECISION = 4;
	public static final double MIN_FREQUENCY = 0.003;
	public static final FrequencyType FREQUENCY_TYPE = FrequencyType.MID;
	
	public static final double INC = INC_C;
	
	public static void main(String[] args) throws FragmentNotFoundException, FrequencyTypeMismatchException {
		Fragment fragmentC = new Fragment(FragmentKey.UNKNOWN, "C2", "C");

		IsotopeSet naturalSet = new IsotopeSet(fragmentC, NUMBER_OF_FRAGMENTS * (1 - INC), IncorporationType.NATURAL);
		IsotopeSet markedSetC = new IsotopeSet(fragmentC, NUMBER_OF_FRAGMENTS * (INC_C), IncorporationType.EXPERIMENTAL);
		
		MassSpectrum naturalSpectrum = naturalSet.simulateSpectrum(0);
		MassSpectrum markedSpectrumC = markedSetC.simulateSpectrum(0);
		MassSpectrum mixedSpectrum = naturalSpectrum.merge(markedSpectrumC);
		
		naturalSpectrum = IsotopePatternSimulator.prepareSpectrum(naturalSpectrum, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		markedSpectrumC = IsotopePatternSimulator.prepareSpectrum(markedSpectrumC, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		mixedSpectrum = IsotopePatternSimulator.prepareSpectrum(mixedSpectrum, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		
		MSShiftDatabase msShiftDatabase = new MSShiftDatabase();
		msShiftDatabase.setIncorporatedTracers("C");
		msShiftDatabase.setIncorporationRate(INC);
		msShiftDatabase.setFragmentKey(fragmentC.getFragmentKey());
		msShiftDatabase.setNaturalSpectrum(naturalSpectrum);
		msShiftDatabase.setMarkedSpectrum(markedSpectrumC);
		msShiftDatabase.setMixedSpectrum(mixedSpectrum);
		msShiftDatabase.setFragmentFormula(fragmentC.getFormula());
		msShiftDatabase.analyseAllShifts();
		
		LOGGER.info(msShiftDatabase);
		MSBarChartApplicationWindow demo = new MSBarChartApplicationWindow("Bar Demo 1", msShiftDatabase, MSBarChartType.ALL_SPECTRA);
		demo.pack();
		demo.setVisible(true);
	}
}
