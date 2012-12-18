package de.ifcore.argue.selenium.topic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AddReferenceDialog
{
	private final WebElement textElement;
	private final WebElement submitButton;
	private final WebElement cancelButton;

	public AddReferenceDialog(WebElement editDialog)
	{
		this.textElement = editDialog.findElement(By.name("text"));
		this.submitButton = editDialog.findElement(By.cssSelector(".btn-success"));
		this.cancelButton = editDialog.findElement(By.cssSelector(".btn-danger"));
	}

	public WebElement getTextElement()
	{
		return textElement;
	}

	public WebElement getSubmitButton()
	{
		return submitButton;
	}

	public WebElement getCancelButton()
	{
		return cancelButton;
	}
}
