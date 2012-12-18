package de.ifcore.argue.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AnyPage
{
	public static boolean isLoggedIn(WebDriver driver)
	{
		return !driver.findElements(By.id("user")).isEmpty();
	}

	public static String getUsername(WebDriver driver)
	{
		return driver.findElement(By.id("user")).getText();
	}
}
