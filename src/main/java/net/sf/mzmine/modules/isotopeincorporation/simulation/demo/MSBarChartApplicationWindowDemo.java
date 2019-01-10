package net.sf.mzmine.modules.isotopeincorporation.simulation.demo;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentList;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentsDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IncorporationRate;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MyLogger;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FragmentKey;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FrequencyType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.MSBarChartType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FragmentNotFoundException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulator;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorRequest;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorResponse;
import net.sf.mzmine.modules.isotopeincorporation.simulation.visualisation.MSBarChartApplicationWindow;

public class MSBarChartApplicationWindowDemo {
	
	public static final MyLogger LOGGER = MyLogger.getLogger(MSBarChartApplicationWindowDemo.class);
	
	public static void main(String[] args) throws FrequencyTypeMismatchException, FragmentNotFoundException {
		IsotopePatternSimulatorRequest simulatorRequest = new IsotopePatternSimulatorRequest();
		Fragment fragment = FragmentsDatabase.getFragment(FragmentKey.GLN_156);
		fragment.changeCapacity("C4");
		simulatorRequest.setFragments(new FragmentList(fragment));
		simulatorRequest.setIncorporationRate(new IncorporationRate(0.7));
		simulatorRequest.setMinimalRelativeFrequency(0.1);
		simulatorRequest.setAnalyzeMassShifts(true);
		simulatorRequest.setTotalNumberOfFragments(10000.0);
		simulatorRequest.setRoundedMassPrecision(4);
		simulatorRequest.setTargetFrequencyType(FrequencyType.RELATIVE);
		IsotopePatternSimulatorResponse response = IsotopePatternSimulator.simulate(simulatorRequest);
		MSDatabase msDatabase =  response.getMsDatabaseList().get(0);
		LOGGER.info(msDatabase);
		MSBarChartApplicationWindow demo = new MSBarChartApplicationWindow("Bar Demo 1", msDatabase,
				MSBarChartType.COMPLETELY_LABELED_SPECTRUM_ONLY);
		demo.pack();
		demo.setVisible(true);
	}
}
