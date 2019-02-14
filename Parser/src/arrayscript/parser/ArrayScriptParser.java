package arrayscript.parser;

import java.io.File;
import java.io.IOException;

import arrayscript.lang.Application;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.source.reading.SourceFilesReader;
import arrayscript.parser.source.reading.SourceFolderReader;
import arrayscript.parser.util.ParsingException;

public class ArrayScriptParser {
	
	/**
	 * Attempts to parse the source files located in the given directory. If it succeeds, an ASApplication
	 * instance will be returned that contains all data about the application. If it fails, a
	 * ParsingException will be thrown that should contain the reason the parsing failed.
	 * @param sourcesDirectory The directory where all source files should be in
	 * @return An instance of ASApplication representing the parsed source files
	 * @throws ParsingException If the parsing failed
	 */
	public static Application parse(File sourcesDirectory) throws ParsingException {
		SourceFilesReader sourceFiles = new SourceFolderReader(sourcesDirectory);
		try {
			AppBuilder application = new AppBuilder();
			SourceFileReader reader = sourceFiles.next();
			while (reader != null) {
				processSourceFile(reader, application);
				reader = sourceFiles.next();
			}
			return application.build();
		} catch (IOException ioex) {
			throw new ParsingException("An IO error occured", ioex);
		}
	}
	
	private static void processSourceFile(SourceFileReader reader, AppBuilder app) throws ParsingException, IOException {
		NamespaceParser.parseNamespace(reader, app, app.getGlobalNamespace());
	}
}