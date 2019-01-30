package arrayscript.lang.var.type;

import arrayscript.lang.value.Value;

/**
 * All types that ArrayScript variables and properties can have must implement this interface.
 * @author knokko
 *
 */
public interface Type {
	
	/**
	 * Determines if a variable of this type can have the specified value.
	 * @param value The value to check
	 * @return true if a variable of this type can have the specified value, false if not
	 */
	boolean canHaveValue(Value value);
}