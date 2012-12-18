package de.ifcore.argue.selenium.topic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CreateFactFragment
{
	private final WebElement evidenceElement;
	private final WebElement referenceElement;
	private final WebElement submitButton;

	public CreateFactFragment(WebElement formElement)
	{
		this.evidenceElement = formElement.findElement(By.name("text"));
		this.referenceElement = formElement.findElement(By.name("reference"));
		this.submitButton = formElement.findElement(By.tagName("button"));
	}

	public WebElement getEvidenceElement()
	{
		return evidenceElement;
	}

	public WebElement getReferenceElement()
	{
		return referenceElement;
	}

	public WebElement getSubmitButton()
	{
		return submitButton;
	}
}
