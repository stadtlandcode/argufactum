package de.ifcore.argue.selenium.topic;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.selenium.PageUtils;
import de.ifcore.argue.selenium.SeleniumTest;

public class TopicPage
{
	protected final String url;
	protected final WebDriver driver;

	@FindBy(css = ".topic-content")
	private WebElement contentElement;

	@FindBy(css = ".topic-page-header")
	private WebElement pageHeader;

	public TopicPage(WebDriver driver)
	{
		this.driver = driver;
		this.url = driver.getCurrentUrl();
	}

	public TopicPage open(WebDriver driver)
	{
		if (!driver.getCurrentUrl().equals(url))
			driver.get(url);
		return PageFactory.initElements(driver, TopicPage.class);
	}

	public String getTerm()
	{
		return pageHeader.findElement(By.cssSelector("h1 span")).getText();
	}

	public String getDefinition()
	{
		return pageHeader.findElement(By.cssSelector(".definition")).getText();
	}

	public String getCategory()
	{
		List<WebElement> elements = pageHeader.findElements(By.cssSelector("h1 small"));
		if (elements.isEmpty())
			return null;
		else
			return elements.iterator().next().getText();
	}

	public boolean hasEditorNote()
	{
		return !pageHeader.findElements(By.cssSelector(".editor-note")).isEmpty();
	}

	public void edit(String term, String definition)
	{
		EditTopicDialog editTopicDialog = clickEditButton();
		PageUtils.clearAndSend(editTopicDialog.getTermElement(), term);
		PageUtils.clearAndSend(editTopicDialog.getDefinitionElement(), definition);
		editTopicDialog.getSubmitButton().click();
		SeleniumTest.waitForAjax(driver);
	}

	public boolean isEditable()
	{
		return !pageHeader.findElements(By.cssSelector(".edit-topic-button")).isEmpty();
	}

	public boolean isVoteable()
	{
		return !contentElement.findElements(By.cssSelector(".box-tools .stars")).isEmpty();
	}

	public EditTopicDialog clickEditButton()
	{
		pageHeader.findElement(By.cssSelector(".edit-topic-button")).click();
		return new EditTopicDialog(driver.findElement(By.id("edit-topic")));
	}

	private void clickEditDropdown()
	{
		pageHeader.findElement(By.cssSelector(".dropdown-toggle")).click();
	}

	public HistoryDialog clickHistoryButton()
	{
		clickEditDropdown();
		pageHeader.findElement(By.cssSelector(".topic-history-button")).click();
		SeleniumTest.waitForAjax(driver);
		return new HistoryDialog(driver.findElement(By.id("history")));
	}

	public TopicPage clickCopyButton()
	{
		pageHeader.findElement(By.cssSelector(".topic-copy-button")).click();
		return PageFactory.initElements(driver, TopicPage.class);
	}

	public ShareTopicDialog clickShareButton()
	{
		pageHeader.findElement(By.cssSelector(".share-topic-button")).click();
		return new ShareTopicDialog(driver.findElement(By.id("share-topic")));
	}

	public void autoShareTo(String username)
	{
		ShareTopicDialog dialog = clickShareButton();
		dialog.getContactInputElement().sendKeys(username);
		dialog.getPermissionSelect().selectByValue(EntityPermission.WRITE.name());
		dialog.submitAddAccessRightForm();
		dialog.close();
		SeleniumTest.waitForAjax(driver);
	}
}
