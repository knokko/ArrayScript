package arrayscript.lang.element;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * All the element types of the language.
 * @author knokko
 *
 */
public class ElementTypes {
	
	/**
	 * Represents an arrayscript namespace. A namespace can be used to declare other elements in.
	 */
	public static final ElementType.Namespace NAMESPACE = new ElementType.Namespace();
	
	/**
	 * Represents an arrayscript class. Classes can have properties and methods as well as variables and
	 * functions.
	 */
	public static final ElementType.Class CLASS = new ElementType.Class();
	
	/**
	 * I am planning to properly implement interfaces later
	 */
	public static final ElementType.Interface INTERFACE = new ElementType.Interface();
	public static final ElementType.Enum ENUM = new ElementType.Enum();
	
	/**
	 * A variable is 'something' in the application that has a type and a current value. The value of
	 * variables can be read and changed during execution of the program, unless it is declared const or
	 * define.
	 */
	public static final ElementType.Variable VARIABLE = new ElementType.Variable();
	public static final ElementType.Function FUNCTION = new ElementType.Function();
	
	/**
	 * A property is like a variable, but it belongs to an instance of the class it is defined in. It can
	 * not be accessed without an instance of its class and it can only be declared within a class.
	 */
	public static final ElementType.Property PROPERTY = new ElementType.Property();
	
	/**
	 * A method is like a function, but it belongs to a class and requires an instance of its class to be
	 * invoked on.
	 */
	public static final ElementType.Method METHOD = new ElementType.Method();
	
	/**
	 * An init is a named block of code that should be executed before the application truly begins. The
	 * program configuration can determine the order in which all the init blocks get executed.
	 */
	public static final ElementType.Init INIT = new ElementType.Init();
	
	/**
	 * A main is a named block of code that should start (a part of) the application. The program
	 * configuration determines which main blocks get executed first and which blocks get (not) executed.
	 */
	public static final ElementType.Main MAIN = new ElementType.Main();
	
	private static final Map<String,ElementType> NAME_MAP;
	
	static {
		Field[] fields = ElementTypes.class.getDeclaredFields();
		NAME_MAP = new HashMap<String,ElementType>(fields.length - 1);
		for (Field field : fields) {
			try {
				if (field.getType() != HashMap.class) {
					ElementType type = (ElementType) field.get(null);
					NAME_MAP.put(type.getName(), type);
				}
			} catch (Exception ex) {
				throw new Error(ex);
			}
		}
	}
	
	/**
	 * Gets an element type by its name. The result is guaranteed to be one of the constants of this class.
	 * @param name The name of the element type, as returned by the getName method or as defined by the
	 * programmer.
	 * @return The element type with the given name, or null if there is no element type with the given name
	 */
	public static ElementType getByName(String name) {
		return NAME_MAP.get(name);
	}
}