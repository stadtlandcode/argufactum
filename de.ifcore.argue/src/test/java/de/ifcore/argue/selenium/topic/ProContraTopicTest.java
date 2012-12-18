package de.ifcore.argue.selenium.topic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ifcore.argue.domain.enumerations.TopicThesesMessageCodeAppendix;
import de.ifcore.argue.selenium.SeleniumTest;
import de.ifcore.argue.selenium.user.LoginPage;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProContraTopicTest extends SeleniumTest
{
	@Before
	public void assureLogin()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		LoginPage.autoLogin(driver);
	}

	@Test
	public void testEditTopicTerm()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);

		ArgumentBox argument = topicPage.autoCreateArgument();
		ArgumentPage argumentPage = argument.clickLink();
		argumentPage.clickOnTopicTerm();

		String newTerm = "new term";
		topicPage.edit(newTerm, "");
		assertEquals(newTerm, topicPage.getTerm());
		argument.clickLink();
		assertEquals(newTerm, argumentPage.getTopicTerm());
	}

	@Test
	public void testEditTheses()
	{
		WebDriver driver = driverService.getSharedWebDriver();
		ProContraTopicPage topicPage = CreateTopicPage.autoCreateProContra(driver);
		String firstThesis = topicPage.getThesis(topicPage.getProContainer());

		ArgumentBox argument = topicPage.autoCreateArgument();
		ArgumentPage argumentPage = argument.clickLink();
		assertEquals(firstThesis, argumentPage.getTopicThesis());
		argumentPage.clickOnTopicTerm();

		EditThesesDialog editThesesDialog = topicPage.clickEditThesesButton();
		editThesesDialog.getThesesSelect().selectByValue(TopicThesesMessageCodeAppendix.ADVANTAGE_DISADVANTAGE.name());
		editThesesDialog.clickSubmitButton();
		waitForAjax(driver);

		assertNotSame(firstThesis, topicPage.getThesis(topicPage.getProContainer()));
		argument.clickLink();
		assertNotSame(firstThesis, argumentPage.getTopicThesis());
	}
}
