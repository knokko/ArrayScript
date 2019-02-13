package arrayscript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.ElementType;
import arrayscript.lang.element.ElementTypes;
import arrayscript.lang.var.type.PrimitiveTypes;
import arrayscript.lang.var.type.Type;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.NamespaceBuilder;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.source.reading.SourceFileReader1;
import arrayscript.parser.util.ParsingException;
import arrayscript.util.ArrayHelper;

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
	public static void parseNamespace(SourceFileReader1 reader, AppBuilder app, NamespaceBuilder namespace) throws ParsingException, IOException {
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
				
				// If the type is primitive, we can easily confirm it already
				// If not, we will check for the declaration in a later stage
				Type primitiveType = PrimitiveTypes.getByName(typeName);
				TypeBuilder typeBuilder;
				if (primitiveType == null) {
					typeBuilder = new TypeBuilder(typeName);
				} else {
					typeBuilder = new TypeBuilder(primitiveType);
				}
				
				// Read the name of the variable/function
				String name = reader.next();
				if (name == null) {
					throw new ParsingException("The type " + typeName + " is not defined correctly");
				}
				
				// If a function is being declared, the 'name' will contain the brackets and parameters
				int indexLeftBracket = name.indexOf('(');
				if (indexLeftBracket == -1) {
					
					// Should be followed by the equals sign
					String equals = reader.next();
					if (!"=".equals(equals)) {
						throw new ParsingException("The variable " + name + " of type " + typeName + " is not declared correctly.");
					}
					
					// Everything until the ';' should be the unparsed initial value
					List<String> unparsedValueList = new ArrayList<String>();
					String partOfValue = reader.next();
					
					if (partOfValue == null) {
						throw new ParsingException("The end of file was reached before " + typeName + " " + name + " started its initial value");
					}
					
					while (!partOfValue.endsWith(";")) {
						
						unparsedValueList.add(partOfValue);
						partOfValue = reader.next();
						
						// Throw a ParsingException instead of NullPointerException
						if (partOfValue == null) {
							throw new ParsingException("The variable " + name + " of type " + typeName + " has an unfinished intial value.");
						}
					}
					
					String unparsedValue = ArrayHelper.concatenate(unparsedValueList);
					
					namespace.createVariable(name, typeBuilder, new ValueBuilder(unparsedValue));
				} else {
					
					// This is a function, so lets gather the parameters
					String params = name.substring(indexLeftBracket);
					
					List<String> paramsList = new ArrayList<String>();
					while (!params.contains("{")) {
						paramsList.add(params);
						params = reader.next();
					}
					
					// It is possible that there is something right before the {
					if (!params.startsWith("{")) {
						paramsList.add(params.substring(0, params.indexOf('{')));
					}
					
					// Observe that this includes the opening ( and the closing )
					String fullParams = ArrayHelper.concatenate(paramsList);
					
					// This is the actual name, the previous name included a part of the parameters
					name = name.substring(0, indexLeftBracket);
					
					ParamsBuilder parameters = ParamsParser.parse(fullParams);
					
					// TODO parse parameters and add the function to the namespace
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