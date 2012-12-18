package de.ifcore.argue.selenium.topic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.selenium.PageUtils;
import de.ifcore.argue.selenium.SeleniumTest;

public class FactBox
{
	private final WebElement boxElement;
	private final WebDriver driver;

	public FactBox(WebElement boxElement, WebDriver driver)
	{
		this.driver = driver;
		this.boxElement = boxElement;
	}

	public String getText()
	{
		return boxElement.findElement(By.cssSelector(".box-text")).getText();
	}

	public List<ReferenceBox> getReferences()
	{
		List<ReferenceBox> references = new ArrayList<>();
		for (WebElement webElement : boxElement.findElements(By.cssSelector(".references span[data-reference-id]")))
			references.add(new ReferenceBox(webElement, driver));
		return references;
	}

	public ReferenceBox autoCreateReference()
	{
		List<ReferenceBox> previousReferences = getReferences();
		AddReferenceDialog addReferenceDialog = clickAddReference();
		PageUtils.clearAndSend(addReferenceDialog.getTextElement(), RandomStringUtils.randomAlphabetic(10));
		addReferenceDialog.getSubmitButton().click();
		SeleniumTest.waitForAjax(driver);
		Collection<?> subtract = CollectionUtils.subtract(getReferences(), previousReferences);
		return (ReferenceBox)subtract.iterator().next();
	}

	public AddReferenceDialog clickAddReference()
	{
		boxElement.findElement(By.cssSelector(".create-reference a")).click();
		return new AddReferenceDialog(driver.findElement(By.id("create-reference")));
	}

	public String getFactId()
	{
		return boxElement.getAttribute("data-fact-id");
	}

	public void delete()
	{
		boxElement.findElement(By.cssSelector(".delete-box-form button")).click();
		driver.switchTo().alert().accept();
	}

	public void clickEditButton()
	{
		boxElement.findElement(By.cssSelector(".edit-box-button")).click();
	}

	public HistoryDialog clickHistoryButton()
	{
		boxElement.findElement(By.cssSelector(".box-history-button")).click();
		return new HistoryDialog(driver.findElement(By.id("history")));
	}

	public boolean isEditFormVisible()
	{
		List<WebElement> elements = boxElement.findElements(By.cssSelector(".edit-form"));
		return !elements.isEmpty() && elements.iterator().next().isDisplayed();
	}

	public WebElement getEditForm()
	{
		return boxElement.findElement(By.cssSelector(".edit-form"));
	}

	public boolean hasEditorNote()
	{
		return !boxElement.findElements(By.cssSelector(".editor-note")).isEmpty();
	}

	public WebElement getEditTextInput()
	{
		return getEditForm().findElement(By.cssSelector("textarea"));
	}

	public void clickEditSubmitButton()
	{
		getEditForm().findElement(By.cssSelector(".btn-success")).click();
	}

	public void clickEditCancelButton()
	{
		getEditForm().findElement(By.cssSelector(".btn-danger")).click();
	}

	public WebElement getPersonalRatingElement()
	{
		return boxElement.findElement(By.cssSelector(".box-tools .stars"));
	}

	public void clickOnRating(Relevance relevance)
	{
		for (WebElement star : getPersonalRatingElement().findElements(By.cssSelector(".star-rating")))
		{
			if (relevance.name().equals(star.getAttribute("data-star-relevance")))
			{
				star.findElement(By.tagName("a")).click();
				break;
			}
		}
	}

	public void clickOnCancelRating()
	{
		getPersonalRatingElement().findElement(By.cssSelector(".rating-cancel a")).click();
	}

	public Relevance getCurrentPersonalRelevance()
	{
		List<WebElement> activeStars = getPersonalRatingElement().findElements(By.cssSelector(".star-rating-on"));
		return activeStars.isEmpty() ? null : Relevance.forByteValue((byte)activeStars.size());
	}

	public Relevance getCurrentRelevance()
	{
		List<WebElement> activeStars = boxElement.findElements(By.cssSelector(".stats-box .star-rating-on"));
		return activeStars.isEmpty() ? null : Relevance.forByteValue((byte)activeStars.size());
	}
}
