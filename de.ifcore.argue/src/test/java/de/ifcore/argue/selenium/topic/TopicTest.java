package de.ifcore.argue.selenium.topic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.enumerations.EntityPermission;
import de.ifcore.argue.selenium.SeleniumTest;
import de.ifcore.argue.selenium.topic.HistoryDialog.HistoryEntry;
import de.ifcore.argue.selenium.topic.ShareTopicDialog.ContactEntry;
import de.ifcore.argue.selenium.user.LoginPage;
import de.ifcore.argue.selenium.user.RegistrationPage;

@RunWith(SpringJUnit4ClassRunner.class)
public class TopicTest extends SeleniumTest
{
	@Before
	public void assureLogin()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		LoginPage.autoLogin(driver);
	}

	@Test
	public void testCreateTopic()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		CreateTopicPage createTopicPage = CreateTopicPage.open(driver);

		String term = "abc";
		String definition = "";
		TopicPage topicPage = createTopicPage.create(term, definition);
		assertEquals(term, topicPage.getTerm());
		assertEquals(definition, topicPage.getDefinition());
		assertNull(topicPage.getCategory());
	}

	@Test
	public void testEditTopic()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		TopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);
		assertFalse(topicPage.hasEditorNote());

		EditTopicDialog editDialog = topicPage.clickEditButton();
		assertEquals(topicPage.getTerm(), editDialog.getTermElement().getAttribute("value"));
		assertEquals(topicPage.getDefinition(), editDialog.getDefinitionElement().getAttribute("value"));
		assertEquals("0", editDialog.getCategoryElement().getFirstSelectedOption().getAttribute("value"));

		editDialog.getSubmitButton().click();
		topicPage.clickEditButton();

		editDialog.getDefinitionElement().clear();
		selectAnotherOption(editDialog.getCategoryElement());
		editDialog.getSubmitButton().click();
		waitForAjax(driver);

		assertEquals("", topicPage.getDefinition());
		assertNotNull(topicPage.getCategory());
		assertTrue(topicPage.hasEditorNote());
	}

	@Test
	public void testTopicHistory()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		TopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);
		topicPage.edit("term2", "def2");
		topicPage.edit("term3", "def2");
		assertEquals("term3", topicPage.getTerm());

		HistoryDialog historyDialog = topicPage.clickHistoryButton();
		assertEquals(3, historyDialog.getEntries().size());

		HistoryEntry historyEntry = historyDialog.getEntry("term2");
		historyEntry.clickRestoreButton();
		waitForAjax(driver);
		assertEquals("term2", topicPage.getTerm());
	}

	@Test
	public void testCopyTopic()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		TopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);

		RegistrationPage.autoRegister(driver);
		topicPage.open(driver);
		assertFalse(topicPage.isEditable());

		TopicPage copiedTopicPage = topicPage.clickCopyButton();
		assertTrue(copiedTopicPage.isEditable());
	}

	@Test
	public void testAccessPrivateTopic()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		TopicPage topicPage = CreateTopicPage.autoCreateProContra(driver, false);

		RegistrationPage.autoRegister(driver);
		topicPage.open(driver);
		wait(driver);
		assertTrue(driver.getTitle().contains("403"));
	}

	@Test
	public void testSetTopicPublic()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		TopicPage topicPage = CreateTopicPage.autoCreateProContra(driver, false);
		ShareTopicDialog shareDialog = topicPage.clickShareButton();

		assertFalse(shareDialog.getVisibilityCheckbox().isSelected());
		shareDialog.getVisibilityCheckbox().click();
		shareDialog.submitVisibilityForm();
		waitForAjax(driver);
		assertTrue(shareDialog.getVisibilityCheckbox().isSelected());

		RegistrationPage.autoRegister(driver);
		TopicPage topicPage2 = topicPage.open(driver);
		wait(driver);
		assertFalse(driver.getTitle().contains("403"));
		assertFalse(topicPage2.isEditable());
	}

	@Test
	public void testShareTopic()
	{
		WebDriver driverOfAuthor = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driverOfAuthor, false);
		topicPage.autoCreateArgument();
		ShareTopicDialog shareDialog = topicPage.clickShareButton();

		WebDriver driverOfFriend = driverService.getFreshWebDriver();
		String usernameOfFriend = RandomStringUtils.randomAlphabetic(10);
		RegistrationPage.autoRegister(driverOfFriend, usernameOfFriend);

		// Leserechte
		assertEquals(1, shareDialog.getEntries().size());
		shareDialog.getContactInputElement().sendKeys(usernameOfFriend);
		shareDialog.getPermissionSelect().selectByValue(EntityPermission.READ.name());
		shareDialog.submitAddAccessRightForm();
		waitForAjax(driverOfAuthor);
		assertEquals(2, shareDialog.getEntries().size());

		TopicPage topicPageOfFriend = topicPage.open(driverOfFriend);
		assertFalse(driverOfFriend.getTitle().contains("403"));
		assertFalse(topicPageOfFriend.isEditable());
		assertTrue(topicPageOfFriend.isVoteable());

		// Rechte entfernen
		ContactEntry friendEntry = shareDialog.getEntry(usernameOfFriend);
		friendEntry.clickDeleteButton();
		waitForAjax(driverOfAuthor);
		assertEquals(1, shareDialog.getEntries().size());

		driverOfFriend.navigate().refresh();
		assertTrue(driverOfFriend.getTitle().contains("403"));

		// Schreibrechte
		shareDialog.getContactInputElement().sendKeys(usernameOfFriend);
		shareDialog.getPermissionSelect().selectByValue(EntityPermission.WRITE.name());
		shareDialog.submitAddAccessRightForm();
		waitForAjax(driverOfAuthor);
		assertEquals(2, shareDialog.getEntries().size());

		driverOfFriend.navigate().refresh();
		assertFalse(driverOfFriend.getTitle().contains("403"));
		assertTrue(topicPageOfFriend.isEditable());
		assertTrue(topicPageOfFriend.isVoteable());

		driverService.quitWebDriver(driverOfFriend);
	}
}
