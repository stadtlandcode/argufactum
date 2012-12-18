package de.ifcore.argue.selenium.topic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class EditTopicDialog
{
	private final WebElement termElement;
	private final WebElement definitionElement;
	private final Select categoryElement;
	private final WebElement submitButton;
	private final WebElement cancelButton;

	public EditTopicDialog(WebElement editDialog)
	{
		this.termElement = editDialog.findElement(By.name("term"));
		this.definitionElement = editDialog.findElement(By.name("definition"));
		this.categoryElement = new Select(editDialog.findElement(By.name("categoryId")));
		this.submitButton = editDialog.findElement(By.cssSelector(".btn-success"));
		this.cancelButton = editDialog.findElement(By.cssSelector(".btn-danger"));
	}

	public WebElement getTermElement()
	{
		return termElement;
	}

	public WebElement getDefinitionElement()
	{
		return definitionElement;
	}

	public Select getCategoryElement()
	{
		return categoryElement;
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
