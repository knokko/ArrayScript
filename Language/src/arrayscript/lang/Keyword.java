package arrayscript.lang;

import arrayscript.lang.var.type.Type;
import arrayscript.lang.var.type.PrimitiveTypes;

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
	NAMESPACE(null, null),
	CLASS(null, null),
	INTERFACE(null, null),
	ENUM(null, null),
	INIT(null, null),
	MAIN(null, null),
	
	/*
	 * Class-related keywords
	 */
	STATIC(Modifier.STATIC, null),
	CONSTRUCTOR(null, null),
	GETTER(null, null),
	SETTER(null, null),
	
	
	/*
	 * Variable types
	 */
	ANY(null, PrimitiveTypes.ANY),
	BOOLEAN(null, PrimitiveTypes.BOOLEAN),
	NUMBER(null, PrimitiveTypes.NUMBER),
	STRING(null, PrimitiveTypes.STRING),
	FUNCTION(null, PrimitiveTypes.FUNCTION),
	INT8(null, PrimitiveTypes.INT8),
	INT16(null, PrimitiveTypes.INT16),
	INT32(null, PrimitiveTypes.INT32),
	UINT8(null, PrimitiveTypes.UINT8),
	UINT16(null, PrimitiveTypes.UINT16),
	UINT32(null, PrimitiveTypes.UINT32),
	
	/*
	 * Modifiers
	 */
	CONST(Modifier.CONST, null),
	DEFINE(Modifier.DEFINE, null),
	OPEN(Modifier.OPEN, null),
	CLOSED(Modifier.CLOSED, null),
	
	/*
	 * Other
	 */
	SCOPE(null, null);
	
	private final Modifier modifier;
	private final Type primitiveType;
	
	Keyword(Modifier modifier, Type primitiveType){
		this.modifier = modifier;
		
		if (!primitiveType.isPrimitive()) {
			throw new IllegalArgumentException("The provided primitive type should be primitive");
		}
		this.primitiveType = primitiveType;
	}
	
	/**
	 * Determines whether this keyword is a modifier, or not
	 * @return true if this keyword is a modifier, false otherwise
	 */
	public boolean isModifier() {
		return modifier != null;
	}
	
	/**
	 * Determines whether this keyword is a primitive type, or not
	 * @return the if this keyword is a primitive type, false otherwise
	 */
	public boolean isType() {
		return primitiveType != null;
	}
	
	/**
	 * Gets the modifier that belongs to this keyword. If this keyword is not a modifier, an
	 * UnsupportedOperationException will be thrown.
	 * @return The modifier that belongs to this keyword
	 * @throws UnsupportedOperationException If this keyword is not a modifier
	 */
	public Modifier getModifier() throws UnsupportedOperationException {
		if (modifier == null) {
			throw new UnsupportedOperationException("This keyword (" + name() + ") is not a modifier");
		}
		return modifier;
	}
	
	/**
	 * Gets the primitive type that belongs to this keyword. If this keyword is not a primitive type, an
	 * UnsupportedOperationException will be thrown.
	 * @return The primitive type that belongs to this keyword
	 * @throws UnsupportedOperationException If this keyword is not a primitive type
	 */
	public Type getPrimitiveType() throws UnsupportedOperationException {
		if (primitiveType == null) {
			throw new UnsupportedOperationException("This keyword (" + name() + ") is not a keyword");
		}
		return primitiveType;
	}
}