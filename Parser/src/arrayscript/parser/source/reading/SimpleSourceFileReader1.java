package arrayscript.parser.source.reading;

import java.io.IOException;
import java.util.Scanner;

public class SimpleSourceFileReader1 implements SourceFileReader1 {

	private static boolean isSeparator(char value) {
		return Character.isWhitespace(value);
	}

	private Scanner scanner;
	private String currentLine;
	private int currentIndex;

	private boolean inDoubleString;
	private boolean inSingleString;

	public SimpleSourceFileReader1(Scanner fileScanner) {
		scanner = fileScanner;
	}

	private void skipCurrentLineWhitespaces() {
		// Find the next non-whitespace character
		while (currentIndex < currentLine.length() && isSeparator(currentLine.charAt(currentIndex))) {
			currentIndex++;
		}

		if (currentIndex >= currentLine.length()) {

			// End of line has been reached, so try to read the next line in the next if
			// block
			currentLine = null;
		}
	}

	private void skipCurrentFileWhitespaces() throws IOException {
		if (currentLine != null && !inDoubleString && !inSingleString) {
			skipCurrentLineWhitespaces();
		}
		while (scanner != null && currentLine == null) {

			// All strings must be terminated before the end of its line
			if (inDoubleString || inSingleString) {
				throw new IOException("Unterminated string");
			}

			if (scanner.hasNextLine()) {

				// Start at the new line
				currentLine = scanner.nextLine();
				currentIndex = 0;
				skipCurrentLineWhitespaces();
			} else {

				// End of file has been reached
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
		int endIndex = currentIndex;
		char current = endIndex < currentLine.length() ? currentLine.charAt(endIndex) : 0;
		while (endIndex < currentLine.length() && (!isSeparator(current) || inDoubleString || inSingleString)) {
			if (current == '"' && !inSingleString) {
				inDoubleString = !inDoubleString;
			} else if (current == '\'' && !inDoubleString) {
				inSingleString = !inSingleString;
			}
			endIndex++;
			if (endIndex < currentLine.length()) {
				current = currentLine.charAt(endIndex);
			}
		}
		if (endIndex == currentLine.length() && (inSingleString || inDoubleString)) {
			throw new IOException("Unterminated string");
		}
		String result = currentLine.substring(currentIndex, endIndex);
		currentIndex = endIndex + 1;
		return result;
	}
}