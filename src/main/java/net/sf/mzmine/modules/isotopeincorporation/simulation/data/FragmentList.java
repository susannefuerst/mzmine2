package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class FragmentList extends ArrayList<Fragment> {
	
	public FragmentList() {
		
	}
	
	public FragmentList(Fragment...fragments ) {
		for (Fragment fragment : fragments) {
			this.add(fragment);
		}
	}

}
