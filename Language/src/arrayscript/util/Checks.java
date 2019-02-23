package arrayscript.util;

import java.util.Collection;

/**
 * Utility class that contains fast checks for bad parameters like null values
 * @author knokko
 *
 */
public class Checks {
	
	/**
	 * Checks if the given element is null, throws an NPE if so, returns silently otherwise.
	 * @param element The element to check
	 * @param name The name to display in the NPE if the element happens to be null
	 * @throws NullPointerException if the given element is null
	 */
	public static void notNull(Object element, String name) throws NullPointerException {
		if (element == null) {
			throw new NullPointerException(name + " is null");
		}
	}
	
	/**
	 * Checks if the given element is null, throws an NPE if so, returns silently otherwise.
	 * @param element The element to check
	 * @throws NullPointerException if the given element is null
	 */
	public static void notNull(Object element) throws NullPointerException {
		notNull(element, "element");
	}
	
	/**
	 * Checks if the given array is null or contains null elements. Throws an NPE if so, returns silently
	 * otherwise.
	 * @param elements The array to check
	 * @param name The name to display in the NPE if the array or an element of the array happens to be null
	 * @throws NullPointerException If the array or an element in the array is null
	 */
	public static void noNull(Object[] elements, String name) throws NullPointerException {
		notNull(elements, name);
		for (int index = 0; index < elements.length; index++) {
			if (elements[index] == null) {
				throw new NullPointerException(name + "[" + index + "] is null");
			}
		}
	}
	
	/**
	 * Checks if the given array is null or contains null elements. Throws an NPE if so, returns silently
	 * otherwise.
	 * @param elements The array to check
	 * @throws NullPointerException If the array or an element in the array is null
	 */
	public static void noNull(Object[] elements) throws NullPointerException {
		noNull(elements, "elements");
	}
	
	/**
	 * Checks if the given collection is null or contains null elements. Throws a NullPointerException if so,
	 * returns silently if not.
	 * @param elements The collection to check
	 * @param name The name to add to the exception message when the collection is null or contains null
	 * @throws NullPointerException If the given collection is null or contains null
	 */
	public static void noNull(Collection<?> elements, String name) throws NullPointerException {
		notNull(elements, name);
		for (Object element : elements) {
			if (element == null) {
				throw new NullPointerException(name + " contains null");
			}
		}
	}
	
	/**
	 * Checks if the given collection is null or contains null elements. Throws a NullPointerException if so,
	 * returns silently if not.
	 * @param elements The collection to check
	 * @throws NullPointerException If the given collection is null or contains null
	 */
	public static void noNull(Collection<?> elements) throws NullPointerException {
		noNull(elements, "elements");
	}
}