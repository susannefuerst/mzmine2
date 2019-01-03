package net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants;
/**
 * An enumeration of error messages, used for the isotopeincorporation project
 * @author sfuerst
 *
 */
public enum ErrorMessage {
	INVALID_FORMULA("Cannot read formula. Please enter a valid format.", "A valid format does not contain any brackets."),
	INVALID_MASS_SHIFT_PATTERN("Cannot read MassShift from string.", "The input string does not match the ecpected pattern."),
	FREQUENCY_TYPE_MISMATCH("Can only merge maps with FrequencyType.ABSOLUTE", " "),
	INVALID_ISOTOPE_NAME("No such isotope.", ""),
	NO_TRACER("There is no tracer defined for this element.", "");
	
 	private String message;
	private String detail;

	private ErrorMessage(String message, String detail) {
		this.message = message;
		this.detail = detail;
	}
	
	public String getMessage() {
		return message;
	}

	public String getDetail() {
		return detail;
	}
}
