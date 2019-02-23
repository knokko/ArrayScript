package arrayscript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.Operator;
import arrayscript.lang.element.ElementType;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.util.ParsingException;

/**
 * A utility class that parses fragments of source code. This class is useful because it keeps the namespace
 * parser and class parser cleaner. Also, the namespace parser and class parser need to do a lot of similar
 * operations, so it is useful that they are programmed once in this class rather than in both the class
 * parser and namespace parser.
 * @author knokko
 *
 */
public class SmallParser {
	
	/**
	 * Reads all modifiers at this point until the reader reads a source element that is not
	 * a modifier. Because we need to read the next source element in order to determine whether it is a
	 * modifier or not, the first source element after the last modifier will be read by this method as well.
	 * That next source element can be obtained by calling getNext() on the result of this method. The result
	 * of this method will be an instance of ModResult that contains the collection of modifiers and the
	 * source element after the last modifier.
	 * @param reader The reader to read the modifiers from
	 * @return A ModResult containing the modifiers and the next source element
	 * @throws IOException If the reader thrown an IOException
	 * @throws ParsingException If the reader throws a ParsingException of if the end of file is reached
	 */
	public static ModResult parseModifiers(SourceFileReader reader) throws IOException, ParsingException {
		
		// Store all modifiers here
		Set<Modifier> modifiers = new HashSet<Modifier>(2);
		
		// Returning when the next element is not a modifier is easier than a loop terminate condition
		while (true) {
			SourceElement next = reader.next();
			
			if (next == null) {
				throw new ParsingException("End of file was reached while reading modifiers");
			}
			
			// If it is a modifier, add the modifier. If not, terminate the loop
			if (next.isKeyword() && next.getKeyword().isModifier()) {
				
				// Don't allow the same element to have the same modifier twice
				if (!modifiers.add(next.getKeyword().getModifier())) {
					throw new ParsingException("Duplicate modifier " + next.getKeyword().getModifier());
				}
			} else {
				
				// The next source element is no modifier, so we have had all modifiers
				return new ModResult(modifiers, next);
			}
		}
	}
	
	/**
	 * This class contains a collection of modifiers and a source element. It was created for the method
	 * parseModifiers because this method needs to return 2 objects rather than just 1. The collection of
	 * modifiers contains all modifiers that were read and next is the source element that came after the
	 * last modifier.
	 * @author knokko
	 *
	 */
	public static class ModResult {
		
		private final Set<Modifier> modifiers;
		private final SourceElement next;
		
		private ModResult(Set<Modifier> modifiers, SourceElement next) {
			this.modifiers = modifiers;
			this.next = next;
		}
		
		public Set<Modifier> getModifiers(){
			return modifiers;
		}
		
		/**
		 * This method returns the source element that came after the last modifier, so this is NOT a modifier!
		 * @return The source element that comes after the last read modifier
		 */
		public SourceElement getNext() {
			return next;
		}
	}
	
	/**
	 * Reads all source elements from the reader until the semicolon. The semicolon will not be added to the
	 * returned list, but it will be read already. (So the next call to reader.next() after this method has
	 * returned will NOT read the semicolon.) The elements that were read first will be at the start of the
	 * list and the elements right before the semicolon will be at the end of the list.
	 * @param reader The source reader to read from
	 * @return A list containing all source elements until the semicolon in the order they were read
	 * @throws IOException If the reader throws an IOException
	 * @throws ParsingException If the reader throws a ParsingException or if the end of file is reached
	 */
	public static List<SourceElement> readUntilSemiColon(SourceFileReader reader) throws IOException, ParsingException {
		
		// Store all source elements here
		List<SourceElement> elements = new ArrayList<SourceElement>(3);
		
		// Returning upon encountering semicolon is easier than using a proper loop condition
		while (true) {
			SourceElement next = reader.next();
			
			if (next == null) {
				throw new ParsingException("End of file was reached before the ';' was found");
			}
			
			// Check if this is the semicolon
			// This will skip semicolons within strings because the reader takes care of strings
			if (next.isOperator() && next.getOperator() == Operator.SEMICOLON) {
				return elements;
			}
			
			// Intentionally don't add the semicolon to the list
			// If next was a semicolon, we would have returned already
			elements.add(next);
		}
	}
	
