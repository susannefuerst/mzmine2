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
	
	public static final double INC_CN = 0.2;
	public static final double INC_C = 0.2;
	public static final double INC_N = 0.2;
	public static final double NUMBER_OF_FRAGMENTS = 100000.0;
	public static final Integer PRECISION = 4;
	public static final double MIN_FREQUENCY = 0.1;
	public static final FrequencyType FREQUENCY_TYPE = FrequencyType.RELATIVE;
	
	public static final double INC = INC_C + INC_CN + INC_N;
	
	public static void main(String[] args) throws FragmentNotFoundException, FrequencyTypeMismatchException {
		Fragment fragmentCN = FragmentsDatabase.getFragment(FragmentKey.GLN_156);
		fragmentCN.changeCapacity("C4N");
		Fragment fragmentC = FragmentsDatabase.getFragment(FragmentKey.GLN_156);
		fragmentC.changeCapacity("C4");
		Fragment fragmentN = FragmentsDatabase.getFragment(FragmentKey.GLN_156);
		fragmentN.changeCapacity("N");

		IsotopeSet naturalSet = new IsotopeSet(fragmentCN, NUMBER_OF_FRAGMENTS * (1 - INC), IncorporationType.NATURAL);
		IsotopeSet markedSetCN = new IsotopeSet(fragmentCN, NUMBER_OF_FRAGMENTS * (INC_CN), IncorporationType.EXPERIMENTAL);
		IsotopeSet markedSetC = new IsotopeSet(fragmentC, NUMBER_OF_FRAGMENTS * (INC_C), IncorporationType.EXPERIMENTAL);
		IsotopeSet markedSetN = new IsotopeSet(fragmentN, NUMBER_OF_FRAGMENTS * (INC_N), IncorporationType.EXPERIMENTAL);
		
		MassSpectrum naturalSpectrum = naturalSet.simulateSpectrum(0);
		MassSpectrum markedSpectrumCN = markedSetCN.simulateSpectrum(0);
		MassSpectrum markedSpectrumC = markedSetC.simulateSpectrum(0);
		MassSpectrum markedSpectrumN = markedSetN.simulateSpectrum(0);
		MassSpectrum mixedSpectrum = naturalSpectrum.merge(markedSpectrumCN);
		mixedSpectrum = mixedSpectrum.merge(markedSpectrumC);
		mixedSpectrum = mixedSpectrum.merge(markedSpectrumN);
//		MassSpectrum mixedSpectrum = naturalSpectrum.merge(markedSpectrumN);
//		mixedSpectrum = mixedSpectrum.merge(markedSpectrumC2);
		
		naturalSpectrum = IsotopePatternSimulator.prepareSpectrum(naturalSpectrum, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		markedSpectrumCN = IsotopePatternSimulator.prepareSpectrum(markedSpectrumCN, PRECISION, PRECISION,
				MIN_FREQUENCY, FREQUENCY_TYPE);
		markedSpectrumC = IsotopePatternSimulator.prepareSpectrum(markedSpectrumC, PRECISION, PRECISION,
				MIN_FREQUENCY, FREQUENCY_TYPE);
		markedSpectrumN = IsotopePatternSimulator.prepareSpectrum(markedSpectrumN, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		mixedSpectrum = IsotopePatternSimulator.prepareSpectrum(mixedSpectrum, PRECISION, PRECISION, MIN_FREQUENCY,
				FREQUENCY_TYPE);
		
		MSShiftDatabase msShiftDatabase = new MSShiftDatabase();
		msShiftDatabase.setIncorporatedTracers("CN,C,N");
		msShiftDatabase.setIncorporationRate(INC);
		msShiftDatabase.setFragmentKey(fragmentCN.getFragmentKey());
		msShiftDatabase.setNaturalSpectrum(naturalSpectrum);
		msShiftDatabase.setMarkedSpectrum(markedSpectrumC);
		msShiftDatabase.setMixedSpectrum(mixedSpectrum);
		msShiftDatabase.setFragmentFormula(fragmentCN.getFormula());
//		msShiftDatabase.analyseAllShifts();
		
		LOGGER.info(msShiftDatabase);
		MSBarChartApplicationWindow demo = new MSBarChartApplicationWindow("Bar Demo 1", msShiftDatabase, MSBarChartType.ALL_SPECTRA);
		demo.pack();
		demo.setVisible(true);
	}
}
