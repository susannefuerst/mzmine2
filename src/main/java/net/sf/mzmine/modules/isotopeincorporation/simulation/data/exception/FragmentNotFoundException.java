package net.sf.mzmine.modules.isotopeincorporation.simulation.data.exception;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.FragmentsDatabase;

/**
 * An exception to be thrown whenever we try to access a fragment that cannot be found in our {@link FragmentsDatabase}
 * @author sfuerst
 *
 */
@SuppressWarnings("serial")
public class FragmentNotFoundException extends Exception{

	public FragmentNotFoundException(String message) {
		super(message);
	}

}
