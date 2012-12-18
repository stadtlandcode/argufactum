package de.ifcore.argue.selenium.topic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.selenium.PageUtils;
import de.ifcore.argue.selenium.SeleniumTest;
import de.ifcore.argue.selenium.topic.RestoreDialog.RestoreEntry;
import de.ifcore.argue.selenium.user.LoginPage;
import de.ifcore.argue.selenium.user.RegistrationPage;

@RunWith(SpringJUnit4ClassRunner.class)
public class ArgumentTest extends SeleniumTest
{
	@Before
	public void assureLogin()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		LoginPage.autoLogin(driver);
	}

	@Test
	public void testCreateArgument()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);

		CreateArgumentFragment form = topicPage.getCreateArgumentForm(topicPage.getProContainer());
		form.getSubmitButton().click();
		waitForAjax(driver);
		assertEquals(0, topicPage.getArguments().size());

		form.getThesisElement().sendKeys("abc");
		form.getSubmitButton().click();
		waitForAjax(driver);
		assertEquals(1, topicPage.getArguments().size());
		assertEquals("abc", topicPage.getRandomArgument().getText());
		assertEquals("", form.getThesisElement().getAttribute("value"));
	}

	@Test
	public void testEditArgument()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);
		ArgumentBox argument = topicPage.autoCreateArgument();
		String newText = "abcdef";

		ArgumentPage argumentPage = argument.clickLink();
		String argumentId = argument.getArgumentId();
		argumentPage.clickOnTopicTerm();

		assertFalse(argument.isEditFormVisible());
		argument.clickEditButton();
		assertTrue(argument.isEditFormVisible());

		PageUtils.clearAndSend(argument.getEditThesisInput(), newText);
		argument.clickEditSubmitButton();
		argument = topicPage.getArgument(argumentId);

		assertFalse(argument.isEditFormVisible());
		assertEquals(newText, argument.getText());
		argument.clickLink();
		assertEquals(newText, argumentPage.getText());
		assertTrue(argument.hasEditorNote());
	}

	@Test
	public void testEditArgumentConcurrent()
	{
		WebDriver driverOfAuthor = driverService.getSharedWebDriver();
		ProContraTopicPage topicPageOfAuthor = CreateTopicPage.autoCreateProContra(driverOfAuthor);
		ArgumentBox argumentOfAuthor = topicPageOfAuthor.autoCreateArgument();

		WebDriver driverOfFriend = driverService.getFreshWebDriver();
		String usernameOfFriend = RegistrationPage.autoRegister(driverOfFriend);

		topicPageOfAuthor.autoShareTo(usernameOfFriend);
		ProContraTopicPage topicPageOfFriend = topicPageOfAuthor.open(driverOfFriend);
		ArgumentBox argumentOfFriend = topicPageOfFriend.getArgument(argumentOfAuthor.getArgumentId());

		argumentOfAuthor.clickEditButton();
		argumentOfFriend.clickEditButton();
		argumentOfAuthor.getEditThesisInput().sendKeys("123456");
		argumentOfAuthor.clickEditSubmitButton();
		waitForAjax(driverOfAuthor);
		assertFalse(argumentOfFriend.getEditForm().findElements(By.cssSelector(".alert-info")).isEmpty());

		argumentOfAuthor.delete();
		waitForAjax(driverOfAuthor);
		assertTrue(argumentOfFriend.getEditForm().findElements(By.cssSelector(".alert-info")).isEmpty());
		assertFalse(argumentOfFriend.getEditForm().findElements(By.cssSelector(".alert-error")).isEmpty());
		assertEquals(1, topicPageOfFriend.getArguments().size());
		argumentOfFriend.clickEditSubmitButton();
		assertEquals(0, topicPageOfFriend.getArguments().size());

		driverService.quitWebDriver(driverOfFriend);
	}

	@Test
	public void testDeleteArgument()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);
		ArgumentBox argument = topicPage.autoCreateArgument();

		assertEquals(1, topicPage.getArguments().size());
		argument.delete();
		waitForAjax(driver);
		assertEquals(0, topicPage.getArguments().size());
	}

	@Test
	public void testArgumentHistory()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);
		ArgumentBox argument = topicPage.autoCreateArgument();
		String argumentId = argument.getArgumentId();
		String firstText = argument.getText();

		argument.clickEditButton();
		PageUtils.clearAndSend(argument.getEditThesisInput(), "abc");
		argument.clickEditSubmitButton();
		argument = topicPage.getArgument(argumentId);
		assertEquals("abc", argument.getText());

		HistoryDialog historyDialog = argument.clickHistoryButton();
		assertEquals(2, historyDialog.getEntries().size());
		historyDialog.getEntry(firstText).clickRestoreButton();
		waitForAjax(driver);

		argument = topicPage.getArgument(argumentId);
		assertEquals(firstText, argument.getText());
	}

	@Test
	public void testRateArgument()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);
		ArgumentBox argument1 = topicPage.autoCreateArgument();
		ArgumentBox argument2 = topicPage.autoCreateArgument();
		Relevance relevance = Relevance.HIGH;

		assertNull(argument2.getCurrentPersonalRelevance());
		assertNull(argument2.getCurrentRelevance());
		argument2.clickOnRating(relevance);
		waitForAjax(driver);
		assertEquals(relevance, argument2.getCurrentPersonalRelevance());
		assertEquals(relevance, argument2.getCurrentRelevance());

		assertTrue(argument1.getBoxElement().getLocation().getY() < argument2.getBoxElement().getLocation().getY());
		topicPage.clickSortByRatingOfAllUsers();
		assertFalse(argument1.getBoxElement().getLocation().getY() < argument2.getBoxElement().getLocation().getY());

		argument2.clickOnCancelRating();
		waitForAjax(driver);
		assertNull(argument2.getCurrentPersonalRelevance());
		assertNull(argument2.getCurrentRelevance());
	}

	@Test
	public void testRating()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);

		WebDriver driverOfVisitor = driverService.getFreshWebDriver();
		String usernameOfVisitor = RegistrationPage.autoRegister(driverOfVisitor);
		topicPage.autoShareTo(usernameOfVisitor);

		ProContraTopicPage topicPageOfVisitor = topicPage.open(driverOfVisitor);
		ArgumentBox argument = topicPage.autoCreateArgument();
		ArgumentBox argumentOfVisitor = topicPageOfVisitor.getArgument(argument.getArgumentId());

		argument.clickOnRating(Relevance.HIGH);
		waitForAjax(driver);
		argumentOfVisitor.clickOnRating(Relevance.LOW);
		waitForAjax(driverOfVisitor);

		assertEquals(Relevance.AVERAGE, argument.getCurrentRelevance());
		assertEquals(Relevance.HIGH, argument.getCurrentPersonalRelevance());
		assertEquals(Relevance.AVERAGE, argumentOfVisitor.getCurrentRelevance());
		assertEquals(Relevance.LOW, argumentOfVisitor.getCurrentPersonalRelevance());
	}

	@Test
	public void testRestoreArgument()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);

		RestoreDialog dialog = topicPage.clickRestoreArgumentsButton();
		assertTrue(dialog.getEntries().isEmpty());
		dialog.close();

		ArgumentBox argument = topicPage.autoCreateArgument();
		String argumentId = argument.getArgumentId();
		argument.delete();
		waitForAjax(driver);

		dialog = topicPage.clickRestoreArgumentsButton();
		assertEquals(1, dialog.getEntries().size());
		RestoreEntry restoreArgumentEntry = dialog.getEntry(argumentId);
		restoreArgumentEntry.clickRestoreButton();
		waitForAjax(driver);
		dialog.close();

		assertNotNull(topicPage.getArgument(argumentId));
		dialog = topicPage.clickRestoreArgumentsButton();
		assertTrue(dialog.getEntries().isEmpty());
	}
}
