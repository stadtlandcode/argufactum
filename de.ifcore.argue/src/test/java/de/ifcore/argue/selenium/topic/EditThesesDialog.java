package de.ifcore.argue.selenium.topic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class EditThesesDialog
{
	private final WebElement dialogElement;

	public EditThesesDialog(WebElement dialogElement)
	{
		this.dialogElement = dialogElement;
	}

	public Select getThesesSelect()
	{
		return new Select(dialogElement.findElement(By.tagName("select")));
	}

	public void clickSubmitButton()
	{
		dialogElement.findElement(By.cssSelector(".btn-success")).click();
	}

	public void clickCancelButton()
	{
		dialogElement.findElement(By.cssSelector(".btn-danger")).click();
	}
}
