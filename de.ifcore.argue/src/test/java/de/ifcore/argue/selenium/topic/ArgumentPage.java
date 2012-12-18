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

public class ArgumentPage
{
	private final String url;
	private final WebDriver driver;
	private final List<FactBox> facts = new ArrayList<>();

	@FindBy(css = ".content")
	private WebElement contentElement;

	public ArgumentPage(WebDriver driver)
	{
		this.driver = driver;
		this.url = driver.getCurrentUrl();
	}

	public ArgumentPage(WebDriver driver, String argumentId)
	{
		this(driver);
		this.contentElement = driver.findElement(By.id("argument-content-" + argumentId));
	}

	public static ArgumentPage autoCreate(WebDriver driver)
	{
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);
		ArgumentBox argument = topicPage.autoCreateArgument();
		ArgumentPage argumentPage = argument.clickLink();
		SeleniumTest.waitForAjax(driver);
		return argumentPage;
	}

	public FactBox autoCreateFact()
	{
		List<FactBox> previousFacts = new ArrayList<>(facts);
		CreateFactFragment createFactForm = getCreateFactForm(getConfirmativeContainer());
		PageUtils.clearAndSend(createFactForm.getEvidenceElement(), RandomStringUtils.randomAlphabetic(10));
		createFactForm.getSubmitButton().click();
		SeleniumTest.waitForAjax(driver);
		scanFacts();
		Collection<?> subtract = CollectionUtils.subtract(facts, previousFacts);
		return (FactBox)subtract.iterator().next();
	}

	public WebElement getConfirmativeContainer()
	{
		return contentElement.findElement(By.cssSelector(".CONFIRMATIVE"));
	}

	public WebElement getDebilitativeContainer()
	{
		return contentElement.findElement(By.cssSelector(".DEBILITATIVE"));
	}

	public ArgumentPage open(WebDriver driver)
	{
		if (!driver.getCurrentUrl().equals(url))
			driver.get(url);
		return PageFactory.initElements(driver, ArgumentPage.class);
	}

	public void clickOnTopicTerm()
	{
		getTopicTermLink().click();
		SeleniumTest.waitForAjax(driver);
	}

	public String getTopicTerm()
	{
		return getTopicTermLink().getText();
	}

	private WebElement getTopicTermLink()
	{
		return contentElement.findElement(By.cssSelector("h1 a"));
	}

	public String getTopicThesis()
	{
		return contentElement.findElement(By.cssSelector("h2 span:first-child")).getText();
	}

	public String getText()
	{
		return contentElement.findElement(By.cssSelector("h2 span:last-child")).getText();
	}

	public WebElement getContentElement()
	{
		return contentElement;
	}

	public CreateFactFragment getCreateFactForm(WebElement parent)
	{
		return new CreateFactFragment(parent.findElement(By.cssSelector(".create-fact-form")));
	}

	private void scanFacts()
	{
		facts.clear();
		List<WebElement> factElements = contentElement.findElements(By.cssSelector(".fact"));
		for (WebElement factElement : factElements)
		{
			facts.add(new FactBox(factElement, driver));
		}
	}

	public List<FactBox> getFacts()
	{
		scanFacts();
		return facts;
	}

	public FactBox getFact(String factId)
	{
		for (FactBox fact : getFacts())
		{
			if (fact.getFactId().equals(factId))
				return fact;
		}
		return null;
	}

	public FactBox getFactByText(String text)
	{
		for (FactBox fact : getFacts())
		{
			if (fact.getText().equals(text))
				return fact;
		}
		return null;
	}

	public RestoreDialog clickRestoreFactsButton()
	{
		clickConfirmativeDropdown();
		getConfirmativeContainer().findElement(By.cssSelector(".btn-group .dropdown-menu li:first-child a")).click();
		return new RestoreDialog(driver.findElement(By.id("restore")));
	}

	private void clickConfirmativeDropdown()
	{
		getConfirmativeContainer().findElement(By.cssSelector(".btn-group .dropdown-toggle")).click();
		SeleniumTest.wait(driver);
	}
}
