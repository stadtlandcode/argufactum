package de.ifcore.argue.selenium.topic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CreateArgumentFragment
{
	private final WebElement thesisElement;
	private final WebElement submitButton;

	public CreateArgumentFragment(WebElement formElement)
	{
		this.thesisElement = formElement.findElement(By.name("thesis"));
		this.submitButton = formElement.findElement(By.tagName("button"));
	}

	public WebElement getThesisElement()
	{
		return thesisElement;
	}

	public WebElement getSubmitButton()
	{
		return submitButton;
	}
}
