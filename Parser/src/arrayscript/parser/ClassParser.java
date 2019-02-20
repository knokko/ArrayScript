package arrayscript.parser;

import java.io.IOException;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.Operator;
import arrayscript.lang.element.ElementType;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.SmallParser.ModResult;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.ClassBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.HistorySourceFileReader;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.util.ParsingException;

public class ClassParser {
	
	/**
	 * Attempts to parse the content of a single class and adds the parsed content to the provided class
	 * builder. This method must be called after the name of the class and the opening '{' have been read. The
	 * read name should be used to create the class builder that should be provided as parameter to this
	 * method. After the content is read and parsed, the closing '}' will be read by this method.
	 * @param reader The reader to read the source code for this class from
	 * @param app The instance of the app builder
	 * @param classBuilder The class builder that this method should parse content for
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException or if the class is not
	 * defined correctly in the source code
	 */
	public static void parseClass(SourceFileReader reader, AppBuilder app, ClassBuilder classBuilder) throws IOException, ParsingException {
		
		// Breaking upon finding the closing '}' is easier than using a loop terminate condition
		while (true) {
			
			SourceElement first = reader.next();
			
			if (first == null) {
				throw new ParsingException("End of file was reached before class " + classBuilder.getName() + " was closed");
			}
			
			// break if the closing '}' is read
			if (first.isOperator()) {
				if (first.getOperator() == Operator.CLOSE_BLOCK) {
					break;
				} else {
					throw new ParsingException("Unexpected " + first);
				}
			}
			
			// the first should be a word or keyword
			else if (first.isWord() || first.isKeyword()) {
				
				// first is also a part of the modifiers, so use HistorySourceFileReader to pass it along
				ModResult foundModifiers = SmallParser.parseModifiers(new HistorySourceFileReader(reader, first));
				Set<Modifier> modifiers = foundModifiers.getModifiers();

				// Now that we have had all modifiers, the next source element(s) must be the type
				
				// parseModifiers consumes an extra source element, so use HistorySourceFileReader to pass
				// it along to the parseSomeType method
				SmallParser.SomeType nextType = SmallParser.parseSomeType(new HistorySourceFileReader(reader, foundModifiers.getNext()));

				// Distinguish between element types (class, namespace...) and variable types
				// (string,number...)
				if (nextType.isElementType()) {
					
					// The next type is an element type
					ElementType elementType = nextType.getElementType();
					if (elementType.needsName()) {
						
						SourceElement nameElement = nextType.getNext();
						
						if (nameElement == null) {
							throw new ParsingException("Name of " + elementType.getName() + " was expected, but end of file was reached");
						}
						
						if (!nameElement.isWord()) {
							throw new ParsingException("Name of " + elementType.getName() + " was expected, but found " + nameElement);
						}
						
						String name = nameElement.getWord();
						
						// I hate switch
						if (elementType == ElementTypes.CLASS) {
							throw new ParsingException("I think I will allow inner classes later");
						} else if (elementType == ElementTypes.ENUM) {
							throw new ParsingException("Enums are not very high on my priority list");
						} else if (elementType == ElementTypes.GETTER) {
							// TODO parse getter
							SourceElement colonOrCurly = reader.next();
							
							if (colonOrCurly == null) {
								throw new ParsingException("Expected a ';' or '{' after setter " + name + ", but end of file was reached");
							}
							
							if (colonOrCurly.isOperator() && colonOrCurly.getOperator() == Operator.SEMICOLON) {
								
								// A default getter is being defined
							} else if (colonOrCurly.isOperator() && colonOrCurly.getOperator() == Operator.OPEN_BLOCK) {
								
								// The programmer creates a getter with a custom body
							} else {
								throw new ParsingException("Expected a ';' or '{' after setter " + name + ", but found " + colonOrCurly);
							}
						} else if (elementType == ElementTypes.INIT) {
							throw new ParsingException("I think I will allow inits in classes later");
						} else if (elementType == ElementTypes.INTERFACE) {
							throw new ParsingException("Interfaces are not high on my priority list");
						} else if (elementType == ElementTypes.MAIN) {
							throw new ParsingException("I don't think I will allow mains in classes");
						} else if (elementType == ElementTypes.NAMESPACE) {
							throw new ParsingException("I think I will allow namespaces in classes later");
						} else if (elementType == ElementTypes.SETTER) {
							// TODO parse setter
						} else {
							throw new Error("It looks like I forgot element type " + elementType);
						}
					} else {
						
						if (elementType == ElementTypes.CONSTRUCTOR) {
							// TODO parse constructor
						} else {
							throw new Error("It looks like I forgot element type " + elementType);
						}
					}
				} else {
					// The next type is a variable type
				}
			}
			
			// Nothing else
			else {
				throw new ParsingException("Unexpected " + first);
			}
		}
	}
}