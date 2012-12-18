package de.ifcore.argue.selenium.topic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import de.ifcore.argue.selenium.PageUtils;
import de.ifcore.argue.selenium.SeleniumTest;

public class ProContraTopicPage extends TopicPage
{
	@FindBy(css = ".box-list-container")
	private WebElement argumentListContainer;

	private final List<ArgumentBox> arguments = new ArrayList<>();

	public ProContraTopicPage(WebDriver driver)
	{
		super(driver);
	}

	@Override
	public ProContraTopicPage open(WebDriver driver)
	{
		if (!driver.getCurrentUrl().equals(url))
			driver.get(url);
		return PageFactory.initElements(driver, ProContraTopicPage.class);
	}

	public ArgumentBox autoCreateArgument()
	{
		List<ArgumentBox> previousArguments = new ArrayList<>(arguments);
		CreateArgumentFragment createArgumentForm = getCreateArgumentForm(getProContainer());
		PageUtils.clearAndSend(createArgumentForm.getThesisElement(), RandomStringUtils.randomAlphabetic(10));
		createArgumentForm.getSubmitButton().click();
		SeleniumTest.waitForAjax(driver);
		scanArguments();
		Collection<?> subtract = CollectionUtils.subtract(arguments, previousArguments);
		return (ArgumentBox)subtract.iterator().next();
	}

	private void scanArguments()
	{
		arguments.clear();
		List<WebElement> argumentElements = argumentListContainer.findElements(By.cssSelector(".argument"));
		for (WebElement argumentElement : argumentElements)
		{
			arguments.add(new ArgumentBox(argumentElement, driver));
		}
	}

	public WebElement getProContainer()
	{
		return argumentListContainer.findElement(By.cssSelector(".PRO"));
	}

	public WebElement getContraContainer()
	{
		return argumentListContainer.findElement(By.cssSelector(".CONTRA"));
	}

	public String getThesis(WebElement thesesContainer)
	{
		return thesesContainer.findElement(By.tagName("h2")).getText();
	}

	public CreateArgumentFragment getCreateArgumentForm(WebElement parent)
	{
		return new CreateArgumentFragment(parent.findElement(By.cssSelector(".create-argument-form")));
	}

	public List<ArgumentBox> getArguments()
	{
		scanArguments();
		return arguments;
	}

	public ArgumentBox getRandomArgument()
	{
		return arguments.iterator().next();
	}

	public ArgumentBox getArgument(String argumentId)
	{
		for (ArgumentBox argument : getArguments())
		{
			if (argument.getArgumentId().equals(argumentId))
				return argument;
		}
		return null;
	}

	private void clickProDropdown()
	{
		argumentListContainer.findElement(By.cssSelector(".PRO .dropdown-toggle")).click();
	}

	public void clickSortByRatingOfAllUsers()
	{
		WebElement box = argumentListContainer.findElement(By.id("reorder-arguments"));
		box.findElement(By.cssSelector(".dropdown-toggle")).click();
		box.findElement(By.cssSelector("li[data-order-by=\"relevance\"] a")).click();
	}

	public EditThesesDialog clickEditThesesButton()
	{
		clickProDropdown();
		argumentListContainer.findElement(By.cssSelector(".PRO .dropdown-menu li:first-child a")).click();
		return new EditThesesDialog(driver.findElement(By.id("edit-proContraTheses")));
	}

	public RestoreDialog clickRestoreArgumentsButton()
	{
		clickProDropdown();
		argumentListContainer.findElement(By.cssSelector(".PRO .dropdown-menu li:last-child a")).click();
		return new RestoreDialog(driver.findElement(By.id("restore")));
	}
}
