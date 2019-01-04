package net.sf.mzmine.modules.isotopeincorporation.simulation.demo;

import java.io.IOException;

import net.sf.mzmine.datamodel.RawDataFile;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentList;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentsDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IncorporationRate;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MassSpectrum;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FragmentKey;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FrequencyType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FragmentNotFoundException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulator;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorRequest;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorResponse;
import net.sf.mzmine.modules.visualization.spectra.SpectraVisualizerWindow;

public class SpectraVisualizerWindowDemo {

	public static void main(String[] args)
			throws FragmentNotFoundException, FrequencyTypeMismatchException, IOException {

		MZmineCore.main(new String[0]);

		IsotopePatternSimulatorRequest simulatorRequest = new IsotopePatternSimulatorRequest();
		Fragment fragment = FragmentsDatabase.getFragment(FragmentKey.ALA_116);
//		fragment.changeCapacity("C4N");
		simulatorRequest.setFragments(new FragmentList(fragment));
		simulatorRequest.setIncorporationRate(new IncorporationRate(0.7));
		simulatorRequest.setMinimalRelativeFrequency(0.001);
		simulatorRequest.setAnalyzeMassShifts(true);
		simulatorRequest.setTotalNumberOfFragments(10000.0);
		simulatorRequest.setRoundedMassPrecision(4);
		simulatorRequest.setTargetFrequencyType(FrequencyType.RELATIVE);
		IsotopePatternSimulatorResponse response = IsotopePatternSimulator.simulate(simulatorRequest);
		MSDatabase msDatabase = response.getMsDatabaseList().get(0);
		MassSpectrum naturalSpectrum = msDatabase.getNaturalSpectrum();
		RawDataFile rawDataFile = naturalSpectrum.toRawDataFile();
		SpectraVisualizerWindow newWindow = new SpectraVisualizerWindow(rawDataFile);
		newWindow.loadRawData(rawDataFile.getScan(1));
		newWindow.setVisible(true);
	}

}
