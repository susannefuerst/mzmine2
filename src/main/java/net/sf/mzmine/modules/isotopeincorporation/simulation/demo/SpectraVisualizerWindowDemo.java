package net.sf.mzmine.modules.isotopeincorporation.simulation.demo;

import java.awt.Color;
import java.io.IOException;

import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.isotopeincorporation.impl.SimulatedSpectrumDataset;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentList;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentsDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IncorporationRate;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSShiftDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FragmentKey;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FrequencyType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.IncorporationType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FragmentNotFoundException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulator;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorRequest;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorResponse;
import net.sf.mzmine.modules.visualization.spectra.SpectraVisualizerWindow;
import net.sf.mzmine.project.impl.RawDataFileImpl;

public class SpectraVisualizerWindowDemo {

	public static void main(String[] args)
			throws FragmentNotFoundException, FrequencyTypeMismatchException, IOException {

		MZmineCore.main(new String[0]);

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
		MSDatabase msDatabase = response.getMsDatabaseList().get(0);
//		RawDataFile rawDataFile = MassSpectrum.toRawDataFile(msDatabase, IncorporationType.MIXED);
//		SpectraVisualizerWindow newWindow = new SpectraVisualizerWindow(rawDataFile);
//		newWindow.loadRawData(rawDataFile.getScan(1));
//		newWindow.getSpectrumPlot().getXYPlot().getRenderer().setDefaultToolTipGenerator(new SpectraToolTipGenerator());
                SpectraVisualizerWindow newWindow = new SpectraVisualizerWindow(new RawDataFileImpl("simulatedDataFile"));
                SimulatedSpectrumDataset dataSet = ((MSShiftDatabase) msDatabase).toSimulatedSpectrumDataSet(IncorporationType.MIXED);
                newWindow.getSpectrumPlot().addDataSet(dataSet, Color.blue, true);
		newWindow.setVisible(true);
	}

}
