package de.ifcore.argue.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for URL handling
 * 
 * @author Felix Ebert
 * 
 */
public class UrlUtils
{
	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
	private static final String[] umlautsSearchList = { "Ä", "ä", "Ö", "ö", "Ü", "ü", "ß" };
	private static final String[] umlautsReplaceList = { "Ae", "ae", "Oe", "oe", "Ue", "ue", "ss" };

	/**
	 * Converts the input-String to a String which is designed for URLs. Any Whitespace will be converted to "-", all
	 * nonlatin characters get removed and the input gets normalized, converted to lowercase and shortened if necessary.<br />
	 * Example:<br />
	 * input: Soll Atommüll im Ausland entsorgt werden?<br />
	 * output: soll-atommuell-im-ausland-entsorgt-werden
	 * 
	 * @param input
	 * @return slugified input, max 80 characters long
	 */
	public static String slugify(String input)
	{
		if (input == null)
			return "-";
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(
				StringUtils.replaceEachRepeatedly(nowhitespace, umlautsSearchList, umlautsReplaceList), Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		if (slug.length() > 80)
			slug = slug.substring(0, 80);
		return slug.isEmpty() ? "-" : slug.toLowerCase(Locale.ENGLISH);
	}

	public static URL getUrl(String input)
	{
		if (input == null)
			return null;

		URL url = null;
		try
		{
			if (!StringUtils.startsWithIgnoreCase(input, "http://")
					&& !StringUtils.startsWithIgnoreCase(input, "https://") && StringUtils.startsWith(input, "www."))
				input = "http://" + input;
			url = new URL(input);

		}
		catch (MalformedURLException e)
		{
		}
		return url;
	}
}
