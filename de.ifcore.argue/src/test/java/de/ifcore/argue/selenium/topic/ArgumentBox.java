package de.ifcore.argue.selenium.topic;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.selenium.SeleniumTest;

public class ArgumentBox
{
	private final WebElement boxElement;
	private final WebDriver driver;
	private final String argumentId;

	public ArgumentBox(WebElement boxElement, WebDriver driver)
	{
		this.driver = driver;
		this.boxElement = boxElement;
		this.argumentId = boxElement.getAttribute("data-argument-id");
	}

	public ArgumentPage clickLink()
	{
		getLinkElement().click();
		SeleniumTest.waitForAjax(driver);
		return new ArgumentPage(driver, getArgumentId());
	}

	public WebElement getBoxElement()
	{
		return boxElement;
	}

	public String getText()
	{
		return getLinkElement().getText();
	}

	public WebElement getLinkElement()
	{
		return boxElement.findElement(By.cssSelector(".box-text"));
	}

	public String getArgumentId()
	{
		return argumentId;
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

	public WebElement getEditThesisInput()
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((argumentId == null) ? 0 : argumentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArgumentBox other = (ArgumentBox)obj;
		if (argumentId == null)
		{
			if (other.argumentId != null)
				return false;
		}
		else if (!argumentId.equals(other.argumentId))
			return false;
		return true;
	}
}