	public static SomeType parseSomeType(SourceFileReader reader) throws IOException, ParsingException {
		
		SourceElement first = reader.next();
		
		// Primitive type or element type
		if (first.isKeyword()) {
			
			// It's an element type
			if (first.getKeyword().isElementType()) {
				return new SomeType(first.getKeyword().getElementType(), reader.next());
			}
			
			// It's a primitive type
			else if (first.getKeyword().isType()) {
				return new SomeType(new TypeBuilder(first.getKeyword().getPrimitiveType()), reader.next());
			}
			
			// Not a type or element type
			else {
				throw new ParsingException("Unexpected keyword " + first);
			}
		}
		
		// A class created by the programmer
		else if (first.isWord()) {
			
			// The type could be something like Namespace1.Namespace2.SomeClass, so read all dots...
			List<String> chain = new ArrayList<String>(1);
			chain.add(first.getWord());
			
			// Loop conditions are more annoying than returning once we are done
			while (true) {
				
				SourceElement maybeDot = reader.next();
				
				// End of file is reached or the last part of the type name is not followed by a dot
				// Because end of file can be allowed for rare cases, we will allow it here
				if (maybeDot == null || !(maybeDot.isOperator() && maybeDot.getOperator() == Operator.PROPERTY)) {
					String[] types = new String[chain.size()];
					chain.toArray(types);
					return new SomeType(new TypeBuilder(types), maybeDot);
				}
				
				SourceElement nextPart = reader.next();
				
				if (nextPart == null) {
					throw new ParsingException("Expected property, but end of file was reached");
				}
				
				if (!nextPart.isWord()) {
					throw new ParsingException("Expected property, but found " + nextPart);
				}
				
				chain.add(nextPart.getWord());
			}
		}
		
		// Not a type or element type
		else {
			throw new ParsingException("Expected a type, but found " + first);
		}
	}
	
	/**
	 * This class represents a type that is either a variable type or an element type. Variable types are
	 * types that a variable can have, like number, string and boolean. It is also possible that this type
	 * holds the void return type. Element types are types of the elements of the ArrayScript language, like
	 * class, namespace and variable.
	 * 
	 * This class was created for the method parseSomeType. It also contains the next source element that
	 * was read because parseSomeType needs to read an extra source element.
	 * @author knokko
	 *
	 */
	public static class SomeType {
		
		private final TypeBuilder variableType;
		private final ElementType elementType;
		
		private final SourceElement next;
		
		private SomeType(TypeBuilder type, SourceElement next) {
			this.variableType = type;
			this.elementType = null;
			this.next = next;
		}
		
		private SomeType(ElementType type, SourceElement next) {
			this.variableType = null;
			this.elementType = type;
			this.next = next;
		}
		
		/**
		 * @return True if this is a variable type, false if it is an element type or void
		 */
		public boolean isVariableType() {
			return variableType != null;
		}
		
		/**
		 * @return True if this is a valid function/method return type, false if it is an element type
		 */
		public boolean isReturnType() {
			return !isElementType();
		}
		
		/**
		 * @return True if this is an element type, false if it as a variable type or void
		 */
		public boolean isElementType() {
			return elementType != null;
		}
		
		/**
		 * @return The variable type that this type holds if this type holds variable type
		 * @throws UnsupportedOperationException If this type doesn't hold a variable type, but an element 
		 * type or void return type
		 */
		public TypeBuilder getVariableType() throws UnsupportedOperationException {
			if (!isVariableType()) {
				throw new UnsupportedOperationException("This is not a variable type");
			}
			return variableType;
		}
		
		/**
		 * @return The return type that this type holds if this type holds a return type or null if this is
		 * the void return type
		 * @throws UnsupportedOperationException If this type doesn't hold a return type, but an element type
		 */
		public TypeBuilder getReturnType() throws UnsupportedOperationException {
			if (!isReturnType()) {
				throw new UnsupportedOperationException("This is not a return type");
			}
			return variableType;
		}
		
		/**
		 * @return The element type that this type holds if this type holds an element type
		 * @throws UnsupportedOperationException If this type doesn't hold an element type, but a variable type
		 */
		public ElementType getElementType() throws UnsupportedOperationException {
			if (!isElementType()) {
				throw new UnsupportedOperationException("This is not an element type");
			}
			return elementType;
		}
		
		/**
		 * The parseSomeType method reads too many source elements because it needs to determine whether
		 * every part of the type name is followed by a dot, or not. This means that always 1 extra source
		 * element will be read from the reader. That extra source element will be returned by this method.
		 * If the type was followed directly by the end of this source file, this method will return null.
		 * @return The source element that comes right after the type name
		 */
		public SourceElement getNext() {
			return next;
		}
		
		@Override
		public String toString() {
			if (isElementType()) {
				return elementType.toString();
			} else {
				return variableType.toString();
			}
		}
	}
}