package arrayscript.lang.var.type;

import arrayscript.lang.var.value.Value;

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
	
	/**
	 * Determines whether or not this type is a primitive type of the language. Primitive types can be parsed
	 * earlier than custom types.
	 * @return true if this is a primitive type, false if not
	 */
	boolean isPrimitive();
	
	/**
	 * @return the name of the type, as it should appear in the source code
	 */
	String getName();
}