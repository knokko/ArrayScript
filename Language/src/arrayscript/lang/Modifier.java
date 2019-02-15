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
	 * A constant namespace can't be expanded.
	 */
	CONST,
	
	/**
	 * Variables that have the define keyword are also constant, but must be defined directly in the code.
	 * References to variables with the define keyword will literally be replaced by the value of the variable
	 * right before the actual compilation and the variable itself will not be compiled. For instance, when a
	 * define variable has a value of 5, all places in the code that refer to it will be replaced by 5 right
	 * before the actual compilation and the variable itself will not be compiled.
	 * 
	 * All enum constants of enums with the define keyword will be assigned a unique integer. All references
	 * to the enum constants will be replaced by that number right before compilation.
	 * 
	 * Calls to functions with the define keywords will more or less be replaced with the function body. How
	 * this is done exactly is to determine by the compiler.
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
	 * Namespaces can be declared open. Open namespaces can be expanded in another file. So you don't
	 * necessarily need a massive source file for a massive namespace.
	 */
	OPEN,
	
	/**
	 * I forgot what this was for
	 */
	CLOSED;
	
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
	 * Checks if a single element can have both modifiers. Returns true if possible and false if impossible.
	 * @param mod1 A modifier
	 * @param mod2 The other modifier
	 * @return true if an element can have both modifiers, false if not
	 */
	public static boolean isCompatible(Modifier mod1, Modifier mod2) {
		return mod1 != DEFINE && mod2 != DEFINE;
	}
}