package arrayscript.parser.util.reading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import arrayscript.lang.ArrayScript;

public class SourceFolderReader implements SourceFilesReader {

	private static boolean isSeparator(char value) {
		return Character.isWhitespace(value);
	}

	private FolderProgress currentFolder;

	private Scanner currentScanner;
	private String currentLine;
	private int currentIndex;

	public SourceFolderReader(File sourceFolder) {
		if (!sourceFolder.isDirectory()) {
			throw new IllegalArgumentException("sourceFolder (" + sourceFolder + ") is not a directory!");
		}
		this.currentFolder = new FolderProgress(null, sourceFolder.listFiles());
	}

	private void skipCurrentLineWhitespaces() {
		// Find the next non-whitespace character
		while (currentIndex < currentLine.length() && isSeparator(currentLine.charAt(currentIndex))) {
			currentIndex++;
		}

		if (currentIndex >= currentLine.length()) {

			// End of line has been reached, so try to read the next line in the next if block
			currentLine = null;
		}
	}
	
	private void skipCurrentFileWhitespaces() {
		if (currentLine != null) {
			skipCurrentLineWhitespaces();
		}
		while (currentScanner != null && currentLine == null) {
			if (currentScanner.hasNextLine()) {

				// Start at the new line
				currentLine = currentScanner.nextLine();
				currentIndex = 0;
				skipCurrentLineWhitespaces();
			} else {

				// End of file has been reached, so try to read the next file in the next if block
				currentScanner.close();
				currentScanner = null;
			}
		}
	}
	
	private void findNextScanner() throws FileNotFoundException {
		File nextFile = null;
		while (true) {
			while (currentFolder.fileIndex >= currentFolder.children.length) {
				if (currentFolder.parent == null) {

					// If this occurs, it means that we have read all source files and that we are
					// done.
					currentScanner = null;
					return;
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
		currentScanner = new Scanner(nextFile, StandardCharsets.UTF_8.name());
		currentLine = null;
		currentIndex = 0;

	}

	@Override
	public String next() throws IOException {
		skipCurrentFileWhitespaces();
		while (currentScanner == null) {
			findNextScanner();
			if (currentScanner == null) {
				
				// This will happen when the last source file has been read
				return null;
			}
			skipCurrentFileWhitespaces();
		}

		// By now, the character at the currentIndex should be non-whitespace
		int endIndex = currentIndex + 1;
		while (endIndex < currentLine.length() && !isSeparator(currentLine.charAt(endIndex))) {
			endIndex++;
		}
		String result = currentLine.substring(currentIndex, endIndex);
		currentIndex = endIndex + 1;
		return result;
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