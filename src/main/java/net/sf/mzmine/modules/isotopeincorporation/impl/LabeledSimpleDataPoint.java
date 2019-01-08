package net.sf.mzmine.modules.isotopeincorporation.impl;

import net.sf.mzmine.datamodel.impl.SimpleDataPoint;

public class LabeledSimpleDataPoint extends SimpleDataPoint {

	private String label;

	public LabeledSimpleDataPoint(double mz, double intensity, String label) {
		super(mz, intensity);
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
