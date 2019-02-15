package arrayscript.lang;

/**
 * Reserved keywords of the language, those keywords can not be used for element names. More keywords
 * will be added later.
 * @author knokko
 *
 */
public enum Keyword {
	
	/*
	 * Keywords to define something.
	 */
	NAMESPACE,
	CLASS,
	INTERFACE,
	ENUM,
	INIT,
	MAIN,
	
	/*
	 * Class-related keywords
	 */
	STATIC,
	CONSTRUCTOR,
	GETTER,
	SETTER,
	
	
	/*
	 * Variable types
	 */
	ANY,
	BOOLEAN,
	NUMBER,
	STRING,
	FUNCTION,
	INT8,
	INT16,
	INT32,
	UINT8,
	UINT16,
	UINT32,
	
	/*
	 * Modifiers
	 */
	CONST,
	DEFINE,
	OPEN,
	CLOSED,
	
	/*
	 * Other
	 */
	SCOPE,
}