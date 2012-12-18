package de.ifcore.argue.selenium.topic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.enumerations.Relevance;
import de.ifcore.argue.selenium.PageUtils;
import de.ifcore.argue.selenium.SeleniumTest;
import de.ifcore.argue.selenium.topic.RestoreDialog.RestoreEntry;
import de.ifcore.argue.selenium.user.LoginPage;

@RunWith(SpringJUnit4ClassRunner.class)
public class FactTest extends SeleniumTest
{
	@Before
	public void assureLogin()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		LoginPage.autoLogin(driver);
	}

	@Test
	public void testCreateFact()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ArgumentPage argumentPage = ArgumentPage.autoCreate(driver);

		CreateFactFragment form = argumentPage.getCreateFactForm(argumentPage.getContentElement());
		form.getSubmitButton().click();
		waitForAjax(driver);
		assertEquals(0, argumentPage.getFacts().size());

		form.getEvidenceElement().sendKeys("abc");
		form.getSubmitButton().click();
		waitForAjax(driver);
		assertEquals("", form.getEvidenceElement().getAttribute("value"));
		assertEquals(1, argumentPage.getFacts().size());
		FactBox fact1 = argumentPage.getFacts().iterator().next();
		assertEquals("abc", fact1.getText());
		assertEquals(0, fact1.getReferences().size());

		form.getEvidenceElement().sendKeys("abc2");
		form.getReferenceElement().sendKeys("www.heise.de");
		form.getSubmitButton().click();
		waitForAjax(driver);
		assertEquals(2, argumentPage.getFacts().size());
		FactBox fact2 = argumentPage.getFactByText("abc2");
		assertEquals("abc2", fact2.getText());
		assertEquals(1, fact2.getReferences().size());
		assertEquals("heise.de", fact2.getReferences().iterator().next().getText());
	}

	@Test
	public void testEditFact()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ArgumentPage argumentPage = ArgumentPage.autoCreate(driver);
		FactBox fact = argumentPage.autoCreateFact();
		String factId = fact.getFactId();
		String newText = "abcdef";

		assertFalse(fact.isEditFormVisible());
		fact.clickEditButton();
		assertTrue(fact.isEditFormVisible());

		PageUtils.clearAndSend(fact.getEditTextInput(), newText);
		fact.clickEditSubmitButton();
		fact = argumentPage.getFact(factId);

		assertFalse(fact.isEditFormVisible());
		assertEquals(newText, fact.getText());
	}

	@Test
	public void testDeleteFact()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ArgumentPage argumentPage = ArgumentPage.autoCreate(driver);
		FactBox fact = argumentPage.autoCreateFact();

		assertEquals(1, argumentPage.getFacts().size());
		fact.delete();
		waitForAjax(driver);
		assertEquals(0, argumentPage.getFacts().size());
	}

	@Test
	public void testRestoreFact()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ArgumentPage argumentPage = ArgumentPage.autoCreate(driver);

		RestoreDialog dialog = argumentPage.clickRestoreFactsButton();
		assertTrue(dialog.getEntries().isEmpty());
		dialog.close();

		FactBox fact = argumentPage.autoCreateFact();
		String factId = fact.getFactId();
		fact.delete();
		waitForAjax(driver);

		dialog = argumentPage.clickRestoreFactsButton();
		assertEquals(1, dialog.getEntries().size());
		RestoreEntry restoreEntry = dialog.getEntry(factId);
		restoreEntry.clickRestoreButton();
		waitForAjax(driver);
		dialog.close();

		assertNotNull(argumentPage.getFact(factId));
		dialog = argumentPage.clickRestoreFactsButton();
		assertTrue(dialog.getEntries().isEmpty());
	}

	@Test
	public void testAddReference()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ArgumentPage argumentPage = ArgumentPage.autoCreate(driver);
		FactBox fact = argumentPage.autoCreateFact();

		assertEquals(0, fact.getReferences().size());
		AddReferenceDialog addReferenceDialog = fact.clickAddReference();
		addReferenceDialog.getSubmitButton().click();
		addReferenceDialog.getTextElement().sendKeys("www.google.de");
		addReferenceDialog.getSubmitButton().click();
		waitForAjax(driver);
		assertEquals(1, fact.getReferences().size());
	}

	@Test
	public void testDeleteReference()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ArgumentPage argumentPage = ArgumentPage.autoCreate(driver);
		FactBox fact = argumentPage.autoCreateFact();
		ReferenceBox reference = fact.autoCreateReference();

		assertEquals(1, fact.getReferences().size());
		reference.delete();
		waitForAjax(driver);
		assertEquals(0, fact.getReferences().size());
	}

	@Test
	public void testFactHistory()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ArgumentPage argumentPage = ArgumentPage.autoCreate(driver);
		FactBox fact = argumentPage.autoCreateFact();
		String factId = fact.getFactId();
		String firstText = fact.getText();

		fact.clickEditButton();
		PageUtils.clearAndSend(fact.getEditTextInput(), "abc");
		fact.clickEditSubmitButton();
		fact = argumentPage.getFact(factId);
		assertEquals("abc", fact.getText());

		HistoryDialog historyDialog = fact.clickHistoryButton();
		assertEquals(2, historyDialog.getEntries().size());
		historyDialog.getEntry(firstText).clickRestoreButton();
		waitForAjax(driver);

		fact = argumentPage.getFact(factId);
		assertEquals(firstText, fact.getText());
	}

	@Test
	public void testRateFact()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ArgumentPage argumentPage = ArgumentPage.autoCreate(driver);
		FactBox fact = argumentPage.autoCreateFact();
		Relevance relevance = Relevance.HIGH;

		assertNull(fact.getCurrentPersonalRelevance());
		assertNull(fact.getCurrentRelevance());
		fact.clickOnRating(relevance);
		waitForAjax(driver);
		assertEquals(relevance, fact.getCurrentPersonalRelevance());
		assertEquals(relevance, fact.getCurrentRelevance());

		fact.clickOnCancelRating();
		waitForAjax(driver);
		assertNull(fact.getCurrentPersonalRelevance());
		assertNull(fact.getCurrentRelevance());
	}
}
