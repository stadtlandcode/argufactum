package de.ifcore.argue.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UrlUtilsTest
{
	@Test
	public void testSlugify()
	{
		assertEquals("soll-atommuell-im-ausland-entsorgt-werden",
				UrlUtils.slugify("Soll Atommüll im Ausland entsorgt werden?"));
		assertEquals("veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeryyyyyyyyyyyyyyy-loooooooooooooooooooooooooooooon",
				UrlUtils.slugify("veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeryyyyyyyyyyyyyyy loooooooooooooooooooooooooooooong"));
		assertEquals("oeaeueoeaeuess", UrlUtils.slugify("öäüÖÄÜß"));
		assertEquals("eauoeauo", UrlUtils.slugify("éáúóÈÀÙÒ"));
		assertEquals("-", UrlUtils.slugify("'\"&?/:"));
	}
}
