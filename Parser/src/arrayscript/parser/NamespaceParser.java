package arrayscript.parser;

import java.io.IOException;

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
			
			// This is probably the type of something
			String type = reader.next();
			
			// Check if this is not the end
			if (type == null || type.equals("}")) {
				break;
			}
			
			if (type.equals("namespace")) {
				
				// Observe that the name and opening curly bracket are read
				String name = reader.next();
				String openCurly = reader.next();
				if (name == null || !"{".equals(openCurly)) {
					throw new ParsingException("Namespace " + name + " is not defined correctly");
				}
				
				// Create or expand the nested namespace
				NamespaceBuilder nestedNamespace = namespace.createNamespace(name);
				parseNamespace(reader, app, nestedNamespace);
				
				// The closing curly bracket will be read by the call to parseNamespace in one of the ifs
			} else if (type.equals("class")) {
				
				// Observe that the name and opening curly bracket are read
				String name = reader.next();
				String openCurly = reader.next();
				if (name == null || !"{".equals(openCurly)) {
					throw new ParsingException("Class " + name + " is not defined correctly");
				}
			}
		}
	}
}