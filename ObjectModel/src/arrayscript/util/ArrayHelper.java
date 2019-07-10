package arrayscript.util;

import java.util.List;

public class ArrayHelper {
	
	public static <T> T[] fromList(List<T> source, T[] dest) {
		source.toArray(dest);
		return dest;
	}
	
	public static <T> boolean contains(T[] array, T element) {
		for (T current : array) {
			if ((current == null && element == null) || (current != null && current.equals(element))) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String concatenate(List<String> strings) {
		int length = 0;
		for (String string : strings) {
			length += string.length();
		}
		
		StringBuilder builder = new StringBuilder(length);
		for (String string : strings) {
			builder.append(string);
		}
		
		return builder.toString();
	}
}