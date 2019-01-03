package net.sf.mzmine.modules.isotopeincorporation.simulation.data;

import java.util.InputMismatchException;

/**
 * The rate of incorporated molecules within a set of fragments used to simulate the corresponding
 * isotope pattern.
 * @author sfuerst
 *
 */
public class IncorporationRate {
	
	private double rateValue;
	
	/**
	 * Generates an {@link IncorporationRate} with rateValue = 1.0
	 */
	public IncorporationRate() {
		this.rateValue = 1.0;
	}
	
	/**
	 * Generates an {@link IncorporationRate} with the specified value.
	 * @param rateValue
	 */
	public IncorporationRate(double rateValue) {
		if (rateValue < 0 || rateValue > 1) {
			throw new InputMismatchException("Incorporation rate value " + rateValue + " is not in [0,1]");
		}
		this.rateValue = rateValue;
	}
	
	/**
	 * 
	 * @return the rate value in percent
	 */
	public int perCernt() {
		return (int) (this.rateValue * 100);
	}

	public double getRateValue() {
		return rateValue;
	}

	public void setRateValue(double rateValue) {
		if (rateValue < 0 || rateValue > 1) {
			throw new InputMismatchException("Incorporation rate value " + rateValue + " is not in [0,1]");
		}
		this.rateValue = rateValue;
	}
	
	@Override
	public String toString() {
		return String.valueOf(rateValue);
	}

}
