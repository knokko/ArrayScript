package arrayscript.lang.element;

import java.util.Arrays;

import arrayscript.util.Checks;

/**
 * ArrayScript elements can be defined in namespaces (possibly in the global namespace). They
 * can be accessed by using NameSpace.ElementName. Anything in the global namespace can be accessed by
 * just its ElementName.
 * @author knokko
 *
 */
public class Namespace implements Element {
	
	private final String name;
	
	private final Element[] elements;
	
	public Namespace(String name, Element[] elements) {
		this.name = name;
		Checks.noNull(elements, "elements");
		this.elements = elements;
	}
	
	public boolean isGlobal() {
		return name == null;
	}
	
	/**
	 * Gets the name of this namespace. Throws an UnsupportedOperationException if this is the global
	 * namespace.
	 * @return the name of this namespace
	 * @throws UnsupportedOperationException if this namespace is global
	 */
	public String getName() throws UnsupportedOperationException {
		if (name == null) {
			throw new UnsupportedOperationException("Attempted to get name of global namespace");
		}
		return name;
	}
	
	/**
	 * The returned array contains all elements that are directly in this namespace. The array is a copy of
	 * the element array of this namespace, but the elements in the array are not.
	 * @return a copy of the array containing the elements of this namespace
	 */
	public Element[] getElements() {
		return Arrays.copyOf(elements, elements.length);
	}
}