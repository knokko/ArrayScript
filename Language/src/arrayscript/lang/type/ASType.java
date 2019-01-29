package arrayscript.lang.type;

import arrayscript.lang.value.ASValue;

/**
 * All types that ArrayScript variables can have must implement this interface.
 * @author knokko
 *
 */
public interface ASType {
	
	/**
	 * Determines if a variable of this type can have the specified value.
	 * @param value The value to check
	 * @return true if a variable of this type can have the specified value, false if not
	 */
	boolean canHaveValue(ASValue value);
}