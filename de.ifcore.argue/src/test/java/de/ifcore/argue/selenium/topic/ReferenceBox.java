package de.ifcore.argue.selenium.topic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ReferenceBox
{
	private final WebElement boxElement;
	private final WebDriver driver;

	public ReferenceBox(WebElement boxElement, WebDriver driver)
	{
		this.driver = driver;
		this.boxElement = boxElement;
	}

	public String getText()
	{
		if (boxElement.findElements(By.tagName("a")).isEmpty())
			return boxElement.getText();
		else
			return boxElement.findElement(By.tagName("a")).getText();
	}

	public void delete()
	{
		boxElement.findElement(By.cssSelector(".delete-reference-form i")).click();
		driver.switchTo().alert().accept();
	}
}
