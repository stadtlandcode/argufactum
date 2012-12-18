package de.ifcore.argue.selenium.topic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import de.ifcore.argue.selenium.PageUtils;
import de.ifcore.argue.selenium.SeleniumTest;

public class CreateTopicPage
{
	public static final String path = "/topic";
	private final WebDriver driver;

	@FindBy(id = "topic-term")
	private WebElement termElement;

	@FindBy(id = "topic-definition")
	private WebElement definitionElement;

	@FindBy(name = "visibility")
	private WebElement visibilityElement;

	@FindBy(name = "categoryId")
	private WebElement categoryElement;

	@FindBy(css = "#create-topic-form button")
	private WebElement submitButton;

	public CreateTopicPage(WebDriver driver)
	{
		this.driver = driver;
	}

	public static CreateTopicPage open(WebDriver driver)
	{
		driver.get(SeleniumTest.urlFor(path));
		return PageFactory.initElements(driver, CreateTopicPage.class);
	}

	public static ProContraTopicPage autoCreateProContra(WebDriver driver)
	{
		return autoCreateProContra(driver, true);
	}

	public static ProContraTopicPage autoCreateProContra(WebDriver driver, boolean isPublic)
	{
		CreateTopicPage topicPage = open(driver);
		return topicPage.create("TestThema", "TestDefinition", isPublic, ProContraTopicPage.class);
	}

	public void enterTerm(String term)
	{
		PageUtils.clearAndSend(termElement, term);
	}

	public void enterDefinition(String definition)
	{
		PageUtils.clearAndSend(definitionElement, definition);
	}

	public void selectProContra()
	{

	}

	public void selectComparison()
	{

	}

	public void togglePublic()
	{
		visibilityElement.click();
	}

	public WebElement getCategoryElement()
	{
		return categoryElement;
	}

	public void submit()
	{
		submitButton.submit();
	}

	public TopicPage create(String term, String definition)
	{
		return create(term, definition, true, TopicPage.class);
	}

	private <T extends TopicPage> T create(String term, String definition, boolean isPublic, Class<T> pageClass)
	{
		enterTerm(term);
		enterDefinition(definition);
		if (isPublic)
			togglePublic();
		submit();
		return PageFactory.initElements(driver, pageClass);
	}
}
