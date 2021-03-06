package br.com.anteros.helpme.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

public abstract class StringUtils {

	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	public static final int DEFAULT_WRAPLENGTH = 150;

	private static final String NEWLINE_SEPARATOR = "\n";

	private static final String NEWLINE_EXPR = "\\n";

	private static final String RETURN_EXPR = "\\r";

	private static final String TAB_EXPR = "\\t";

	public static String replaceAll(String input, String forReplace, String replaceWith) {
		if (input == null)
			return null;
		StringBuffer result = new StringBuffer();
		boolean hasMore = true;
		while (hasMore) {
			int start = input.toUpperCase().indexOf(forReplace.toUpperCase());
			int end = start + forReplace.length();
			if (start != -1) {
				result.append(input.substring(0, start) + replaceWith);
				input = input.substring(end);
			} else {
				hasMore = false;
				result.append(input);
			}
		}
		if (result.toString().equals(""))
			return input;

		return result.toString();
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	public static boolean containsWhitespace(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsWhitespace(String str) {
		return containsWhitespace((CharSequence) str);
	}

	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		int index = 0;
		while (buf.length() > index) {
			if (Character.isWhitespace(buf.charAt(index))) {
				buf.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return buf.toString();
	}

	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	public static String trimTrailingCharacter(String str, char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(buf.length() - 1) == trailingCharacter) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}

		String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;
			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}
		return true;
	}

	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	public static String replace(String inString, String oldPattern, String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		StringBuffer sbuf = new StringBuffer();
		int pos = 0;
		int index = inString.indexOf(oldPattern);
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));
		return sbuf.toString();
	}

	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	public static String quote(String str) {
		return (str != null ? "'" + str + "'" : null);
	}

	public static Object quoteIfString(Object obj) {
		return (obj instanceof String ? quote((String) obj) : obj);
	}

	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
	}

	public static String stripFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List pathElements = new LinkedList();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
			} else if (TOP_PATH.equals(element)) {
				tops++;
			} else {
				if (tops > 0) {
					tops--;
				} else {
					pathElements.add(0, element);
				}
			}
		}
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = (parts.length > 0 ? parts[0] : "");
		String country = (parts.length > 1 ? parts[1] : "");
		String variant = "";
		if (parts.length >= 2) {
			int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
			variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}
		return (language.length() > 0 ? new Locale(language, country, variant) : null);
	}

	public static String toLanguageTag(Locale locale) {
		return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
	}

	public static String[] addStringToArray(String[] array, String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] { str };
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	public static String[] concatenateStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	public static String[] mergeStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		List result = new ArrayList();
		result.addAll(Arrays.asList(array1));
		for (int i = 0; i < array2.length; i++) {
			String str = array2[i];
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

	public static String[] sortStringArray(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}

	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	public static String[] toStringArray(Enumeration enumeration) {
		if (enumeration == null) {
			return null;
		}
		List list = Collections.list(enumeration);
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String[] trimArrayElements(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = (element != null ? element.trim() : null);
		}
		return result;
	}

	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}
		Set set = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return toStringArray(set);
	}

	public static String[] split(String toSplit, String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		Properties result = new Properties();
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			if (charsToDelete != null) {
				element = deleteAny(array[i], charsToDelete);
			}
			String[] splittedElement = split(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List result = new ArrayList();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		} else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	public static Set commaDelimitedListToSet(String str) {
		Set set = new TreeSet();
		String[] tokens = commaDelimitedListToStringArray(str);
		for (int i = 0; i < tokens.length; i++) {
			set.add(tokens[i]);
		}
		return set;
	}

	public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	public static String collectionToDelimitedString(Collection coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	public static String collectionToCommaDelimitedString(Collection coll) {
		return collectionToDelimitedString(coll, ",");
	}

	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	public static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}

	public static boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	public static boolean isEmptyOrSpace(String s) {
		return s == null || s.length() == 0 || s.trim().length() == 0;
	}

	public static boolean isInteger(String str) {
		return isInteger(str, false);
	}

	public static boolean isInteger(String str, boolean trimFirst) {
		if (str == null) {
			return false;
		}
		if (trimFirst) {
			str = str.trim();
		}
		if (str.length() == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!(Character.isDigit(str.charAt(i)) || (i == 0 && (str.charAt(i) == '-') || str.charAt(i) == '+'))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumber(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		char[] chars = str.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;

		int start = (chars[0] == '-') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz) {
					return false;
				}

				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f')
							&& (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--;
		int i = start;

		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {

					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {

				if (hasExp) {

					return false;
				}
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false;
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {

				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {

				return false;
			}
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {

				return foundDigit && !hasExp;
			}

			return false;
		}

		return !allowSigns && foundDigit;
	}

	private static Pattern patternEmailAddr = null;

	public static boolean isEmailAddr(String s) {
		if (s == null) {
			return false;
		}
		if (patternEmailAddr == null) {
			patternEmailAddr = Pattern.compile("[\\w\\.%\\+-]+@[\\w\\.%\\+-]+\\.[A-Za-z]+");
		}
		return patternEmailAddr.matcher(s).matches();
	}

	public static String removeWordSpace(String word) {
		StringBuilder sb = new StringBuilder("");
		int len = word.length();
		for (int i = 0; i < len; i++) {
			Character c = word.charAt(i);
			if (!Character.isSpaceChar(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuffer buf = new StringBuffer(length * strings[0].length()).append(strings[0]);
		for (int i = 1; i < length; i++) {
			buf.append(seperator).append(strings[i]);
		}
		return buf.toString();
	}

	public static String handleQuotedString(String quotedString) {
		if (quotedString == null)
			return "null";
		String retVal = quotedString;
		if ((retVal.startsWith("'") && retVal.endsWith("'"))) {
			if (!retVal.equals("''")) {
				retVal = retVal.substring(retVal.indexOf("'") + 1, retVal.lastIndexOf("'"));
			} else {
				retVal = "";
			}
		}
		return retVal;
	}

	public static boolean containsDigitsOnly(String s) {
		for (int i = 0; s != null && i < s.length(); i++) {
			char c = s.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static boolean containsAsciiCharsOnly(String s) {
		for (int i = 0; s != null && i < s.length(); i++) {
			if (s.charAt(i) > 0x00ff) {
				return false;
			}
		}
		return true;
	}

	public static String getWrappedText(String input, int maxWidth) {

		if (input == null) {
			return "";
		}

		String[] text = input.split(NEWLINE_EXPR);
		String wrappedText = "";

		for (int i = 0; i < text.length; i++) {

			text[i] = text[i].replaceAll(RETURN_EXPR, "");

			if (text[i].length() == 0) {
				continue;
			}

			if (text[i].length() <= maxWidth) {
				wrappedText += text[i];

				if (i < text.length - 1) {
					wrappedText += NEWLINE_SEPARATOR;
				}
			} else {

				String tmp = text[i];

				while (tmp.length() > maxWidth) {

					for (int j = tmp.length() - 1; j >= 0; j--) {

						if (j < maxWidth) {

							char c = text[i].charAt(j);
							if (c == ',') {
								wrappedText += tmp.substring(0, j + 1);
								wrappedText += NEWLINE_SEPARATOR;
								tmp = tmp.substring(j + 1);
								break;
							}
							if (c == ' ') {
								wrappedText += tmp.substring(0, j + 1);
								wrappedText += NEWLINE_SEPARATOR;
								tmp = tmp.substring(j + 1);
								break;
							}
						}

						if (j == 0) {
							wrappedText += tmp.substring(0, maxWidth + 1);
							tmp = "";
							break;
						}
					}

				}

				wrappedText += tmp;
				wrappedText += NEWLINE_SEPARATOR;
			}

		}

		return wrappedText;
	}

	public static String removeLineBreaks(String input) {
		if (input == null) {
			return null;
		}
		String tmp = input.replaceAll(NEWLINE_EXPR, " ");
		tmp = tmp.replaceAll(TAB_EXPR, " ");
		return tmp.replaceAll(RETURN_EXPR, "");
	}

	public static String getWrappedText(String input) {
		return getWrappedText(input, DEFAULT_WRAPLENGTH);
	}

	public static String compressWhitespace(CharSequence source) {
		return compressWhitespace(source, 0);
	}

	/**
	 * Trims whitespace from a string and compresses all internal whitespace
	 * down to a single space. Keeps the length of the string to at most
	 * maxLength, but if it truncates then it makes the last 3 characters an
	 * elipsis
	 * 
	 * @param source
	 *            the string to compress
	 * @param maxLength
	 *            maximum length of the result
	 * @return the compressed string
	 */
	public static String compressWhitespace(CharSequence source, int maxLength) {
		StringBuffer sb = new StringBuffer(source);

		// Trim leading whitespace
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0)))
			sb.deleteCharAt(0);

		boolean lastWasWhite = false;
		for (int i = 0; i < sb.length(); i++) {
			if (Character.isWhitespace(sb.charAt(i))) {
				if (lastWasWhite) {
					// Delete continguous whitespace
					sb.deleteCharAt(i);
					i--;
				} else {
					lastWasWhite = true;

					// Force all whitespace to be a space - IE no funny
					// characters for CR etc
					sb.setCharAt(i, ' ');
				}
			} else
				lastWasWhite = false;
		}

		// Optionally trim to size
		if (maxLength > 0 && sb.length() > maxLength) {
			if (maxLength > 3) {
				sb.delete(maxLength - 3, sb.length());
				sb.append("...");
			} else
				sb.delete(maxLength, sb.length());
		}

		return sb.toString().trim();
	}

	public static String replaceChar(String inputString, char replaceFrom, String replaceTo) {

		if (inputString == null || inputString.length() == 0) {
			return inputString;
		}

		StringBuffer buffer = new StringBuffer();
		char[] input = inputString.toCharArray();

		for (int i = 0; i < input.length; i++) {

			if (input[i] == replaceFrom) {
				buffer.append(replaceTo);
			} else {
				buffer.append(input[i]);
			}
		}

		return buffer.toString();
	}

	public static String rtrim(String input) {
		if (input == null)
			return null;
		int i = 0;
		for (i = input.length() - 1; i >= 0 && Character.isWhitespace(input.charAt(i)); i--)
			;
		return input.substring(0, i + 1);
	}

	public static String htmlEscape(String input) {
		String ret = input.replaceAll("&", "&amp;");
		ret = ret.replaceAll("<", "&lt;");
		ret = ret.replaceAll(">", "&gt;");
		return ret;
	}

	public static final String encodeDoubleQuotation(String str) {
		return str.replaceAll("\"", "\\\\\"");
	}

	private static Pattern commentPattern = Pattern.compile("/\\*.*?(\\*/|$)", Pattern.DOTALL);

	private static Pattern lineCommentPattern = Pattern.compile("--.*?(\r|\n|$)");

	private static Pattern leftTrimPattern = Pattern.compile("^(　| )+");

	private static Pattern rightTrimPattern = Pattern.compile("(　| )+$");

	public static String removeComment(String s) {
		return commentPattern.matcher(s).replaceAll("");
	}

	public static String removeLineComment(String s) {
		return lineCommentPattern.matcher(s).replaceAll("");
	}

	public static String removeLeftFullSpace(String s) {
		return leftTrimPattern.matcher(s).replaceAll("");
	}

	public static String removerightFullSpace(String s) {
		return rightTrimPattern.matcher(s).replaceAll("");
	}

	public static String convertLineSep(String text) {
		return text.replaceAll("\\r\\n|\\r|\\n", "\n");
	}

	public static String convertLineSep(String text, String demiliter) {
		return text.replaceAll("\\r\\n|\\r|\\n", demiliter);
	}

	public static int endWordPosition(String text) {
		boolean flg = false;

		for (int i = text.length() - 1; i >= 0; i--) {
			char chr = text.charAt(i);
			switch (chr) {
			case ' ':
			case ',':
			case '\t':
			case '\r':
			case '\n':
				if (flg) {
					return i + 1;
				} else {
					break;
				}

			default:
				if (!flg)
					flg = true;
				break;
			}

		}
		return 0;
	}

	public static int firstWordPosition(String text) {
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			switch (chr) {
			case ' ':
				// case ' ':
			case '\t':
			case '\r':
			case '\n':
				break;
			default:
				return i;
			}

		}
		return 0;
	}

	public static String padLeft(String s, int width, char padChar) {
		if ((s == null) || (s.length() >= width)) {
			return s;
		}
		StringBuffer sb = new StringBuffer(width);
		int loop = width - s.length();

		for (int i = 0; i < loop; i++)
			sb.append(padChar);
		sb.append(s);

		return sb.toString();
	}

	public static String padRight(String s, int width, char padChar) {
		if ((s == null) || (s.length() >= width)) {
			return s;
		}
		StringBuffer sb = new StringBuffer(width);
		int loop = width - s.length();

		sb.append(s);
		for (int i = 0; i < loop; i++) {
			sb.append(padChar);
		}
		return sb.toString();
	}

	public static String padCenter(String s, int width, char padChar) {
		if ((s == null) || (s.length() >= width)) {
			return s;
		}
		StringBuffer sb = new StringBuffer(width);
		int loop = (width - s.length()) / 2;

		for (int i = 0; i < loop; i++)
			sb.append(padChar);
		String padString = sb.toString();
		sb.append(s);
		sb.append(padString);
		if (sb.length() != width) {
			sb.append(padChar);
		}
		return sb.toString();
	}

	public static String union(String[] strs, char c) {
		if (strs == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
			sb.append(c);
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	public static String leftTrim(String str, char[] charArray) {
		if ((str == null) || (charArray == null)) {
			return str;
		}
		Arrays.sort(charArray);

		int len = str.length();
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (Arrays.binarySearch(charArray, c) < 0)
				return str.substring(i);
		}
		return str;
	}

	public static String rightTrim(String str, char[] charArray) {
		if ((str == null) || (charArray == null)) {
			return str;
		}
		Arrays.sort(charArray);

		int len = str.length();
		for (int i = len - 1; i > 0; i--) {
			char c = str.charAt(i);
			if (Arrays.binarySearch(charArray, c) < 0)
				return str.substring(0, i);
		}
		return str;
	}

	public static String Trim(String str, char[] charArray) {
		if ((str == null) || (charArray == null)) {
			return str;
		}
		Arrays.sort(charArray);

		return rightTrim(leftTrim(str, charArray), charArray);
	}

	public static String toCapitalcase(String str) {
		if ((str == null) || (str.length() == 0)) {
			return str;
		}
		StringBuffer sb = new StringBuffer(str.toLowerCase());
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	public static String[] toUpperCase(String[] strs) {
		if ((strs == null) || (strs.length == 0)) {
			return strs;
		}
		for (int i = 0; i < strs.length; i++) {
			strs[i] = strs[i].toUpperCase();
		}
		return strs;
	}

	public static String[] toLowerCase(String[] strs) {
		if ((strs == null) || (strs.length == 0)) {
			return strs;
		}
		for (int i = 0; i < strs.length; i++) {
			strs[i] = strs[i].toLowerCase();
		}
		return strs;
	}

	public static String subString(String modifier, int length) {
		if (modifier == null)
			return null;
		if (modifier.length() <= length) {
			return modifier;
		} else {
			return modifier.substring(0, length);
		}
	}

}
