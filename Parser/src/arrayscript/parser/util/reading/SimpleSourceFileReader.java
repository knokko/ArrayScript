package arrayscript.parser.util.reading;

import java.io.IOException;
import java.util.Scanner;

public class SimpleSourceFileReader implements SourceFileReader {
	
	private static boolean isSeparator(char value) {
		return Character.isWhitespace(value);
	}
	
	private Scanner scanner;
	private String currentLine;
	private int currentIndex;
	
	public SimpleSourceFileReader(Scanner fileScanner) {
		scanner = fileScanner;
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
		while (scanner != null && currentLine == null) {
			if (scanner.hasNextLine()) {

				// Start at the new line
				currentLine = scanner.nextLine();
				currentIndex = 0;
				skipCurrentLineWhitespaces();
			} else {

				// End of file has been reached, so try to read the next file in the next if block
				scanner.close();
				currentLine = null;
				return;
			}
		}
	}

	@Override
	public String next() throws IOException {
		skipCurrentFileWhitespaces();
		
		if (currentLine == null) {
			
			// If we reach this, the end of the file has been reached
			return null;
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
}