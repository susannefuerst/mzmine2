package net.sf.mzmine.modules.isotopeincorporation.simulation.demo;

import java.io.IOException;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentsDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IncorporationRate;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.MSBarChartType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.PathConstants;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulator;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorRequest;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorResponse;

public class MassSpectraToCsvAndJpeg {

    public static void main(String[] args)
            throws FrequencyTypeMismatchException, IOException {
        IsotopePatternSimulatorRequest simulatorRequest = new IsotopePatternSimulatorRequest();
        simulatorRequest.setFragments(FragmentsDatabase.getAllFregments());
        simulatorRequest.setIncorporationRate(new IncorporationRate(0.6));
        simulatorRequest.setMinimalFrequency(0.002);
        simulatorRequest.setAnalyzeMassShifts(true);
        IsotopePatternSimulatorResponse response = IsotopePatternSimulator
                .simulate(simulatorRequest);
        for (MSDatabase msDatabase : response.getMsDatabaseList()) {
            String metaboliteAbbreviation = msDatabase.getFragmentKey()
                    .getMetaboliteKey().getAbbreviation();
            msDatabase.writeCsv(PathConstants.FILE_OUTPUT_FOLDER
                    .toAbsolutePath(metaboliteAbbreviation + "\\"));
            msDatabase.saveMSCategoryBarChartAsJPEG(
                    PathConstants.FILE_OUTPUT_FOLDER
                            .toAbsolutePath(metaboliteAbbreviation + "\\"),
                    MSBarChartType.ALL_SPECTRA);
        }
    }

}
