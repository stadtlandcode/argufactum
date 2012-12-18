package de.ifcore.argue.selenium;

import org.openqa.selenium.WebElement;

public class PageUtils
{
	public static void clearAndSend(WebElement element, String content)
	{
		element.clear();
		element.sendKeys(content);
	}
}
