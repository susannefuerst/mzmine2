package net.sf.mzmine.modules.isotopeincorporation.simulation.demo;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentList;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentsDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IncorporationRate;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MyLogger;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FragmentKey;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.MSBarChartType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FragmentNotFoundException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulator;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorRequest;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorResponse;
import net.sf.mzmine.modules.isotopeincorporation.simulation.visualisation.MSBarChartApplicationWindow;

public class MSBarChartApplicationWindowDemo2 {

public static final MyLogger LOGGER = MyLogger.getLogger(MSBarChartApplicationWindowDemo2.class);
	
	public static void main(String[] args) throws FrequencyTypeMismatchException, FragmentNotFoundException {
		IsotopePatternSimulatorRequest simulatorRequest = new IsotopePatternSimulatorRequest();
		Fragment fragment1 = FragmentsDatabase.getFragment(FragmentKey.ALA_116);
		Fragment fragment2 = FragmentsDatabase.getFragment(FragmentKey.ALA_116);
		fragment2.changeCapacity("C2");
		Fragment fragment3 = FragmentsDatabase.getFragment(FragmentKey.ALA_116);
		fragment3.changeCapacity("N");
		simulatorRequest.setFragments(new FragmentList(fragment1, fragment2, fragment3));
		simulatorRequest.setIncorporationRate(new IncorporationRate(0.6));
		simulatorRequest.setMinimalRelativeFrequency(0.001);
		simulatorRequest.setAnalyzeMassShifts(true);
		simulatorRequest.setTotalNumberOfFragments(10000.0);
		IsotopePatternSimulatorResponse response = IsotopePatternSimulator.simulate(simulatorRequest);
		for (MSDatabase msDatabase : response.getMsDatabaseList()) {
			LOGGER.info(msDatabase);
			MSBarChartApplicationWindow demo = new MSBarChartApplicationWindow("Bar Demo 1", msDatabase, MSBarChartType.ALL_SPECTRA);
			demo.pack();
			demo.setVisible(true);
		}
	}
}
