package net.sf.mzmine.modules.isotopeincorporation.simulation.simulation;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabaseList;

/**
 * Includes a list of {@link MSDatabase}s corresponding to the requested fragments and options from a
 * {@link IsotopePatternSimulatorRequest}.
 * @author sfuerst
 *
 */
public class IsotopePatternSimulatorResponse {
	
	private MSDatabaseList msDatabaseList;

	/**
	 * @return the msDatabaseList
	 */
	public MSDatabaseList getMsDatabaseList() {
		return msDatabaseList;
	}

	/**
	 * @param msDatabaseList the msDatabaseList to set
	 */
	public void setMsDatabaseList(MSDatabaseList msDatabaseList) {
		this.msDatabaseList = msDatabaseList;
	}

}
