package arrayscript.lang;

import arrayscript.util.ArrayHelper;

/**
 * Representations for the modifiers arrayscript elements can have.
 * @author knokko
 *
 */
public enum Modifier {
	
	/**
	 * A constant variable can't change its value. 
	 * A constant class or interface can't be extended.
	 */
	CONST,
	
	/**
	 * The define keyword can be used on variables, functions, classes and enums
	 * 
	 * Variables that have the define keyword are also constant, but must be defined directly in the code.
	 * References to variables with the define keyword will literally be replaced by the value of the variable
	 * right before the actual compilation and the variable itself will not be compiled. For instance, when a
	 * define variable has a value of 5, all places in the code that refer to it will be replaced by 5 right
	 * before the actual compilation and the variable itself will not be compiled.
	 * 
	 * Calls to functions with the define keywords will more or less be replaced with the function body. How
	 * this is done exactly is to determine by the compiler.
	 * 
	 * Classes can have the define keyword if they have at most 1 property. During compilation, instances
	 * of the class will be replaced by their single property rather than keeping a proper instance. If the
	 * class doesn't have any properties, it will be passed around as 0 and method calls will be replaced by
	 * simple function calls.
	 * 
	 * All enum constants of enums with the define keyword will be assigned a unique integer. All references
	 * to the enum constants will be replaced by that number right before compilation.
	 * 
	 * As you might have noticed, this keyword is mostly made for micro-performance operations.
	 */
	DEFINE,
	
	/**
	 * The static keyword is for class variables or class functions. This keyword indicates that the variable
	 * or function can be accessed without the need for an instance of the given class. Static elements in a
	 * class act the same as 'normal' elements in a namespace.
	 */
	STATIC,
	
	/**
	 * The implicit keyword can be used for constructors with 1 parameter, getters and setters.
	 * 
	 * Constructors with this modifier can be invoked by specifying the value of the parameter without 
	 * using the new keyword in some circumstances. That is, when the value of a variable with the class 
	 * as its type is about to be assigned.
	 * 
	 * Getters with this modifier can be invoked by simply using classInstance.variableName rather than
	 * classInstance.getVariableName(). Setters with this modifier can be invoked by simply using
	 * classInstance.variableName = newValue rather than classInstance.setVariableName(newValue).
	 */
	IMPLICIT,
	
	/**
	 * Namespaces can be declared open. Open namespaces can be expanded in another file. So you don't
	 * necessarily need a massive source file for a massive namespace.
	 */
	OPEN,
	
	/**
	 * The private modifier can be used on variables and functions to make sure they can only be accessed
	 * from the namespace or class that owns the variable or function.
	 */
	PRIVATE,
	
	/**
	 * The protected modifier can be used on variables and functions to make sure they can not be accessed
	 * from everywhere. They can be accessed from the class or namespace that declares them, from anything
	 * that is defined inside the class or namespace where the variable or function is defiend or by
	 * subclasses of the class where the modifier is defined (if it is defined in a class).
	 */
	PROTECTED;
	
	private static final String[] LOWERCASE;
	
	static {
		Modifier[] values = values();
		LOWERCASE = new String[values.length];
		for (int index = 0; index < LOWERCASE.length; index++) {
			LOWERCASE[index] = values[index].name().toLowerCase();
		}
	}
	
	/**
	 * Checks if the given word should be interpreted as modifier, that is when the word exactly equals the
	 * lowercase name of a Modifier constant.
	 * @param word The word to check
	 * @return true if the word should be interpreted as modifier
	 */
	public static boolean isModifier(String word) {
		return ArrayHelper.contains(LOWERCASE, word);
	}
	
	/**
	 * Returns the modifier the given word should be interpreted as. This method should only be called after
	 * a call to isModifier returned true for the given word.
	 * @param word The word that represent a modifier
	 * @return the modifier the word should be interpreted as
	 */
	public static Modifier getByWord(String word) {
		return valueOf(word.toUpperCase());
	}
	
	/**
	 * Determines whether or not this is a visibility modifier. A modifier is considered to be a visibility
	 * modifier if it is either Modifier.PRIVATE or Modifier.PROTECTED.
	 * @return True if and only if this is a visibility modifier
	 */
	public boolean isVisibility() {
		return this == PRIVATE || this == PROTECTED;
	}
}