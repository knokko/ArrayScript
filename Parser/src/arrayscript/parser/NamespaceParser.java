package arrayscript.parser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.ElementType;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.NamespaceBuilder;
import arrayscript.parser.util.ParsingException;
import arrayscript.parser.util.reading.SourceFileReader;

public class NamespaceParser {
	
	/**
	 * Attempts to parse the content of a single namespace. This method must be called after the name of the
	 * namespace and the '{' have been read. The read name should be used to create the NamespaceBuilder
	 * instance that is required for this method. The namespace does not necessarily have to be empty, if it
	 * is not empty, the parsed content will be added to it. After the content is parsed, the closing '}' will
	 * be read.
	 * @param reader The reader that should be used to read the content
	 * @param app The instance of the AppBuilder that is being used, it will be used to link certain elements
	 * if they are referred to.
	 * @param namespace The namespace builder where all parsed content will be added to
	 * @throws ParsingException If the provided source can not be parsed
	 * @throws IOException if the provided reader throws an IOException
	 */
	public static void parseNamespace(SourceFileReader reader, AppBuilder app, NamespaceBuilder namespace) throws ParsingException, IOException {
		while (true) {
			
			// This is probably the type of the element that is about to be declared
			String typeName = reader.next();
			
			// Check if this is not the end
			if (typeName == null || typeName.equals("}")) {
				break;
			}
			
			Set<Modifier> modifiers = new HashSet<Modifier>(1);
			
			// All modifiers should be declared before the type (if there are any)
			while (Modifier.isModifier(typeName)) {
				
				// Don't allow duplicate modifiers
				if (!modifiers.add(Modifier.getByWord(typeName))) {
					throw new ParsingException("Duplicated modifier " + typeName);
				}
				
				typeName = reader.next();
			}
			
			ElementType type = ElementTypes.getByName(typeName);
			
			// type names that do not have special meaning in source code will be treated as normal names
			if (type == null || !type.shouldAppearInSource()) {
				
				// Read the name of the variable/function
				String name = reader.next();
				if (name == null) {
					throw new ParsingException("The type " + typeName + " is not defined correctly");
				}
				
				// If a function is being declared, the 'name' will contain the brackets and parameters
				int indexLeftBracket = name.indexOf('(');
				if (indexLeftBracket == -1) {
					
					// This is a variable
					if (name.endsWith(";")) {
						
						// This is the end of the declaration
						name = name.substring(0, name.length() - 1);
					} else {
						
						// An initial value is given
					}
				} else {
					
					// This is a function
				}
			} else {
				if (type.needsName()) {
					
					// Observe that the name and opening curly bracket are read here
					String name = reader.next();
					String openCurly = reader.next();
					if (name == null || !"{".equals(openCurly)) {
						throw new ParsingException(typeName + " " + name + " is not defined correctly");
					}
					
					// I hate switch
					if (type == ElementTypes.NAMESPACE) {
						parseNamespace(reader, app, namespace.createNamespace(name, modifiers));
					} else if (type == ElementTypes.CLASS) {
						// TODO parse class
					} else if (type == ElementTypes.INTERFACE) {
						throw new ParsingException("Interfaces are not high on my priority list");
					} else if (type == ElementTypes.ENUM) {
						// TODO parse enum
					} else if (type == ElementTypes.INIT) {
						// TODO parse code block and register init
					} else if (type == ElementTypes.MAIN) {
						// TODO parse code block and register main
					} else {
						throw new ParsingException("Did I forget a named type that should be in source?" + type.getClass().getName());
					}
				} else {
					
					// No name, so read the next curly bracket and start the parsing
					String openCurly = reader.next();
					if (!"{".equals(openCurly)) {
						throw new ParsingException(typeName + " is not defined correctly");
					}
					
					// Ehm... well... I initially designed main and init not to have names
					
					throw new ParsingException("All current types need names, but this type is " + type.getClass().getName());
				}
			}
		}
	}
}