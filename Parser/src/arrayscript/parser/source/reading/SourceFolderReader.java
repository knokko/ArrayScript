package arrayscript.parser.source.reading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import arrayscript.lang.ArrayScript;

public class SourceFolderReader implements SourceFilesReader {

	private FolderProgress currentFolder;

	public SourceFolderReader(File sourceFolder) {
		if (!sourceFolder.isDirectory()) {
			throw new IllegalArgumentException("sourceFolder (" + sourceFolder + ") is not a directory!");
		}
		this.currentFolder = new FolderProgress(null, sourceFolder.listFiles());
	}
	
	private Scanner findNextScanner() throws FileNotFoundException {
		File nextFile = null;
		while (true) {
			while (currentFolder.fileIndex >= currentFolder.children.length) {
				if (currentFolder.parent == null) {

					// If this occurs, it means that we have read all source files and that we are
					// done.
					return null;
				}
				currentFolder = currentFolder.parent;
			}
			nextFile = currentFolder.children[currentFolder.fileIndex++];

			// Dive in the folder hierarchy until we find a real file
			if (nextFile.isDirectory()) {
				currentFolder = new FolderProgress(currentFolder, nextFile.listFiles());
			} else {
				if (nextFile.isFile() && nextFile.getName().endsWith(ArrayScript.SOURCE_FILE_NAME_END)) {

					// We found a source file
					break;
				} else {

					// Skip this file
					System.out.println("Skipped " + nextFile);
				}
			}
		}

		// Now that we found the next file, let's start reading it...
		return new Scanner(nextFile, StandardCharsets.UTF_8.name());
	}

	@Override
	public SourceFileReader next() throws IOException {
		Scanner nextScanner = findNextScanner();
		if (nextScanner != null) {
			return new SimpleSourceFileReader(new DefaultSourceFileReader3(new DefaultSourceFileReader2(new SimpleSourceFileReader1(nextScanner))));
		} else {
			return null;
		}
	}

	private static class FolderProgress {

		private FolderProgress parent;
		private File[] children;

		private int fileIndex;

		private FolderProgress(FolderProgress parent, File[] children) {
			this.parent = parent;
			this.children = children;
		}
	}
}