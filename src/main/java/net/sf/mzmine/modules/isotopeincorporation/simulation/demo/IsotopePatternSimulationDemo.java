package net.sf.mzmine.modules.isotopeincorporation.simulation.demo;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.Fragment;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentList;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentsDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.IncorporationRate;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.FragmentKey;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FragmentNotFoundException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception.FrequencyTypeMismatchException;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulator;
import net.sf.mzmine.modules.isotopeincorporation.simulation.simulation.IsotopePatternSimulatorRequest;

public class IsotopePatternSimulationDemo {

    public static void main(String[] args)
            throws FragmentNotFoundException, FrequencyTypeMismatchException {
        IsotopePatternSimulatorRequest simulatorRequest = new IsotopePatternSimulatorRequest();
        Fragment fragment = FragmentsDatabase.getFragment(FragmentKey.ASP_130);
        fragment.changeCapacity("N");
        // Fragment fragment = new Fragment(FragmentKey.UNKNOWN, "N10", "N");
        simulatorRequest.setFragments(new FragmentList(fragment));
        simulatorRequest.setIncorporationRate(new IncorporationRate(0.6));
        simulatorRequest.setMinimalFrequency(0.002);
        simulatorRequest.setAnalyzeMassShifts(true);
        IsotopePatternSimulator.simulate(simulatorRequest);
    }
}
