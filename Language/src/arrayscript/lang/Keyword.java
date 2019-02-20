package arrayscript.lang;

import arrayscript.lang.var.type.Type;
import arrayscript.lang.element.ElementType;
import arrayscript.lang.element.ElementTypes;
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
	NAMESPACE(null, null, ElementTypes.NAMESPACE),
	CLASS(null, null, ElementTypes.CLASS),
	INTERFACE(null, null, ElementTypes.INTERFACE),
	ENUM(null, null, ElementTypes.ENUM),
	INIT(null, null, ElementTypes.INIT),
	MAIN(null, null, ElementTypes.MAIN),
	
	/*
	 * Class-related keywords
	 */
	STATIC(Modifier.STATIC, null, null),
	CONSTRUCTOR(null, null, ElementTypes.CONSTRUCTOR),
	GETTER(null, null, ElementTypes.GETTER),
	SETTER(null, null, ElementTypes.SETTER),
	NEW(null, null, null),
	
	
	/*
	 * Variable types
	 */
	ANY(null, PrimitiveTypes.ANY, null),
	BOOLEAN(null, PrimitiveTypes.BOOLEAN, null),
	NUMBER(null, PrimitiveTypes.NUMBER, null),
	STRING(null, PrimitiveTypes.STRING, null),
	FUNCTION(null, PrimitiveTypes.FUNCTION, null),
	INT8(null, PrimitiveTypes.INT8, null),
	INT16(null, PrimitiveTypes.INT16, null),
	INT32(null, PrimitiveTypes.INT32, null),
	UINT8(null, PrimitiveTypes.UINT8, null),
	UINT16(null, PrimitiveTypes.UINT16, null),
	UINT32(null, PrimitiveTypes.UINT32, null),
	
	/*
	 * Modifiers
	 */
	CONST(Modifier.CONST, null, null),
	DEFINE(Modifier.DEFINE, null, null),
	OPEN(Modifier.OPEN, null, null),
	PRIVATE(Modifier.PRIVATE, null, null),
	PROTECTED(Modifier.PROTECTED, null, null),
	
	/*
	 * Code block keywords
	 */
	SCOPE(null, null, null),
	IF(null, null, null),
	ELSE(null, null, null),
	WHILE(null, null, null),
	FOR(null, null, null),
	
	/*
	 * Javascript-only
	 */
	WINDOW(null, null, null),
	DOCUMENT(null, null, null),
	
	/*
	 * Other keywords
	 */
	IMPORT(null, null, null)
	;
	
	private final Modifier modifier;
	private final Type primitiveType;
	private final ElementType elementType;
	
	Keyword(Modifier modifier, Type primitiveType, ElementType elementType){
		this.modifier = modifier;
		
		if (primitiveType != null && !primitiveType.isPrimitive()) {
			throw new IllegalArgumentException("The provided primitive type should be primitive");
		}
		this.primitiveType = primitiveType;
		
		if (elementType != null && !elementType.shouldAppearInSource()) {
			throw new IllegalArgumentException("The element type " + elementType + " should not appear in source code");
		}
		this.elementType = elementType;
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
	 * @return true if this keyword is a primitive type, false otherwise
	 */
	public boolean isType() {
		return primitiveType != null;
	}
	
	/**
	 * Determines whether this keyword is an element type, or not
	 * @return true if this keyword is an element type, false otherwise
	 */
	public boolean isElementType() {
		return elementType != null;
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
	
	/**
	 * Gets the element type that belongs to this keyword. If this keyword is not an element type, an
	 * UnsupportedOperationException will be thrown.
	 * @return The element type that belongs to this keyword
	 * @throws UnsupportedOperationException If this keyword is not an element type
	 */
	public ElementType getElementType() throws UnsupportedOperationException {
		if (elementType == null) {
			throw new UnsupportedOperationException("This keyword (" + name() + ") is not an element type");
		}
		return elementType;
	}
}