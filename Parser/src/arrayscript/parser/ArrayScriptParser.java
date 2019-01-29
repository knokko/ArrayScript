package arrayscript.parser;

import java.io.File;
import java.io.IOException;

import arrayscript.lang.ASApplication;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.util.ParsingException;
import arrayscript.parser.util.reading.SourceFileReader;
import arrayscript.parser.util.reading.SourceFilesReader;
import arrayscript.parser.util.reading.SourceFolderReader;

public class ArrayScriptParser {
	
	public static ASApplication parse(File sourcesDirectory) throws ParsingException {
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
	
	private static void processSourceFile(SourceFileReader reader, AppBuilder app) throws ParsingException {
		
	}
}